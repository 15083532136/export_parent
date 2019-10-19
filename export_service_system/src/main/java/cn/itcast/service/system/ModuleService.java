package cn.itcast.service.system;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.ModuleExample;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author lenovo
 * @date 2019年10月8日21:05:42
 */
public interface ModuleService {

    /**
     * 1.查询所有模块 Module
     * @return cn.itcast.domain.system.Module
     */
    List<Module> findAll(ModuleExample moduleExample);

    /**
     * 2.分页
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @param moduleExample moduleExample
     * @return 封装的当前页对象
     */
    PageInfo<Module> findByPage(ModuleExample moduleExample, Integer pageNum, Integer pageSize);

    /**
     * 通过id查找模块
     * @param id
     * @return Module 模块
     * @date 2019/10/7 21:45
     * @auther lenovo
     */
    Module findById(String id);

    /**
     * 添加
     * @param dept
     * @return void
     * @date 2019/10/7 21:54
     * @auther lenovo
     */
    void save(Module dept);

    /**
     * 修改
     * @param dept
     * @return void
     * @date 2019/10/7 21:55
     * @auther lenovo
     */
    void update(Module dept);

    /**
     * 删除
     * @param id
     * @return Boolean
     * @date 2019/10/7 22:12
     * @auther lenovo
     */
    Boolean delete(String id);

    /**
     * 当类型的选项发生改变时，它的上级模块也要改变
     * @param ctype
     * @return
     */
    List<Module> findParentModule(String ctype);

    /**
     * 8.上级部门的下拉框被选定时，类型要被确定
     * @param id 父模块id
     * @return String ctype
     */
    String findCtypeByParentModuleId(String id);

    /**
     * 通过从属关系来获取权限列表
     * @Method: findByBelong
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param belong 从属关系
     * @Return List<Module> 权限列表
     * @Exception 
     * @Date 2019/10/10 18:14
     */
    List<Module> findByBelong(Integer belong);
}
