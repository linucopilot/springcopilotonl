package com.lincopilot.springcopilotonl.common.session;

import com.lincopilot.springcopilotonl.common.constants.Constants;
import com.lincopilot.springcopilotonl.common.util.SessionUtils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;


/**
 * 세션 Object 사용을 위한 유틸
 * 
 */
public class SessionHelper {
	
	private SessionHelper() {
		
	}
	
	/**
	 * 세션생성 여부를 리턴함.
	 * @param request
	 * @return
	 */
	public static Boolean isSessionInfo(HttpServletRequest request) {
		boolean result = false;
		
		Object obj = SessionUtils.getAttribute(request, Constants.SESSION_INFO);
		
		if(null != obj && obj instanceof SessionInfo) {
			SessionInfo sessionInfo = (SessionInfo) obj;
			if(StringUtils.isNotBlank(sessionInfo.getUserInfo().getUno())) {
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 세션정보 전체를 새로운 sessionInfo로 저장함.
	 * @param request
	 * @param siteCode
	 * @param natCode
	 */
	public static void setSessionInfo(HttpServletRequest request) {
		
		SessionInfo sessionInfo = new SessionInfo();
		SessionUtils.setAttribute(request, Constants.SESSION_INFO, sessionInfo);
	
	}
	
	
	/**
	 * 세션정보 전체를 리턴함. - 세션이 없는경우, 생성 후 리턴함.
	 * @param request
	 * @return
	 */
	public static SessionInfo getSessionInfo(HttpServletRequest request) {
		SessionInfo sessionInfo = null;
		
		// sparrow 조치 2020.11.20
		boolean isSessionInfo = false;
		Object obj = SessionUtils.getAttribute(request, Constants.SESSION_INFO);
		if(null != obj && obj instanceof SessionInfo) {
			
			isSessionInfo = true;
		}
		
		if(isSessionInfo) {
			sessionInfo = (SessionInfo) SessionUtils.getAttribute(request, Constants.SESSION_INFO);
			
		} else {
			sessionInfo = new SessionInfo();
			SessionUtils.setAttribute(request, Constants.SESSION_INFO, sessionInfo);
		}
		
		return sessionInfo;
	}
	
	/**
	 * 로그인 여부를 리턴함.
	 * @param request
	 * @return
	 */
	public static Boolean isLogin(HttpServletRequest request) {
		boolean result = false;
		SessionInfo sessionInfo = getSessionInfo(request);
		if(sessionInfo.getUserInfo() != null && StringUtils.isNotBlank(sessionInfo.getUserInfo().getUno())) {
			result = true;
		}
		return result;
	}
	
	/**
	 * 로그인 사용자 정보를 리턴함.
	 * @param request
	 * @return
	 */
	public static UserInfo getUserInfo(HttpServletRequest request) {
		
		return getSessionInfo(request).getUserInfo();
	}
	
	/**
	 * 로그인사용자 정보를 세션에 저장.
	 * @param request
	 * @param userInfo
	 */
	public static void setUserInfo(HttpServletRequest request, UserInfo userInfo) {
		SessionInfo sessionInfo = SessionHelper.getSessionInfo(request);
	 	sessionInfo.setUserInfo(userInfo);
	 	SessionUtils.setAttribute(request, Constants.SESSION_INFO, sessionInfo);
	}
		
	
	/**
	 * 로그인 할때 입력한 사용자 아이디 값을 리턴함. - 필요한 업무에서 사용하도록 함. 
	 * ex) OTP 인증에서 사용
	 * (기업 subId로 로그인 한 경우는 userIdnfr 항목에 마스터 아이디가 세팅되므로...)
	 * @param request
	 * @return
	 */
	public static String getLoginUno(HttpServletRequest request) {
		UserInfo userInfo = getUserInfo(request);
		return getUno(userInfo);
	}
	
	/**
	 * 파라미터로 받은 userInfo대한 사용자 아이디 값을 리턴함. - 필요한 업무에서 사용하도록 함. 
	 * ex) OTP 인증에서 사용
	 * (기업 subId로 로그인 한 경우는 userIdnfr 항목에 마스터 아이디가 세팅되므로...)
	 * @param request
	 * @return
	 */
	public static String getUno(UserInfo userInfo) {
		if(userInfo == null) {
			return "";
		}
		return userInfo.getUno();
	}
	public static String getBrnCode(UserInfo userInfo) {
		if(userInfo == null) {
			return "";
		}
		return userInfo.getBrnCode();
	}
	
}