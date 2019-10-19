package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ContractProductDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.*;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.vo.ContractProductVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/14 19:40
 * @description：购销合同货物模块
 * @modified By：
 * @version: 1$
 */
@Service(timeout = 100000)
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public PageInfo<ContractProduct> findByPage(ContractProductExample contractProductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ContractProduct> contractProductList = contractProductDao.selectByExample(contractProductExample);
        return new PageInfo<>(contractProductList);
    }

    @Override
    public List<ContractProduct> findAll(ContractProductExample contractProductExample) {
        return contractProductDao.selectByExample(contractProductExample);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ContractProduct contractProduct) {
        //1.设置货物id
        contractProduct.setId(UUID.randomUUID().toString());
        //2.获得货物所属的购销合同的信息
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //3.更新货物的总金额
        ContractProduct product = setContractProductAmount(contractProduct);
        //6.更新购销合同的信息
        contract.setProNum(contract.getProNum()+1);
        contract.setTotalAmount(contract.getTotalAmount().add(product.getAmount()));
        //7.保存货物信息和更新购销合同
        contractProductDao.insertSelective(contractProduct);
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        //1.获得货物所属的购销合同的信息
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //2.获取货物旧的总金额
        BigDecimal oldAmount = contractProduct.getAmount();
        //3.获取货物新的总金额
        ContractProduct product = setContractProductAmount(contractProduct);
        //4.更新货物和购销合同
        contractProductDao.updateByPrimaryKeySelective(product);
        contract.setTotalAmount(contract.getTotalAmount().subtract(oldAmount).add(product.getAmount()));
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //1.获取货物和购销合同、附件的信息
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        ExtCproductExample extCproductExample = new ExtCproductExample();
        extCproductExample.createCriteria().andContractIdEqualTo(contractProduct.getContractId());
        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        //2.定义变量为要删除的总金额为0
        BigDecimal delAmount = new BigDecimal(0);
        //3.获取要删除的货物和附件的总金额
        delAmount = contractProduct.getAmount();
        if (extCproducts!=null&&extCproducts.size()>0){
            for (ExtCproduct extCproduct : extCproducts) {
                delAmount = delAmount.add(extCproduct.getAmount());
                //删除附件
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
        }
        //4.删除货物
        contractProductDao.deleteByPrimaryKey(id);
        //5.更新购销合同
        contract.setTotalAmount(contract.getTotalAmount().subtract(delAmount));
        contract.setProNum(contract.getProNum()-1);
        contract.setExtNum(contract.getExtNum()-extCproducts.size());
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public List<ContractProductVo> findByShipTime(String companyId,String inputDate) {
        return contractProductDao.findByShipTime(companyId,inputDate);
    }

    /**
     * 获取货物的总金额
     * @Method: setContractProductAmount
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param contractProduct
     * @Return cn.itcast.domain.cargo.ContractProduct
     * @Exception
     * @Date 2019/10/16 20:44
     */
    public ContractProduct setContractProductAmount(ContractProduct contractProduct){
        //3.设置货物的属性 金额=单价 * 数量
        BigDecimal price = contractProduct.getPrice();
        Integer cnumber = contractProduct.getCnumber();
        //4.设置总金额的条件
        Boolean flag = price!=null&&price.compareTo(BigDecimal.ZERO)>=0
                &&cnumber!=null&&cnumber>=0 ? true:false;
        //定义一个变量来保存货物的总金额
        BigDecimal amount = new BigDecimal(0);
        if (flag){
            //5.货物的总金额 = 单价 * 数量
            amount = (new BigDecimal(cnumber)).multiply(price);
            contractProduct.setAmount(amount);
        }
        return contractProduct;
    }
}
