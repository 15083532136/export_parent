package cn.itcast.dao.system;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.SysLogExample;

import java.util.List;

public interface SysLogDao {
    int deleteByPrimaryKey(String id);

    int insertSelective(SysLog record);

    List<SysLog> selectByExample(SysLogExample example);

    SysLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysLog record);

}