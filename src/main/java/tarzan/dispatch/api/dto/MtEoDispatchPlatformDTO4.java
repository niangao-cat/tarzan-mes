package tarzan.dispatch.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author : MrZ
 * @date : 2019-12-04 10:41
 **/
public class MtEoDispatchPlatformDTO4 implements Serializable {
    private static final long serialVersionUID = -6197527877481653945L;
    @ApiModelProperty("工作单元ID")
    private String workcellId;
    @ApiModelProperty(value = "工作单元编号", required = true)
    private String workcellCode;
    @ApiModelProperty(value = "工作单元名称", required = true)
    private String workcellName;
    @ApiModelProperty(value = "工作单元描述")
    private String description;
    @ApiModelProperty(value = "延期数量，可以是6位小数")
    private Double extensionsQty;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getExtensionsQty() {
        return extensionsQty;
    }

    public void setExtensionsQty(Double extensionsQty) {
        this.extensionsQty = extensionsQty;
    }
}
