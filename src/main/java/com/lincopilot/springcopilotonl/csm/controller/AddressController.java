package com.lincopilot.springcopilotonl.csm.controller;

import com.lincopilot.springcopilotonl.common.exception.ApiResponse;
import com.lincopilot.springcopilotonl.csm.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/csm")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/address")
    public ApiResponse<Map<String, Object>> getAddress(HttpServletRequest request, @RequestBody Map<String, Object> inData) {
        Map<String, Object> resultData = addressService.selectAddress(request, inData);
        return ApiResponse.success(resultData);
    }
}
