package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/17 11:38
 */
@Data
public class HmeMtTagVO implements Serializable {

    private static final long serialVersionUID = 570680027683131869L;

    @ApiModelProperty(value = "数据项ID")
    private String tagId;
    @ApiModelProperty(value = "数据项编码")
    private String tagCode;
    @ApiModelProperty(value = "数据项描述")
    private String tagDescription;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否启用")
    private String enableFlag;
    @ApiModelProperty(value = "数据收集方式")
    private String collectionMethod;
    @ApiModelProperty(value = "数据类型")
    private String valueType;
    @ApiModelProperty(value = "符合值")
    private String trueValue;
    @ApiModelProperty(value = "不符合值")
    private String falseValue;
    @ApiModelProperty(value = "最小值")
    private Double minimumValue;
    @ApiModelProperty(value = "最大值")
    private Double maximalValue;
    @ApiModelProperty(value = "计量单位")
    private String unit;
    @ApiModelProperty(value = "允许缺失值")
    private String valueAllowMissing;
    @ApiModelProperty(value = "必需的数据条数")
    private Long mandatoryNum;
    @ApiModelProperty(value = "可选的数据条数")
    private Long optionalNum;
    @ApiModelProperty(value = "转化API_ID")
    private String apiId;
    @ApiModelProperty(value = "最新一次新增历史表的主键")
    private String latestHisId;
    @ApiModelProperty(value = "CID")
    private Long cid;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "精度")
    private String accuracy;
    @ApiModelProperty(value = "标准值")
    private String standardValue;
}
