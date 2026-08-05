package com.lincopilot.springcopilotonl.csm.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lincopilot.springcopilotonl.common.constants.Constants;
import com.lincopilot.springcopilotonl.common.dto.ResultPayload;
import com.lincopilot.springcopilotonl.csm.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/csm")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResultPayload login(HttpServletRequest request, @RequestBody Map<String, Object> inData) {

        // log.info("LoginController.login() inData : {}", inData);
        ResultPayload result = new ResultPayload();
        result.setResultCd(Constants.SUCCESS_CD);
        result.setResultData(loginService.actionLogin(request, inData));
        return result;
    }

    @GetMapping("/logout")
    public ResultPayload logout(HttpServletRequest request) {
        ResultPayload result = new ResultPayload();
        result.setResultCd(Constants.SUCCESS_CD);
        return result;
    }
}
