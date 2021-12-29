package tarzan.material.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.*;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料基础属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:08:45
 */
@ApiModel("物料基础属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_material")
@CustomPrimary
public class MtMaterial extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_MATERIAL_NAME = "materialName";
    public static final String FIELD_MATERIAL_DESIGN_CODE = "materialDesignCode";
    public static final String FIELD_MATERIAL_IDENTIFY_CODE = "materialIdentifyCode";
    public static final String FIELD_LENGTH = "length";
    public static final String FIELD_WIDTH = "width";
    public static final String FIELD_HEIGHT = "height";
    public static final String FIELD_SIZE_UOM_ID = "sizeUomId";
    public static final String FIELD_MODEL = "model";
    public static final String FIELD_VOLUME = "volume";
    public static final String FIELD_VOLUME_UOM_ID = "volumeUomId";
    public static final String FIELD_WEIGHT = "weight";
    public static final String FIELD_WEIGHT_UOM_ID = "weightUomId";
    public static final String FIELD_SHELF_LIFE = "shelfLife";
    public static final String FIELD_SHELF_LIFE_UOM_ID = "shelfLifeUomId";
    public static final String FIELD_PRIMARY_UOM_ID = "primaryUomId";
    public static final String FIELD_SECONDARY_UOM_ID = "secondaryUomId";
    public static final String FIELD_CONVERSION_RATE = "conversionRate";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    @Where
    private String materialId;
    @ApiModelProperty(value = "物料编号", required = true)
    @NotBlank
    @Where
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    @MultiLanguageField
    @Where
    private String materialName;
    @ApiModelProperty(value = "物料图号")
    @Where
    private String materialDesignCode;
    @ApiModelProperty(value = "物料简称")
    @Where
    private String materialIdentifyCode;
    @ApiModelProperty(value = "长")
    @Where
    private Double length;
    @ApiModelProperty(value = "宽")
    @Where
    private Double width;
    @ApiModelProperty(value = "高")
    @Where
    private Double height;
    @ApiModelProperty(value = "尺寸单位")
    @Where
    private String sizeUomId;
    @ApiModelProperty(value = "材质/型号")
    @Where
    private String model;
    @ApiModelProperty(value = "体积")
    @Where
    private Double volume;
    @ApiModelProperty(value = "体积单位")
    @Where
    private String volumeUomId;
    @ApiModelProperty(value = "重量")
    @Where
    private Double weight;
    @ApiModelProperty(value = "重量单位")
    @Where
    private String weightUomId;
    @ApiModelProperty(value = "保质期")
    @Where
    private Double shelfLife;
    @ApiModelProperty(value = "保质期单位")
    @Where
    private String shelfLifeUomId;
    @ApiModelProperty(value = "基本计量单位", required = true)
    @NotBlank
    @Where
    private String primaryUomId;
    @ApiModelProperty(value = "辅助单位")
    @Where
    private String secondaryUomId;
    @ApiModelProperty(value = "主辅单位转换比例：基本计量单位/辅助单位")
    @Where
    private Double conversionRate;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
