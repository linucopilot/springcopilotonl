package com.lincopilot.springcopilotonl.common.session;

import java.io.Serializable;

import lombok.Data;

/**
 * Session에 저장되는 VO
 * 
 */
@Data
public class SessionInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public SessionInfo() {
		//Do nothing
	}	
	
	// 로그인 사용자 정보
	UserInfo userInfo ;	
}