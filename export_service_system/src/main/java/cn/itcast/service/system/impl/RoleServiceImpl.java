package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.dao.system.RoleDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.RoleExample;
import cn.itcast.service.system.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/9 11:36
 * @description：用户的业务实现类
 * @modified By：
 * @version: 1.0$
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ModuleDao moduleDao;

    @Override
    public List<Role> findAll(String companyId) {
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andCompanyIdEqualTo(companyId);
        return roleDao.selectByExample(roleExample);
    }

    @Override
    public Role findById(String roleId) {
        return roleDao.selectByPrimaryKey(roleId);
    }

    @Override
    public Boolean delete(String roleId) {

        List<String> userIdList = roleDao.findUserIdsByroleId(roleId);
        List<String> moduleIdList = roleDao.findModuleIdsByroleId(roleId);
        //1.判断该角色是否有其它用户使用
        if (userIdList == null || userIdList.size() == 0){
            //2.判断该角色是否有其它模块的权限
            if (moduleIdList == null || moduleIdList.size() == 0){
                //3.可以删除
                roleDao.deleteByPrimaryKey(roleId);
                return true;
            }
            return false;
        }
            return false;
    }

    @Override
    public void save(Role role) {

        //设置id添加用户
        role.setRoleId(UUID.randomUUID().toString());
        roleDao.insertSelective(role);
    }

    @Override
    public void update(Role role) {
        //修改用户
        roleDao.updateByPrimaryKeySelective(role);
    }

    @Override
    public PageInfo<Role> findByPage(String companyId,Integer pageNum, Integer pageSize) {
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andCompanyIdEqualTo(companyId);
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        List<Role> list = roleDao.selectByExample(roleExample);
        return new PageInfo<>(list);
    }

    @Override
    public List<Module> findRoleModules(String roleId) {
        //1.获取角色的所有权限的id
        List<String> moduleIds = roleDao.findModuleIdsByroleId(roleId);
        List<Module> moduleList = new ArrayList<>();
        for (String moduleId : moduleIds) {
            //2.通过moduleId获取module
            Module module = moduleDao.selectByPrimaryKey(moduleId);
            moduleList.add(module);
        }
        return moduleList;
    }

    @Override
    public void updateRoleModule(String roleId, String moduleIds) {
        //1.删除角色已有的权限
        roleDao.deleteRoleModulesByRoleId(roleId);
        //拆分权限的字符串成权限的id数组
        String[] moduleIdArr = moduleIds.split(",");
        //对数组进行非空判断，若数组为空或null，则不执行添加操作
        if (moduleIdArr != null && moduleIdArr.length > 1){
            //数组长度大于1是因为若是添加了权限，必定会加上它的上级权限，而若是选择上级权限则为全选
            //添加角色的权限
            roleDao.saveRoleModule(roleId,moduleIdArr);
        }
    }
}
