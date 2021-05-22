package com.weirwei.cansee.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author weirwei 2021/4/18 19:12
 */
@Slf4j
public class IdUtil {
    public static String getUserId() {
        return "U" + getId();
    }

    public static String getOrgId() {
        return "O" + getId();
    }

    public static String getProjId() {
        return "P" + getId();
    }

    public static String getLogId() {
        return "L" + getId();
    }

    private static String getId() {
        String ipAddress = "";
        try {
            //获取服务器IP地址
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.error("getNewUserId=" + e.getMessage());
        }
        //获取UUID
        String uuid = ipAddress + "$" + UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        //生成后缀
        long suffix = Math.abs(uuid.hashCode() % 100000000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String time = sdf.format(new Date(System.currentTimeMillis()));
        //生成前缀
        long prefix = Long.parseLong(time) * 100000000;
        return String.valueOf(prefix + suffix);
    }
}
