package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/5/7 15:01
 */
@Data
public class HmeEquipmentWorkingVO4 implements Serializable {

    @ApiModelProperty(value = "总页数")
    private int totalPages;
    @ApiModelProperty(value = "总大小")
    private long totalElements;
    @ApiModelProperty(value = "当前页数量")
    private int numberOfElements;
    @ApiModelProperty(value = "大小")
    private int size;
    @ApiModelProperty(value = "页码")
    private int number;
    @ApiModelProperty(value = "内容")
    private List<HmeEquipmentWorkingVO> content;
    @ApiModelProperty(value = "汇总信息")
    private List<HmeEquipmentWorkingVO5> summaryList;
}
