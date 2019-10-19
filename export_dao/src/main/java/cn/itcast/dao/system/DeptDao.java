package cn.itcast.dao.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.DeptExample;

import java.util.List;

public interface DeptDao {
    int deleteByPrimaryKey(String deptId);

    int insertSelective(Dept record);

    List<Dept> selectByExample(DeptExample example);

    Dept selectByPrimaryKey(String deptId);

    int updateByPrimaryKeySelective(Dept record);

}