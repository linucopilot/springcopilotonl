package com.lincopilot.springcopilotonl.common.component;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component 
public class SystemUtils {

	private final Environment environment;

	private static String springProfilesActive = System.getProperty("spring.profiles.active");

	private static String applicationName = "";
	private static String jdbcDatabaseSchema = "";

	SystemUtils(Environment environment) {
		this.environment = environment;
	}

	@PostConstruct
	public void init() {
		try {
			applicationName = environment.getProperty("spring.application.name");
            jdbcDatabaseSchema = environment.getProperty("spring.sql.init.schema-locations");
		} catch (Exception e) {
			log.error("Error initializing SystemUtils: ", e);
		}
	}

	public static boolean isLocal() {
		boolean isLocal = false;
		if (StringUtils.isBlank(springProfilesActive)) {
			isLocal = true; // Default to local if the property is not set
		}
		if (StringUtils.isNotBlank(springProfilesActive) && springProfilesActive.equals("local")) {
			isLocal = true;
		}
		return isLocal;
	}

	public static boolean isDev() {
		boolean isDev = false;
		if (StringUtils.isNotBlank(springProfilesActive) && springProfilesActive.equals("dev")) {
			isDev = true;
		}
		return isDev;
	}

	public static boolean isStg() {
		boolean isStg = false;
		if (StringUtils.isNotBlank(springProfilesActive) && springProfilesActive.equals("stg")) {
			isStg = true;
		}
		return isStg;
	}

	public static boolean isProd() {
		boolean isProd = false;
		if (StringUtils.isNotBlank(springProfilesActive) && springProfilesActive.equals("prod")) {
			isProd = true;
		}
		return isProd;
	}

	public static String getApplicationName() {

		return applicationName;
	}

	public static String getJdbcDatabaseSchema() {
		return jdbcDatabaseSchema;
	}
}
