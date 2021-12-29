package tarzan.material.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO;

/**
 * @author benjamin
 */
public class MtUomDTO5 implements Serializable {
    private static final long serialVersionUID = -9180848625114481912L;

    @ApiModelProperty("主键ID,标识唯一一条记录")
    private String uomId;
    @ApiModelProperty(value = "单位类型(修改时必输)")
    @NotBlank
    private String uomType;
    @ApiModelProperty(value = "单位编码(修改时必输)")
    @NotBlank
    private String uomCode;
    @ApiModelProperty(value = "单位描述(修改时必输)")
    @NotBlank
    private String uomName;
    @ApiModelProperty("有效性")
    private String enableFlag;
    @ApiModelProperty("单位扩展属性")
    private List<MtExtendAttrDTO> uomAttrList;

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getUomType() {
        return uomType;
    }

    public void setUomType(String uomType) {
        this.uomType = uomType;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public List<MtExtendAttrDTO> getUomAttrList() {
        return uomAttrList;
    }

    public void setUomAttrList(List<MtExtendAttrDTO> uomAttrList) {
        this.uomAttrList = uomAttrList;
    }
}
