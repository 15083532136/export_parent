package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.domain.cargo.Factory;
import cn.itcast.domain.cargo.FactoryExample;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.service.cargo.FactoryService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.utils.FileUploadUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/16 19:32
 * @description：购销合同货物模块
 * @modified By：
 * @version: 1.0$
 */
@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;
    @Reference
    private FactoryService factoryService;
    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 货物列表:
     * 1.显示生产厂家
     * 2.显示购销合同的货物
     * @Method: list
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param contractId 购销合同id
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/16 19:34
     */
    @RequestMapping("/list")
    public String list(
            String contractId,
            @RequestParam(defaultValue = "1") Integer pageNum,//0d897ef2-680f-4748-92e3-21b073997af9
            @RequestParam(defaultValue = "5")Integer pageSize){
        //1.显示购销合同的货物
        ContractProductExample contractProductExample = new ContractProductExample();
        ContractProductExample.Criteria criteria = contractProductExample.createCriteria();
        criteria.andCompanyIdEqualTo(getUser().getCompanyId());
        criteria.andContractIdEqualTo(contractId);
        PageInfo<ContractProduct> page = contractProductService.findByPage(contractProductExample, pageNum, pageSize);
        request.setAttribute("page",page);
        //2.显示所有生产货物的厂家
        FactoryExample factoryExample = new FactoryExample();
        factoryExample.createCriteria().andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        request.setAttribute("factoryList",factoryList);
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-list";
    }

    /**
     * 前往修改页面
     * @Method: toUpdate
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param id 货物的id
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/16 19:56
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //1.回显页面数据
        ContractProduct contractProduct = contractProductService.findById(id);
        request.setAttribute("contractProduct",contractProduct);
        //2.显示所有生产厂家
        List<Factory> factoryList = factoryService.findAll(null);
        request.setAttribute("factoryList",factoryList);
        return "cargo/product/product-update";
    }

    /**
     * 修改或增加
     * @Method: edit
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param contractProduct 要修改或添加的货物
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/16 20:02
     */
    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto){
        //设置货物所属公司
        contractProduct.setCompanyId(getLoginCompanyId());
        contractProduct.setCompanyName(getLoginCompanyName());
        //图片上传
        if (productPhoto!=null){
            try {
                String upload = fileUploadUtil.upload(productPhoto);
                contractProduct.setProductImage("http://"+upload);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }
        }
        //1.根据货物的id来判断是增加还是修改
        if (StringUtils.isEmpty(contractProduct.getId())){
            //设置货物的属性
            contractProduct.setCreateBy(getUser().getUserId());
            contractProduct.setCreateDept(getUser().getDeptId());
            contractProduct.setCreateTime(new Date());
            //添加货物
            contractProductService.save(contractProduct);
        }
        else {
            //设置货物的属性
            contractProduct.setUpdateBy(getUser().getUserId());
            contractProduct.setUpdateTime(new Date());
            //修改货物
            contractProductService.update(contractProduct);
        }
        return "forward:list.do?contractId="+contractProduct.getContractId();
    }

    /**
     * 删除货物
     * @Method: delete
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param id
     * @param contractId
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/16 21:30
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId){
        contractProductService.delete(id);
        return "redirect:list.do?contractId="+contractId;
    }

    @RequestMapping("/toImport")
    public String toImport(String contractId){
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }

    /**
     * 读取上传的配置文件
     * @Method: importExcel
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param contractId
     * @param file
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/17 20:56
     */
    @RequestMapping("/import")
    public String importExcel(String contractId,MultipartFile file) throws Exception {

        //获取文件输入流
        InputStream is = file.getInputStream();
        //创建excel文件
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        //遍历工作簿 获取所有的行
        int rows = sheet.getPhysicalNumberOfRows();
        for (int index = 1; index < rows; index++) {
            Row row = sheet.getRow(index);
            ContractProduct cp = new ContractProduct();
            cp.setFactoryName(row.getCell(1).getStringCellValue());
            cp.setProductNo(row.getCell(2).getStringCellValue());
            cp.setCnumber((int) row.getCell(3).getNumericCellValue());
            cp.setPackingUnit(row.getCell(4).getStringCellValue());
            cp.setLoadingRate(row.getCell(5).getNumericCellValue() + "");
            cp.setBoxNum((int) row.getCell(6).getNumericCellValue());
            double price = row.getCell(7).getNumericCellValue();
            cp.setPrice(new BigDecimal(price));
            cp.setProductDesc(row.getCell(8).getStringCellValue());
            cp.setProductRequest(row.getCell(9).getStringCellValue());
            // 设置企业信息
            cp.setCompanyId(getLoginCompanyId());
            cp.setCompanyName(getLoginCompanyName());
            // 设置厂家id
            FactoryExample factoryExample = new FactoryExample();
            factoryExample.createCriteria().andFactoryNameEqualTo(cp.getFactoryName());
            List<Factory> factoryList = factoryService.findAll(factoryExample);
            if (factoryList != null&&factoryList.size()>0) {
                cp.setFactoryId(factoryList.get(0).getId());
            }
            //【注意：设置购销合同id】
            cp.setContractId(contractId);
            contractProductService.save(cp);
        }
        return "forward:list.do?contractId="+contractId;
    }
}
