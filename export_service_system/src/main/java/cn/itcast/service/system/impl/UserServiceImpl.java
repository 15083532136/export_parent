package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.dao.system.RoleDao;
import cn.itcast.dao.system.UserDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.domain.system.UserExample;
import cn.itcast.service.system.UserService;
import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/9 11:36
 * @description：用户的业务实现类
 * @modified By：
 * @version: 1.0$
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ModuleDao moduleDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<User> findAll(String companyId) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(companyId);
        return userDao.selectByExample(example);
    }

    @Override
    public User findById(String userId) {
        return userDao.selectByPrimaryKey(userId);
    }

    @Override
    public Boolean delete(String userId) {
        //删除前判断该用户是否有角色未删除
        List<String> roleIdList = userDao.findUserRoleByUserId(userId);
        if (roleIdList == null || roleIdList.size() == 0) {
            //用户没有角色，可以删除
            userDao.deleteByPrimaryKey(userId);
            return true;
        }
            return false;
    }

    @Override
    public void save(User user) {
        //判断user的部门id是否为空
        if (StringUtils.isEmpty(user.getDeptId())){
            //部门id为空设置为null
            user.setDeptId(null);
        }
        //设置id添加用户
        user.setUserId(UUID.randomUUID().toString());
        String password = user.getPassword();
        user.setPassword(new Md5Hash(password,user.getEmail(),1024).toString());
        //判断用户密码是否为空，为空设置为默认值1
        if (StringUtils.isEmpty(user.getPassword())){

        }
        userDao.insertSelective(user);
    }

    @Override
    public void update(User user) {
        //判断user的部门id是否为空
        if (StringUtils.isEmpty(user.getDeptId())){
            //部门id为空设置为null
            user.setDeptId(null);
        }
        //修改用户
        userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public PageInfo<User> findByPage(String companyId, Integer pageNum, Integer pageSize) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(companyId);
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userDao.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public List<String> findUserRoleIdsByUserId(String userId) {

        return userDao.findUserRoleByUserId(userId);
    }

    @Override
    public void changeRole(String userId, String[] roleIds) {
        //1.根据用户id删除用户的角色
        userDao.deleteUserRoles(userId);
        //2.判断roleIds是否为空，若为空则不做添加
        if (roleIds!=null && roleIds.length > 0){
            //不为空，添加用户的角色
            for (String roleId : roleIds) {
                userDao.saveUserRoleByRoleId(userId,roleId);
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        List<User> userList = userDao.findByEmail(email);
        if (userList != null && userList.size() > 0){
            return userList.get(0);
        }
        return null;
    }

    @Override
    public Set<Module> showMenus(String userId) {
        Set<Module> modules = new HashSet<>();
        //1.通过用户id得到用户
        User user = userDao.selectByPrimaryKey(userId);
        if (user.getDegree() == 0){
            //2.用户的等级为0saas管理员，获得saas管理员的所有权限,belong为0的是saas管理员的权限
            List<Module> moduleList = moduleDao.findByBelong(0);
            modules.addAll(moduleList);
            return modules;
        }
        else if (user.getDegree() == 1){
            //3.用户的等级为1是企业管理员，获得企业管理员的所有权限,belong为1的是saas管理员的权限
            List<Module> moduleList = moduleDao.findByBelong(1);
            modules.addAll(moduleList);
            return modules;
        }else {
            //4.通过用户的id获得用户的角色
            List<String> roleIds = userDao.findUserRoleByUserId(userId);
            //5.遍历角色id
            for (String roleId : roleIds) {
                //6.通过角色id获得权限的id集合
                List<String> moduleIds = roleDao.findModuleIdsByroleId(roleId);
                //7.遍历权限的id集合
                for (String moduleId : moduleIds) {
                    //8.通过id获取权限并加入modules集合中
                    Module module = moduleDao.selectByPrimaryKey(moduleId);
                    modules.add(module);
                }
            }
            //9.modules 剔除belong为0的权限
            modules.removeAll(moduleDao.findByBelong(0));
            return modules;
        }

    }

    @Override
    public Map<String,String> checkUser(User user) {
        //List<String> list = new ArrayList<>(16);
        Map<String,String> maps = new HashMap<>(16);
        if (user == null){
            //用户为空，没有数据
            maps.put("userName","请输入要保存的用户信息");
            //list.add("请输入要保存的用户信息");
            return maps;
        }
        if (StringUtils.isEmpty(user.getEmail())){
            //邮箱为空
            //list.add("请输入用户的邮箱");
            maps.put("email","请输入用户的邮箱");
        }else {
            String email = user.getEmail();
            List<User> userList = userDao.findByEmail(email);
            if (userList != null && userList.size() > 0){
                maps.put("email","该邮箱已被注册");
            }
            //User loginUser = userDao.findByEmail(email).get(0);
           /* if (loginUser != null) {
                //用户已存在
                maps.put("email","该邮箱已被注册");
                //list.add("该邮箱已被注册");
            }*/
        }
        if (StringUtils.isEmpty(user.getPassword())){
            //list.add("请输入密码！");
            maps.put("password","请输入密码");

        }
        if (StringUtils.isEmpty(user.getUserName())){
            //list.add("请输入用户名");
            maps.put("userName","请输入用户名");
        }
        return maps;
    }
}
