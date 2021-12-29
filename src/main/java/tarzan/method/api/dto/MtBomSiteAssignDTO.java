package tarzan.method.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtBomSiteAssignDTO implements Serializable {


    private static final long serialVersionUID = -5493369092468877466L;
    
    @ApiModelProperty(value = "装配清单ID",required = true)
    @NotBlank
    private String bomId;
    
    @ApiModelProperty(value = "装配清单站点分配ID")
    private String assignId;
    
    @ApiModelProperty(value = "站点ID",required = true)
    @NotBlank
    private String siteId;
    
    @ApiModelProperty(value = "有效性",required = true)
    @NotBlank
    private String enableFlag;
    
    
    public String getAssignId() {
        return assignId;
    }
    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }
    public String getBomId() {
        return bomId;
    }
    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
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
        builder.append("MtBomReferencePointDTO2 [assignId=");
        builder.append(assignId);
        builder.append(", bomId=");
        builder.append(bomId);
        builder.append(", siteId=");
        builder.append(siteId);
        builder.append(", enableFlag=");
        builder.append(enableFlag);
        builder.append("]");
        return builder.toString();
    }
    
}
