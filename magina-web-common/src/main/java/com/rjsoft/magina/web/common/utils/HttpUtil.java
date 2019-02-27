package com.rjsoft.magina.web.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Http 工具类
 * 用于对HttpServletRequest或HttpServletResponse进行操作
 */
@Slf4j
public class HttpUtil {

    private static final Pattern PATTERN_HEADER = Pattern.compile("(^|-)([a-zA-Z])([a-zA-Z]*)");

    /**
     * 转换 Http 请求头 或 响应头
     *
     * @param headerName
     * @return
     */
    public static String convertHeaderName(String headerName) {
        Matcher matcher = PATTERN_HEADER.matcher(headerName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb,
                    matcher.group(1) + matcher.group(2).toUpperCase() + matcher.group(3).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 打印请求参数
     *
     * @param request
     */
    public static void printRequestParameter(HttpServletRequest request) {
        request.getParameterMap().forEach((k, v) ->
                log.info("            {}=\"{}\"",
                        k,
                        request.getParameter(k)
                )
        );
    }

    /**
     * 打印请求头
     *
     * @param request
     */
    public static void printRequestHeader(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            log.info("            {}: \"{}\"",
                    convertHeaderName(key),
                    request.getHeader(key)
            );
        }
    }

    /**
     * 打印响应头
     *
     * @param response
     */
    public static void printResponseHeader(HttpServletResponse response) {
        Collection<String> headerNames = response.getHeaderNames();
        headerNames.forEach(key ->
                log.info("            {}: \"{}\"",
                        convertHeaderName(key),
                        response.getHeader(key)
                )
        );
    }

    /**
     * 打印cookie
     *
     * @param request
     */
    public static void printRequestCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return;
        }
        for (Cookie cookie : cookies) {
            try {
                log.info("                {Domain:{}, Path:{}, Name:{}, Value:{}, MaxAge:{}, Comment:{}, Version:{}, Secure:{}}",
                        cookie.getDomain(),
                        cookie.getPath(),
                        cookie.getName(),
                        null != cookie.getValue()
                                ? URLDecoder.decode(cookie.getValue(), "UTF-8")
                                : null,
                        cookie.getMaxAge(),
                        cookie.getComment(),
                        cookie.getVersion(),
                        cookie.getSecure()
                );
            } catch (UnsupportedEncodingException e) {
                log.error("不支持的编码", e);
            }
        }
    }
}
