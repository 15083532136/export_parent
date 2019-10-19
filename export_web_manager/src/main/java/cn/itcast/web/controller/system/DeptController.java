package cn.itcast.web.controller.system;

import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.DeptExample;
import cn.itcast.service.system.DeptService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
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
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    /**
     * 自动注入接口
     */
    @Autowired
    private DeptService deptService;

    /**
     * @param pageNum 当前页 默认值1
     * @param pageSize 页大小 默认10
     * @return "system/dept/dept-list"
     * @date 2019/10/7 20:37
     * @auther 屈雪耀
     */
    @RequestMapping("/list")
    protected String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize){
        DeptExample deptExample = new DeptExample();
        DeptExample.Criteria criteria = deptExample.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        //获取当前页的内容
        PageInfo<Dept> page = deptService.findByPage(deptExample, pageNum, pageSize);
        //将获取的页面数据封装到域中
        request.setAttribute("page",page);
        return "system/dept/dept-list";
    }

    /**
     * 3.跳转到添加页面
     * @param []
     * @return "system/dept/dept-add"
     * @date 2019/10/7 21:42
     * @auther lenovo
     */
    @RequestMapping("toAdd")
    protected String toAdd(){
        DeptExample deptExample = new DeptExample();
        DeptExample.Criteria criteria = deptExample.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        //查找所有部门作为下拉框
        List<Dept> deptList = deptService.findAll(deptExample);
        request.setAttribute("deptList",deptList);
        return "system/dept/dept-add";
    }

    /**
     * 4.跳转到修改页面
     * @param id
     * @return "system/dept/dept-update"
     * @date 2019/10/7 21:50
     * @auther lenovo
     */
    @RequestMapping("/toUpdate")
    protected String toUpdate(String id){
        DeptExample deptExample = new DeptExample();
        DeptExample.Criteria criteria = deptExample.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        //查找所有部门作为上级部门下拉框
        List<Dept> deptList = deptService.findAll(deptExample);
        //通过id查找要修改的部门信息回显
        Dept dept = deptService.findById(id);
        //删除自己部门
        deptList.remove(dept);
        request.setAttribute("deptList",deptList);
        request.setAttribute("dept",dept);
        return "system/dept/dept-update";
    }

    /**
     * 5.添加或修改
     * @param  dept
     * @return "redirect:list.do"
     * @date 2019/10/7 22:04
     * @auther lenovo
     */
    @RequestMapping("/edit")
    protected String edit(Dept dept){
        //给要修改或添加的部门设置公司的id和name
        dept.setCompanyId(getLoginCompanyId());
        dept.setCompanyName(getLoginCompanyName());
        //通过判断部门id来添加或修改
        if (StringUtils.isEmpty(dept.getDeptId())){
            //添加
            deptService.save(dept);
        }else {
            //修改
            deptService.update(dept);
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
    protected Map<String,String> delete(String id){
        Boolean flag = deptService.delete(id);
        Map<String,String> result = new HashMap<>();
        if (flag){
            result.put("mess","删除成功");
        }else {
            result.put("mess","删除失败，该部门有子部门，不能直接删除");
        }
        return result;
    }
}
