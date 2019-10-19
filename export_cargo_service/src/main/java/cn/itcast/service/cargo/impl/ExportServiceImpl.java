package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.*;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ExportService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/18 19:38
 * @description：出口报运模块
 * @modified By：
 * @version: 1.0$
 */
@Service(timeout = 1000000)
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    ContractProductDao contractProductDao;
    @Autowired
    ExtCproductDao extCproductDao;
    @Autowired
    private ExportProductDao exportProductDao;

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        //设置id
        export.setId(UUID.randomUUID().toString());
        export.setInputDate(new Date());
        export.setCreateTime(new Date());
        export.setState(0);
        /**
         * 合同号、货物数量、附件数量
         */
        //报运合同的商品
        List<ExportProduct> exportProductList = new ArrayList<>(16);
        //报运附件
        List<ExtEproduct> extEproductList = new ArrayList<>(16);
        String[] contractIds = export.getContractIds().split(",");
        ArrayList<String> contractIdList = new ArrayList<>(Arrays.asList(contractIds));
        String customerContract = "";
        if (contractIdList!=null&&contractIdList.size()>0) {
            for (String contractId : contractIdList) {
                //查找购销合同
                Contract contract = contractDao.selectByPrimaryKey(contractId);
                customerContract += contract.getContractNo() + ",";
                contract.setState(2);
                //添加报运合同下的商品
                ContractProductExample contractProductExample = new ContractProductExample();
                contractProductExample.createCriteria().andContractIdEqualTo(contractId);
                List<ContractProduct> contractProducts = contractProductDao.selectByExample(contractProductExample);
                Map<String,String> map = new HashMap<>(16);
                if (contractProducts!=null&&contractProducts.size()>0){
                    for (ContractProduct contractProduct : contractProducts) {
                        ExportProduct exportProduct = new ExportProduct();
                        exportProduct.setId(UUID.randomUUID().toString());
                        BeanUtils.copyProperties(contractProduct,exportProduct);
                        //设置关联合同的id
                        exportProduct.setExportId(export.getId());
                        exportProductList.add(exportProduct);
                        map.put(contractProduct.getId(),exportProduct.getId());
                    }
                }
                //添加商品下的附件
                ExtCproductExample extCproductExample = new ExtCproductExample();
                extCproductExample.createCriteria().andContractIdEqualTo(contractId);
                List<ExtCproduct> extCproductList = extCproductDao.selectByExample(extCproductExample);
                for (ExtCproduct extCproduct : extCproductList) {
                    ExtEproduct extEproduct = new ExtEproduct();
                    extEproduct.setId(UUID.randomUUID().toString());
                    BeanUtils.copyProperties(extCproduct,extEproduct);
                    //设置exportProductId、exportId
                    extEproduct.setExportId(export.getId());
                    extEproduct.setExportProductId(map.get(extCproduct.getContractProductId()));
                    extEproductList.add(extEproduct);
                }
                export.setExtNum(exportProductList.size());
                export.setBoxNums(extEproductList.size());
                contractDao.updateByPrimaryKeySelective(contract);
            }
        }
        //合同号
        export.setCustomerContract(customerContract);
        exportDao.insertSelective(export);

    }

    @Override
    public void update(Export export) {
        exportDao.updateByPrimaryKeySelective(export);
        List<ExportProduct> exportProducts = export.getExportProducts();
        if (exportProducts!=null&&exportProducts.size()>0){
            for (ExportProduct exportProduct : exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public PageInfo<Export> findByPage(ExportExample example, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Export> list = exportDao.selectByExample(example);
        return new PageInfo<>(list);
    }
}
