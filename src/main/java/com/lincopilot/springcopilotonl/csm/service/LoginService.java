package com.lincopilot.springcopilotonl.csm.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface LoginService {

    public Map<String, Object> actionLogin(HttpServletRequest request, Map<String, Object> params);

}
