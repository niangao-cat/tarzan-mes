package tarzan.method.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtBomDTO implements Serializable {


    private static final long serialVersionUID = -886172929798192794L;
    
    @ApiModelProperty(value = "主键")
    private String bomId;
    
    @ApiModelProperty(value = "名称")
    private String bomName;
    
    @ApiModelProperty(value = "描述")
    private String description;
    
    @ApiModelProperty(value = "版本")
    private String revision;
    
    @ApiModelProperty(value = "类型")
    private String bomTypeDesc;
    
    @ApiModelProperty(value = "状态")
    private String bomStatusDesc;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getBomTypeDesc() {
        return bomTypeDesc;
    }

    public void setBomTypeDesc(String bomTypeDesc) {
        this.bomTypeDesc = bomTypeDesc;
    }

    public String getBomStatusDesc() {
        return bomStatusDesc;
    }

    public void setBomStatusDesc(String bomStatusDesc) {
        this.bomStatusDesc = bomStatusDesc;
    }
}
