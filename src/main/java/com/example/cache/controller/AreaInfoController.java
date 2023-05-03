package com.example.cache.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cache.common.SpringContextUtil;
import com.example.cache.entity.AreaInfo;
import com.example.cache.mapper.AreaInfoMapper;
import com.example.cache.service.impl.DoubleCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.Callable;


@Slf4j
@RestController
@RequestMapping("/area")
public class AreaInfoController {
    @Resource
    private AreaInfoMapper areaInfoMapper;

    @GetMapping("/query/area")
    @Cacheable(value = "areaInfo", key = "#areaCode")
    public AreaInfo queryAreaInfoById(@RequestParam Long areaCode) {
        return areaInfoMapper.selectById(areaCode);
    }

    @PostMapping("/update/area")
    @CachePut(cacheNames = "areaInfo", key = "#updateInfo.areaCode")
    public AreaInfo updateAreaInfo(AreaInfo updateInfo) {
        areaInfoMapper.updateById(updateInfo);
        return updateInfo;
    }

    @PutMapping("/delete/area")
    @CacheEvict(cacheNames = "areaInfo", key = "#areaCode")
    public int deleteAreaInfo(Long areaCode) {
        return areaInfoMapper.deleteById(areaCode);
    }

    public AreaInfo getOrderByAreaCode(Long areaCode) {
        DoubleCacheManager cacheManager = SpringContextUtil.getBean(DoubleCacheManager.class);
        Cache cache = cacheManager.getCache("order");
        AreaInfo areaInfo =(AreaInfo) cache.get(areaCode, (Callable<Object>) () -> {
            log.info("get data from database");
            AreaInfo area = areaInfoMapper.selectOne(new LambdaQueryWrapper<AreaInfo>()
                    .eq(AreaInfo::getAreaCode, areaCode));
            return area;
        });
        return areaInfo;
    }


}
