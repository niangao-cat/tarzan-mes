package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料升级
 */
@Data
public class HmeEoJobSnVO20 implements Serializable {
    private static final long serialVersionUID = 5027167035816637194L;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("EO ID")
    private String eoId;
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("步骤ID")
    private String eoStepId;
    @ApiModelProperty("进站条码ID")
    private String snMaterialLotId;
    @ApiModelProperty("工序作业ID")
    private String jobId;
    @ApiModelProperty("投料条码信息")
    private List<HmeEoJobSnVO21> materialLotList;
}
