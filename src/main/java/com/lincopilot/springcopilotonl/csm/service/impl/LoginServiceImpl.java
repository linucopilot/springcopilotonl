package com.lincopilot.springcopilotonl.csm.service.impl;

import com.lincopilot.springcopilotonl.common.exception.BusinessException;
import com.lincopilot.springcopilotonl.csm.dao.LoginDao;
import com.lincopilot.springcopilotonl.csm.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    LoginDao loginDao;

    @Override
    public Map<String, Object> actionLogin(HttpServletRequest request, Map<String, Object> params) {

        Map<String, Object> userMap = loginDao.selectUser(params);
        if (userMap == null || userMap.isEmpty()) {

            throw new BusinessException("Error", "User not found!");
        }

        return userMap;
    }
}
