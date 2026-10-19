package com.lincopilot.springcopilotonl.common.util;

import java.util.Enumeration;

import org.apache.tomcat.util.json.JSONParser;
import io.micrometer.common.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.lincopilot.springcopilotonl.common.constants.Constants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtils {

	private HttpUtils() {
		// Private constructor to prevent instantiation
	}

	public static HttpServletRequest getCurrentRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	public static HttpServletResponse getCurrentResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
	}

	public static boolean isMutipartFormData(HttpServletRequest request) {
		try {
			return request.getHeader("content-type").contains("multipart");

		} catch (RuntimeException e) {
			log.error("Error checking if request is multipart/form-data: ", e);
		}
		return false;
	}

	public static boolean isMutipartFormData() {
		return isMutipartFormData(getCurrentRequest());
	}

	private static String getFirstIp(String ip) {
		if (ip == null || ip.isEmpty()) {
			return "";
		}
		String[] ipArray = ip.split(",");
		return ipArray[0].trim();
	}

	public static String getRemoteAddr(HttpServletRequest request) {
		String clientIp = request.getHeader("X-Forwarded-For");
		if (!StringUtil.isEmpty(clientIp)) {
			return getFirstIp(clientIp);
		}
		clientIp = request.getHeader("X-Real-IP");
		if (!StringUtil.isEmpty(clientIp)) {
			return getFirstIp(clientIp);
		}
		clientIp = request.getHeader("Proxy-Client-IP");
		if (!StringUtil.isEmpty(clientIp)) {
			return getFirstIp(clientIp);
		}
		clientIp = request.getHeader("WL-Proxy-Client-IP");
		if (!StringUtil.isEmpty(clientIp)) {
			return getFirstIp(clientIp);
		}
		clientIp = request.getHeader("HTTP_CLIENT_IP");
		if (!StringUtil.isEmpty(clientIp)) {
			return getFirstIp(clientIp);
		}
		clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (!StringUtil.isEmpty(clientIp)) {
			return getFirstIp(clientIp);
		}
		clientIp = request.getRemoteAddr();
		return getFirstIp(clientIp);
	}

	public static JsonNode getVuejsCommonHeader(HttpServletRequest request) {
		JsonNode vuejsCommonHeader = null;
		try {
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				if (headerName != null && Constants.KBPB_CLIENT_INFO.equalsIgnoreCase(headerName)
						&& StringUtils.isNotBlank(request.getHeader(headerName))
						&& request.getHeader(headerName) instanceof String) {
							JSONParser jsonParser = new JSONParser (request.getHeader(headerName));
							vuejsCommonHeader = JsonUtils.convertValue(jsonParser.parse(), JsonNode.class);

				}
			}
		} catch (Exception e) {
			log.error("Error parsing Kbpb-Client-Info header: ", e);
		}
		return vuejsCommonHeader;
	}
}
