package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/25 0:03
 */
@Data
public class HmeCosRetestVO9 implements Serializable {

    private static final long serialVersionUID = 6592972754407429653L;

    @ApiModelProperty(value = "工单id")
    private String workOrderId;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "班次")
    private String wkcShiftId;

    @ApiModelProperty(value = "WAFER")
    private String wafer;

    @ApiModelProperty(value = "容器类型")
    private String containerTypeCode;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "录入批次")
    private String jobBatch;

    @ApiModelProperty(value = "LOTNO")
    private String lotNo;

    @ApiModelProperty(value = "Avg(nm)")
    private BigDecimal averageWavelength;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "TYPE")
    private String type;

    @ApiModelProperty(value = "剩余芯片数")
    private BigDecimal remainingQty;

    @ApiModelProperty(value = "拆分数量")
    private BigDecimal splitQty;

    @ApiModelProperty(value = "来料条码")
    private String sourceMaterialLotCode;

    @ApiModelProperty(value = "来料条码数量")
    private String primaryUomQty;

    private String materialId;

    @ApiModelProperty(value = "条码清单")
    private List<HmeCosRetestVO10> returnMaterialLotList;

    @ApiModelProperty(value = "投料条码清单")
    private List<HmeCosRetestVO10> feelMaterialLotList;

}
