package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MP提供的自动填充配置
 */
@Slf4j
@Component
public class MetaHandler implements MetaObjectHandler {
    /**
     * 插入填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert......");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",ReggieThreadLocal.getCurrentThreadLocal());
        metaObject.setValue("createUser",ReggieThreadLocal.getCurrentThreadLocal());
    }

    /**
     * 修改填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update......");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",ReggieThreadLocal.getCurrentThreadLocal());
    }
}