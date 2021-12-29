package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Tangxiao
 * @date : 2020-11-02 15:55
 **/
public class MtAssembleProcessActualVO17 implements Serializable {
    private static final long serialVersionUID = 1755219674613075060L;
    @ApiModelProperty("执行作业ID")
    private String eoId;
    @ApiModelProperty("装配所在工艺路线")
    private String routerId;
    @ApiModelProperty("强制装配工艺路线类型")
    private String assembleRouterType;
    @ApiModelProperty("工艺路线步骤ID")
    private String routerStepId;
    @ApiModelProperty("装配所在子步骤")
    private String substepId;
    @ApiModelProperty("装配清单")
    private String bomId;

    @ApiModelProperty("物料信息")
    private List<MtAssembleProcessActualVO11> materialInfo;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getAssembleRouterType() {
        return assembleRouterType;
    }

    public void setAssembleRouterType(String assembleRouterType) {
        this.assembleRouterType = assembleRouterType;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getSubstepId() {
        return substepId;
    }

    public void setSubstepId(String substepId) {
        this.substepId = substepId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public List<MtAssembleProcessActualVO11> getMaterialInfo() {
        return materialInfo;
    }

    public void setMaterialInfo(List<MtAssembleProcessActualVO11> materialInfo) {
        this.materialInfo = materialInfo;
    }
}
