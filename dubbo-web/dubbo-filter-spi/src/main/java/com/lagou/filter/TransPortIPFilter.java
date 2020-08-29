package com.lagou.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

@Activate(group = {CommonConstants.CONSUMER})
public class TransPortIPFilter extends HandlerInterceptorAdapter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransPortIPFilter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = getIpAddr(request);
        RpcContext context = RpcContext.getContext();
        context.setAttachment("ipHost", ip);
        LOGGER.info("客户端请求IP：{}", ip);
        return super.preHandle(request, response, handler);
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        return invoker.invoke(invocation);
    }

    /**
     * 从HttpServletRequest对象中获取客户端ip
     *
     * @param request HttpServletRequest
     * @return String
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (isInvalided(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isInvalided(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isInvalided(ip)) {
            ip = request.getRemoteAddr();
        }
        if (isLocal(ip)) {
            ip = getLocalIpAddr(ip);
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    /**
     * 遍历本机物理网卡ip
     *
     * @return String
     */
    private String getLocalIpAddr(String ip) {
        try {
            Enumeration<NetworkInterface> faces = NetworkInterface.getNetworkInterfaces();
            // 遍历网络接口
            while (faces.hasMoreElements()) {
                NetworkInterface face = faces.nextElement();
                if (face.isLoopback() || face.isVirtual() || !face.isUp() || face.getDisplayName().contains("VMware")) {
                    // 忽略本机虚拟网卡
                    continue;
                }
                LOGGER.info("有效网络接口名：{}", face.getDisplayName());
                Enumeration<InetAddress> address = face.getInetAddresses();
                // 遍历网卡下的网络地址
                while (address.hasMoreElements()) {
                    InetAddress addr = address.nextElement();
                    if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress() && !addr.isAnyLocalAddress()) {
                        ip = addr.getHostAddress();
                        LOGGER.info("有效网络地址：{}", ip);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return ip;
    }
    /**
     * 判断是否无效ip
     *
     * @param ip String
     * @return boolean
     */
    private boolean isInvalided(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    /**
     * 判断是否是本地ip，如:127.0.0.1, localhost, 0:0:0:0:0:0:0:1
     *
     * @param ip String
     * @return boolean
     */
    private boolean isLocal(String ip) {
        final List<String> localhost = Arrays.asList("127.0.0.1", "localhost", "0:0:0:0:0:0:0:1");
        return localhost.contains(ip);
    }

}
