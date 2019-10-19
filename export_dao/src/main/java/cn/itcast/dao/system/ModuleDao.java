package cn.itcast.dao.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.ModuleExample;

import java.util.List;

public interface ModuleDao {
    int deleteByPrimaryKey(String moduleId);

    int insertSelective(Module record);

    List<Module> selectByExample(ModuleExample example);

    Module selectByPrimaryKey(String moduleId);

    int updateByPrimaryKeySelective(Module record);

    /**
     * 通过从属关系来获取权限列表
     * @Method: findByBelong
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param belong 从属关系
     * @Return List<Module> 权限列表
     * @Exception
     * @Date 2019/10/10 18:19
     */
    List<Module> findByBelong(Integer belong);
}