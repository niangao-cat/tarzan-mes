package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.common.query.Where;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 外协发货扫描物料返回的vo
 * @author: han.zhang
 * @create: 2020/06/22 11:17
 */
@Getter
@Setter
@ToString
public class WmsOutSourceMaterialReturnVO implements Serializable {
    private static final long serialVersionUID = 2371997801482827284L;

    @ApiModelProperty("物料批id")
    private String materialLotId;
    @ApiModelProperty(value = "描述该物料批的唯一编码，用于方便识别")
    private String materialLotCode;
    @ApiModelProperty("物料id")
    private String materialId;
    @ApiModelProperty(value = "物料编号")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料批所标识实物的质量状态：")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;
    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;
    @ApiModelProperty(value = "该物料批表示实物的主计量单位")
    private String primaryUomId;
    @ApiModelProperty(value = "实物在主计量单位下的数量")
    private Double primaryUomQty;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    private String uomName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "指令状态")
    @LovValue(lovCode = "WMS.OUTSOURCING_LINE_STATUS",meaningField="instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty(value = "单据状态含义")
    private String instructionStatusMeaning;
    @ApiModelProperty(value = "取料货位Id")
    private String getMaterialLocatorId;
    @ApiModelProperty(value = "取料货位编码")
    private String getMaterialLocatorCode;

    @ApiModelProperty(value = "返回结果标识，1代表进入拆分界面，2代表弹窗，3代表逻辑正常跑完")
    private String popWindowFlag;

    @ApiModelProperty(value = "任务号")
    private String taskNum;

    @ApiModelProperty(value = "亮灯接口成功/失败状态")
    private String status;

    @ApiModelProperty(value = "亮灯接口返回状态")
    private String taskStatus;

    @ApiModelProperty(value = "行Id")
    private String instructionId;
}