package com.example.cache.controller;

import com.example.cache.entity.AreaInfo;
import com.example.cache.service.AreaInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping("/area")
public class AreaInfoController {
    @Resource
    private AreaInfoService areaInfoService;

    @GetMapping("/query/area")
    public AreaInfo queryAreaInfoById(@RequestParam Long areaCode) {
        return areaInfoService.getAreaByAreaCode(areaCode);
    }

    @PostMapping("/update/area")
    public int updateAreaInfo(AreaInfo updateInfo) {
        return areaInfoService.updateAreaInfo(updateInfo);
    }

    @PutMapping("/delete/area")
    public int deleteAreaInfo(Long areaCode) {
        return areaInfoService.deleteAreaByAreaCode(areaCode);
    }


}
