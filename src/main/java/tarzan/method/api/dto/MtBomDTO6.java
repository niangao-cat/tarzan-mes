package tarzan.method.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtBomDTO6 implements Serializable {

    private static final long serialVersionUID = 6983014559016223328L;
    
    @ApiModelProperty(value = "元数据主键",required = true)
    @NotBlank
    private String bomId;
    
    @ApiModelProperty(value = "目标bom名称",required = true)
    @NotBlank
    private String bomNameNew;
    
    @ApiModelProperty(value = "目标bom版本",required = true)
    @NotBlank
    private String revisionNew;
    
    @ApiModelProperty(value = "目标bom类型",required = true)
    @NotBlank
    private String bomTypeNew;
    
    public String getBomId() {
        return bomId;
    }
    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
    public String getBomNameNew() {
        return bomNameNew;
    }
    public void setBomNameNew(String bomNameNew) {
        this.bomNameNew = bomNameNew;
    }
    public String getRevisionNew() {
        return revisionNew;
    }
    public void setRevisionNew(String revisionNew) {
        this.revisionNew = revisionNew;
    }
    public String getBomTypeNew() {
        return bomTypeNew;
    }
    public void setBomTypeNew(String bomTypeNew) {
        this.bomTypeNew = bomTypeNew;
    }

}
