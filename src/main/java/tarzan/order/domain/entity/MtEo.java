package tarzan.order.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
 * 执行作业【执行作业需求和实绩拆分开】
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@ApiModel("执行作业【执行作业需求和实绩拆分开】")

@ModifyAudit

@Table(name = "mt_eo")
@CustomPrimary
public class MtEo extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_EO_NUM = "eoNum";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_LAST_EO_STATUS = "lastEoStatus";
    public static final String FIELD_PRODUCTION_LINE_ID = "productionLineId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_PLAN_START_TIME = "planStartTime";
    public static final String FIELD_PLAN_END_TIME = "planEndTime";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_UOM_ID = "uomId";
    public static final String FIELD_EO_TYPE = "eoType";
    public static final String FIELD_VALIDATE_FLAG = "validateFlag";
    public static final String FIELD_IDENTIFICATION = "identification";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_LATEST_HIS_ID = "latestHisId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -641521441243809863L;

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
    private String eoId;
    @ApiModelProperty(value = "EO编号", required = true)
    @NotBlank
    @Where
    private String eoNum;
    @ApiModelProperty(value = "生产站点", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "工单，EO对应生产指令")
    @NotBlank
    @Where
    private String workOrderId;
    @ApiModelProperty(value = "EO状态（运行、完成、作废、序列化等）", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "前次EO状态")
    @Where
    private String lastEoStatus;
    @ApiModelProperty(value = "生产线ID")
    @NotBlank
    @Where
    private String productionLineId;
    @ApiModelProperty(value = "当前工作单元（是否不需要，然后通过工艺路线步骤反映即可）", required = true)
    @NotBlank
    @Where
    private String workcellId;
    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull
    @Where
    private Date planStartTime;
    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull
    @Where
    private Date planEndTime;
    @ApiModelProperty(value = "数量", required = true)
    @NotNull
    @Where
    private Double qty;
    @ApiModelProperty(value = "单位", required = true)
    @NotBlank
    @Where
    private String uomId;
    @ApiModelProperty(value = "EO类型", required = true)
    @NotBlank
    @Where
    private String eoType;
    @ApiModelProperty(value = "是否验证通过", required = true)
    @NotBlank
    @Where
    private String validateFlag;
    @ApiModelProperty(value = "标识说明")
    @Where
    private String identification;
    @ApiModelProperty(value = "物料ID")
    @Where
    private String materialId;
    @ApiModelProperty(value = "最新一次新增历史表的主键")
    @Where
    private String latestHisId;

    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    // 20210826 add by sanfeng.zhang for hui.gu 增加非数据库字段
    @ApiModelProperty("返修条码")
    @Transient
    private String repairSn;

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
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return EO编号
     */
    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    /**
     * @return 生产站点
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 工单，EO对应生产指令
     */
    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    /**
     * @return EO状态（运行、完成、作废、序列化等）
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 前次EO状态
     */
    public String getLastEoStatus() {
        return lastEoStatus;
    }

    public void setLastEoStatus(String lastEoStatus) {
        this.lastEoStatus = lastEoStatus;
    }

    /**
     * @return 生产线ID
     */
    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    /**
     * @return 当前工作单元（是否不需要，然后通过工艺路线步骤反映即可）
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public Date getPlanStartTime() {
        if (planStartTime != null) {
            return (Date) planStartTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanStartTime(Date planStartTime) {
        if (planStartTime == null) {
            this.planStartTime = null;
        } else {
            this.planStartTime = (Date) planStartTime.clone();
        }
    }

    public Date getPlanEndTime() {
        if (planEndTime != null) {
            return (Date) planEndTime.clone();
        } else {
            return null;
        }
    }

    public void setPlanEndTime(Date planEndTime) {
        if (planEndTime == null) {
            this.planEndTime = null;
        } else {
            this.planEndTime = (Date) planEndTime.clone();
        }
    }

    /**
     * @return 数量
     */
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    /**
     * @return 单位
     */
    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    /**
     * @return EO类型
     */
    public String getEoType() {
        return eoType;
    }

    public void setEoType(String eoType) {
        this.eoType = eoType;
    }

    /**
     * @return 是否验证通过
     */
    public String getValidateFlag() {
        return validateFlag;
    }

    public void setValidateFlag(String validateFlag) {
        this.validateFlag = validateFlag;
    }

    /**
     * @return 标识说明
     */
    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    /**
     * @return 物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 最新一次新增历史表的主键
     */
    public String getLatestHisId() {
        return latestHisId;
    }

    public void setLatestHisId(String latestHisId) {
        this.latestHisId = latestHisId;
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

    public String getRepairSn() {
        return repairSn;
    }

    public void setRepairSn(String repairSn) {
        this.repairSn = repairSn;
    }
}