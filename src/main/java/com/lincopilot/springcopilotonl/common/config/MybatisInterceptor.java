package com.lincopilot.springcopilotonl.common.config;

import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import com.lincopilot.springcopilotonl.common.constants.Constants;
import com.lincopilot.springcopilotonl.common.util.HttpUtils;
import com.lincopilot.springcopilotonl.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j 
public class MybatisInterceptor implements Interceptor {
    
    @Override 
    public Object intercept(Invocation invocation) throws Throwable {
        // Implement your interception logic here
        processInvocation(invocation);
        return invocation.proceed();
    }

    @SuppressWarnings ({"rawtypes", "unchecked"})
    private void processInvocation(Invocation invocation) throws IllegalAccessException, NoSuchFieldException {
        // Log the details of the invocation for debugging purposes
        String method = invocation.getMethod().getName();
        Object[] args = invocation.getArgs();
        Object param = args[1];
        String queryID = ((MappedStatement) invocation.getArgs()[0]).getId();
        BoundSql boundSql = ((MappedStatement) invocation.getArgs()[0]).getBoundSql(param);

        String queryString = boundSql.getSql();
        log.debug("Method: " + method + ", Query ID: " + queryID + ", SQL: " + queryString);

        if(param instanceof Map) {
            // If the parameter is a Map, you can log its contents
            Map map = (Map) param;
            if(StringUtil.isEmpty((String)map.get("operGroupCd"))) {
                map.put("operGroupCd", Constants.OPER_GROUP_CD);
            }
            if(StringUtil.isEmpty((String)map.get("sysLasUno"))) {
               map.put("sysLastUno", HttpUtils.getCurrentRequest().getAttribute("sysLastUno"));
            }
            if(StringUtil.isEmpty((String)map.get("langDstcd"))) {
               map.put("langDstcd", HttpUtils.getCurrentRequest().getAttribute("langDstcd"));
            }
            if(StringUtil.isEmpty((String)map.get("UUID"))) {
               map.put("UUID", HttpUtils.getCurrentRequest().getAttribute("UUID"));
            }
            if(StringUtil.isEmpty((String)map.get("schema"))) {
                map.put("schema", HttpUtils.getCurrentRequest().getAttribute("schema"));
            }
        } 
    }

}
