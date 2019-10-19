package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.Role;
import cn.itcast.service.system.ModuleService;
import cn.itcast.service.system.RoleService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2019/10/7 21:43
 * @auther lenovo
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    /**
     * 自动注入接口
     */
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModuleService moduleService;


    /**
     * @param pageNum 当前页 默认值1
     * @param pageSize 页大小 默认10
     * @return "system/role/role-list"
     * @date 2019/10/7 20:37
     * @auther 屈雪耀
     */
    @RequestMapping("/list")
    protected String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize){
        //获取当前页的内容
        PageInfo<Role> page = roleService.findByPage(getLoginCompanyId(),pageNum, pageSize);
        //将获取的页面数据封装到域中
        request.setAttribute("page",page);
        return "system/role/role-list";
    }

    /**
     * 3.跳转到添加页面
     * @param
     * @return "system/role/role-add"
     * @date 2019/10/7 21:42
     * @auther lenovo
     */
    @RequestMapping("toAdd")
    protected String toAdd(){

        return "system/role/role-add";
    }

    /**
     * 4.跳转到修改页面
     * @param id
     * @return "system/role/role-update"
     * @date 2019/10/7 21:50
     * @auther lenovo
     */
    @RequestMapping("/toUpdate")
    protected String toUpdate(String id){
        //通过id查找要修改的员工信息回显
        Role role = roleService.findById(id);
        request.setAttribute("role",role);
        return "system/role/role-update";
    }

    /**
     * 5.添加或修改
     * @param  role
     * @return "redirect:list.do"
     * @date 2019/10/7 22:04
     * @auther lenovo
     */
    @RequestMapping("/edit")
    protected String edit(Role role){
        //给要修改或添加的员工设置公司的id和name
        role.setCompanyId(getLoginCompanyId());
        role.setCompanyName(getLoginCompanyName());
        //通过判断部门id来添加或修改
        if (StringUtils.isEmpty(role.getRoleId())){
            //添加
            roleService.save(role);
        }else {
            //修改
            roleService.update(role);
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
        Boolean flag = roleService.delete(id);
        result.put("message",flag);
        return result;
    }

    /**
     * 角色权限操作
     * 1.显示角色权限界面,并获取角色信息
     * @Method: roleModule
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/9 14:56
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleId){
        //通过id获取角色的信息并存储到域中
        Role role = roleService.findById(roleId);
        request.setAttribute("role",role);
        return "system/role/role-module";
    }

    /**
     * 角色权限操作
     * 2.通过异步请求获得json格式的zNodes返回
     * @Method: findzNodes
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId 角色role的id
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>> zNodes的集合
     * @Exception
     * @Date 2019/10/9 15:05
     */
    @RequestMapping("/getZtreeNode")
    @ResponseBody
    public List<Map<String,Object>> findzNodes(String roleId){
        //1.创建zNodes的集合
        List<Map<String,Object>> zNodes = new ArrayList<>();
        //2.获取所有权限的信息(出saas管理员权限外)
        List<Module> moduleList = moduleService.findByBelong(1);
        //3.获取角色的权限信息
        List<Module> roleModules = roleService.findRoleModules(roleId);
        //4.遍历所有权限的信息
        for (Module module : moduleList) {
            Map<String,Object> map = new HashMap<>(16);
            /*
            { id:2, pId:0, name:"随意勾选 2", checked:true, open:true}
            按照ztree的要求搭配map集合
             */
            map.put("id",module.getModuleId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());
            map.put("open",true);
            if (roleModules.contains(module)){
                //如果角色也拥有这个权限就设置此参数
                map.put("checked",true);
            }
            zNodes.add(map);
        }
        return zNodes;
    }

    /**
     * 角色权限操作
     * 3.保存角色的权限
     * @Method: updateRoleModule
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param roleId
     * @param moduleIds
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/9 16:04
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleId,String moduleIds){
        roleService.updateRoleModule(roleId,moduleIds);
        return "redirect:list.do";
    }
}
