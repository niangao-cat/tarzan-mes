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
public class HmeCosChipNumImportVO4 extends HmeCosChipNumImportVO2 implements Serializable  {

    private static final long serialVersionUID = 3178024012883930009L;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "容器类型ID")
    private String containerTypeId;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "芯片数")
    private Long capacity;

    @ApiModelProperty(value = "行数")
    private Long lineNum;

    @ApiModelProperty(value = "列数")
    private Long columnNum;

    @ApiModelProperty("条码批次")
    private String lot;
}
