package cn.itcast.service.system.impl;

import cn.itcast.dao.system.SysLogDao;
import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.SysLogExample;
import cn.itcast.service.system.SysLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/10 22:00
 * @description：日志业务实现类
 * @modified By：
 * @version: 1.0$
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public PageInfo<SysLog> findPageInfo(String companyId, Integer pageNum, Integer pageSize) {
        SysLogExample example = new SysLogExample();
        SysLogExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(companyId);
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<SysLog>(sysLogDao.selectByExample(example));
    }

    @Override
    public void save(SysLog log) {
        log.setId(UUID.randomUUID().toString());
        sysLogDao.insertSelective(log);
    }
}
