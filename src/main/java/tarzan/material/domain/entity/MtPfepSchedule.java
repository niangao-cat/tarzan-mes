package tarzan.material.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料计划属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
@ApiModel("物料计划属性")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_pfep_schedule")
@CustomPrimary
public class MtPfepSchedule extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_PFEP_SCHEDULE_ID = "pfepScheduleId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_KEY_COMPONENT_FLAG = "keyComponentFlag";
    public static final String FIELD_SCHEDULE_FLAG = "scheduleFlag";
    public static final String FIELD_MAKE_TO_ORDER_FLAG = "makeToOrderFlag";
    public static final String FIELD_PROD_LINE_RULE = "prodLineRule";
    public static final String FIELD_PRE_PROCESSING_LEAD_TIME = "preProcessingLeadTime";
    public static final String FIELD_PROCESSING_LEAD_TIME = "processingLeadTime";
    public static final String FIELD_POST_PROCESSING_LEAD_TIME = "postProcessingLeadTime";
    public static final String FIELD_SAFETY_LEAD_TIME = "safetyLeadTime";
    public static final String FIELD_EXCEED_LEAD_TIME = "exceedLeadTime";
    public static final String FIELD_DEMAND_TIME_FENCE = "demandTimeFence";
    public static final String FIELD_RELEASE_TIME_FENCE = "releaseTimeFence";
    public static final String FIELD_ORDER_TIME_FENCE = "orderTimeFence";
    public static final String FIELD_DEMAND_MERGE_TIME_FENCE = "demandMergeTimeFence";
    public static final String FIELD_SUPPLY_MERGE_TIME_FENCE = "supplyMergeTimeFence";
    public static final String FIELD_SAFETY_STOCK_METHOD = "safetyStockMethod";
    public static final String FIELD_SAFETY_STOCK_PERIOD = "safetyStockPeriod";
    public static final String FIELD_SAFETY_STOCK_VALUE = "safetyStockValue";
    public static final String FIELD_ECONOMIC_LOT_SIZE = "economicLotSize";
    public static final String FIELD_ECONOMIC_SPLIT_PARAMETER = "economicSplitParameter";
    public static final String FIELD_MIN_ORDER_QTY = "minOrderQty";
    public static final String FIELD_FIXED_LOT_MULTIPLE = "fixedLotMultiple";
    public static final String FIELD_RATE_TYPE = "rateType";
    public static final String FIELD_RATE = "rate";
    public static final String FIELD_ACTIVITY = "activity";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_PROCESS_BATCH = "processBatch";
    public static final String FIELD_STANDARD_RATE_TYPE = "standardRateType";
    public static final String FIELD_STANDARD_RATE = "standardRate";
    public static final String FIELD_AUTO_ASSIGN_FLAG = "autoAssignFlag";
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
    @ApiModelProperty("主键，标识唯一一条记录")
    @Id
    @Where
    private String pfepScheduleId;
    @ApiModelProperty(value = "物料站点主键，标识唯一物料站点对应关系，限定为计划站点", required = true)
    @NotBlank
    @Where
    private String materialSiteId;
    @ApiModelProperty(value = "组织类型，可选计划站点下区域、生产线、工作单元等类型")
    @Where
    private String organizationType;
    @ApiModelProperty(value = "组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元")
    @Where
    private String organizationId;
    @ApiModelProperty(value = "关键组件标识，标识计算订单投入时，物料是否关键组件")
    @Where
    private String keyComponentFlag;
    @ApiModelProperty(value = "参与排产标识，只有标识为“是”的物料才能参与计划排程")
    @Where
    private String scheduleFlag;
    @ApiModelProperty(value = "是否按单生产，标识物料是否按单生产，只有按单生产物料才可以通过需求或订单分解创建下层订单")
    @Where
    private String makeToOrderFlag;
    @ApiModelProperty(value = "选线规则，如N、Z、N+Z、Z+等规则")
    @Where
    private String prodLineRule;
    @ApiModelProperty(value = "前处理提前期（小时）")
    @Where
    private Double preProcessingLeadTime;
    @ApiModelProperty(value = "处理提前期（小时）")
    @Where
    private Double processingLeadTime;
    @ApiModelProperty(value = "后处理提前期（小时）")
    @Where
    private Double postProcessingLeadTime;
    @ApiModelProperty(value = "安全生产周期（小时）")
    @Where
    private Double safetyLeadTime;
    @ApiModelProperty(value = "最大提前生产周期（天）")
    @Where
    private Double exceedLeadTime;
    @ApiModelProperty(value = "需求时间栏（天），通过设置需求时间栏的方式对进入计划排程的需求进行限定")
    @Where
    private Double demandTimeFence;
    @ApiModelProperty(value = "下达时间栏（天），通过设置下达时间栏来防止计划过早下达")
    @Where
    private Double releaseTimeFence;
    @ApiModelProperty(value = "订单时间栏（天），通过设置订单时间栏来限定下层物料计划的下达")
    @Where
    private Double orderTimeFence;
    @ApiModelProperty(value = "需求合并时间栏，通过设置需求合并时间栏来将一定时间内（比如1天或3天或1周）的需求合并进行批量生产")
    @Where
    private Double demandMergeTimeFence;
    @ApiModelProperty(value = "供应合并时间栏，通过设置供应合并时间栏来实现某个时间范围内的供应满足需求")
    @Where
    private Double supplyMergeTimeFence;
    @ApiModelProperty(value = "安全库存方法")
    @Where
    private String safetyStockMethod;
    @ApiModelProperty(value = "安全库存周期（天）")
    @Where
    private Double safetyStockPeriod;
    @ApiModelProperty(value = "安全库存")
    @Where
    private Double safetyStockValue;
    @ApiModelProperty(value = "经济批量，在综合考虑产能、计划的可能变更、生产组织管理等因素下确定的一次生产数量")
    @Where
    private Double economicLotSize;
    @ApiModelProperty(value = "批量舍入比例%，用于确定经济批量的多少比例需要进行合并，避免出现按批量拆分后的小批量尾数订单")
    @Where
    private Double economicSplitParameter;
    @ApiModelProperty(value = "最小订单数量")
    @Where
    private Double minOrderQty;
    @ApiModelProperty(value = "圆整批量，考虑产品包装或生产过程中的物流器具装载而设置的生产订单数量最小倍数")
    @Where
    private Double fixedLotMultiple;
    @ApiModelProperty(value = "速率类型，评估物料组织产能的具体单位，包括小时产量、秒两种")
    @Where
    private String rateType;
    @ApiModelProperty(value = "默认速率")
    @Where
    private Double rate;
    @ApiModelProperty(value = "开动率%，与默认速率配合使用，物料在组织上生产时的开动率")
    @Where
    private Double activity;
    @ApiModelProperty(value = "优先级")
    @Where
    private Long priority;
    @ApiModelProperty(value = "处理批量")
    @Where
    private Double processBatch;
    @ApiModelProperty(value = "标准速率类型，小时产量和生产节拍两种")
    @Where
    private String standardRateType;
    @ApiModelProperty(value = "标准速率，定义用于产能评估时考虑的速率")
    @Where
    private Double standardRate;
    @ApiModelProperty(value = "计划自动分配，维护组织时，用于标识自动计划排程时是否考虑该组织")
    @Where
    private String autoAssignFlag;
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
     * @return 主键，标识唯一一条记录
     */
    public String getPfepScheduleId() {
        return pfepScheduleId;
    }

    public void setPfepScheduleId(String pfepScheduleId) {
        this.pfepScheduleId = pfepScheduleId;
    }

    /**
     * @return 物料站点主键，标识唯一物料站点对应关系，限定为计划站点
     */
    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    /**
     * @return 组织类型，可选计划站点下区域、生产线、工作单元等类型
     */
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return 组织，取自所选组织类型下的组织结构表，标识唯一区域、生产线或工作单元
     */
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return 关键组件标识，标识计算订单投入时，物料是否关键组件
     */
    public String getKeyComponentFlag() {
        return keyComponentFlag;
    }

    public void setKeyComponentFlag(String keyComponentFlag) {
        this.keyComponentFlag = keyComponentFlag;
    }

    /**
     * @return 参与排产标识，只有标识为“是”的物料才能参与计划排程
     */
    public String getScheduleFlag() {
        return scheduleFlag;
    }

    public void setScheduleFlag(String scheduleFlag) {
        this.scheduleFlag = scheduleFlag;
    }

    /**
     * @return 是否按单生产，标识物料是否按单生产，只有按单生产物料才可以通过需求或订单分解创建下层订单
     */
    public String getMakeToOrderFlag() {
        return makeToOrderFlag;
    }

    public void setMakeToOrderFlag(String makeToOrderFlag) {
        this.makeToOrderFlag = makeToOrderFlag;
    }

    /**
     * @return 选线规则，如N、Z、N+Z、Z+等规则
     */
    public String getProdLineRule() {
        return prodLineRule;
    }

    public void setProdLineRule(String prodLineRule) {
        this.prodLineRule = prodLineRule;
    }

    /**
     * @return 前处理提前期（小时）
     */
    public Double getPreProcessingLeadTime() {
        return preProcessingLeadTime;
    }

    public void setPreProcessingLeadTime(Double preProcessingLeadTime) {
        this.preProcessingLeadTime = preProcessingLeadTime;
    }

    /**
     * @return 处理提前期（小时）
     */
    public Double getProcessingLeadTime() {
        return processingLeadTime;
    }

    public void setProcessingLeadTime(Double processingLeadTime) {
        this.processingLeadTime = processingLeadTime;
    }

    /**
     * @return 后处理提前期（小时）
     */
    public Double getPostProcessingLeadTime() {
        return postProcessingLeadTime;
    }

    public void setPostProcessingLeadTime(Double postProcessingLeadTime) {
        this.postProcessingLeadTime = postProcessingLeadTime;
    }

    /**
     * @return 安全生产周期（小时）
     */
    public Double getSafetyLeadTime() {
        return safetyLeadTime;
    }

    public void setSafetyLeadTime(Double safetyLeadTime) {
        this.safetyLeadTime = safetyLeadTime;
    }

    /**
     * @return 最大提前生产周期（天）
     */
    public Double getExceedLeadTime() {
        return exceedLeadTime;
    }

    public void setExceedLeadTime(Double exceedLeadTime) {
        this.exceedLeadTime = exceedLeadTime;
    }

    /**
     * @return 需求时间栏（天），通过设置需求时间栏的方式对进入计划排程的需求进行限定
     */
    public Double getDemandTimeFence() {
        return demandTimeFence;
    }

    public void setDemandTimeFence(Double demandTimeFence) {
        this.demandTimeFence = demandTimeFence;
    }

    /**
     * @return 下达时间栏（天），通过设置下达时间栏来防止计划过早下达
     */
    public Double getReleaseTimeFence() {
        return releaseTimeFence;
    }

    public void setReleaseTimeFence(Double releaseTimeFence) {
        this.releaseTimeFence = releaseTimeFence;
    }

    /**
     * @return 订单时间栏（天），通过设置订单时间栏来限定下层物料计划的下达
     */
    public Double getOrderTimeFence() {
        return orderTimeFence;
    }

    public void setOrderTimeFence(Double orderTimeFence) {
        this.orderTimeFence = orderTimeFence;
    }

    /**
     * @return 需求合并时间栏，通过设置需求合并时间栏来将一定时间内（比如1天或3天或1周）的需求合并进行批量生产
     */
    public Double getDemandMergeTimeFence() {
        return demandMergeTimeFence;
    }

    public void setDemandMergeTimeFence(Double demandMergeTimeFence) {
        this.demandMergeTimeFence = demandMergeTimeFence;
    }

    /**
     * @return 供应合并时间栏，通过设置供应合并时间栏来实现某个时间范围内的供应满足需求
     */
    public Double getSupplyMergeTimeFence() {
        return supplyMergeTimeFence;
    }

    public void setSupplyMergeTimeFence(Double supplyMergeTimeFence) {
        this.supplyMergeTimeFence = supplyMergeTimeFence;
    }

    /**
     * @return 安全库存方法
     */
    public String getSafetyStockMethod() {
        return safetyStockMethod;
    }

    public void setSafetyStockMethod(String safetyStockMethod) {
        this.safetyStockMethod = safetyStockMethod;
    }

    /**
     * @return 安全库存周期（天）
     */
    public Double getSafetyStockPeriod() {
        return safetyStockPeriod;
    }

    public void setSafetyStockPeriod(Double safetyStockPeriod) {
        this.safetyStockPeriod = safetyStockPeriod;
    }

    /**
     * @return 安全库存
     */
    public Double getSafetyStockValue() {
        return safetyStockValue;
    }

    public void setSafetyStockValue(Double safetyStockValue) {
        this.safetyStockValue = safetyStockValue;
    }

    /**
     * @return 经济批量，在综合考虑产能、计划的可能变更、生产组织管理等因素下确定的一次生产数量
     */
    public Double getEconomicLotSize() {
        return economicLotSize;
    }

    public void setEconomicLotSize(Double economicLotSize) {
        this.economicLotSize = economicLotSize;
    }

    /**
     * @return 批量舍入比例%，用于确定经济批量的多少比例需要进行合并，避免出现按批量拆分后的小批量尾数订单
     */
    public Double getEconomicSplitParameter() {
        return economicSplitParameter;
    }

    public void setEconomicSplitParameter(Double economicSplitParameter) {
        this.economicSplitParameter = economicSplitParameter;
    }

    /**
     * @return 最小订单数量
     */
    public Double getMinOrderQty() {
        return minOrderQty;
    }

    public void setMinOrderQty(Double minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    /**
     * @return 圆整批量，考虑产品包装或生产过程中的物流器具装载而设置的生产订单数量最小倍数
     */
    public Double getFixedLotMultiple() {
        return fixedLotMultiple;
    }

    public void setFixedLotMultiple(Double fixedLotMultiple) {
        this.fixedLotMultiple = fixedLotMultiple;
    }

    /**
     * @return 速率类型，评估物料组织产能的具体单位，包括小时产量、秒两种
     */
    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    /**
     * @return 默认速率
     */
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * @return 开动率%，与默认速率配合使用，物料在组织上生产时的开动率
     */
    public Double getActivity() {
        return activity;
    }

    public void setActivity(Double activity) {
        this.activity = activity;
    }

    /**
     * @return 优先级
     */
    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    /**
     * @return 处理批量
     */
    public Double getProcessBatch() {
        return processBatch;
    }

    public void setProcessBatch(Double processBatch) {
        this.processBatch = processBatch;
    }

    /**
     * @return 标准速率类型，小时产量和生产节拍两种
     */
    public String getStandardRateType() {
        return standardRateType;
    }

    public void setStandardRateType(String standardRateType) {
        this.standardRateType = standardRateType;
    }

    /**
     * @return 标准速率，定义用于产能评估时考虑的速率
     */
    public Double getStandardRate() {
        return standardRate;
    }

    public void setStandardRate(Double standardRate) {
        this.standardRate = standardRate;
    }

    /**
     * @return 计划自动分配，维护组织时，用于标识自动计划排程时是否考虑该组织
     */
    public String getAutoAssignFlag() {
        return autoAssignFlag;
    }

    public void setAutoAssignFlag(String autoAssignFlag) {
        this.autoAssignFlag = autoAssignFlag;
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
