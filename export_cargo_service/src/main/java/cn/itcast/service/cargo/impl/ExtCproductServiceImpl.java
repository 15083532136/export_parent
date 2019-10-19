package cn.itcast.service.cargo.impl;

import cn.itcast.dao.cargo.ContractDao;
import cn.itcast.dao.cargo.ExtCproductDao;
import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;
import cn.itcast.service.cargo.ExtCproductService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/17 19:10
 * @description：货物的附件模块
 * @modified By：
 * @version: 1.0$
 */
@Service(timeout = 100000)
public class ExtCproductServiceImpl implements ExtCproductService {

    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ContractDao contractDao;


    @Override
    public PageInfo<ExtCproduct> findByPage(ExtCproductExample extCproductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ExtCproduct> list = extCproductDao.selectByExample(extCproductExample);
        return new PageInfo<ExtCproduct>(list);
    }

    @Override
    public List<ExtCproduct> findAll(ExtCproductExample extCproductExample) {
        return extCproductDao.selectByExample(extCproductExample);
    }

    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    /**
     * 增加附件
     *  1.设置附件的总金额
     *  2.更新购销合同的数量，总金额
     * @param extCproduct
     */
    @Override
    public void save(ExtCproduct extCproduct) {
        //1.设置id
        extCproduct.setId(UUID.randomUUID().toString());
        //2.更新附件的总金额
        BigDecimal amount = BigDecimal.ZERO;
        //2.1 设置更新条件
        Integer cnumber = extCproduct.getCnumber();
        BigDecimal price = extCproduct.getPrice();
        extCproduct.setAmount(BigDecimal.ZERO);
        Boolean flag = cnumber!=null&&cnumber>0&&price!=null&&(price.compareTo(BigDecimal.ZERO)>0)?true:false;
        if (flag){
            //更新附件的金额
            amount = price.multiply(new BigDecimal(cnumber));
            extCproduct.setAmount(amount);
            extCproductDao.insertSelective(extCproduct);
        }
        //更新购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(amount.add(contract.getTotalAmount()));
        contract.setExtNum(contract.getExtNum()+1);
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 修改附件
     *  1.更新附件的总金额
     *  2.更新购销合同的总金额
     * @param extCproduct
     */
    @Override
    public void update(ExtCproduct extCproduct) {
        //1.1更新前的总金额
        BigDecimal oldAmount = extCproduct.getAmount();
        //1.2更新后的总金额
        BigDecimal amount = BigDecimal.ZERO;
        //2.1 设置更新条件
        Integer cnumber = extCproduct.getCnumber();
        BigDecimal price = extCproduct.getPrice();
        Boolean flag = cnumber!=null&&cnumber>0&&price!=null&&(price.compareTo(BigDecimal.ZERO)>0)?true:false;
        if (flag){
            //更新附件的金额
            amount = price.multiply(new BigDecimal(cnumber));
            extCproduct.setAmount(amount);
            extCproductDao.updateByPrimaryKeySelective(extCproduct);
        }
        //3.更新购销合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount().add(amount).subtract(oldAmount));
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除附件
     * 1.删除附件
     * 2.更新购销合同的总金额和数量
     * @param id
     */
    @Override
    public void delete(String id) {
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        //附件的总金额
        BigDecimal amount = extCproduct.getAmount();
        //附件中购销合同的信息
        String contractId = extCproduct.getContractId();
        Contract contract = contractDao.selectByPrimaryKey(contractId);
        //删除附件
        extCproductDao.deleteByPrimaryKey(id);
        //更新购销合同
        contract.setTotalAmount(contract.getTotalAmount().subtract(amount));
        contract.setExtNum(contract.getExtNum()-1);
        contractDao.updateByPrimaryKeySelective(contract);
    }
}
