package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * HmePumpPreSelectionVO5
 *
 * @author: chaonan.hu@hand-china.com 2021/08/31 10:16:48
 **/
@Data
public class HmePumpPreSelectionVO5 implements Serializable {
    private static final long serialVersionUID = 5903728465930107139L;

    @ApiModelProperty(value = "头主键")
    private String pumpPreSelectionId;

    @ApiModelProperty(value = "行主键")
    private String pumpSelectionDetailsId;

    @ApiModelProperty(value = "筛选批次")
    private String selectionLot;

    @ApiModelProperty(value = "组合物料ID")
    private String combMaterialId;

    @ApiModelProperty(value = "组合物料编号")
    private String combMaterialCode;

    @ApiModelProperty(value = "bomId")
    private String bomId;

    @ApiModelProperty(value = "BOM版本号")
    private String revision;

    @ApiModelProperty(value = "原容器ID")
    private String oldContainerId;

    @ApiModelProperty(value = "原容器编码")
    private String oldContainerCode;

    @ApiModelProperty(value = "目标容器ID")
    private String newContainerId;

    @ApiModelProperty(value = "目标容器编码")
    private String newContainerCode;

    @ApiModelProperty(value = "泵浦源SNID")
    private String materialLotId;

    @ApiModelProperty(value = "泵浦源SN")
    private String materialLotCode;

    @ApiModelProperty(value = "泵浦源物料ID")
    private String materialId;

    @ApiModelProperty(value = "泵浦源物料编码")
    private String materialCode;

    @ApiModelProperty(value = "泵浦源物料描述")
    private String materialName;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "HME.PUMP_SELECT_STATUS", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty(value = "状态含义")
    private String statusMeaning;

    @ApiModelProperty(value = "挑选顺序")
    private Long selectionOrder;

    @ApiModelProperty(value = "挑选时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty(value = "分组编号,相等的为同一组")
    private Long groupNum;

    @ApiModelProperty(value = "当前行数据所属分组下的元素个数")
    private Long pumpQty;
}
