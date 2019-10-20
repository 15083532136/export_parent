package cn.itcast.service.stat.impl;

import cn.itcast.Service.stat.StatService;
import cn.itcast.dao.stat.StatDao;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service(timeout = 100000)
public class StatServiceImpl implements StatService {

    @Autowired
    private StatDao statDao;


    @Override
    public List<Map<String, Object>> factorySale() {
        return statDao.factorySale();
    }

    @Override
    public List<Map<String, Object>> sellSale(int top) {
        return statDao.sellSale(top);
    }

    @Override
    public List<Map<String, Object>> online() {
        return statDao.online();
    }
}
