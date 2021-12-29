package tarzan.method.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtBomDTO1 implements Serializable {

    private static final long serialVersionUID = -24897063465517536L;

    @ApiModelProperty("名称")
    private String bomName;
    
    @ApiModelProperty("版本")
    private String revision;
    
    @ApiModelProperty("类型")
    private String bomType;
    
    @ApiModelProperty("状态")
    private String bomStatus;
    
    @ApiModelProperty("描述")
    private String description;

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getBomType() {
        return bomType;
    }

    public void setBomType(String bomType) {
        this.bomType = bomType;
    }

    public String getBomStatus() {
        return bomStatus;
    }

    public void setBomStatus(String bomStatus) {
        this.bomStatus = bomStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
