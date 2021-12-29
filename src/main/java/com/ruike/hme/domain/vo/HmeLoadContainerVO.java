package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
* @Classname HmeLoadContainerVO
* @Description 批量完工装箱 外箱条码VO
* @Date  2020/6/5 9:56
* @Created by Deng xu
*/
@Setter
@Getter
@ToString
public class HmeLoadContainerVO implements Serializable {

    private static final long serialVersionUID = -1279650289333405129L;

    /**
        外箱条码信息
     */
    @ApiModelProperty(value = "外箱条码ID")
    private String outerContainerId;

    @ApiModelProperty(value = "外箱条码")
    private String outerContainerCode;

    @ApiModelProperty(value = "外箱条码已装载标识，Y-已装载")
    private String loadFlag;

    @ApiModelProperty(value = "条码个数")
    private Double scanQty;

    @ApiModelProperty(value = "条码数量")
    private Double barCodeQty;

    @ApiModelProperty(value = "扫描条码的类型,CONTAINER-容器，MATERIAL_LOT-物料批")
    private String codeType;

    /**
        待装箱条码
     */
    @ApiModelProperty(value = "箱条码ID")
    private String containerId;

    @ApiModelProperty(value = "箱条码编码")
    private String containerCode;

    @ApiModelProperty(value = "箱条码名称")
    private String containerName;

    @ApiModelProperty(value = "原容器")
    private String orgContainerId;

    /**
        物料批明细信息
     */
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态描述")
    private String qualityStatusDes;

    @ApiModelProperty(value = "工单编号")
    private String workOrderNum;

    @ApiModelProperty(value = "数量")
    private Double primaryUomQty;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "条码货位ID")
    private String locatorId;

    @ApiModelProperty(value = "条码货位编码")
    private String locatorCode;

    @ApiModelProperty(value = "特殊备注")
    private String remark;

    @ApiModelProperty(value = "有效性标识")
    private String enableFlag;

    @ApiModelProperty(value = "冻结标识")
    private String freezeFlag;

    @ApiModelProperty(value = "盘点标识")
    private String stocktakeFlag;

    @ApiModelProperty(value = "扫描标识")
    private String scanFlag;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "在制标识")
    private String mfFlag;

    @ApiModelProperty(value = "验证标识 Y-不在(其他外箱) N-在")
    private String verifyFlag;

    @ApiModelProperty(value = "条码状态")
    private String materialLotStatus;

    @ApiModelProperty(value = "扫描外箱时外箱装载的物料批信息、确认装载时所有待装箱条码")
    List<HmeLoadContainerVO> containerDtlList;

}
