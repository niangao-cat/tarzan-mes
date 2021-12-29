package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeEoJobBeyondMaterialDTO
 *
 * @author liyuan.lv@hand-china.com 20.7.15 07:35:27
 */
@Data
public class HmeEoJobBeyondMaterialVO implements Serializable {

    private static final long serialVersionUID = -5344043836778800296L;

    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工序作业ID")
    private String jobId;
    @ApiModelProperty("物料类型")
    private String materialType;
    @ApiModelProperty("时效")
    private String availableTime;
    @ApiModelProperty("物料ID")
    private String materialId;
}
