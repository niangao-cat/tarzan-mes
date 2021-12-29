package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtMaterialDTO6 implements Serializable {
    private static final long serialVersionUID = -3044202564271318490L;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料图号")
    private String materialDesignCode;
    @ApiModelProperty(value = "物料简称")
    private String materialIdentifyCode;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialDesignCode() {
        return materialDesignCode;
    }

    public void setMaterialDesignCode(String materialDesignCode) {
        this.materialDesignCode = materialDesignCode;
    }

    public String getMaterialIdentifyCode() {
        return materialIdentifyCode;
    }

    public void setMaterialIdentifyCode(String materialIdentifyCode) {
        this.materialIdentifyCode = materialIdentifyCode;
    }
}
