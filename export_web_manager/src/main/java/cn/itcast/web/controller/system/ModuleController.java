package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Module;
import cn.itcast.service.system.ModuleService;
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
 * @author lenovo
 * @date 2019/10/7 21:43
 */
@Controller
@RequestMapping("/system/module")
public class ModuleController extends BaseController {

    /**
     * 自动注入接口
     */
    @Autowired
    private ModuleService moduleService;

    /**
     * @param pageNum 当前页 默认值1
     * @param pageSize 页大小 默认10
     * @return "system/module/module-list"
     * @date 2019/10/7 20:37
     * @auther 屈雪耀
     */
    @RequestMapping("/list")
    protected String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize){
        //获取当前页的内容
        PageInfo<Module> page = moduleService.findByPage(null,pageNum , pageSize);
        //将获取的页面数据封装到域中
        request.setAttribute("page",page);
        return "system/module/module-list";
    }

    /**
     * 3.跳转到添加页面
     * @param []
     * @return "system/module/module-add"
     * @date 2019/10/7 21:42
     * @auther lenovo
     */
    @RequestMapping("/toAdd")
    protected String toAdd(){
        //查找所有模块作为下拉框
        List<Module> moduleList = moduleService.findAll(null);
        request.setAttribute("moduleList",moduleList);
        return "system/module/module-add";
    }

    /**
     * 4.跳转到修改页面
     * @param id
     * @return "system/module/module-update"
     * @date 2019/10/7 21:50
     * @auther lenovo
     */
    @RequestMapping("/toUpdate")
    protected String toUpdate(String id){
        //查找所有模块作为上级模块下拉框
        List<Module> moduleList = moduleService.findAll(null);
        //通过id查找要修改的模块信息回显
        Module module = moduleService.findById(id);
        //删除自己模块
        moduleList.remove(module);
        request.setAttribute("moduleList",moduleList);
        request.setAttribute("module",module);
        return "system/module/module-update";
    }

    /**
     * 5.添加或修改
     * @param  module
     * @return "redirect:list.do"
     * @date 2019/10/7 22:04
     * @auther lenovo
     */
    @RequestMapping("/edit")
    protected String edit(Module module){
        //通过判断模块id来添加或修改
        if (StringUtils.isEmpty(module.getModuleId())){
            //添加
            moduleService.save(module);
        }else {
            //修改
            moduleService.update(module);
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

        Boolean flag = moduleService.delete(id);
        Map<String,Boolean> result = new HashMap<>();
        result.put("message",flag);
        return result;
    }

    /**
     * 7.当类型的选项发生改变时，它的上级模块也要改变
     * @param ctype
     * @return
     */
    @RequestMapping("/findParentModule")
    @ResponseBody
    public List<Module> findParentModule(String ctype){
        System.out.println("aaaaa");
        //通过ctype找到它的父模块
        List<Module> moduleList = moduleService.findParentModule(ctype);
        return moduleList;
    }

    /**
     * 8.上级部门的下拉框被选定时，类型要被确定
     * @param id id
     * @return map
     */
    @RequestMapping("/findCTypeByParentModuleId")
    @ResponseBody
    public Map<String,String> findModuleByParentModuleId(String id){
        Map<String,String> map = new HashMap<>(16);
        String ctype = moduleService.findCtypeByParentModuleId(id);
        map.put("ctype",ctype);
        return map;
    }
}
