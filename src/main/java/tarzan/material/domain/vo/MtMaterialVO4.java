package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtMaterialVO4 implements Serializable {

    private static final long serialVersionUID = -2922893222060825683L;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料图号")
    private String materialDesignCode;
    @ApiModelProperty("物料简码")
    private String materialIdentifyCode;
    @ApiModelProperty("材质&型号")
    private String model;
    @ApiModelProperty("基本计量单位")
    private String primaryUomId;
    @ApiModelProperty("辅助计量单位")
    private String secondaryUomId;
    @ApiModelProperty("是否有效")
    private String enableFlag;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
