package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeVisualInspectionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeVisualInspectionDTO3
 *
 * @author: chaonan.hu@hand-china.com 2020/01/21 10:15:53
 **/
@Data
public class HmeVisualInspectionDTO3 implements Serializable {
    private static final long serialVersionUID = -254824881403244800L;

    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工艺Id", required = true)
    private String operationId;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "勾选条码数据", required = true)
    private List<HmeVisualInspectionVO> materialLotList;
}
