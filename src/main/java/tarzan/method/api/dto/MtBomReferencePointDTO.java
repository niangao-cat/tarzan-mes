package tarzan.method.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class MtBomReferencePointDTO implements Serializable {


    private static final long serialVersionUID = 841928802391105931L;
    
    @ApiModelProperty(value = "参考点主键")
    private String bomReferencePointId;
    
    @ApiModelProperty(value = "参考点关联的BOM行ID",required = true)
    @NotBlank
    private String bomComponentId;
    
    @ApiModelProperty(value = "参考点位置描述",required = true)
    @NotBlank
    private String referencePoint;
    
    @ApiModelProperty(value = "参考点数量",required = true)
    @NotNull
    private Double qty;
    
    @ApiModelProperty(value = "参考点序号",required = true)
    @NotNull
    private Long lineNumber;
    
    @ApiModelProperty(value = "有效性",required = true)
    @NotBlank
    private String enableFlag;

    public String getBomReferencePointId() {
        return bomReferencePointId;
    }

    public void setBomReferencePointId(String bomReferencePointId) {
        this.bomReferencePointId = bomReferencePointId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MtBomDTO123 [bomReferencePointId=");
        builder.append(bomReferencePointId);
        builder.append(", bomComponentId=");
        builder.append(bomComponentId);
        builder.append(", referencePoint=");
        builder.append(referencePoint);
        builder.append(", qty=");
        builder.append(qty);
        builder.append(", lineNumber=");
        builder.append(lineNumber);
        builder.append(", enableFlag=");
        builder.append(enableFlag);
        builder.append("]");
        return builder.toString();
    }

}
