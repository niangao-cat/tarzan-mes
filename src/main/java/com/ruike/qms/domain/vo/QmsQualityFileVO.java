package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/7 16:21
 */
@Data
public class QmsQualityFileVO implements Serializable {

    private static final long serialVersionUID = -5171464593807535766L;

    @ApiModelProperty(value = "送货单行ID")
    private String deliveryDocLineId;

    @ApiModelProperty(value = "文件地址")
    private String fileUrl;

    @ApiModelProperty(value = "导入类型")
    @LovValue(lovCode = "HME.QUALITY_ANALYZE_TYPE", meaningField = "importTypeMeaning")
    private String importType;

    @ApiModelProperty(value = "导入类型含义")
    private String importTypeMeaning;
}
