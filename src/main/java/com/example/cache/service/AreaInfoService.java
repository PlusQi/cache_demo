package com.example.cache.service;

import com.example.cache.entity.AreaInfo;

/**
 *
 * @author PlusQi
 */
public interface AreaInfoService {

    /**
     * 根据区域代码获取地区信息
     * @param areaCode 区域代码
     * @return
     */
    AreaInfo getAreaByAreaCode(Long areaCode);

    /**
     * 修改地区信息
     * @param areaInfo 地区信息修改内容
     * @return
     */
    int updateAreaInfo(AreaInfo areaInfo);

    /**
     * 根据区域代码删除地区
     * @param areaCode 区域代码
     * @return
     */
    int deleteAreaByAreaCode(Long areaCode);

    /**
     * 不使用注解，通过DoubleCache.get(key, callable)获取缓存
     * @param areaCode 区域代码
     * @return
     */
    AreaInfo getAreaByAreaCode2(Long areaCode);
}
