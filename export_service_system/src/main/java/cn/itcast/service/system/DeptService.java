package cn.itcast.service.system;
import cn.itcast.domain.system.Dept;
import cn.itcast.domain.system.DeptExample;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface DeptService {

    /**
     * 1.查询所有部门 Dept
     * @return cn.itcast.domain.system.Dept
     */
    List<Dept> findAll(DeptExample deptExample);

    /**
     * 2.分页
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @param deptExample deptExample
     * @return 封装的当前页对象
     */
    PageInfo<Dept> findByPage(DeptExample deptExample, Integer pageNum, Integer pageSize);

    /**
     * 通过id查找部门
     * @param id
     * @return Dept 部门
     * @date 2019/10/7 21:45
     * @auther lenovo
     */
    Dept findById(String id);

    /**
     * 添加
     * @param dept
     * @return void
     * @date 2019/10/7 21:54
     * @auther lenovo
     */
    void save(Dept dept);

    /**
     * 修改
     * @param dept
     * @return void
     * @date 2019/10/7 21:55
     * @auther lenovo
     */
    void update(Dept dept);

    /**
     * 删除
     * @param id
     * @return Boolean
     * @date 2019/10/7 22:12
     * @auther lenovo
     */
    Boolean delete(String id);
}
