package com.example.cache.controller;

import com.example.cache.entity.AreaInfo;
import com.example.cache.mapper.AreaInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/area")
public class AreaInfoController {
    @Resource
    private AreaInfoMapper areaInfoMapper;

    @GetMapping("/query/area")
    public AreaInfo queryAreaInfoById(@RequestParam Long areaCode) {


        return areaInfoMapper.selectById(areaCode);
    }
}
