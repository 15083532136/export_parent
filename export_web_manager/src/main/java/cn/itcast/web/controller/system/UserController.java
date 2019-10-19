package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.DeptExample;
import cn.itcast.domain.system.Role;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.DeptService;
import cn.itcast.service.system.RoleService;
import cn.itcast.service.system.UserService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2019/10/7 21:43
 * @auther lenovo
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    /**
     * 自动注入接口
     */
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;

    /**
     * @param pageNum 当前页 默认值1
     * @param pageSize 页大小 默认10
     * @return "system/user/user-list"
     * @date 2019/10/7 20:37
     * @auther 屈雪耀
     */
    @RequestMapping("/list")
    protected String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize){
        //获取当前页的内容
        PageInfo<User> page = userService.findByPage(getLoginCompanyId(), pageNum, pageSize);
        //将获取的页面数据封装到域中
        request.setAttribute("page",page);
        return "system/user/user-list";
    }

    /**
     * 3.跳转到添加页面
     * @param []
     * @return "system/user/user-add"
     * @date 2019/10/7 21:42
     * @auther lenovo
     */
    @RequestMapping("toAdd")
    protected String toAdd(){
        //查找所有部门作为员工部门下拉框
        DeptExample deptExample = new DeptExample();
        DeptExample.Criteria criteria = deptExample.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        List<Dept> deptList = deptService.findAll(deptExample);
        request.setAttribute("deptList",deptList);
        return "system/user/user-add";
    }

    /**
     * 4.跳转到修改页面
     * @param id
     * @return "system/user/user-update"
     * @date 2019/10/7 21:50
     * @auther lenovo
     */
    @RequestMapping("/toUpdate")
    protected String toUpdate(String id){
        //查找所有部门作为员工部门下拉框
        DeptExample deptExample = new DeptExample();
        DeptExample.Criteria criteria = deptExample.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        List<Dept> deptList = deptService.findAll(deptExample);
        //通过id查找要修改的员工信息回显
        User user = userService.findById(id);
        request.setAttribute("deptList",deptList);
        request.setAttribute("user",user);
        return "system/user/user-update";
    }

    /**
     * 5.添加或修改
     * @param  user
     * @return "redirect:list.do"
     * @date 2019/10/7 22:04
     * @auther lenovo
     */
    @RequestMapping("/edit")
    protected String edit(User user){
        //给要修改或添加的员工设置公司的id和name
        user.setCompanyId(getLoginCompanyId());
        user.setCompanyName(getLoginCompanyName());
        //通过判断部门id来添加或修改
        if (StringUtils.isEmpty(user.getUserId())){
            //添加
            userService.save(user);
        }else {
            //修改
            userService.update(user);
        }
        return "redirect:list.do";
    }

    /**
     * 6.删除
     * @param  id
     * @return "redirect:list.do"
     * @date 2019/10/7 22:10
     * @auther lenovo
     */
    @RequestMapping("/delete")
    @ResponseBody
    protected Map<String,Boolean> delete(String id){
        //设置一个map集合来储存结果
        Map<String,Boolean> result = new HashMap<>(16);
        //删除用户返回删除的结果
        Boolean flag = userService.delete(id);
        result.put("message",flag);
        return result;
    }

    /**
     * 通过用户的id获取用户的角色信息
     *  请求地址：WEB-INF/pages/system/user/user-list.jsp
     * ​	响应地址：WEB-INF/pages/system/user/user-role.jsp
     * @Method: roleList
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param userId
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/10 19:13
     */
    @RequestMapping("/roleList")
    public String roleList(String userId){
        //1.通过用户id得到用户的信息
        User user = userService.findById(userId);
        //2.获取用户的角色id
        List<String> roleIds = userService.findUserRoleIdsByUserId(userId);
        //3.将角色id拼接成字符串
        String userRoleStr = "";
        for (String roleId : roleIds) {
            //将roleId用逗号分隔开来
            userRoleStr += roleId + ",";
        }
        //4.获取所有的角色信息
        List<Role> roleList = roleService.findAll(user.getCompanyId());
        //5.将需要的信息存储到域中
        request.setAttribute("user",user);
        request.setAttribute("userRoleStr",userRoleStr);
        request.setAttribute("roleList",roleList);
        return "system/user/user-role";
    }

    /**
     * 更新用户角色
     *  请求路径：WEB-INF/pages/system/user/user-role.jsp
     * ​ 响应路径：/System/user/list.do
     * @Method: changeRole
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param userId
     * @param roleIds
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/10 20:00
     */
    @RequestMapping("/changeRole")
    public String changeRole(String userId,String[] roleIds){

        userService.changeRole(userId,roleIds);
        return "redirect:list.do";
    }

    /**
     * 保存前检查用户提交的用户名 邮箱 和密码等必须数据
     * @Method: checkUser
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param user
     * @Return java.util.Map<java.lang.String,java.util.List<java.lang.String>>
     * @Exception
     * @Date 2019/10/11 10:55
     */
    @RequestMapping("/checkUser")
    @ResponseBody
    public Map<String,Object> checkUser(User user){
        Map<String,Object> result = new HashMap<>(16);
        Map<String,String> list = userService.checkUser(user);
        if (list == null || list.size() == 0){
            result.put("flag",true);
        }else {
            result.put("message", list);
            result.put("flag",false);
        }
        return result;
    }
}
