package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmePumpPreSelectionVO4;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmePumpPreSelectionDTO4
 *
 * @author: chaonan.hu@hand-china.com 2021/08/31 15:38:21
 **/
@Data
public class HmePumpPreSelectionDTO5 implements Serializable {
    private static final long serialVersionUID = 2540095597148266568L;

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;

    @ApiModelProperty(value = "筛选批次")
    private String selectionLot;

    @ApiModelProperty(value = "物料ID", required = true)
    private String materialId;

    @ApiModelProperty(value = "物料编码", required = true)
    private String materialCode;

    @ApiModelProperty(value = "版本号", required = true)
    private String revision;

    @ApiModelProperty(value = "筛选池条码数据", required = true)
    private List<HmePumpPreSelectionVO4> pumpMaterialLotInfoList;
}
