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
public class MtRouterOperationComponentDTO implements Serializable {
    private static final long serialVersionUID = 3454107894185235857L;

    @ApiModelProperty("工艺路线步骤组件唯一标识")
    private String routerOperationComponentId;
    @ApiModelProperty("工艺路线步骤唯一标识")
    private String routerOperationId;
    @ApiModelProperty(value = "组件")
    @NotBlank
    private String bomComponentId;
    @ApiModelProperty(value = "组件物料编码")
    private String bomComponentMaterialCode;
    @ApiModelProperty(value = "组件物料描述")
    private String bomComponentMaterialDesc;
    @ApiModelProperty(value = "数量")
    private Double qty;
    @ApiModelProperty(value = "顺序")
    @NotNull
    private Long sequence;
    @ApiModelProperty(value = "是否有效")
    @NotBlank
    private String enableFlag;
    @ApiModelProperty("工艺路线步骤对应工序扩展属性")
    private List<MtExtendAttrDTO3> routerOperationComponentAttrs;
    @ApiModelProperty("组件版本")
    private String bomVersion;

    public String getRouterOperationComponentId() {
        return routerOperationComponentId;
    }

    public void setRouterOperationComponentId(String routerOperationComponentId) {
        this.routerOperationComponentId = routerOperationComponentId;
    }

    public String getRouterOperationId() {
        return routerOperationId;
    }

    public void setRouterOperationId(String routerOperationId) {
        this.routerOperationId = routerOperationId;
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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public List<MtExtendAttrDTO3> getRouterOperationComponentAttrs() {
        return routerOperationComponentAttrs;
    }

    public void setRouterOperationComponentAttrs(List<MtExtendAttrDTO3> routerOperationComponentAttrs) {
        this.routerOperationComponentAttrs = routerOperationComponentAttrs;
    }

    public String getBomVersion() {
        return bomVersion;
    }

    public void setBomVersion(String bomVersion) {
        this.bomVersion = bomVersion;
    }
}
