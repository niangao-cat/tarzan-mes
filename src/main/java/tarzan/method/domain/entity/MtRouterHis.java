package tarzan.method.domain.entity;

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
 * 工艺路线历史
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@ApiModel("工艺路线历史")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_router_his")
@CustomPrimary
public class MtRouterHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ROUTER_HIS_ID = "routerHisId";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_ROUTER_NAME = "routerName";
    public static final String FIELD_ROUTER_TYPE = "routerType";
    public static final String FIELD_REVISION = "revision";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_CURRENT_FLAG = "currentFlag";
    public static final String FIELD_ROUTER_STATUS = "routerStatus";
    public static final String FIELD_ORIGINAL_STATUS = "originalStatus";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_BOM_ID = "bomId";
    public static final String FIELD_TEMPORARY_ROUTER_FLAG = "temporaryRouterFlag";
    public static final String FIELD_RELAXED_FLOW_FLAG = "relaxedFlowFlag";
    public static final String FIELD_HAS_BEEN_RELEASED_FLAG = "hasBeenReleasedFlag";
    public static final String FIELD_COPIED_FROM_ROUTER_ID = "copiedFromRouterId";
    public static final String FIELD_DISPOSITION_GROUP_ID = "dispositionGroupId";
    public static final String FIELD_HOLD_ID = "holdId";
    public static final String FIELD_AUTO_REVISION_FLAG = "autoRevisionFlag";
    public static final String FIELD_EVENT_ID = "eventId";
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
    @ApiModelProperty("工艺路线历史唯一标识")
    @Id
    @Where
    private String routerHisId;
    @ApiModelProperty(value = "工艺路线唯一标识", required = true)
    @NotBlank
    @Where
    private String routerId;
    @ApiModelProperty(value = "工艺路线简称", required = true)
    @NotBlank
    @Where
    private String routerName;
    @ApiModelProperty(value = "工艺路线类型", required = true)
    @NotBlank
    @Where
    private String routerType;
    @ApiModelProperty(value = "工艺路线版本", required = true)
    @NotBlank
    @Where
    private String revision;
    @ApiModelProperty(value = "工艺路线描述")
    @Where
    private String description;
    @ApiModelProperty(value = "是否为当前版本")
    @Where
    private String currentFlag;
    @ApiModelProperty(value = "工艺路线状态", required = true)
    @NotBlank
    @Where
    private String routerStatus;
    @ApiModelProperty(value = "来源状态")
    @Where
    private String originalStatus;
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull
    @Where
    private Date dateFrom;
    @ApiModelProperty(value = "失效时间")
    @Where
    private Date dateTo;
    @ApiModelProperty(value = "装配清单ID")
    @Where
    private String bomId;
    @ApiModelProperty(value = "是否为临时工艺路线")
    @Where
    private String temporaryRouterFlag;
    @ApiModelProperty(value = "是否为松散工艺路线")
    @Where
    private String relaxedFlowFlag;
    @ApiModelProperty(value = "是否已经应用于EO")
    @Where
    private String hasBeenReleasedFlag;
    @ApiModelProperty(value = "复制来源工艺路线标识")
    @Where
    private String copiedFromRouterId;
    @ApiModelProperty(value = "处置组")
    @Where
    private String dispositionGroupId;
    @ApiModelProperty(value = "当执行HOLD时，具体HOLD明细")
    @Where
    private String holdId;
    @ApiModelProperty(value = "自动升版本")
    @Where
    private String autoRevisionFlag;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
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
     * @return 工艺路线历史唯一标识
     */
    public String getRouterHisId() {
        return routerHisId;
    }

    public void setRouterHisId(String routerHisId) {
        this.routerHisId = routerHisId;
    }

    /**
     * @return 工艺路线唯一标识
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return 工艺路线简称
     */
    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    /**
     * @return 工艺路线类型
     */
    public String getRouterType() {
        return routerType;
    }

    public void setRouterType(String routerType) {
        this.routerType = routerType;
    }

    /**
     * @return 工艺路线版本
     */
    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * @return 工艺路线描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 是否为当前版本
     */
    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    /**
     * @return 工艺路线状态
     */
    public String getRouterStatus() {
        return routerStatus;
    }

    public void setRouterStatus(String routerStatus) {
        this.routerStatus = routerStatus;
    }

    /**
     * @return 来源状态
     */
    public String getOriginalStatus() {
        return originalStatus;
    }

    public void setOriginalStatus(String originalStatus) {
        this.originalStatus = originalStatus;
    }

    public Date getDateFrom() {
        if (dateFrom != null) {
            return (Date) dateFrom.clone();
        } else {
            return null;
        }
    }

    public void setDateFrom(Date dateFrom) {
        if (dateFrom == null) {
            this.dateFrom = null;
        } else {
            this.dateFrom = (Date) dateFrom.clone();
        }
    }

    public Date getDateTo() {
        if (dateTo != null) {
            return (Date) dateTo.clone();
        } else {
            return null;
        }
    }

    public void setDateTo(Date dateTo) {
        if (dateTo == null) {
            this.dateTo = null;
        } else {
            this.dateTo = (Date) dateTo.clone();
        }
    }

    /**
     * @return 装配清单ID
     */
    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    /**
     * @return 是否为临时工艺路线
     */
    public String getTemporaryRouterFlag() {
        return temporaryRouterFlag;
    }

    public void setTemporaryRouterFlag(String temporaryRouterFlag) {
        this.temporaryRouterFlag = temporaryRouterFlag;
    }

    /**
     * @return 是否为松散工艺路线
     */
    public String getRelaxedFlowFlag() {
        return relaxedFlowFlag;
    }

    public void setRelaxedFlowFlag(String relaxedFlowFlag) {
        this.relaxedFlowFlag = relaxedFlowFlag;
    }

    /**
     * @return 是否已经应用于EO
     */
    public String getHasBeenReleasedFlag() {
        return hasBeenReleasedFlag;
    }

    public void setHasBeenReleasedFlag(String hasBeenReleasedFlag) {
        this.hasBeenReleasedFlag = hasBeenReleasedFlag;
    }

    /**
     * @return 复制来源工艺路线标识
     */
    public String getCopiedFromRouterId() {
        return copiedFromRouterId;
    }

    public void setCopiedFromRouterId(String copiedFromRouterId) {
        this.copiedFromRouterId = copiedFromRouterId;
    }

    /**
     * @return 处置组
     */
    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    /**
     * @return 当执行HOLD时，具体HOLD明细
     */
    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }

    /**
     * @return 自动升版本
     */
    public String getAutoRevisionFlag() {
        return autoRevisionFlag;
    }

    public void setAutoRevisionFlag(String autoRevisionFlag) {
        this.autoRevisionFlag = autoRevisionFlag;
    }

    /**
     * @return 事件ID
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
