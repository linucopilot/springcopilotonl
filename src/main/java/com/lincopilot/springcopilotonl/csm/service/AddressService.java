package com.lincopilot.springcopilotonl.csm.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface AddressService {

    public Map<String, Object> selectAddress(HttpServletRequest request, Map<String, Object> params);

}
