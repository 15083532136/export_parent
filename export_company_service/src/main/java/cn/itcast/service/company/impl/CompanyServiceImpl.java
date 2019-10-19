package cn.itcast.service.company.impl;

import cn.itcast.dao.company.CompanyDao;
import cn.itcast.domain.company.Company;
import cn.itcast.service.company.CompanyService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    /**
     * 1.查询所有 Company
     * @return cn.itcast.domain.company.Company
     */
    @Override
    public List<Company> findAll() {

        return companyDao.findAll();
    }

    /**
     * 2.查找单个Company
     * @param id
     * @return Company
     */
    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    /**
     * 3.添加Company
     * @param company company
     */
    @Override
    public void save(Company company) {
        //设置id
        company.setId(UUID.randomUUID().toString());
        companyDao.save(company);
    }

    /**
     * 4.修改Company
     * @param company company
     */
    @Override
    public void update(Company company) {
        companyDao.update(company);
    }

    /**
     * 5.删除
     * @param id company id
     */
    @Override
    public void delete(String id) {
        companyDao.delete(id);
    }

    /**
     * 6.分页
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @return 封装的当前页对象
     */
    @Override
    public PageInfo<Company> findByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<Company>(companyDao.findAll());
    }
}
