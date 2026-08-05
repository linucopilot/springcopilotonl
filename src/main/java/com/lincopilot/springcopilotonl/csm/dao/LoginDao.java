package com.lincopilot.springcopilotonl.csm.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LoginDao {

    public Map<String, Object> selectUser(Map<String, Object> inData);

    public List<Map<String, Object>> selectListUser(Map<String, Object> inData);

    public int insertUser(Map<String, Object> inData);

    public int updateUser(Map<String, Object> inData);

    public int deleteUser(Map<String, Object> inData);

    // Refresh Token Management Methods
    public int saveRefreshToken(Map<String, Object> inData);

    public Map<String, Object> selectRefreshToken(Map<String, Object> inData);

    public int deleteRefreshToken(Map<String, Object> inData);
}
