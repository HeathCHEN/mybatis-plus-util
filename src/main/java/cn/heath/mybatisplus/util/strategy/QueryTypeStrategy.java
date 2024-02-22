package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;
import java.util.Map;

public interface QueryTypeStrategy {


    public <T> void buildQuery(CustomerQuery customerQuery ,
                               QueryWrapper<T> queryWrapper,
                               Map<String, Object> objectMap,
                               Object value,
                               String[] orColumns,
                               String underlineCase,
                               List<String> usedProperties);
}
