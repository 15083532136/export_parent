package cn.itcast.service.system.impl;

import cn.itcast.dao.system.DeptDao;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.DeptExample;
import cn.itcast.service.system.DeptService;
import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门业务实现
 */
@Service
public class DeptServiceImpl implements DeptService {

    /**
     * 接口自动注入
     */
    @Autowired
    private DeptDao deptDao;

    /**
     * 1.查询所有部门 Dept
     * @return cn.itcast.domain.system.Dept
     */
    @Override
    public List<Dept> findAll(DeptExample deptExample) {
        List<Dept> deptList = deptDao.selectByExample(deptExample);
        for (Dept dept : deptList) {
            DeptExample example = new DeptExample();
            DeptExample.Criteria criteria = example.createCriteria();
            if (!StringUtils.isEmpty(dept.getParentId())) {
                criteria.andParentIdEqualTo(dept.getParentId());
            }
            List<Dept> parent = deptDao.selectByExample(example);
            dept.setParent(parent.get(0));
        }
        return deptList;
    }

    /**
     * 2.分页
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @param deptExample 动态条件
     * @return 封装的当前页对象
     */
    @Override
    public PageInfo<Dept> findByPage(DeptExample deptExample,Integer pageNum, Integer pageSize) {
        //开始分页
        PageHelper.startPage(pageNum,pageSize);
        List<Dept> list = deptDao.selectByExample(deptExample);
        return new PageInfo<>(list);
    }

    @Override
    public Dept findById(String id) {

        Dept dept = deptDao.selectByPrimaryKey(id);
        return dept;
    }

    @Override
    public void save(Dept dept) {
        deptDao.insertSelective(dept);
    }

    @Override
    public void update(Dept dept) {
       deptDao.updateByPrimaryKeySelective(dept);
    }

    @Override
    public Boolean delete(String id) {
        //通过id获取该部门的子部门
        DeptExample deptExample = new DeptExample();
        DeptExample.Criteria criteria = deptExample.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<Dept> depts = deptDao.selectByExample(deptExample);
        //判断该部门是否有子部门
        if (depts == null || depts.size() == 0){
            //没有子部门，可以直接删除
            deptDao.deleteByPrimaryKey(id);
            return true;
        }
        return false;
    }
}
