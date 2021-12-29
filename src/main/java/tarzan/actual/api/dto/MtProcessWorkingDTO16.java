package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 0017, 2020-02-17 9:48
 */
public class MtProcessWorkingDTO16 implements Serializable {
    private static final long serialVersionUID = -8129403725577850895L;

    @ApiModelProperty("执行作业ID-卡片获取")
    private String eoId;

    @ApiModelProperty("装配清单ID-卡片获取")
    private String bomId;

    @ApiModelProperty("工序装配标识-卡片获取")
    private String operationAssembleFlag;

    @ApiModelProperty("工序ID-卡片获取")
    private String operationId;

    @ApiModelProperty("工艺步骤ID-卡片获取")
    private String routerStepId;

    @ApiModelProperty("是否按照物料批退回或报废-配置项")
    private String assembleAbandonOrCancelAssociatedMateriallot;

    @ApiModelProperty("登入的站点ID")
    private String siteId;

    @ApiModelProperty("登入的工作单元ID")
    private String workcellId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getOperationAssembleFlag() {
        return operationAssembleFlag;
    }

    public void setOperationAssembleFlag(String operationAssembleFlag) {
        this.operationAssembleFlag = operationAssembleFlag;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getAssembleAbandonOrCancelAssociatedMateriallot() {
        return assembleAbandonOrCancelAssociatedMateriallot;
    }

    public void setAssembleAbandonOrCancelAssociatedMateriallot(String assembleAbandonOrCancelAssociatedMateriallot) {
        this.assembleAbandonOrCancelAssociatedMateriallot = assembleAbandonOrCancelAssociatedMateriallot;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }
}
