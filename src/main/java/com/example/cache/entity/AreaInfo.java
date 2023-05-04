package com.example.cache.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("area_info")
public class AreaInfo implements Serializable {

    // 地区层级
    public final static int AREA_LEVEL_PROVINCE = 1; // 省
    public final static int AREA_LEVEL_PREFECTURE = 2; // 市
    public final static int AREA_LEVEL_COUNTY = 3; // 县
    public final static int AREA_LEVEL_TOWNSHIP = 4; // 乡
    public final static int AREA_LEVEL_VILLAGE = 5; // 村

    @TableId
    private Long areaCode;
    /**
     * 
     */
    private String areaName;

    /**
     * 地区层级:1(省)2(市)3(县)4(乡)5(村)
     */
    private Integer areaLevel;

    /**
     * 父级行政区代码
     */
    private Long parentCode;


}