package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Map;

import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.order.domain.entity.MtEoRouter;

@Data
public class HmeEoJobSnReworkVO5 implements Serializable {
    private static final long serialVersionUID = 8317903480743825427L;
    @ApiModelProperty(value = "物料、物料下条码")
    private HmeEoJobSnReworkVO4 dto;
    @ApiModelProperty(value = "事件请求ID")
    private String eventRequestId;
    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty(value = "事务类型")
    Map<String, WmsTransactionTypeDTO> wmsTransactionTypeMap;
    @ApiModelProperty(value = "区域库位")
    MtModLocator areaLocator;
    @ApiModelProperty(value = "条码扩展属性")
    Map<String,String> materialLotExtendAttrMap;
    @ApiModelProperty(value = "执行作业实绩")
    Map<String, MtEoComponentActual> mtEoComponentActualMap;
    @ApiModelProperty(value = "班次")
    HmeEoJobSnBatchVO11 hmeEoJobShift;
    @ApiModelProperty(value = "工艺路线")
    Map<String, MtEoRouter> eoRouterMap;
    @ApiModelProperty(value = "区域库位")
    Map<String, HmeModLocatorVO2> areaLocatorMap;
}
