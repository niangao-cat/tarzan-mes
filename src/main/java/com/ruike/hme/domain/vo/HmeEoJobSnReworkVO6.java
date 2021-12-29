package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.modeling.domain.entity.MtModLocator;

@Data
public class HmeEoJobSnReworkVO6 implements Serializable {
    private static final long serialVersionUID = 7877446860212793743L;
    @ApiModelProperty(value = "退料条码")
    HmeEoJobSnBatchVO21 backMaterialLot;
    @ApiModelProperty(value = "条码扩展属性")
    Map<String,String> materialLotAttrMap;
    @ApiModelProperty(value = "工单扩展属性")
    Map<String, String> woExtendAttrMap;
    @ApiModelProperty(value = "EO组件清单")
    List<HmeEoJobSnBatchVO4> eoComponentList;
    @ApiModelProperty(value = "EO组件清单扩展属性")
    Map<String, String> eoComponentExtendAttrMap;
    @ApiModelProperty(value = "工单组件清单")
    List<HmeEoJobSnBatchVO4> woComponentList;
    @ApiModelProperty(value = "工单组件清单扩展属性")
    Map<String, String> woComponentExtendAttrMap;
    @ApiModelProperty(value = "工单组件实绩")
    Map<String, MtWorkOrderComponentActual> mtWoComponentActualMap;
    @ApiModelProperty(value = "库位")
    HmeEoJobSnBatchVO21 locator;
    @ApiModelProperty(value = "事务类型")
    Map<String, WmsTransactionTypeDTO> wmsTransactionTypeMap;
    @ApiModelProperty(value = "虚拟件组件")
    HmeEoJobSnBatchVO22 virtualComponent;
    @ApiModelProperty(value = "预装库位")
    MtModLocator preLoadLocator;
}
