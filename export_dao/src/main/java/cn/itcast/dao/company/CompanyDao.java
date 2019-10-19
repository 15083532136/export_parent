package cn.itcast.dao.company;

import cn.itcast.domain.company.Company;

import java.util.List;

public interface CompanyDao {

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
}
