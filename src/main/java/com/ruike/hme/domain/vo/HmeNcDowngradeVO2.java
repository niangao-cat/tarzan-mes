package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeNcDowngradeVO2
 *
 * @author: chaonan.hu@hand-china.com 2021-05-18 15:21:01
 **/
@Data
public class HmeNcDowngradeVO2 implements Serializable {
    private static final long serialVersionUID = -7703210426066936365L;

    @ApiModelProperty(value = "主键历史ID")
    private String downgradeHisId;

    @ApiModelProperty(value = "主表ID")
    private String downgradeId;

    @ApiModelProperty(value = "产品ID")
    private String materialId;

    @ApiModelProperty(value = "产品编码")
    private String materialCode;

    @ApiModelProperty(value = "产品描述")
    private String materialName;

    @ApiModelProperty(value = "降级代码ID")
    private String ncCodeId;

    @ApiModelProperty(value = "降级代码")
    private String ncCode;

    @ApiModelProperty(value = "降级代码描述")
    private String description;

    @ApiModelProperty(value = "降级物料ID")
    private String transitionMaterialId;

    @ApiModelProperty(value = "降级物料编码")
    private String transitionMaterialCode;

    @ApiModelProperty(value = "降级物料描述")
    private String transitionMaterialName;

    @ApiModelProperty(value = "有效性")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "有效性含义")
    private String enableFlagMeaning;

    @ApiModelProperty(value = "变更时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty(value = "变更人ID")
    private String createdBy;

    @ApiModelProperty(value = "变更人姓名")
    private String createdByName;
}
