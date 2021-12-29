package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtMaterialVO5 implements Serializable {

    private static final long serialVersionUID = -1747396317704045422L;
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
    @ApiModelProperty("物料长度值")
    private Double length;
    @ApiModelProperty("物料宽度值")
    private Double width;
    @ApiModelProperty("物料高度值")
    private Double height;
    @ApiModelProperty("尺寸单位ID")
    private String sizeUomId;
    @ApiModelProperty("尺寸单位编码")
    private String sizeUomCode;
    @ApiModelProperty("尺寸单位名称")
    private String sizeUomName;
    @ApiModelProperty("物料体积")
    private Double volume;
    @ApiModelProperty("体积单位ID")
    private String volumeUomId;
    @ApiModelProperty("体积单位编码")
    private String volumeUomCode;
    @ApiModelProperty("体积单位名称")
    private String volumeUomName;
    @ApiModelProperty("物料重量")
    private Double weight;
    @ApiModelProperty("重量单位ID")
    private String weightUomId;
    @ApiModelProperty("重量单位编码")
    private String weightUomCode;
    @ApiModelProperty("重量单位名称")
    private String weightUomName;
    @ApiModelProperty("物料保质期")
    private Double shelfLife;
    @ApiModelProperty("保质期单位ID")
    private String shelfLifeUomId;
    @ApiModelProperty("保质期单位编码")
    private String shelfLifeUomCode;
    @ApiModelProperty("保质期单位名称")
    private String shelfLifeUomName;
    @ApiModelProperty("基本计量单位ID")
    private String primaryUomId;
    @ApiModelProperty("基本计量单位编码")
    private String primaryUomCode;
    @ApiModelProperty("基本计量单位名称")
    private String primaryuomName;
    @ApiModelProperty("辅助计量单位ID")
    private String secondaryUomId;
    @ApiModelProperty("辅助计量单位编码")
    private String secondaryUomCode;
    @ApiModelProperty("辅助计量单位名称")
    private String secondaryUomName;
    @ApiModelProperty("主辅单位换算比例")
    private Double conversionRate;
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

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getSizeUomId() {
        return sizeUomId;
    }

    public void setSizeUomId(String sizeUomId) {
        this.sizeUomId = sizeUomId;
    }

    public String getSizeUomCode() {
        return sizeUomCode;
    }

    public void setSizeUomCode(String sizeUomCode) {
        this.sizeUomCode = sizeUomCode;
    }

    public String getSizeUomName() {
        return sizeUomName;
    }

    public void setSizeUomName(String sizeUomName) {
        this.sizeUomName = sizeUomName;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getVolumeUomId() {
        return volumeUomId;
    }

    public void setVolumeUomId(String volumeUomId) {
        this.volumeUomId = volumeUomId;
    }

    public String getVolumeUomCode() {
        return volumeUomCode;
    }

    public void setVolumeUomCode(String volumeUomCode) {
        this.volumeUomCode = volumeUomCode;
    }

    public String getVolumeUomName() {
        return volumeUomName;
    }

    public void setVolumeUomName(String volumeUomName) {
        this.volumeUomName = volumeUomName;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getWeightUomId() {
        return weightUomId;
    }

    public void setWeightUomId(String weightUomId) {
        this.weightUomId = weightUomId;
    }

    public String getWeightUomCode() {
        return weightUomCode;
    }

    public void setWeightUomCode(String weightUomCode) {
        this.weightUomCode = weightUomCode;
    }

    public String getWeightUomName() {
        return weightUomName;
    }

    public void setWeightUomName(String weightUomName) {
        this.weightUomName = weightUomName;
    }

    public Double getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Double shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getShelfLifeUomId() {
        return shelfLifeUomId;
    }

    public void setShelfLifeUomId(String shelfLifeUomId) {
        this.shelfLifeUomId = shelfLifeUomId;
    }

    public String getShelfLifeUomCode() {
        return shelfLifeUomCode;
    }

    public void setShelfLifeUomCode(String shelfLifeUomCode) {
        this.shelfLifeUomCode = shelfLifeUomCode;
    }

    public String getShelfLifeUomName() {
        return shelfLifeUomName;
    }

    public void setShelfLifeUomName(String shelfLifeUomName) {
        this.shelfLifeUomName = shelfLifeUomName;
    }

    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    public String getPrimaryUomCode() {
        return primaryUomCode;
    }

    public void setPrimaryUomCode(String primaryUomCode) {
        this.primaryUomCode = primaryUomCode;
    }

    public String getPrimaryuomName() {
        return primaryuomName;
    }

    public void setPrimaryuomName(String primaryuomName) {
        this.primaryuomName = primaryuomName;
    }

    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    public String getSecondaryUomCode() {
        return secondaryUomCode;
    }

    public void setSecondaryUomCode(String secondaryUomCode) {
        this.secondaryUomCode = secondaryUomCode;
    }

    public String getSecondaryUomName() {
        return secondaryUomName;
    }

    public void setSecondaryUomName(String secondaryUomName) {
        this.secondaryUomName = secondaryUomName;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
