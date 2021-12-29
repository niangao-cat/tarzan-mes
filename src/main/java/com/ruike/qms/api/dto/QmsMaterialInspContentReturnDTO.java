package com.ruike.qms.api.dto;

import com.ruike.qms.domain.entity.QmsMaterialInspContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 物料检验计划行查询
 * @author: han.zhang
 * @create: 2020/04/26 15:35
 */
@Getter
@Setter
@ToString
public class QmsMaterialInspContentReturnDTO extends QmsMaterialInspContent implements Serializable {
    private static final long serialVersionUID = 3329528195032283135L;

    @ApiModelProperty(value = "检验工具含义")
    private String inspectionToolMeaning;

    @ApiModelProperty(value = "检验项类别含义")
    private String inspectionTypeMeaning;

    @ApiModelProperty(value = "缺陷等级")
    private String defectLevelMeaning;

    @ApiModelProperty(value = "规格类型含义")
    private String standardTypeMeaning;

    @ApiModelProperty(value = "检验方法含义")
    private String inspectionMethodMeaning;

    @ApiModelProperty(value = "抽样方式编码")
    private String sampleTypeCode;

    @ApiModelProperty(value = "检验值从字符型")
    private String standardFromStr;

    @ApiModelProperty(value = "检验值至字符型")
    private String standardFromTo;
}