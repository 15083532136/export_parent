package cn.itcast.web.controller.stat;

import cn.itcast.Service.stat.StatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/20 19:43
 * @description：图形报表
 * @modified By：
 * @version: 1.0$
 */
@Controller
@RequestMapping("/stat")
public class StatController {

    @Reference
    private StatService statService;

    /**
     * 统计分析页面
     * @param chartsType chartsType
     * @return String
     */
    @RequestMapping("/toCharts")
    public String toCharts(String chartsType){

        return "stat/stat-"+chartsType;
    }

    /**
     * 生产厂家销售统计
     * @return List<Map<String,Object>>
     */
    @RequestMapping("/factorySale")
    @ResponseBody
    public List<Map<String, Object>> factorySale(){
        List<Map<String, Object>> factorySale = statService.factorySale();
        return factorySale;
    }

    /**
     * 商品销售统计排行
     * @return List<Map<String, Object>>
     */
    @RequestMapping("/sellSale")
    @ResponseBody
    public List<Map<String, Object>> sellSale(){
        List<Map<String, Object>> sellSale = statService.sellSale(5);
        return sellSale;
    }

    /**
     * 生产厂家销售统计
     * @return List<Map<String,Object>>
     */
    @RequestMapping("/online")
    @ResponseBody
    public List<Map<String, Object>> online(){
        List<Map<String, Object>> online = statService.online();
        return online;
    }
}
