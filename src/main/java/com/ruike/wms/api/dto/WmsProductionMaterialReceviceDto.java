package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsMaterialDocReturnVO;
import com.ruike.wms.domain.vo.WmsProductReturnVO2;
import com.ruike.wms.domain.vo.WmsProductionMaterialReturnVO;
import com.ruike.wms.domain.vo.WmsProductionReturnInstructionVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/13 11:24
 */
@Data
public class WmsProductionMaterialReceviceDto implements Serializable {

    private static final long serialVersionUID = -6238027184347492290L;

    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("上次扫描结果")
    private WmsProductionMaterialReturnVO wmsProductionMaterialReturnVO;
    @ApiModelProperty(value = "退料单ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据行list")
    private List<WmsProductionReturnInstructionVO> instructionList;
    @ApiModelProperty(value = "货位Id")
    private String locatorId;

}
