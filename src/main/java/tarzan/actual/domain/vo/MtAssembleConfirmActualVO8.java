package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleConfirmActualVO8 implements Serializable {

    private static final long serialVersionUID = 3618642559050348686L;
    private String workOrderId;// 生产指令Id

    private String bomComponentId;// 装配清单行ID

    private Double qty;// 本次装配数量

    private String materialId;// 物料ID

    private String routerStepId;// 工艺路线步骤Id

    private String issueControlFlag;// 是否考虑投料限制

    private String eoId;// 执行作业Id

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getIssueControlFlag() {
        return issueControlFlag;
    }

    public void setIssueControlFlag(String issueControlFlag) {
        this.issueControlFlag = issueControlFlag;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }
}
