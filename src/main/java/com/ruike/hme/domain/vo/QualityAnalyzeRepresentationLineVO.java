package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/8 11:01
 */
@Data
public class QualityAnalyzeRepresentationLineVO {
    @ApiModelProperty(value = "序号")
    private Integer seqNum;

    @ApiModelProperty(value = "单据ID")
    private String qaDocId;

    @ApiModelProperty(value = "类型")
    @LovValue(lovCode = "HME.QUALITY_ANALYZE_TYPE", meaningField = "qaTypeMeaning")
    private String qaType;

    @ApiModelProperty(value = "类型含义")
    private String qaTypeMeaning;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;

    @ApiModelProperty(value = "物料批")
    private String materialLotCode;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "结果列表")
    private List<String> resultList;
}
