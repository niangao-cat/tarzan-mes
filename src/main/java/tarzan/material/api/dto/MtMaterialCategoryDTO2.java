package tarzan.material.api.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtMaterialCategoryDTO2 implements Serializable {

    private static final long serialVersionUID = -5785689429301162175L;

    @ApiModelProperty(value = "物料类别Id")
    private String materialCategoryId;
    
    @ApiModelProperty(value = "物料类别编码")
    @NotBlank
    private String categoryCode;

    @ApiModelProperty(value = "物料类别描述")
    private String description;

    @ApiModelProperty(value = "物料类别集Id")
    @NotBlank
    private String materialCategorySetId;
    
    @ApiModelProperty(value = "是否有效")
    @NotBlank
    private String enableFlag;
    
    @ApiModelProperty(hidden = true)
    private Map<String, Map<String, String>> _tls;

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialCategorySetId() {
        return materialCategorySetId;
    }

    public void setMaterialCategorySetId(String materialCategorySetId) {
        this.materialCategorySetId = materialCategorySetId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
    
}
