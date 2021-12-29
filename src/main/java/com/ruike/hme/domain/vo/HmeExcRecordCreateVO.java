package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 异常处理平台-异常创建VO
 *
 * @author Deng xu
 * @date 2020/6/23 11:05
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HmeExcRecordCreateVO implements Serializable {

    private static final long serialVersionUID = -6242999203256942232L;

    @ApiModelProperty(value = "用户默认站点ID")
    private String siteId;

    @ApiModelProperty(value = "在制品sn")
    private String snNum;

    @ApiModelProperty(value = "在制品eo")
    private String eoId;

    @ApiModelProperty("异常ID")
    private String exceptionId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("设备编码")
    private String equipmentCode;

    @ApiModelProperty("设备ID")
    private String equipmentId;

    @ApiModelProperty("物料条码")
    private String materialLotCode;

    @ApiModelProperty("物料条码ID")
    private String materialLotId;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("异常备注")
    private String exceptionRemark;

    @ApiModelProperty("附件ID")
    private String attachmentUuid;

    @ApiModelProperty("异常组ID")
    private String exceptionGroupId;

    @ApiModelProperty("记录ID")
    private String exceptionWkcRecordId;

    @ApiModelProperty("发起类型，区域-AREA、车间-WORKSHOP、产线-PROD_LINE、工位-WKC")
    private String initiationType;




}
