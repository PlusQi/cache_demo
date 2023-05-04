package com.example.cache.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cache.cache.impl.DoubleCacheManager;
import com.example.cache.util.SpringContextUtil;
import com.example.cache.entity.AreaInfo;
import com.example.cache.mapper.AreaInfoMapper;
import com.example.cache.service.AreaInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Callable;

/**
 * AreaInfo业务实现类
 * @author PlusQi
 */
@Slf4j
@Service
public class AreaInfoServiceImpl implements AreaInfoService {

    @Resource
    private AreaInfoMapper areaInfoMapper;


    @Override
    @Cacheable(cacheNames = "areaInfo", key = "#areaCode")
    public AreaInfo getAreaByAreaCode(Long areaCode) {
        return areaInfoMapper.selectById(areaCode);
    }

    @Override
    @CachePut(cacheNames = "areaInfo", key = "#areaInfo.areaCode")
    public int updateAreaInfo(AreaInfo areaInfo) {
        return areaInfoMapper.updateById(areaInfo);
    }

    @Override
    @CacheEvict(cacheNames = "areaInfo", key = "#areaCode")
    public int deleteAreaByAreaCode(Long areaCode) {
        return areaInfoMapper.deleteById(areaCode);
    }

    @Override
    public AreaInfo getAreaByAreaCode2(Long areaCode) {
        DoubleCacheManager cacheManager = SpringContextUtil.getBean(DoubleCacheManager.class);

        Cache cache = cacheManager.getCache("areaInfo");
        AreaInfo areaInfo =(AreaInfo) cache.get(areaCode, (Callable<Object>) () -> {
            log.info("get data from database");
            AreaInfo area = areaInfoMapper.selectOne(new LambdaQueryWrapper<AreaInfo>()
                    .eq(AreaInfo::getAreaCode, areaCode));
            return area;
        });

        return areaInfo;
    }

}
