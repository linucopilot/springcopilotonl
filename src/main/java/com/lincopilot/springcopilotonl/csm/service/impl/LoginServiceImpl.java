package com.lincopilot.springcopilotonl.csm.service.impl;

import com.lincopilot.springcopilotonl.csm.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public Map<String, Object> actionLogin(HttpServletRequest request, Map<String, Object> params) {

        return null;
    }
}
