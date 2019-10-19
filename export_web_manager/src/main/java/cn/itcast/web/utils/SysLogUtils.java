package cn.itcast.web.utils;

import cn.itcast.domain.system.SysLog;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.SysLogService;
import cn.itcast.web.controller.BaseController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/11 8:03
 * @description：日志自动记录
 * @modified By：
 * @version: 1.0$
 */
@Component
@Aspect
public class SysLogUtils extends BaseController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 自动记录日志
     * @Method: saveSysLog
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param joinPoint 要执行的方法
     * @Return java.lang.Object
     * @Exception
     * @Date 2019/10/11 8:08
     */
    @Around("execution(* cn.itcast.web.controller..*.*(..))")
    public Object saveSysLog(ProceedingJoinPoint joinPoint){

        SysLog sysLog = new SysLog();

        Object object;
        try {
            //要执行的方法
            object = joinPoint.proceed();
            User user = (User) session.getAttribute("user");
            if (user != null){
                sysLog.setUserName(user.getUserName());
                sysLog.setCompanyId(user.getCompanyId());
                sysLog.setCompanyName(user.getCompanyName());
            }
            //用户名
            Object target = joinPoint.getTarget();
            String className = target.getClass().getName();
            //执行的方法的全类名

            sysLog.setAction(className);
            //执行方法的IP
            sysLog.setIp(request.getRemoteAddr());
            //要执行的方法
            Signature signature = joinPoint.getSignature();
            sysLog.setMethod(signature.getName());
            //方法执行的时间
            sysLog.setTime(new Date());
            //自动保存
            sysLogService.save(sysLog);
            return object;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //return "forward:/login.jsp";
            return null;
        }
    }
}
