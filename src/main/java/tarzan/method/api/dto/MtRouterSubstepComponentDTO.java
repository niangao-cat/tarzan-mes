package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author benjamin
 */
public class MtRouterSubstepComponentDTO implements Serializable {
    private static final long serialVersionUID = -4253673912260063363L;

    @ApiModelProperty("工艺路线子步骤组件标识")
    private String routerSubstepComponentId;
    @ApiModelProperty("工艺路线子步骤标识")
    private String routerSubstepId;
    @ApiModelProperty(value = "BOM组件标识")
    @NotBlank
    private String bomComponentId;
    @ApiModelProperty(value = "组件物料编码")
    private String bomComponentMaterialCode;
    @ApiModelProperty(value = "组件物料描述")
    private String bomComponentMaterialDesc;
    @ApiModelProperty(value = "子步骤顺序")
    private Long sequence;
    @ApiModelProperty(value = "组件使用数量")
    @NotNull
    private Double qty;
    @ApiModelProperty("工艺路线子步骤组件扩展属性")
    private List<MtExtendAttrDTO3> routerSubstepComponentAttrs;

    public String getRouterSubstepComponentId() {
        return routerSubstepComponentId;
    }

    public void setRouterSubstepComponentId(String routerSubstepComponentId) {
        this.routerSubstepComponentId = routerSubstepComponentId;
    }

    public String getRouterSubstepId() {
        return routerSubstepId;
    }

    public void setRouterSubstepId(String routerSubstepId) {
        this.routerSubstepId = routerSubstepId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomComponentMaterialCode() {
        return bomComponentMaterialCode;
    }

    public void setBomComponentMaterialCode(String bomComponentMaterialCode) {
        this.bomComponentMaterialCode = bomComponentMaterialCode;
    }

    public String getBomComponentMaterialDesc() {
        return bomComponentMaterialDesc;
    }

    public void setBomComponentMaterialDesc(String bomComponentMaterialDesc) {
        this.bomComponentMaterialDesc = bomComponentMaterialDesc;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public List<MtExtendAttrDTO3> getRouterSubstepComponentAttrs() {
        return routerSubstepComponentAttrs;
    }

    public void setRouterSubstepComponentAttrs(List<MtExtendAttrDTO3> routerSubstepComponentAttrs) {
        this.routerSubstepComponentAttrs = routerSubstepComponentAttrs;
    }
}
