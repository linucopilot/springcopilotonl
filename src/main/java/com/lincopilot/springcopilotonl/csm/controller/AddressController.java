package com.lincopilot.springcopilotonl.csm.controller;

import com.lincopilot.springcopilotonl.common.constants.Constants;
import com.lincopilot.springcopilotonl.common.dto.ResultPayload;
import com.lincopilot.springcopilotonl.csm.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddressController {
    AddressService addressService;

     public ResultPayload login(HttpServletRequest request, @RequestBody Map<String, Object> inData) {

        // log.info("AddressController.login() inData : {}", inData);
        ResultPayload result = new ResultPayload();
        result.setResultCd(Constants.SUCCESS_CD);
        result.setResultData(addressService.selectAddress(request, inData));
        return result;
    }
}
