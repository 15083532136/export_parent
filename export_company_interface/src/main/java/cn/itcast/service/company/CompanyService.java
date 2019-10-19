package cn.itcast.service.company;

import cn.itcast.domain.company.Company;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 公司的独立接口工程
 * @author 屈雪耀
 * @date 2019年10月13日19:50:56
 */
public interface CompanyService {

    /**
     * 1.查询所有 Company
     * @return cn.itcast.domain.company.Company
     */
    List<Company> findAll();

    /**
     * 2.查找单个Company
     * @param id
     * @return Company
     */
    Company findById(String id);

    /**
     * 3.添加Company
     * @param company company
     */
    void save(Company company);

    /**
     * 4.修改Company
     * @param company company
     */
    void update(Company company);

    /**
     * 5.删除
     * @param id company id
     */
    void delete(String id);

    /**
     * 6.分页
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @return 封装的当前页对象
     */
    PageInfo<Company> findByPage(Integer pageNum, Integer pageSize);
}
