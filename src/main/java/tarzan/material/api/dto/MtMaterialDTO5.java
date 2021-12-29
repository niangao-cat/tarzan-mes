package tarzan.material.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;

/**
 * @author YUAN.YUAN
 */
public class MtMaterialDTO5 implements Serializable {
    private static final long serialVersionUID = 2053227258568074457L;

    @ApiModelProperty("主键ID，表示唯一一条记录")
    private String materialId;
    @ApiModelProperty(value = "物料编号", required = true)
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料图号")
    private String materialDesignCode;
    @ApiModelProperty(value = "物料简称")
    private String materialIdentifyCode;
    @ApiModelProperty(value = "长")
    private Double length;
    @ApiModelProperty(value = "宽")
    private Double width;
    @ApiModelProperty(value = "高")
    private Double height;
    @ApiModelProperty(value = "尺寸单位")
    private String sizeUomId;
    @ApiModelProperty(value = "材质/型号")
    private String model;
    @ApiModelProperty(value = "体积")
    private Double volume;
    @ApiModelProperty(value = "体积单位")
    private String volumeUomId;
    @ApiModelProperty(value = "重量")
    private Double weight;
    @ApiModelProperty(value = "重量单位")
    private String weightUomId;
    @ApiModelProperty(value = "保质期")
    private Double shelfLife;
    @ApiModelProperty(value = "保质期单位")
    private String shelfLifeUomId;
    @ApiModelProperty(value = "基本计量单位", required = true)
    private String primaryUomId;
    @ApiModelProperty(value = "辅助单位")
    private String secondaryUomId;
    @ApiModelProperty(value = "主辅单位转换比例：基本计量单位/辅助单位")
    private Double conversionRate;
    @ApiModelProperty(value = "是否有效", required = true)
    private String enableFlag;
    @ApiModelProperty(value = "物料站点集合")
    private List<MtMaterialSiteDTO4> mtMaterialSites;
    @ApiModelProperty("物料扩展属性集合")
    private List<MtExtendAttrDTO3> mtMaterialAttrs;
    @ApiModelProperty("物料分配集合")
    private List<MtMaterialCategoryAssignDTO> mtMaterialCategoryAssigns;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();

    /**
     * @return 多语言信息
     */
    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    /**
     * @return 物料分配集合
     */
    public List<MtMaterialCategoryAssignDTO> getMtMaterialCategoryAssigns() {
        return mtMaterialCategoryAssigns;
    }

    public void setMtMaterialCategoryAssigns(List<MtMaterialCategoryAssignDTO> mtMaterialCategoryAssigns) {
        this.mtMaterialCategoryAssigns = mtMaterialCategoryAssigns;
    }

    /**
     * @return 物料扩展属性集合
     */
    public List<MtExtendAttrDTO3> getMtMaterialAttrs() {
        return mtMaterialAttrs;
    }

    public void setMtMaterialAttrs(List<MtExtendAttrDTO3> mtMaterialAttrs) {
        this.mtMaterialAttrs = mtMaterialAttrs;
    }

    /**
     * @return 物料站点集合
     */
    public List<MtMaterialSiteDTO4> getMtMaterialSites() {
        return mtMaterialSites;
    }

    public void setMtMaterialSites(List<MtMaterialSiteDTO4> mtMaterialSites) {
        this.mtMaterialSites = mtMaterialSites;
    }

    /**
     * @return 主键ID，表示唯一一条记录
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 物料编号
     */
    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    /**
     * @return 物料名称
     */
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    /**
     * @return 物料图号
     */
    public String getMaterialDesignCode() {
        return materialDesignCode;
    }

    public void setMaterialDesignCode(String materialDesignCode) {
        this.materialDesignCode = materialDesignCode;
    }

    /**
     * @return 物料简称
     */
    public String getMaterialIdentifyCode() {
        return materialIdentifyCode;
    }

    public void setMaterialIdentifyCode(String materialIdentifyCode) {
        this.materialIdentifyCode = materialIdentifyCode;
    }

    /**
     * @return 长
     */
    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    /**
     * @return 宽
     */
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    /**
     * @return 高
     */
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    /**
     * @return 尺寸单位
     */
    public String getSizeUomId() {
        return sizeUomId;
    }

    public void setSizeUomId(String sizeUomId) {
        this.sizeUomId = sizeUomId;
    }

    /**
     * @return 材质/型号
     */
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return 体积
     */
    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    /**
     * @return 体积单位
     */
    public String getVolumeUomId() {
        return volumeUomId;
    }

    public void setVolumeUomId(String volumeUomId) {
        this.volumeUomId = volumeUomId;
    }

    /**
     * @return 重量
     */
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * @return 重量单位
     */
    public String getWeightUomId() {
        return weightUomId;
    }

    public void setWeightUomId(String weightUomId) {
        this.weightUomId = weightUomId;
    }

    /**
     * @return 保质期
     */
    public Double getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Double shelfLife) {
        this.shelfLife = shelfLife;
    }

    /**
     * @return 保质期单位
     */
    public String getShelfLifeUomId() {
        return shelfLifeUomId;
    }

    public void setShelfLifeUomId(String shelfLifeUomId) {
        this.shelfLifeUomId = shelfLifeUomId;
    }

    /**
     * @return 基本计量单位
     */
    public String getPrimaryUomId() {
        return primaryUomId;
    }

    public void setPrimaryUomId(String primaryUomId) {
        this.primaryUomId = primaryUomId;
    }

    /**
     * @return 辅助单位
     */
    public String getSecondaryUomId() {
        return secondaryUomId;
    }

    public void setSecondaryUomId(String secondaryUomId) {
        this.secondaryUomId = secondaryUomId;
    }

    /**
     * @return 主辅单位转换比例：基本计量单位/辅助单位
     */
    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
