package tarzan.modeling.domain.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.*;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 库位
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@ApiModel("库位")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_mod_locator")
@CustomPrimary
public class MtModLocator extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_LOCATOR_CODE = "locatorCode";
    public static final String FIELD_LOCATOR_NAME = "locatorName";
    public static final String FIELD_LOCATOR_LOCATION = "locatorLocation";
    public static final String FIELD_LOCATOR_TYPE = "locatorType";
    public static final String FIELD_LOCATOR_GROUP_ID = "locatorGroupId";
    public static final String FIELD_LENGTH = "length";
    public static final String FIELD_WIDTH = "width";
    public static final String FIELD_HEIGHT = "height";
    public static final String FIELD_SIZE_UOM_ID = "sizeUomId";
    public static final String FIELD_MAX_WEIGHT = "maxWeight";
    public static final String FIELD_WEIGHT_UOM_ID = "weightUomId";
    public static final String FIELD_MAX_CAPACITY = "maxCapacity";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_PARENT_LOCATOR_ID = "parentLocatorId";
    public static final String FIELD_LOCATOR_CATEGORY = "locatorCategory";
    public static final String FIELD_NEGATIVE_FLAG = "negativeFlag";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 8811477929128747704L;

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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String locatorId;
    @ApiModelProperty(value = "库位编码", required = true)
    @NotBlank
    @Where
    private String locatorCode;
    @ApiModelProperty(value = "库位名称", required = true)
    @NotBlank
    @MultiLanguageField
    @Where
    private String locatorName;
    @ApiModelProperty(value = "货位位置")
    @MultiLanguageField
    @Where
    private String locatorLocation;
    @ApiModelProperty(value = "库位类型", required = true)
    @NotBlank
    @Where
    private String locatorType;
    @ApiModelProperty(value = "所属库位组ID")
    @Where
    private String locatorGroupId;
    @ApiModelProperty(value = "长")
    @Where
    private Double length;
    @ApiModelProperty(value = "宽")
    @Where
    private Double width;
    @ApiModelProperty(value = "高")
    @Where
    private Double height;
    @ApiModelProperty(value = "货位的尺寸单位")
    @Where
    private String sizeUomId;
    @ApiModelProperty(value = "最大载重重量")
    @Where
    private Double maxWeight;
    @ApiModelProperty(value = "重量单位")
    @Where
    private String weightUomId;
    @ApiModelProperty(value = "容量")
    @Where
    private Double maxCapacity;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "父层库位ID")
    @Where
    private String parentLocatorId;
    @ApiModelProperty(value = "库位类别，区域/库存/地点类别", required = true)
    @NotBlank
    @Where
    private String locatorCategory;
    @ApiModelProperty(value = "是否允许负库存，标识库位库存是否允许为负值", required = true)
    @NotBlank
    @Where
    private String negativeFlag;
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 库位编码
     */
    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    /**
     * @return 库位名称
     */
    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
    }

    /**
     * @return 货位位置
     */
    public String getLocatorLocation() {
        return locatorLocation;
    }

    public void setLocatorLocation(String locatorLocation) {
        this.locatorLocation = locatorLocation;
    }

    /**
     * @return 库位类型
     */
    public String getLocatorType() {
        return locatorType;
    }

    public void setLocatorType(String locatorType) {
        this.locatorType = locatorType;
    }

    /**
     * @return 所属库位组ID
     */
    public String getLocatorGroupId() {
        return locatorGroupId;
    }

    public void setLocatorGroupId(String locatorGroupId) {
        this.locatorGroupId = locatorGroupId;
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
     * @return 货位的尺寸单位
     */
    public String getSizeUomId() {
        return sizeUomId;
    }

    public void setSizeUomId(String sizeUomId) {
        this.sizeUomId = sizeUomId;
    }

    /**
     * @return 最大载重重量
     */
    public Double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
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
     * @return 容量
     */
    public Double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Double maxCapacity) {
        this.maxCapacity = maxCapacity;
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
     * @return 父层库位ID
     */
    public String getParentLocatorId() {
        return parentLocatorId;
    }

    public void setParentLocatorId(String parentLocatorId) {
        this.parentLocatorId = parentLocatorId;
    }

    /**
     * @return 库位类别，区域/库存/地点类别
     */
    public String getLocatorCategory() {
        return locatorCategory;
    }

    public void setLocatorCategory(String locatorCategory) {
        this.locatorCategory = locatorCategory;
    }

    /**
     * @return 是否允许负库存，标识库位库存是否允许为负值
     */
    public String getNegativeFlag() {
        return negativeFlag;
    }

    public void setNegativeFlag(String negativeFlag) {
        this.negativeFlag = negativeFlag;
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

    @Transient
    @ApiModelProperty(value = "区域库位ID")
    private String areaLocatorId;
    @Transient
    @ApiModelProperty(value = "区域库位编码")
    private String areaLocatorCode;
    public String getAreaLocatorId() {
        return areaLocatorId;
    }

    public void setAreaLocatorId(String areaLocatorId) {
        this.areaLocatorId = areaLocatorId;
    }

    public String getAreaLocatorCode() {
        return areaLocatorCode;
    }

    public void setAreaLocatorCode(String areaLocatorCode) {
        this.areaLocatorCode = areaLocatorCode;
    }
}
