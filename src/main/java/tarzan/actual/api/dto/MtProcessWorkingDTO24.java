package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/20 9:42 上午
 */
public class MtProcessWorkingDTO24 implements Serializable {
    private static final long serialVersionUID = -5018126657840389772L;

    @ApiModelProperty(value = "工作单元Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "eo步骤实绩ID", required = true)
    private String eoStepActualId;

    @ApiModelProperty("执行作业ID-卡片获取")
    private String eoId;

    @ApiModelProperty("装配清单ID-卡片获取")
    private String bomId;

    @ApiModelProperty("生产指令ID-卡片获取")
    private String workOrderId;

    @ApiModelProperty("工艺路线Id-卡片获取")
    private String routerId;

    @ApiModelProperty("工艺步骤ID-卡片获取")
    private String routerStepId;

    @ApiModelProperty("物料ID-卡片获取")
    private String materialId;

    @ApiModelProperty("工序ID-登录获取")
    private String operationId;

    @ApiModelProperty("配置项EO_DATA_DATA_COLLECT")
    private List<String> eoDataDataCollect = new ArrayList<>();

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

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

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public List<String> getEoDataDataCollect() {
        return eoDataDataCollect;
    }

    public void setEoDataDataCollect(List<String> eoDataDataCollect) {
        this.eoDataDataCollect = eoDataDataCollect;
    }
}
