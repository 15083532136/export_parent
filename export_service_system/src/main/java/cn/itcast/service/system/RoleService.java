package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/9 11:32
 * @description：用户类业务层接口
 * @modified By：
 * @version: 1.0$
 */
public interface RoleService {

    /**
     * @Method: 查询全部
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @Return
     * @Exception
     * @Date 2019/10/9 11:32
     */
    List<Role> findAll(String companyId);

    /**
     * @Method: 根据id查询
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId
     * @Return Role
     * @Exception
     * @Date 2019/10/9 11:33
     */
    Role findById(String roleId);

    /**
     * @Method: 根据id删除
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId
     * @Return void
     * @Exception
     * @Date 2019/10/9 11:33
     */
    Boolean delete(String roleId);

    /**
     * @Method: 保存
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param Role
     * @Return void
     * @Exception
     * @Date 2019/10/9 11:34
     */
    void save(Role role);

    /**
     * @Method: update
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param role
     * @Return void
     * @Exception
     * @Date 2019/10/9 11:34
     */
    void update(Role role);

    /**
     * 分页
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @param companyId
     * @return 封装的当前页对象
     */
    PageInfo<Role> findByPage(String companyId, Integer pageNum, Integer pageSize);

    /**
     * 获取角色的权限信息
     * @Method: findRoleModules
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId
     * @Return List<Module>
     * @Exception
     * @Date 2019/10/9 15:17
     */
    List<Module> findRoleModules(String roleId);

    /**
     * 更新角色权限
     * @Method: updateRoleModule
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId
     * @param moduleIds
     * @Return []
     * @Exception
     * @Date 2019/10/9 16:06
     */
    void updateRoleModule(String roleId, String moduleIds);
}
