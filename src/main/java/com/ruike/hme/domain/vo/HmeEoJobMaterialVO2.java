package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * MtEoStepJobMaterialVO2
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobMaterialVO2 implements Serializable {

    private static final long serialVersionUID = -4268974781785143946L;
    private String workcellId;
    private String jobId;
    private String eoId;
    private String jobType;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "步骤ID")
    private String eoStepId;
}
