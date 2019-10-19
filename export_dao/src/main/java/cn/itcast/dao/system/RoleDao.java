package cn.itcast.dao.system;

import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.RoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {
    int deleteByPrimaryKey(String roleId);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(String roleId);

    int updateByPrimaryKeySelective(Role record);

    /**
     * 角色是否有其它用户
     * @Method: findUserIdsByroleId
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId
     * @Return List<String>
     * @Exception
     * @Date 2019/10/9 14:48
     */
    List<String> findUserIdsByroleId(String roleId);

    /**
     * 角色是否有其它模块的权限
     * @Method: findModuleIdsByroleId
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId
     * @Return List<String>
     * @Exception
     * @Date 2019/10/9 14:50
     */
    List<String> findModuleIdsByroleId(String roleId);

    /**
     * 删除角色已有的权限
     * @Method: deleteRoleModulesByRoleId
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId
     * @Return []
     * @Exception
     * @Date 2019/10/9 16:11
     */
    void deleteRoleModulesByRoleId(String roleId);

    /**
     * @Method: saveRoleModule
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId
     * @param moduleIdArr
     * @Return []
     * @Exception
     * @Date 2019/10/9 16:14
     */
    void saveRoleModule(@Param("roleId") String roleId, @Param("moduleIdArr")String[] moduleIdArr);
}