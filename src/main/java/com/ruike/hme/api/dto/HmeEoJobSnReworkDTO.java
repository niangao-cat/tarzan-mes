package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.List;

import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4;
import com.ruike.hme.domain.vo.HmeEoJobSnVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnReworkDTO implements Serializable {
    private static final long serialVersionUID = 6551991924968221216L;
    @ApiModelProperty(value = "勾选的进站SN列表")
    List<HmeEoJobSnVO> snLineList;
    @ApiModelProperty(value = "条码CODE")
    private String materialLotCode;
    @ApiModelProperty(value = "物料类型")
    private String materialType;
    @ApiModelProperty(value = "组件")
    List<HmeEoJobSnBatchVO4> componentList;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;
}
