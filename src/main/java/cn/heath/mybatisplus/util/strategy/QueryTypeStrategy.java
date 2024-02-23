package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface QueryTypeStrategy {


    <T> void buildQuery(CustomerQuery customerQuery,
                        Field field,
                        QueryWrapper<T> queryWrapper);
}
