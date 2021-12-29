package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeBomComponentTrxVO;
import com.ruike.hme.domain.vo.HmeRouterOperationVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/4 10:10
 */
@Data
public class HmeMesTransformDTO {
    @ApiModelProperty("站点")
    private String siteCode;
    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("工单号")
    private String workOrderNum;
    @ApiModelProperty("工单Id")
    private String workOrderId;
    @ApiModelProperty("产线ID")
    private String prodLineId;
    @ApiModelProperty("产线编码")
    private String prodLineCode;
    @ApiModelProperty("路线步骤")
    private HmeRouterOperationVO routerStep;
    @ApiModelProperty("条码号")
    private String materialLotCode;
    @ApiModelProperty("条码Id")
    private String materialLotId;
    @ApiModelProperty("条码物料Id")
    private String materialId;
    @ApiModelProperty("条码货位Id")
    private String locatorId;
    @ApiModelProperty("条码货位Id")
    private String warehouseId;
    @ApiModelProperty("条码批次")
    private String lotCode;
    @ApiModelProperty("条码数量")
    private Double primaryUomQty;
    @ApiModelProperty("条码单位")
    private String uomCode;
    @ApiModelProperty("转型后物料")
    private String transformMaterial;
    @ApiModelProperty("转型后物料Id")
    private String transformMaterialId;
    @ApiModelProperty("容器编码")
    private String containerCode;
    @ApiModelProperty("容器Id")
    private String containerId;
    @ApiModelProperty("bom信息")
    private HmeBomComponentTrxVO bom;
}
