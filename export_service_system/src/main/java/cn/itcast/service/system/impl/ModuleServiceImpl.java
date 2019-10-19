package cn.itcast.service.system.impl;

import cn.itcast.dao.system.ModuleDao;
import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.ModuleExample;
import cn.itcast.service.system.ModuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


/**
 * 模块业务实现
 *
 * @author lenovo
 * @date 2019年10月8日21:05:42
 */
@Service
public class ModuleServiceImpl implements ModuleService {

    /**
     * 接口自动注入
     */
    @Autowired
    private ModuleDao moduleDao;

    /**
     * 1.查询所有模块 Module
     *
     * @return cn.itcast.domain.system.Module
     */
    @Override
    public List<Module> findAll(ModuleExample moduleExample) {
        return moduleDao.selectByExample(moduleExample);
    }

    /**
     * 2.分页
     * @param pageNum   当前页
     * @param pageSize  页面大小
     * @return 封装的当前页对象
     */
    @Override
    public PageInfo<Module> findByPage(ModuleExample moduleExample,Integer pageNum, Integer pageSize) {
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        List<Module> list = moduleDao.selectByExample(moduleExample);
        return new PageInfo<>(list);
    }

    @Override
    public Module findById(String id) {
        return  moduleDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Module module) {
        //设置模块的id
        module.setModuleId(UUID.randomUUID().toString());
        //执行修改操作
        moduleDao.insertSelective(module);
    }

    @Override
    public void update(Module module) {
        //执行修改操作
        moduleDao.updateByPrimaryKeySelective(module);
    }

    @Override
    public Boolean delete(String id) {
        //1.查询要删除的模块是否有子模块
        ModuleExample moduleExample = new ModuleExample();
        ModuleExample.Criteria criteria = moduleExample.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<Module> moduleList = moduleDao.selectByExample(moduleExample);
        if (moduleList== null || moduleList.size() == 0){
            //如果为空或者没有元素，说明该模块没有子模块，可以删除
            moduleDao.deleteByPrimaryKey(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Module> findParentModule(String ctype) {
        ModuleExample moduleExample = new ModuleExample();
        ModuleExample.Criteria criteria = moduleExample.createCriteria();
        criteria.andCtypeEqualTo(Long.parseLong(ctype)-1);
        List<Module> moduleList ;
        switch (ctype) {
            case "0":
                moduleList = null;
                break;
            default:
                moduleList = moduleDao.selectByExample(moduleExample);
                break;
        }
        return moduleList;
    }

    @Override
    public String findCtypeByParentModuleId(String id) {
        Module module = moduleDao.selectByPrimaryKey(id);
        String ctype = (module.getCtype()+1) + "";
        return ctype;
    }

    @Override
    public List<Module> findByBelong(Integer belong) {
        ModuleExample moduleExample = new ModuleExample();
        ModuleExample.Criteria criteria = moduleExample.createCriteria();
        criteria.andBelongEqualTo(belong+"");
        List<Module> moduleList = moduleDao.selectByExample(moduleExample);
        return moduleList;
    }
}
