package tarzan.inventory.domain.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 容器类型，定义一类容器和一类容器的控制属性，包括容器可装载的对象类型、最大容量、混装类型等
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@ApiModel("容器类型，定义一类容器和一类容器的控制属性，包括容器可装载的对象类型、最大容量、混装类型等")

@ModifyAudit

@Table(name = "mt_container_type")
@CustomPrimary
public class MtContainerType extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CONTAINER_TYPE_ID = "containerTypeId";
    public static final String FIELD_CONTAINER_TYPE_CODE = "containerTypeCode";
    public static final String FIELD_CONTAINER_TYPE_DESCRIPTION = "containerTypeDescription";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_PACKING_LEVEL = "packingLevel";
    public static final String FIELD_CAPACITY_QTY = "capacityQty";
    public static final String FIELD_MIXED_MATERIAL_FLAG = "mixedMaterialFlag";
    public static final String FIELD_MIXED_EO_FLAG = "mixedEoFlag";
    public static final String FIELD_MIXED_WO_FLAG = "mixedWoFlag";
    public static final String FIELD_MIXED_OWNER_FLAG = "mixedOwnerFlag";
    public static final String FIELD_LENGTH = "length";
    public static final String FIELD_WIDTH = "width";
    public static final String FIELD_HEIGHT = "height";
    public static final String FIELD_WEIGHT = "weight";
    public static final String FIELD_SIZE_UOM_ID = "sizeUomId";
    public static final String FIELD_MAX_LOAD_WEIGHT = "maxLoadWeight";
    public static final String FIELD_WEIGHT_UOM_ID = "weightUomId";
    public static final String FIELD_LOCATION_ENABLED_FLAG = "locationEnabledFlag";
    public static final String FIELD_LOCATION_ROW = "locationRow";
    public static final String FIELD_LOCATION_COLUMN = "locationColumn";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -6287186798570186744L;

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
    @ApiModelProperty("作为物料批唯一标识，用于其他数据结构引用该容器类型")
    @Id
    @Where
    private String containerTypeId;
    @ApiModelProperty(value = "该容器类型的编码CODE", required = true)
    @NotBlank
    @Where
    private String containerTypeCode;
    @ApiModelProperty(value = "该容器类型的描述")
    @Where
    private String containerTypeDescription;
    @ApiModelProperty(value = "描述该容器类型的有效状描述该容器类型的有效状态：Y：是，表示物料批当前有效N：否，表示物料批当前已经无效态：", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "该容器类型限定的装载对象类型")
    @Where
    private String packingLevel;
    @ApiModelProperty(value = "表示该类容器最多允许装入的数量（通过主单位计量）")
    @Where
    private Double capacityQty;
    @ApiModelProperty(value = "指示该类容器是否允许放入不同的物料")
    @Where
    private String mixedMaterialFlag;
    @ApiModelProperty(value = "指示该类容器是否允许放入不同执行作业的产品")
    @Where
    private String mixedEoFlag;
    @ApiModelProperty(value = "指示该类容器是否允许放入不同生产指令的产品或在制品")
    @Where
    private String mixedWoFlag;
    @ApiModelProperty(value = "指示该类容器是否允许放入所有权的产品")
    @Where
    private String mixedOwnerFlag;
    @ApiModelProperty(value = "指示该容器的长度值，配合尺寸单位一起使用")
    @Where
    private Double length;
    @ApiModelProperty(value = "指示该容器的宽度值，配合尺寸单位一起使用")
    @Where
    private Double width;
    @ApiModelProperty(value = "指示该容器的高度值，配合尺寸单位一起使用")
    @Where
    private Double height;
    @ApiModelProperty(value = "指示该容器的重量值，配合重量单位一起使用")
    @Where
    private Double weight;
    @ApiModelProperty(value = "指定容器长宽高值的数值单位，如MM/CM/M，需与单位维护内容保持一致")
    @Where
    private String sizeUomId;
    @ApiModelProperty(value = "指示该容器最大可承载重量值，配合重量单位一起使用")
    @Where
    private Double maxLoadWeight;
    @ApiModelProperty(value = "指定容器重量、承载重量值的单位，如KG/G/T，应与单位维护内容保持一致")
    @Where
    private String weightUomId;
    @ApiModelProperty(value = "指示是否需要启用容器的位置，启用后需通过行、列字段定义容器的位置")
    @Where
    private String locationEnabledFlag;
    @ApiModelProperty(value = "启用容器位置时定义该类容器的行数")
    @Where
    private Long locationRow;
    @ApiModelProperty(value = "启用容器位置时定义该类容器的列数")
    @Where
    private Long locationColumn;
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
     * @return 作为物料批唯一标识，用于其他数据结构引用该容器类型
     */
    public String getContainerTypeId() {
        return containerTypeId;
    }

    public void setContainerTypeId(String containerTypeId) {
        this.containerTypeId = containerTypeId;
    }

    /**
     * @return 该容器类型的编码CODE
     */
    public String getContainerTypeCode() {
        return containerTypeCode;
    }

    public void setContainerTypeCode(String containerTypeCode) {
        this.containerTypeCode = containerTypeCode;
    }

    /**
     * @return 该容器类型的描述
     */
    public String getContainerTypeDescription() {
        return containerTypeDescription;
    }

    public void setContainerTypeDescription(String containerTypeDescription) {
        this.containerTypeDescription = containerTypeDescription;
    }

    /**
     * @return 描述该容器类型的有效状描述该容器类型的有效状态：Y：是，表示物料批当前有效N：否，表示物料批当前已经无效态：
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 该容器类型限定的装载对象类型： EO：表示该类容器作为在制品流转载具使用，允许装入执行作业 MATERIAL_LOT：表示该类容器允许装入物料批
     *         CONTAINER：表示该类容器允许装入其他容器 空：不做限制
     */
    public String getPackingLevel() {
        return packingLevel;
    }

    public void setPackingLevel(String packingLevel) {
        this.packingLevel = packingLevel;
    }

    /**
     * @return 表示该类容器最多允许装入的数量（通过主单位计量）
     */
    public Double getCapacityQty() {
        return capacityQty;
    }

    public void setCapacityQty(Double capacityQty) {
        this.capacityQty = capacityQty;
    }

    /**
     * @return 指示该类容器是否允许放入不同的物料 Y：允许放入多种物料 N：只能放入一种物料
     */
    public String getMixedMaterialFlag() {
        return mixedMaterialFlag;
    }

    public void setMixedMaterialFlag(String mixedMaterialFlag) {
        this.mixedMaterialFlag = mixedMaterialFlag;
    }

    /**
     * @return 指示该类容器是否允许放入不同执行作业的产品 Y：允许放入多个执行作业的产品 N：只能放入一个执行作业的产品
     */
    public String getMixedEoFlag() {
        return mixedEoFlag;
    }

    public void setMixedEoFlag(String mixedEoFlag) {
        this.mixedEoFlag = mixedEoFlag;
    }

    /**
     * @return 指示该类容器是否允许放入不同生产指令的产品或在制品 Y：允许放入多个生产指令的产品或在制品 N：只能放入一个生产指令的产品或在制品
     */
    public String getMixedWoFlag() {
        return mixedWoFlag;
    }

    public void setMixedWoFlag(String mixedWoFlag) {
        this.mixedWoFlag = mixedWoFlag;
    }

    /**
     * @return 指示该类容器是否允许放入所有权的产品 Y：允许放入多个所有权的产品 N：只能放入一个所有权的产品
     */
    public String getMixedOwnerFlag() {
        return mixedOwnerFlag;
    }

    public void setMixedOwnerFlag(String mixedOwnerFlag) {
        this.mixedOwnerFlag = mixedOwnerFlag;
    }

    /**
     * @return 指示该容器的长度值，配合尺寸单位一起使用
     */
    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    /**
     * @return 指示该容器的宽度值，配合尺寸单位一起使用
     */
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    /**
     * @return 指示该容器的高度值，配合尺寸单位一起使用
     */
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    /**
     * @return 指示该容器的重量值，配合重量单位一起使用
     */
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * @return 指定容器长宽高值的数值单位，如MM/CM/M，需与单位维护内容保持一致
     */
    public String getSizeUomId() {
        return sizeUomId;
    }

    public void setSizeUomId(String sizeUomId) {
        this.sizeUomId = sizeUomId;
    }

    /**
     * @return 指示该容器最大可承载重量值，配合重量单位一起使用
     */
    public Double getMaxLoadWeight() {
        return maxLoadWeight;
    }

    public void setMaxLoadWeight(Double maxLoadWeight) {
        this.maxLoadWeight = maxLoadWeight;
    }

    /**
     * @return 指定容器重量、承载重量值的单位，如KG/G/T，应与单位维护内容保持一致
     */
    public String getWeightUomId() {
        return weightUomId;
    }

    public void setWeightUomId(String weightUomId) {
        this.weightUomId = weightUomId;
    }

    /**
     * @return 指示是否需要启用容器的位置，启用后需通过行、列字段定义容器的位置
     */
    public String getLocationEnabledFlag() {
        return locationEnabledFlag;
    }

    public void setLocationEnabledFlag(String locationEnabledFlag) {
        this.locationEnabledFlag = locationEnabledFlag;
    }

    /**
     * @return 启用容器位置时定义该类容器的行数
     */
    public Long getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(Long locationRow) {
        this.locationRow = locationRow;
    }

    /**
     * @return 启用容器位置时定义该类容器的列数
     */
    public Long getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(Long locationColumn) {
        this.locationColumn = locationColumn;
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
