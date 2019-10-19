package cn.itcast.service.system;

import cn.itcast.domain.system.SysLog;
import com.github.pagehelper.PageInfo;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/10 21:58
 * @description：日志业务层
 * @modified By：
 * @version: 1.0$
 */
public interface SysLogService {

    /**
     * 查询全部
     * @Method: findAll
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param companyId cn.itcast.domain.company companyId
     * @param pageNum 当前页
     * @param pageSize 页面大小
     * @Return List<SysLog>
     * @Exception
     * @Date 2019/10/10 21:56
     */
    PageInfo<SysLog> findPageInfo(String companyId, Integer pageNum, Integer pageSize);

    /**
     * 添加
     * @Method: save
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param log cn.itcast.domain.system.SysLog
     * @Return void
     * @Exception
     * @Date 2019/10/10 21:57
     */
    void save(SysLog log);
}
