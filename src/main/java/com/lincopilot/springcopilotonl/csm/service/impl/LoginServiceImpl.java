package com.lincopilot.springcopilotonl.csm.service.impl;

import com.lincopilot.springcopilotonl.common.exception.BusinessException;
import com.lincopilot.springcopilotonl.common.jwt.JwtTokenProvider;
import com.lincopilot.springcopilotonl.csm.dao.LoginDao;
import com.lincopilot.springcopilotonl.csm.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Map<String, Object> actionLogin(HttpServletRequest request, Map<String, Object> params) {

        // 1. Query user from database via MyBatis
        Map<String, Object> userMap = loginDao.selectUser(params);
        if (userMap == null || userMap.isEmpty()) {
            throw new BusinessException("AUTH_001", "Invalid Credentials", "User not found or invalid credentials");
        }

        // 2. Extract User Identifier
        String userId = String.valueOf(userMap.get("userId"));
        String username = String.valueOf(userMap.get("username"));

        // 3. Build Claims for Access Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("userName", userMap.get("userName"));
        claims.put("email", userMap.get("email"));
        claims.put("role", userMap.get("role"));

        // 4. Generate JWT Access Token & Refresh Token
        String accessToken = jwtTokenProvider.generateAccessToken(userId, claims);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

        // 5. Save/Update Refresh Token in DB via MyBatis
        Date expiryDate = new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshTokenExpirationMs());
        Map<String, Object> tokenParam = new HashMap<>();
        tokenParam.put("userId", userId);
        tokenParam.put("refreshToken", refreshToken);
        tokenParam.put("expiryDate", expiryDate);
        loginDao.saveRefreshToken(tokenParam);

        // 6. Build User Info (remove password for response security)
        Map<String, Object> userInfo = new HashMap<>(userMap);
        userInfo.remove("password");

        // 7. Return Result Payload
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("tokenType", "Bearer");
        result.put("expiresIn", jwtTokenProvider.getAccessTokenExpirationMs() / 1000);
        result.put("userInfo", userInfo);

        return result;
    }

    @Override
    public Map<String, Object> actionRefreshToken(HttpServletRequest request, Map<String, Object> params) {
        String refreshToken = (String) params.get("refreshToken");

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException("AUTH_002", "Invalid Token", "Refresh token is invalid or expired");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

        // Verify Refresh Token exists in Database
        Map<String, Object> tokenQuery = new HashMap<>();
        tokenQuery.put("userId", userId);
        tokenQuery.put("refreshToken", refreshToken);

        Map<String, Object> dbToken = loginDao.selectRefreshToken(tokenQuery);
        if (dbToken == null || dbToken.isEmpty()) {
            throw new BusinessException("AUTH_003", "Revoked Token", "Refresh token is revoked or not found");
        }

        // Fetch User Details
        Map<String, Object> userQuery = new HashMap<>();
        userQuery.put("userId", userId);
        Map<String, Object> userMap = loginDao.selectUser(userQuery);

        if (userMap == null || userMap.isEmpty()) {
            throw new BusinessException("AUTH_001", "User Not Found", "User associated with token not found");
        }

        // Generate New Pair of Access and Refresh Tokens
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userMap.get("username"));
        claims.put("userName", userMap.get("userName"));
        claims.put("email", userMap.get("email"));
        claims.put("role", userMap.get("role"));

        String newAccessToken = jwtTokenProvider.generateAccessToken(userId, claims);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

        // Update DB with new Refresh Token
        Date expiryDate = new Date(System.currentTimeMillis() + jwtTokenProvider.getRefreshTokenExpirationMs());
        Map<String, Object> tokenParam = new HashMap<>();
        tokenParam.put("userId", userId);
        tokenParam.put("refreshToken", newRefreshToken);
        tokenParam.put("expiryDate", expiryDate);
        loginDao.saveRefreshToken(tokenParam);

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", newAccessToken);
        result.put("refreshToken", newRefreshToken);
        result.put("tokenType", "Bearer");
        result.put("expiresIn", jwtTokenProvider.getAccessTokenExpirationMs() / 1000);

        return result;
    }

    @Override
    public Map<String, Object> actionLogout(HttpServletRequest request, Map<String, Object> params) {
        String userId = (String) params.get("userId");

        if (userId != null && !userId.isEmpty()) {
            loginDao.deleteRefreshToken(params);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Successfully logged out");
        return result;
    }
}
