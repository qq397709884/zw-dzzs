package cn.longicorn.modules.utils;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * 系统级别的工具类
 * @author zhuchanglin
 */
public class Systems {

    public static String getHostname() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            return ia.getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL locateFromClasspath(String resourceName) {
        URL url = null;
        // attempt to load from the context classpath
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            url = loader.getResource(resourceName);
        }

        // attempt to load from the system classpath
        if (url == null) {
            url = ClassLoader.getSystemResource(resourceName);
        }
        return url;
    }

    /**
     * 判断一个IP地址是否是一个内网IP地址
     * @param ipAddress    待判断的IP地址
     * @return 是内网地址（包括本地127地址） true  不是内网地址 false
     *
     * 私有IP：
     *  A类  10.0.0.0-10.255.255.255
     *  B类  172.16.0.0-172.31.255.255
     *  C类  192.168.0.0-192.168.255.25
     *  127.0.0.x
     */
    public static boolean isInnerIP(String ipAddress) {
        boolean isInnerIp;
        long ipNum = getIpNum(ipAddress);

        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");

        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");

        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");

        isInnerIp = isInner(ipNum, aBegin, aEnd) ||
                isInner(ipNum, bBegin, bEnd) ||
                isInner(ipNum, cBegin, cEnd) ||
                ipAddress.equals("127.0.0.1");
        return isInnerIp;
    }

    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);

        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }
}
