package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * 配送需求与派工关系
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/10 09:43
 */
@Data
public class WmsDistDemandDispatchRelVO {
    @ApiModelProperty(value = "派工ID")
    private String woDispatchId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "组件需求")
    private String demandRecordId;
    @ApiModelProperty(value = "组件需求")
    private String demandDetailId;
    @ApiModelProperty(value = "组件需求")
    private String distDemandId;
    @ApiModelProperty(value = "配送单ID")
    private String instructionDocId;
    @ApiModelProperty(value = "配送单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "替代料行数")
    private Boolean substituteExists;
    @ApiModelProperty(value = "派工数量")
    private BigDecimal dispatchQty;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "工序Id")
    private String processId;

    public static WmsDistributionDemandRelSumVO getSummary(WmsDistDemandDispatchRelVO rel) {
        WmsDistributionDemandRelSumVO sum = new WmsDistributionDemandRelSumVO();
        sum.setWoDispatchId(rel.getWoDispatchId());
        sum.setWorkOrderId(rel.getWorkOrderId());
        sum.setMaterialId(rel.getMaterialId());
        sum.setMaterialVersion(rel.getMaterialVersion());
        sum.setProcessId(rel.getProcessId());
        return sum;
    }
}
