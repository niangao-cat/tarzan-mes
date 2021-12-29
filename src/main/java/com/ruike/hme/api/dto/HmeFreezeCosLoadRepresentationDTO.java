package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * <p>
 * 装载信息冻结战士数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/17 14:03
 */
@Data
public class HmeFreezeCosLoadRepresentationDTO implements Serializable {
    private static final long serialVersionUID = -2100337704578462439L;

    @ApiModelProperty("主键")
    private String materialLotLoadId;
    @ApiModelProperty(value = "序号")
    private Integer sequenceNum;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "行", hidden = true)
    private Long loadRow;
    @ApiModelProperty(value = "列", hidden = true)
    private Long loadColumn;
    @ApiModelProperty(value = "位置")
    private String position;
    @ApiModelProperty(value = "wafer")
    private String wafer;
    @ApiModelProperty(value = "金锡比")
    private String ausnRatio;
    @ApiModelProperty(value = "热忱编码")
    private String hotSinkNum;
    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;
    @ApiModelProperty(value = "筛选规则")
    private String cosRuleCode;
    @ApiModelProperty(value = "数量")
    private Long cosNum;
    @ApiModelProperty(value = "冻结标识")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "freezeFlagMeaning")
    private String freezeFlag;
    @ApiModelProperty(value = "冻结标识含义")
    private String freezeFlagMeaning;
}
