package tarzan.actual.domain.entity;

import java.io.Serializable;
import java.util.Date;

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
 * 不良代码记录
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:55
 */
@ApiModel("不良代码记录")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_nc_record")
@CustomPrimary
public class MtNcRecord extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NC_RECORD_ID = "ncRecordId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_PARENT_NC_RECORD_ID = "parentNcRecordId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_NC_INCIDENT_ID = "ncIncidentId";
    public static final String FIELD_DATE_TIME = "dateTime";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_DEFECT_COUNT = "defectCount";
    public static final String FIELD_COMMENTS = "comments";
    public static final String FIELD_NC_CODE_ID = "ncCodeId";
    public static final String FIELD_NC_TYPE = "ncType";
    public static final String FIELD_COMPONENT_MATERIAL_ID = "componentMaterialId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_REFERENCE_POINT = "referencePoint";
    public static final String FIELD_EO_STEP_ACTUAL_ID = "eoStepActualId";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_ROOT_CAUSE_OPERATION_ID = "rootCauseOperationId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_ROOT_CAUSE_WORKCELL_ID = "rootCauseWorkcellId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_NC_STATUS = "ncStatus";
    public static final String FIELD_CONFIRMED_STATUS = "confirmedStatus";
    public static final String FIELD_CONFIRMED_DATE_TIME = "confirmedDateTime";
    public static final String FIELD_CLOSURE_REQUIRED_FLAG = "closureRequiredFlag";
    public static final String FIELD_CLOSED_DATE_TIME = "closedDateTime";
    public static final String FIELD_CLOSED_USER_ID = "closedUserId";
    public static final String FIELD_DISPOSITION_DONE_FLAG = "dispositionDoneFlag";
    public static final String FIELD_DISPOSITION_GROUP_ID = "dispositionGroupId";
    public static final String FIELD_DISPOSITION_ROUTER_ID = "dispositionRouterId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 3899951252900315234L;

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
    @ApiModelProperty("唯一标识，表ID，主键，供其他表做外键")
    @Id
    @Where
    private String ncRecordId;
    @ApiModelProperty(value = "站点", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "EO", required = true)
    @NotBlank
    @Where
    private String eoId;
    @ApiModelProperty(value = "父不良记录")
    @Where
    private String parentNcRecordId;
    @ApiModelProperty(value = "记录人")
    @Where
    private Long userId;
    @ApiModelProperty(value = "顺序", required = true)
    @NotNull
    @Where
    private Long sequence;
    @ApiModelProperty(value = "不良事故ID", required = true)
    @NotBlank
    @Where
    private String ncIncidentId;
    @ApiModelProperty(value = "NC记录时间", required = true)
    @NotNull
    @Where
    private Date dateTime;
    @ApiModelProperty(value = "数量")
    @Where
    private Double qty;
    @ApiModelProperty(value = "缺陷数量")
    @Where
    private Double defectCount;
    @ApiModelProperty(value = "备注")
    @Where
    private String comments;
    @ApiModelProperty(value = "不良代码ID", required = true)
    @NotBlank
    @Where
    private String ncCodeId;
    @ApiModelProperty(value = "不良代码分类，缺陷/瑕疵/修复", required = true)
    @NotBlank
    @Where
    private String ncType;
    @ApiModelProperty(value = "NC记录的组件")
    @Where
    private String componentMaterialId;
    @ApiModelProperty(value = "物料批")
    @Where
    private String materialLotId;
    @ApiModelProperty(value = "组件装配参考点")
    @Where
    private String referencePoint;
    @ApiModelProperty(value = "步骤")
    @Where
    private String eoStepActualId;
    @ApiModelProperty(value = "工艺路线")
    @Where
    private String routerId;
    @ApiModelProperty(value = "工艺")
    @Where
    private String operationId;
    @ApiModelProperty(value = "产生问题的源工艺")
    @Where
    private String rootCauseOperationId;
    @ApiModelProperty(value = "工作单元")
    @Where
    private String workcellId;
    @ApiModelProperty(value = "产生问题的源工作单元")
    @Where
    private String rootCauseWorkcellId;
    @ApiModelProperty(value = "发生不良的装配件", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "NC状态，打开/关闭/取消", required = true)
    @NotBlank
    @Where
    private String ncStatus;
    @ApiModelProperty(value = "NC复核状态，已复核/未复核", required = true)
    @NotBlank
    @Where
    private String confirmedStatus;
    @ApiModelProperty(value = "复核时间")
    @Where
    private Date confirmedDateTime;
    @ApiModelProperty(value = "是否必须被关闭", required = true)
    @NotBlank
    @Where
    private String closureRequiredFlag;
    @ApiModelProperty(value = "事故发生日期")
    @Where
    private Date closedDateTime;
    @ApiModelProperty(value = "事故发生日期")
    @Where
    private Long closedUserId;
    @ApiModelProperty(value = "NC是否被处置，默认N", required = true)
    @NotBlank
    @Where
    private String dispositionDoneFlag;
    @ApiModelProperty(value = "处置组:NC被记录的处置功能")
    @Where
    private String dispositionGroupId;
    @ApiModelProperty(value = "处置工艺路线:NC被记录的处置路线")
    @Where
    private String dispositionRouterId;
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
     * @return 唯一标识，表ID，主键，供其他表做外键
     */
    public String getNcRecordId() {
        return ncRecordId;
    }

    public void setNcRecordId(String ncRecordId) {
        this.ncRecordId = ncRecordId;
    }

    /**
     * @return 站点
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return EO
     */
    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    /**
     * @return 父不良记录
     */
    public String getParentNcRecordId() {
        return parentNcRecordId;
    }

    public void setParentNcRecordId(String parentNcRecordId) {
        this.parentNcRecordId = parentNcRecordId;
    }

    /**
     * @return 记录人
     */
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return 顺序
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return 不良事故ID
     */
    public String getNcIncidentId() {
        return ncIncidentId;
    }

    public void setNcIncidentId(String ncIncidentId) {
        this.ncIncidentId = ncIncidentId;
    }

    /**
     * @return NC记录时间
     */

    public Date getDateTime() {
        if (dateTime != null) {
            return (Date) dateTime.clone();
        } else {
            return null;
        }
    }

    public void setDateTime(Date dateTime) {
        if (dateTime == null) {
            this.dateTime = null;
        } else {
            this.dateTime = (Date) dateTime.clone();
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
     * @return 缺陷数量
     */
    public Double getDefectCount() {
        return defectCount;
    }

    public void setDefectCount(Double defectCount) {
        this.defectCount = defectCount;
    }

    /**
     * @return 备注
     */
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return 不良代码ID
     */
    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    /**
     * @return 不良代码分类，缺陷/瑕疵/修复
     */
    public String getNcType() {
        return ncType;
    }

    public void setNcType(String ncType) {
        this.ncType = ncType;
    }

    /**
     * @return NC记录的组件
     */
    public String getComponentMaterialId() {
        return componentMaterialId;
    }

    public void setComponentMaterialId(String componentMaterialId) {
        this.componentMaterialId = componentMaterialId;
    }

    /**
     * @return 物料批
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    /**
     * @return 组件装配参考点
     */
    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    /**
     * @return 步骤
     */
    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    /**
     * @return 工艺路线
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return 工艺
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
     * @return 产生问题的源工艺
     */
    public String getRootCauseOperationId() {
        return rootCauseOperationId;
    }

    public void setRootCauseOperationId(String rootCauseOperationId) {
        this.rootCauseOperationId = rootCauseOperationId;
    }

    /**
     * @return 工作单元
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 产生问题的源工作单元
     */
    public String getRootCauseWorkcellId() {
        return rootCauseWorkcellId;
    }

    public void setRootCauseWorkcellId(String rootCauseWorkcellId) {
        this.rootCauseWorkcellId = rootCauseWorkcellId;
    }

    /**
     * @return 发生不良的装配件
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return NC状态，打开/关闭/取消
     */
    public String getNcStatus() {
        return ncStatus;
    }

    public void setNcStatus(String ncStatus) {
        this.ncStatus = ncStatus;
    }

    /**
     * @return NC复核状态，已复核/未复核
     */
    public String getConfirmedStatus() {
        return confirmedStatus;
    }

    public void setConfirmedStatus(String confirmedStatus) {
        this.confirmedStatus = confirmedStatus;
    }

    /**
     * @return 复核时间
     */
    public Date getConfirmedDateTime() {
        if (confirmedDateTime != null) {
            return (Date) confirmedDateTime.clone();
        } else {
            return null;
        }
    }

    public void setConfirmedDateTime(Date confirmedDateTime) {
        if (confirmedDateTime == null) {
            this.confirmedDateTime = null;
        } else {
            this.confirmedDateTime = (Date) confirmedDateTime.clone();
        }
    }

    /**
     * @return 是否必须被关闭
     */
    public String getClosureRequiredFlag() {
        return closureRequiredFlag;
    }

    public void setClosureRequiredFlag(String closureRequiredFlag) {
        this.closureRequiredFlag = closureRequiredFlag;
    }

    /**
     * @return 事故发生日期
     */
    public Date getClosedDateTime() {
        if (closedDateTime != null) {
            return (Date) closedDateTime.clone();
        } else {
            return null;
        }
    }

    public void setClosedDateTime(Date closedDateTime) {
        if (closedDateTime == null) {
            this.closedDateTime = null;
        } else {
            this.closedDateTime = (Date) closedDateTime.clone();
        }
    }

    /**
     * @return 事故发生日期
     */
    public Long getClosedUserId() {
        return closedUserId;
    }

    public void setClosedUserId(Long closedUserId) {
        this.closedUserId = closedUserId;
    }

    /**
     * @return NC是否被处置，默认N
     */
    public String getDispositionDoneFlag() {
        return dispositionDoneFlag;
    }

    public void setDispositionDoneFlag(String dispositionDoneFlag) {
        this.dispositionDoneFlag = dispositionDoneFlag;
    }

    /**
     * @return 处置组:NC被记录的处置功能
     */
    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    /**
     * @return 处置工艺路线:NC被记录的处置路线
     */
    public String getDispositionRouterId() {
        return dispositionRouterId;
    }

    public void setDispositionRouterId(String dispositionRouterId) {
        this.dispositionRouterId = dispositionRouterId;
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
