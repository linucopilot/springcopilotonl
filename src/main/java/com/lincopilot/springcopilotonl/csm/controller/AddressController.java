package com.lincopilot.springcopilotonl.csm.controller;

import com.lincopilot.springcopilotonl.common.constants.Constants;
import com.lincopilot.springcopilotonl.common.dto.ResultPayload;
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
    public ResultPayload getAddress(HttpServletRequest request, @RequestBody Map<String, Object> inData) {

        // log.info("AddressController.getAddress() inData : {}", inData);
        ResultPayload result = new ResultPayload();
        result.setResultCd(Constants.SUCCESS_CD);
        result.setResultData(addressService.selectAddress(request, inData));
        return result;
    }
}
