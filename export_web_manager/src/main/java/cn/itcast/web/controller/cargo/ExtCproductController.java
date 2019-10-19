package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ExtCproductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.FileUploadUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/17 17:29
 * @description：货物下的附件
 * @modified By：
 * @version: 1.0$
 */
@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {

    @Reference
    private ExtCproductService extCproductService;
    @Reference
    private FactoryService factoryService;
    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 显示货物的附件列表
     * @Method: list
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param contractId 购销合同id
     * @param contractProductId 货物id
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/17 18:51
     */
    @RequestMapping("/list")
    public String list(String contractId, String contractProductId,
                       @RequestParam(defaultValue = "1")Integer pageNum,
                       @RequestParam(defaultValue = "5")Integer pageSize){
        //1.生产厂家,查找ctype为附件的生产厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //2.附件列表(找到货物id为contractProductId的附件)
        ExtCproductExample extCproductExample = new ExtCproductExample();
        ExtCproductExample.Criteria cproductExampleCriteria = extCproductExample.createCriteria();
        cproductExampleCriteria.andContractProductIdEqualTo(contractProductId);
        PageInfo<ExtCproduct> page = extCproductService.findByPage(extCproductExample, pageNum, pageSize);
        //3.保存数据到域中
        request.setAttribute("page",page);
        request.setAttribute("factoryList",factoryList);
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        return "cargo/extc/extc-list";
    }

    /**
     * 增加或修改
     * @Method: edit
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param contractProductId 货物id
     * @param contractId 购销合同id
     * @param extCproduct 要增加或修改的货物
     * @param productPhoto 上传的照片
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/17 19:24
     */
    @RequestMapping("/edit")
    public String edit(String contractProductId,
                       String contractId,
                       ExtCproduct extCproduct,
                       MultipartFile productPhoto){
        //1.设置公司的id和name
        extCproduct.setCompanyId(getLoginCompanyId());
        extCproduct.setCompanyName(getLoginCompanyName());
        //图片上传
        if (productPhoto!=null){
            try {
                String upload = fileUploadUtil.upload(productPhoto);
                extCproduct.setProductImage("http://"+upload);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }
        }
        if (StringUtils.isEmpty(extCproduct.getId())){
            //增加附件,设置创建人和创建时间，购销合同id和货物id
            extCproduct.setContractId(contractId);
            extCproduct.setContractProductId(contractProductId);
            extCproduct.setCreateBy(getUser().getUserId());
            extCproduct.setCreateDept(getUser().getDeptId());
            extCproduct.setCreateTime(new Date());
            FactoryExample example = new FactoryExample();
            example.createCriteria().andFactoryNameEqualTo(extCproduct.getFactoryName());
            List<Factory> factoryList = factoryService.findAll(example);
            if (factoryList!=null&&factoryList.size()>0){
                extCproduct.setFactoryId(factoryList.get(0).getId());
            }
            //保存附件
            extCproductService.save(extCproduct);
        }
        else {
            //设置修改时间和修改人
            extCproduct.setUpdateBy(getUser().getUserId());
            extCproduct.setUpdateTime(new Date());
            //修改附件
            extCproductService.update(extCproduct);
        }
        return "forward:list.do?contractProductId="+contractProductId+"&contractId="+contractId;
    }

    /**
     * 编辑附件 数据回显
     * @Method: toUpdate
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param id
     * @param contractId
     * @param contractProductId
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/17 20:06
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id,
                           String contractId,
                           String contractProductId){
        //1.生产厂家,查找ctype为附件的生产厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        ExtCproduct extCproduct = extCproductService.findById(id);
        request.setAttribute("extCproduct",extCproduct);
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        request.setAttribute("factoryList",factoryList);
        return "cargo/extc/extc-update";
    }

    /**
     * 删除附件
     * @Method: delete
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param id
     * @param contractId
     * @param contractProductId
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/17 20:35
     */
    @RequestMapping("/delete")
    public String delete(String id,
                         String contractId,
                         String contractProductId){
        extCproductService.delete(id);
        return "rediect:cargo/contract/list.do";
    }
}
