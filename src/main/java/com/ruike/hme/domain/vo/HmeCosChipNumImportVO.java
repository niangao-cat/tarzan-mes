package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 芯片号-导入VO
 *
 * @author jiangling.zheng@hand-china.com 2020/9/16 18:32
 */
@Data
public class HmeCosChipNumImportVO extends HmeCosChipNumImportVO2 implements Serializable  {

    private static final long serialVersionUID = 3178024012883930009L;

    @ApiModelProperty("siteId")
    private String siteId;

    @ApiModelProperty("发货日期")
    private String deliveryDate;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "盒号")
    private String materialLotCode;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "容器类型ID")
    private String containerTypeId;

    @ApiModelProperty(value = "容器类型编码")
    private String containerType;

    @ApiModelProperty(value = "工艺Id")
    private String operationId;

    @ApiModelProperty(value = "bar排列顺序")
    private String barOrderSeq;

    @ApiModelProperty(value = "wafer")
    private String wafer;

    @ApiModelProperty(value = "装载位置")
    private String loadPosition;

    @ApiModelProperty(value = "需要用户验证")
    private String userVerification;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单编号")
    private String workOrderNum;

    @ApiModelProperty(value = "平均波长（nm）")
    private String averageWavelength;

    @ApiModelProperty(value = "作业批次")
    private String jobBatch;

    @ApiModelProperty(value = "批次号")
    private String lotNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "BAR")
    private String bar;

    @ApiModelProperty(value = "工位")
    private String workcellId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "来料录入ID")
    private String operationRecordId;

    @ApiModelProperty(value = "芯片数")
    private Long capacity;

    @ApiModelProperty(value = "不良芯片数")
    private Long badQty;

    @ApiModelProperty(value = "行数")
    private Long lineNum;

    @ApiModelProperty(value = "列数")
    private Long columnNum;

    @ApiModelProperty(value = "行序号")
    private String loadSequence;

    @ApiModelProperty("班次ID")
    private String wkcShiftId;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("条码批次")
    private String lot;
}
