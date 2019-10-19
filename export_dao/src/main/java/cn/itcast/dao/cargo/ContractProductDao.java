package cn.itcast.dao.cargo;

import cn.itcast.domain.cargo.ContractProduct;
import cn.itcast.domain.cargo.ContractProductExample;
import cn.itcast.vo.ContractProductVo;

import java.util.List;

public interface ContractProductDao {

    /**
     * 删除
     * @param id id
     * @return int
     */
    int deleteByPrimaryKey(String id);

    /**
     * 动态保存
     * @param record ContractProduct
     * @return int
     */
    int insertSelective(ContractProduct record);

    /**
     * 条件查询
     * @param example ContractProductExample
     * @return List<ContractProduct>
     */
    List<ContractProduct> selectByExample(ContractProductExample example);

    /**
     * id查询
     * @param id id
     * @return ContractProduct
     */
    ContractProduct selectByPrimaryKey(String id);

    /**
     * 动态更新
     * @param record ContractProduct
     * @return int
     */
    int updateByPrimaryKeySelective(ContractProduct record);

    List<ContractProductVo> findByShipTime(String companyId,String inputDate);
}