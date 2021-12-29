package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsMaterialDocReturnVO;
import com.ruike.wms.domain.vo.WmsMaterialLineReturnVO;
import com.ruike.wms.domain.vo.WmsProductReturnVO2;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/08 13:33
 */
@Data
public class WmsMaterialDocReceviceDto implements Serializable {

    private static final long serialVersionUID = -523510793789310371L;

    @ApiModelProperty(value = "物料批条码")
    private String materialLotCode;
    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "所有单据行list")
    private List<WmsProductReturnVO2> instructionList;
    @ApiModelProperty(value = "已选择单据行list1")
    private List<WmsProductReturnVO2> instructionselectedList;
    @ApiModelProperty(value = "货位Id")
    private String locatorId;
    @ApiModelProperty(value = "上次扫描结果")
    private WmsMaterialDocReturnVO wmsMaterialDocReturnVO;
}
