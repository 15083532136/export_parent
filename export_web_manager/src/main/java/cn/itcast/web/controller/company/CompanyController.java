package cn.itcast.web.controller.company;

import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Reference
    private CompanyService companyService;

    /**
     * 1.查询所有 Company
     * @return /webapp/WEB-INF/pages/company/company-list.jsp
     */
    @RequestMapping("/list")
    public String findAll(Model model){
        List<Company> list = companyService.findAll();
        model.addAttribute("list",list);
        return "/company/company-list";
    }

    /**
     * 2.接收请求跳转到添加页面
     * @return 响应地址
     * 请求地址：WEB-INF/pages/company/company-list.jsp 点击新建
     * 响应地址: WEB-INF/pages/company/company-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "company/company-add";
    }
    /**
     * 2.接收请求跳转到添加页面
     * @return 响应地址
     * 请求地址：WEB-INF/pages/company/company-list.jsp 点击编辑
     * 响应地址: WEB-INF/pages/company/company-update.jsp
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        //回显要修改的数据
        Company company = companyService.findById(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("company",company);
        mv.setViewName("company/company-update");
        return mv;
    }

    /**
     * 3.添加|修改
     * @param company
     * @return 响应地址
     */
    @RequestMapping("/edit")
    public String edit(Company company){
        //通过判断id来确定是修改还是添加
        if (StringUtils.isEmpty(company.getId())){
            //id为空，添加
            companyService.save(company);
        }else {
            //id不为空，修改
            companyService.update(company);
        }
        return "redirect:/company/list.do";
    }

    /**
     * 4.删除
     * @param id company id
     * @return 响应地址 redirect:/company/list
     */
    @RequestMapping("/delete")
    public String delete(String id){
        companyService.delete(id);
        return "redirect:/company/list.do";
    }
}
