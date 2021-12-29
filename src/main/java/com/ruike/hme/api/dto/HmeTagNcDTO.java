package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * 不良审核
 *
 * @author chaonan.hu@hand-china.com 2020-07-20 14:59:18
 */
@Data
public class HmeTagNcDTO implements Serializable {

    private static final long serialVersionUID = -5486518487482864593L;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;
    @ApiModelProperty(value = "主键ID")
    private String tagNcId;
    @ApiModelProperty(value = "数据组ID")
    private String tagGroupId;
    @ApiModelProperty(value = "数据组编码")
    private String tagGroupCode;
    @ApiModelProperty(value = "数据组描述")
    private String tagGroupDesc;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "工艺编码")
    private String operationCode;
    @ApiModelProperty(value = "工艺描述")
    private String operationDesc;
    @ApiModelProperty(value = "数据项ID")
    private String tagId;
    @ApiModelProperty(value = "数据项编码")
    private String tagCode;
    @ApiModelProperty(value = "数据项描述")
    private String tagDesc;
    @ApiModelProperty(value = "数据类型")
    @LovValue(lovCode = "HME.TAG_VALUE_TYPE", meaningField = "tagTypeMeaning")
    private String tagType;
    @ApiModelProperty(value = "数据类型含义")
    private String tagTypeMeaning;
    @ApiModelProperty(value = "最小值")
    private String minValue;
    @ApiModelProperty(value = "最大值")
    private String maxValue;
    @ApiModelProperty(value = "优先级")
    private String priority;
    @ApiModelProperty(value = "不良组ID")
    private String ncCodeGroupId;
    @ApiModelProperty(value = "不良组编码")
    private String ncCodeGroupCode;
    @ApiModelProperty(value = "不良组描述")
    private String ncCodeGroupDesc;
    @ApiModelProperty(value = "不良代码ID")
    private String ncCodeId;
    @ApiModelProperty(value = "不良代码")
    private String ncCode;
    @ApiModelProperty(value = "不良代码描述")
    private String ncCodeDesc;
    @ApiModelProperty(value = "不良处置方式")
    private String ncDealWay;
}
