package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/9 11:32
 * @description：用户类业务层接口
 * @modified By：
 * @version: 1.0$
 */
public interface UserService {

    /**
     * @Method: 根据企业id查询全部
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param companyId
     * @Return
     * @Exception
     * @Date 2019/10/9 11:32
     */
    List<User> findAll(String companyId);

    /**
     * @Method: 根据id查询
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param userId
     * @Return User
     * @Exception
     * @Date 2019/10/9 11:33
     */
    User findById(String userId);

    /**
     * @Method: 根据id删除
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param userId
     * @Return void
     * @Exception
     * @Date 2019/10/9 11:33
     */
    Boolean delete(String userId);

    /**
     * @Method: 保存
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param user
     * @Return void
     * @Exception
     * @Date 2019/10/9 11:34
     */
    void save(User user);

    /**
     * @Method: 更新
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param user
     * @Return void
     * @Exception
     * @Date 2019/10/9 11:34
     */
    void update(User user);

    /**
     * 2.分页
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @param companyId companyId
     * @return 封装的当前页对象
     */
    PageInfo<User> findByPage(String companyId, Integer pageNum, Integer pageSize);

    /**
     * 通过用户的id获取用户的角色id
     * @Method: findUserRoleIdsByUserId
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param userId 用户的id
     * @Return List<String> 角色id
     * @Exception
     * @Date 2019/10/10 19:25
     */
    List<String> findUserRoleIdsByUserId(String userId);

    /**
     * 更新用户角色
     * @Method: changeRole
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param userId
     * @param roleIds
     * @Return []
     * @Exception
     * @Date 2019/10/10 20:02
     */
    void changeRole(String userId, String[] roleIds);

    /**
     * 通过email获得用户信息
     * @Method: findByEmail
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param email cn.itcast.domain.system.User email
     * @Return User
     * @Exception
     * @Date 2019/10/10 20:36
     */
    User findByEmail(String email);

    /**
     * 动态菜单显示
     * @Method: showMenus
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param userId cn.itcast.domain.system.User id
     * @Return Set<Module>
     * @Exception
     * @Date 2019/10/10 21:11
     */
    Set<Module> showMenus(String userId);

    /**
     * 保存前检查用户提交的用户名 邮箱 和密码等必须数据
     * @Method: checkUser
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param user cn.itcast.domain.User
     * @Return java.util.List<java.lang.String>
     * @Exception
     * @Date 2019/10/11 10:58
     */
    Map<String,String> checkUser(User user);
}
