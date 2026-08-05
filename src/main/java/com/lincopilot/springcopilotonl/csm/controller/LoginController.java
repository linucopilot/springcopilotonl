package com.lincopilot.springcopilotonl.csm.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lincopilot.springcopilotonl.common.exception.ApiResponse;
import com.lincopilot.springcopilotonl.csm.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/csm")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(HttpServletRequest request, @RequestBody Map<String, Object> inData) {
        Map<String, Object> resultData = loginService.actionLogin(request, inData);
        return ApiResponse.success("Login Successful", resultData);
    }

    @PostMapping("/refresh-token")
    public ApiResponse<Map<String, Object>> refreshToken(HttpServletRequest request, @RequestBody Map<String, Object> inData) {
        Map<String, Object> resultData = loginService.actionRefreshToken(request, inData);
        return ApiResponse.success("Token Refreshed Successfully", resultData);
    }

    @PostMapping("/logout")
    public ApiResponse<Map<String, Object>> logout(HttpServletRequest request, @RequestBody Map<String, Object> inData) {
        Map<String, Object> resultData = loginService.actionLogout(request, inData);
        return ApiResponse.success("Logged Out Successfully", resultData);
    }
}
