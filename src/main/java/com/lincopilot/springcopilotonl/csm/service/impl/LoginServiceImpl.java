package com.lincopilot.springcopilotonl.csm.service.impl;

import com.lincopilot.springcopilotonl.common.exception.BusinessException;
import com.lincopilot.springcopilotonl.common.util.StringUtil;
import com.lincopilot.springcopilotonl.csm.dao.LoginDao;
import com.lincopilot.springcopilotonl.csm.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

	private final LoginDao loginDao;

	public LoginServiceImpl(LoginDao loginDao) {
		this.loginDao = loginDao;
	}

	@Override
	public Map<String, Object> actionLogin(HttpServletRequest request, Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();

		if (params == null) {
			throw new BusinessException("LOGIN_ERROR", "PARAMS_NULL", "Login parameters cannot be null");
		}

		try {
			Map<String, Object> userMap = loginDao.selectUser(params);
			if (userMap == null || userMap.isEmpty()) {
				throw new BusinessException("LOGIN_ERROR", "USER_NOT_FOUND", "User not found");
			}

			if ("1".equals(userMap.get("useYn"))) {
				if (StringUtil.urlMatchesWithRegEx(request.getRequestURI(), "/mod/**") == true) {
					log.debug("User is active and has access to /mod/**");
				} else {
					if (StringUtil.isEquals(String.valueOf(userMap.get("pwd")),
							String.valueOf(params.get("pwd"))) == false) {
						throw new BusinessException("LOGIN_ERROR", "PWD_MISMATCH", "Password does not match");
					}
				}
				result.put("uno", userMap.get("uno"));
			} else {
				throw new BusinessException("LOGIN_ERROR", "USER_INACTIVE", "User is inactive");
			}

		} catch (Exception e) {
			log.error("error", e);
			throw e;
		}

		return result;
	}

    @Override
    public Map<String, Object> actionLogout(HttpServletRequest request, Map<String, Object> params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionLogout'");
    }
}
