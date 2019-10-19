package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.DeptExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.system.DeptService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.FileUploadUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/14 19:51
 * @description：购销合同模块
 * @modified By：
 * @version: 1$
 */
@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * @param pageNum  当前页 默认值1
     * @param pageSize 页大小 默认10
     * @return "cargo/Contract/contract-list"
     * @date 2019/10/7 20:37
     * @auther 屈雪耀
     */
    @RequestMapping("/list")
    protected String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        ContractExample example = new ContractExample();
        //按创建时间降序排序
        example.setOrderByClause("create_time desc");
        ContractExample.Criteria criteria = example.createCriteria();
        //获取用户当前企业的购销合同列表
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        //3.购销合同细粒度权限控制
        User user = getUser();
        if (user.getDegree()==4){
            //用户等级为4，只能访问自己创建的购销合同
            criteria.andCreateByEqualTo(user.getUserId());
        }
        else if (user.getDegree() == 3){
            //用户等级为3,只能访问本部门的购销合同
            criteria.andCreateDeptEqualTo(user.getDeptId());
        }
        else if (user.getDegree() == 2){
            //用户等级为2，可以访问本部门和自己下属所有子孙部门的购销合同
            //deptIds存储用户可以访问的部门id
            List<String> deptIds = new ArrayList<>(16);
            deptIds = getUserDeptIds(user.getDeptId(),deptIds);
            deptIds.add(user.getDeptId());
            criteria.andCreateDeptIn(deptIds);
        }
        //获取当前页的内容
        PageInfo<Contract> page = contractService.findByPage(example, pageNum, pageSize);
        //将获取的页面数据封装到域中
        request.setAttribute("page", page);
        return "cargo/contract/contract-list";
    }

    /**
     * 3.跳转到添加页面
     *
     * @param []
     * @return "cargo/Contract/Contract-add"
     * @date 2019/10/7 21:42
     * @auther lenovo
     */
    @RequestMapping("toAdd")
    protected String toAdd() {
        return "cargo/contract/contract-add";
    }

    /**
     * 4.跳转到修改页面
     *
     * @param id
     * @return "cargo/Contract/Contract-update"
     * @date 2019/10/7 21:50
     * @auther lenovo
     */
    @RequestMapping("/toUpdate")
    protected String toUpdate(String id) {

        //通过id查找要修改的员工信息回显
        Contract contract = contractService.findById(id);
        request.setAttribute("contract", contract);
        return "cargo/contract/contract-update";
    }

    /**
     * 5.添加或修改
     *
     * @param contract
     * @return "redirect:list.do"
     * @date 2019/10/7 22:04
     * @auther lenovo
     */
    @RequestMapping("/edit")
    protected String edit(Contract contract) {
        //企业id和企业名称
        contract.setCompanyId(getLoginCompanyId());
        contract.setCompanyName(getLoginCompanyName());
        //通过判断部门id来添加或修改
        if (StringUtils.isEmpty(contract.getId())) {
            //添加
            contract.setCreateBy(getUser().getUserId());
            contract.setCreateDept(getUser().getDeptId());
            contract.setCreateTime(new Date());
            contractService.save(contract);
        } else {
            //修改
            contract.setUpdateTime(new Date());
            contract.setUpdateBy(getUser().getUserId());
            contractService.update(contract);
        }
        return "redirect:list.do";
    }

    /**
     * 6.删除
     *
     * @param id
     * @return "redirect:list.do"
     * @date 2019/10/7 22:10
     * @auther lenovo
     */
    @RequestMapping("/delete")
    protected String delete(String id) {
        //删除用户返回删除的结果
       contractService.delete(id);
        return "redirect:list.do";
    }

    /**
     * 购销合同（1）查看
     * @Method: toView
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param id
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/14 21:16
     */
    @RequestMapping("/toView")
    public String toView(String id){
        Contract contract = contractService.findById(id);
        request.setAttribute("contract",contract);
        return "cargo/contract/contract-view";
    }

    /**
     * 购销合同（2）提交，将状态由0改为1
     * @Method: submit
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param id
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/14 21:19
     */
    @RequestMapping("/submit")
    public String submit(String id){
        Contract contract = contractService.findById(id);
        contract.setState(1);
        contractService.update(contract);
        return "redirect:list.do";
    }

    /**
     * 购销合同（3）取消，将状态由1改为0
     * @Method: cancel
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param id
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/14 21:21
     */
    @RequestMapping("/cancel")
    public String cancel(String id){
        Contract contract = contractService.findById(id);
        contract.setState(0);
        contractService.update(contract);
        return "redirect:list.do";
    }

    /**
     * 本部门和自己下属所有子孙部门的部门id
     * @Method: getUserDeptIds
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param deptId
     * @param deptIds
     * @Return java.util.List<java.lang.String>
     * @Exception
     * @Date 2019/10/17 8:24
     */
    private List<String> getUserDeptIds(String deptId, List<String> deptIds) {
        DeptExample deptExample = new DeptExample();
        DeptExample.Criteria criteria = deptExample.createCriteria();
        criteria.andParentIdEqualTo(deptId);
        List<Dept> deptList = deptService.findAll(deptExample);
        if (deptList!=null&&deptList.size()>0){
            for (Dept dept : deptList) {
                deptIds.add(dept.getDeptId());
                getUserDeptIds(dept.getDeptId(),deptIds);
            }
        }
        else {
            return deptIds;
        }
        return deptIds;
    }
}
