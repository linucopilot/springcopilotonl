package com.lincopilot.springcopilotonl.common.session;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.lincopilot.springcopilotonl.common.util.HttpUtils;
import com.lincopilot.springcopilotonl.common.util.StringUtil;

import lombok.Data;

/**
 *  사용자 세션정보
 * 
 */
@Data
public class UserInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String operGroupCd;  	//운영그룹코드
	private String uno;				//사용자ID
	private String userName = "";	//사용자명
	private String ipAddress = "";  //IP주소
	private String langDstcd = "";	//언어구분
	private String brnCode = "";	//부점코드
	private String brnName = ""; 	//부점명
	private String regionalOffice = ""; 	//리전부점코드(지역부점코드)
	private String rmYn = ""; 	//RM여부
	private List<String> multiBrncd; //멀티부점코드
	private List<Map<String, String>> multiBrnList; //멀티부점리스트
	private String tranBaseYmd = "";//거래기준년월일
	private String nowBzoprYmd = "";//금영업년월일
	private String bfBzoprYmd = "";	//전영업년월일
	private String bfBfBzoprYmd = "";//전전영업년월일
	private String nxbizYmd = "";	//익영업년월일
	private List<String> dutyCode;	//직무코드
	private List<String> dutyName;   //직무명
	private List<String> authCode;	//권한코드
	private List<String> authName;   //권한명
	private String applicationName = "";//어플리케이션명
	private String serviceMode = "";//서비스모드
	/**
	 * @param selectUserInfo
	 */
	public UserInfo(Map<String, Object> map) {
		if(map != null) {
			this.operGroupCd = StringUtil.evl(map.get("operGroupCd"),"");
			this.uno = StringUtil.evl(map.get("uno"),"");
			this.userName = StringUtil.evl(map.get("userName"),"");
			this.ipAddress = StringUtil.evl(HttpUtils.getRemoteAddr(HttpUtils.getCurrentRequest()),"-");
			this.langDstcd = StringUtil.evl(map.get("langDstcd"),"");
			this.brnCode = StringUtil.evl(map.get("brnCode"),"");
			this.brnName = StringUtil.evl(map.get("brnName"),"");
			this.regionalOffice = StringUtil.evl(map.get("regioellnalOffice"),"");
			this.rmYn = StringUtil.evl(map.get("rmYn"),"");
			this.tranBaseYmd = StringUtil.evl(map.get("tranBaseYmd"),"");
			this.nowBzoprYmd = StringUtil.evl(map.get("nowBzoprYmd"),"");
			this.bfBzoprYmd = StringUtil.evl(map.get("bfBzoprYmd"),"");
			this.bfBfBzoprYmd = StringUtil.evl(map.get("bfBfBzoprYmd"),"");
			this.nxbizYmd = StringUtil.evl(map.get("nxbizYmd"),"");
			this.applicationName = StringUtil.evl(map.get("applicationName"),"");
		}
	}	
	
}