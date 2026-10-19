package com.lincopilot.springcopilotonl.common.constants;

import lombok.Data;

@Data
public class Constants {
	private Constants() {

	}

	public static final String SUCCESS_CD = "0000";

	public static final String SESSION_INFO = "SESSION_INFO";
	public static final String OPER_GROUP_CD = "KH1";
	public static final String KBPB_CLIENT_INFO = "Kbpb-Client-Info";

	// DB
	public static final String JDBC_JNDI_LOAN = "dbserver.jndi.loan";
	public static final String JDBC_DATABASE_SCHEMA = "dbserver.jdbc.schema";
}
