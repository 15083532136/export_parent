package cn.itcast.dao.cargo;

import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproduct;
import cn.itcast.domain.cargo.ExtCproductExample;

import java.util.List;

public interface ExtCproductDao {

	/**
	 * 删除
	 * @param id id
	 * @return int
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * 动态保存
	 * @param record ExtCproduct
	 * @return int
	 */
	int insertSelective(ExtCproduct record);

	/**
	 * 条件查询
	 * @param example ExtCproductExample
	 * @return List<ExtCproduct>
	 */
	List<ExtCproduct> selectByExample(ExtCproductExample example);

	/**
	 * id查询
	 * @param id id
	 * @return ExtCproduct
	 */
	ExtCproduct selectByPrimaryKey(String id);

	/**
	 * 动态更新
	 * @param record ExtCproduct
	 * @return int
	 */
	int updateByPrimaryKeySelective(ExtCproduct record);

}