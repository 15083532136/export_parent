package cn.itcast.dao.system;

import cn.itcast.domain.system.User;
import cn.itcast.domain.system.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    int deleteByPrimaryKey(String userId);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(User record);

    /**
     * 通过用户的id获取用户的角色id集合
     * @Method: 判断该用户是否有角色
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param userId
     * @Return List<String>
     * @Exception
     * @Date 2019/10/9 12:18
     */
    List<String> findUserRoleByUserId(String userId);

    /**
     * 根据用户id删除用户的角色
     * @Method: deleteUserRoles
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param  userId cn.itcast.domain.system.User id
     * @Return []
     * @Exception
     * @Date 2019/10/10 20:10
     */
    void deleteUserRoles(String userId);

    /**
     * 添加用户的角色
     * @Method: saveUserRoleByRoleId
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param userId cn.itcast.domain.system.User id
     * @param roleId cn.itcast.domain.system.Role id
     * @Return void
     * @Exception []
     * @Date 2019/10/10 20:12
     */
    void saveUserRoleByRoleId(@Param("userId") String userId, @Param("roleId") String roleId);

    /**
     * 通过email获得用户信息
     * @Method: findByEmail
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param email cn.itcast.domain.system.User email
     * @Return List<User>
     * @Exception
     * @Date 2019/10/10 20:41
     */
    List<User> findByEmail(String email);
}