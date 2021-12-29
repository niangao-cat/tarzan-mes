package tarzan.material.domain.entity;

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
 * 物料配送属性（一个WKC的物料类别和物料不能有从属关系）
 *
 * @author yiyang.xie 2020-02-04 15:52:02
 */
@ApiModel("物料配送属性（一个WKC的物料类别和物料不能有从属关系）")
@ModifyAudit
@Table(name = "mt_pfep_distribution")
@CustomPrimary
public class MtPfepDistribution extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PFEP_DISTRIBUTION_ID = "pfepDistributionId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_LOCATOR_CAPACITY = "locatorCapacity";
    public static final String FIELD_FROM_SCHEDULE_RATE_FLAG = "fromScheduleRateFlag";
    public static final String FIELD_MATERIAL_CONSUME_RATE_UOM_ID = "materialConsumeRateUomId";
    public static final String FIELD_MATERIAL_CONSUME_RATE = "materialConsumeRate";
    public static final String FIELD_BUFFER_INVENTORY = "bufferInventory";
    public static final String FIELD_BUFFER_PERIOD = "bufferPeriod";
    public static final String FIELD_MIN_INVENTORY = "minInventory";
    public static final String FIELD_MAX_INVENTORY = "maxInventory";
    public static final String FIELD_PACK_QTY = "packQty";
    public static final String FIELD_MULTIPLES_OF_PACK_FLAG = "multiplesOfPackFlag";
    public static final String FIELD_AREA_LOCATOR_ID = "areaLocatorId";
    public static final String FIELD_AREA_ID = "areaId";
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
    @ApiModelProperty("主键，唯一标识")
    @Id
    @Where
    private String pfepDistributionId;
    @ApiModelProperty(value = "物料站点主键，标识唯一物料站点对应关系，限定为生产站点", required = true)
    @NotBlank
    @Where
    private String materialSiteId;
    @ApiModelProperty(value = "限定类型为工作单元", required = true)
    @NotBlank
    @Where
    private String organizationType;
    @ApiModelProperty(value = "工作单元ID", required = true)
    @NotBlank
    @Where
    private String organizationId;
    @ApiModelProperty(value = "线边库位")
    @Where
    private String locatorId;
    @ApiModelProperty(value = "物料在线边库位的容量")
    @Where
    private Double locatorCapacity;
    @ApiModelProperty(value = "是否采用装配件生产速率（按WO补货模式和按顺序补货模式可以采取装配件街拍，其它模式只能采用组件节拍）")
    @Where
    private String fromScheduleRateFlag;
    @ApiModelProperty(value = "物料消耗的速率单位，统一用per/h单位，计算总数时，要将时间换算成h再进行计算")
    @Where
    private String materialConsumeRateUomId;
    @ApiModelProperty(value = "物料消耗的速率@")
    @Where
    private Double materialConsumeRate;
    @ApiModelProperty(value = "安全库存a")
    @Where
    private Double bufferInventory;
    @ApiModelProperty(value = "缓冲时间A,单位小时（h）")
    @Where
    private Double bufferPeriod;
    @ApiModelProperty(value = "最小库存")
    @Where
    private Double minInventory;
    @ApiModelProperty(value = "最大库存")
    @Where
    private Double maxInventory;
    @ApiModelProperty(value = "配送包装数")
    @Where
    private Double packQty;
    @ApiModelProperty(value = "配送数量是否为包装数的倍数")
    @Where
    private String multiplesOfPackFlag;
    @ApiModelProperty(value = "关联配送库位")
    @Where
    private String areaLocatorId;
    @ApiModelProperty(value = "关联配送路线，必输", required = true)
    @NotBlank
    @Where
    private String areaId;
    @ApiModelProperty(value = "有效性", required = true)
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
     * @return 主键，唯一标识
     */
    public String getPfepDistributionId() {
        return pfepDistributionId;
    }

    public void setPfepDistributionId(String pfepDistributionId) {
        this.pfepDistributionId = pfepDistributionId;
    }

    /**
     * @return 物料站点主键，标识唯一物料站点对应关系，限定为生产站点
     */
    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    /**
     * @return 限定类型为工作单元
     */
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return 工作单元ID
     */
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return 线边库位
     */
    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    /**
     * @return 物料在线边库位的容量
     */
    public Double getLocatorCapacity() {
        return locatorCapacity;
    }

    public void setLocatorCapacity(Double locatorCapacity) {
        this.locatorCapacity = locatorCapacity;
    }

    /**
     * @return 是否采用装配件生产速率（按WO补货模式和按顺序补货模式可以采取装配件街拍，其它模式只能采用组件节拍）
     */
    public String getFromScheduleRateFlag() {
        return fromScheduleRateFlag;
    }

    public void setFromScheduleRateFlag(String fromScheduleRateFlag) {
        this.fromScheduleRateFlag = fromScheduleRateFlag;
    }

    /**
     * @return 物料消耗的速率单位，统一用per/h单位，计算总数时，要将时间换算成h再进行计算
     */
    public String getMaterialConsumeRateUomId() {
        return materialConsumeRateUomId;
    }

    public void setMaterialConsumeRateUomId(String materialConsumeRateUomId) {
        this.materialConsumeRateUomId = materialConsumeRateUomId;
    }

    /**
     * @return 物料消耗的速率@
     */
    public Double getMaterialConsumeRate() {
        return materialConsumeRate;
    }

    public void setMaterialConsumeRate(Double materialConsumeRate) {
        this.materialConsumeRate = materialConsumeRate;
    }

    /**
     * @return 安全库存a
     */
    public Double getBufferInventory() {
        return bufferInventory;
    }

    public void setBufferInventory(Double bufferInventory) {
        this.bufferInventory = bufferInventory;
    }

    /**
     * @return 缓冲时间A,单位小时（h）
     */
    public Double getBufferPeriod() {
        return bufferPeriod;
    }

    public void setBufferPeriod(Double bufferPeriod) {
        this.bufferPeriod = bufferPeriod;
    }

    /**
     * @return 最小库存
     */
    public Double getMinInventory() {
        return minInventory;
    }

    public void setMinInventory(Double minInventory) {
        this.minInventory = minInventory;
    }

    /**
     * @return 最大库存
     */
    public Double getMaxInventory() {
        return maxInventory;
    }

    public void setMaxInventory(Double maxInventory) {
        this.maxInventory = maxInventory;
    }

    /**
     * @return 配送包装数
     */
    public Double getPackQty() {
        return packQty;
    }

    public void setPackQty(Double packQty) {
        this.packQty = packQty;
    }

    /**
     * @return 配送数量是否为包装数的倍数
     */
    public String getMultiplesOfPackFlag() {
        return multiplesOfPackFlag;
    }

    public void setMultiplesOfPackFlag(String multiplesOfPackFlag) {
        this.multiplesOfPackFlag = multiplesOfPackFlag;
    }

    /**
     * @return 关联配送库位
     */
    public String getAreaLocatorId() {
        return areaLocatorId;
    }

    public void setAreaLocatorId(String areaLocatorId) {
        this.areaLocatorId = areaLocatorId;
    }

    /**
     * @return 关联配送路线，必输
     */
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @return 有效性
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
