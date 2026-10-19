package com.lincopilot.springcopilotonl.common.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

/**
 * 세션에 값을 처리하기위한 유틸
 * 
 */
@Slf4j
public class SessionUtils {
	
	public static void setAttribute(HttpServletRequest request, String sessionKey, Object sessionObject) {
		HttpSession httpSession = request.getSession(true);
		httpSession.setAttribute(sessionKey, sessionObject);
	}
	
	public static Object getAttribute(HttpServletRequest request, String sessionKey) {
		HttpSession httpSession = request.getSession(false);
		if( null == httpSession ) {
			return null;
		}
		
		if( null == httpSession.getAttribute(sessionKey) ) {
			return null;
		}
		return httpSession.getAttribute(sessionKey);
	}
	
}