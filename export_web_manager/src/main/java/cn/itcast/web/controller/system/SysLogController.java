package cn.itcast.web.controller.system;

import cn.itcast.domain.system.SysLog;
import cn.itcast.service.system.SysLogService;
import cn.itcast.web.controller.BaseController;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @date 2019/10/7 21:43
 * @auther lenovo
 */
@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {

    /**
     * 自动注入接口
     */
    @Autowired
    private SysLogService sysLogService;

    /**
     * @param pageNum 当前页 默认值1
     * @param pageSize 页大小 默认10
     * @return "system/user/user-list"
     * @date 2019/10/10 20:37
     * @auther 屈雪耀
     */
    @RequestMapping("/list")
    protected String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize){
        //获取当前页的内容
        PageInfo<SysLog> page = sysLogService.findPageInfo(getLoginCompanyId(), pageNum, pageSize);
        //将获取的页面数据封装到域中
        request.setAttribute("page",page);
        return "system/log/log-list";
    }
}
