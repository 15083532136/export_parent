package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractService;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.vo.ExportProductVo;
import cn.itcast.vo.ExportResult;
import cn.itcast.vo.ExportVo;
import cn.itcast.web.controller.BaseController;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;

import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/18 19:43
 * @description：出口报运模块控制器
 * @modified By：
 * @version: 1.0$
 */
@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    @Reference
    private ExportService exportService;
    @Reference
    private ContractService contractService;
    @Reference
    private ExportProductService exportProductService;

    /**
     * 合同管理列表：显示所有已上报的购销合同
     * @Method: list
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/18 19:47
     */
    @RequestMapping("/contractList")
    public String contractList(
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "5")Integer pageSize){
        ContractExample contractExample = new ContractExample();
        ContractExample.Criteria criteria = contractExample.createCriteria();
        criteria.andStateEqualTo(1);
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        contractExample.setOrderByClause("create_time desc");
        PageInfo<Contract> page = contractService.findByPage(contractExample, pageNum, pageSize);
        request.setAttribute("page",page);
        return "cargo/export/export-contractList";
    }

    /**
     * 出口报运列表
     * @Method: list
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param pageNum
     * @param pageSize
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/19 10:21
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1")Integer pageNum,
                       @RequestParam(defaultValue = "5")Integer pageSize){
        ExportExample exportExample = new ExportExample();
        ExportExample.Criteria criteria = exportExample.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        exportExample.createCriteria().andStateEqualTo(0L);
        PageInfo<Export> page = exportService.findByPage(exportExample, pageNum, pageSize);
        request.setAttribute("page",page);
        return "cargo/export/export-list";
    }

    /**
     * 出口报运编辑页面
     * @Method: toUpdate
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param id
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/19 10:34
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){

        Export export = exportService.findById(id);
        request.setAttribute("export",export);
        //出口报运货物列表
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> list = exportProductService.findAll(exportProductExample);
        request.setAttribute("eps",list);
        return "cargo/export/export-update";
    }

    /**
     * 购销合同报运
     * @Method: toExport
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param id 被勾选的所有购销合同id,以，分割
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/18 20:24
     */
    @RequestMapping("/toExport")
    public String toExport(String id){
        request.setAttribute("id",id);
        return "cargo/export/export-toExport";
    }

    /**
     * 合同管理报运
     * @Method: edit
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param export
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/18 20:40
     */
    @RequestMapping("/edit")
    public String edit(Export export){
        export.setCreateBy(getUser().getUserId());
        export.setCreateDept(getUser().getDeptId());
        export.setCompanyId(getLoginCompanyId());
        export.setCompanyName(getLoginCompanyName());
        if (StringUtils.isEmpty(export.getId())){
            //保存
            exportService.save(export);
        }
        else {
            exportService.update(export);
        }
        return "redirect:contractList.do";
    }

    /**
     * 出口报运列表，点击取消
     */
    @RequestMapping("/cancel")
    public String cancel(String id) {
        Export export = exportService.findById(id);
        export.setState(0);
        exportService.update(export);
        return "redirect:/cargo/export/list.do";
    }

    /**
     * 出口报运列表，点击提交
     */
    @RequestMapping("/submit")
    public String submit(String id) {
        Export export = exportService.findById(id);
        export.setState(1);
        exportService.update(export);
        return "redirect:/cargo/export/list.do";
    }

    @RequestMapping("/exportE")
    public String exportE(String id){
        Export export = exportService.findById(id);
        ExportVo exportVo = new ExportVo();
        BeanUtils.copyProperties(export,exportVo);
        ExportProductExample exportProductExample = new ExportProductExample();
        exportProductExample.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> exportProductList = exportProductService.findAll(exportProductExample);
        List<ExportProductVo> list = new ArrayList<>(16);
        for (ExportProduct exportProduct : exportProductList) {
            ExportProductVo exportProductVo =new ExportProductVo();
            BeanUtils.copyProperties(exportProduct,exportProductVo);
            exportProductVo.setExportProductId(exportProduct.getId());
            list.add(exportProductVo);
        }
        exportVo.setProducts(list);
        exportVo.setExportId(export.getId());
        WebClient client = WebClient.create("http://localhost:8091/ws/export/user");
        client.post(exportVo);
        WebClient webClient = WebClient.create("http://localhost:8091/ws/export/user/" + id);
        ExportResult result = webClient.get(ExportResult.class);
        exportService.updateExport(result);
        return "redirect:list.do";
    }
}
