package cn.itcast.dao.cargo;

import cn.itcast.domain.cargo.Contract;
import cn.itcast.domain.cargo.ContractExample;

import java.util.List;

public interface ContractDao {

	/**
     * 1.删除
	 * @Method:
	 * @Author lenovo
	 * @Version  1.0
	 * @Description
	 * @param id
	 * @Return
	 * @Exception
	 * @Date 2019/10/14 19:06
	 */
    int deleteByPrimaryKey(String id);

	/**
     * 动态保存
	 * @Method: insertSelective
	 * @Author lenovo
	 * @Version  1.0
	 * @Description
	 * @param record
	 * @Return int
	 * @Exception
	 * @Date 2019/10/14 19:07
	 */
    int insertSelective(Contract record);

    /**
     * 条件查询
     * @param example ContractExample
     * @return List<Contract>
     */
    List<Contract> selectByExample(ContractExample example);

    /**
     * id查询
     * @param id id
     * @return Contract
     */
    Contract selectByPrimaryKey(String id);

    /**
     * 动态更新
     * @param record
     * @return int
     */
    int updateByPrimaryKeySelective(Contract record);
}