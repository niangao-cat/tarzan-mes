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
 * 装配清单头历史
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@ApiModel("装配清单头历史")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_bom_his")
@CustomPrimary
public class MtBomHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_BOM_HIS_ID = "bomHisId";
    public static final String FIELD_BOM_ID = "bomId";
    public static final String FIELD_BOM_NAME = "bomName";
    public static final String FIELD_REVISION = "revision";
    public static final String FIELD_BOM_TYPE = "bomType";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_CURRENT_FLAG = "currentFlag";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_BOM_STATUS = "bomStatus";
    public static final String FIELD_COPIED_FROM_BOM_ID = "copiedFromBomId";
    public static final String FIELD_RELEASED_FLAG = "releasedFlag";
    public static final String FIELD_PRIMARY_QTY = "primaryQty";
    public static final String FIELD_AUTO_REVISION_FLAG = "autoRevisionFlag";
    public static final String FIELD_ASSEMBLE_AS_MATERIAL_FLAG = "assembleAsMaterialFlag";
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
    @ApiModelProperty("装配清单头历史ID")
    @Id
    @Where
    private String bomHisId;
    @ApiModelProperty(value = "装配清单ID", required = true)
    @NotBlank
    @Where
    private String bomId;
    @ApiModelProperty(value = "装配清单名称", required = true)
    @NotBlank
    @Where
    private String bomName;
    @ApiModelProperty(value = "版本", required = true)
    @NotBlank
    @Where
    private String revision;
    @ApiModelProperty(value = "装配清单类型，包括物料、WO、EO的BOM类型", required = true)
    @NotBlank
    @Where
    private String bomType;
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull
    @Where
    private Date dateFrom;
    @ApiModelProperty(value = "失效生产")
    @Where
    private Date dateTo;
    @ApiModelProperty(value = "当前版本")
    @Where
    private String currentFlag;
    @ApiModelProperty(value = "物料清单描述")
    @Where
    private String description;
    @ApiModelProperty(value = "物料清单状态")
    @Where
    private String bomStatus;
    @ApiModelProperty(value = "引用的源物料清单ID")
    @Where
    private String copiedFromBomId;
    @ApiModelProperty(value = "已经下达EO标识")
    @Where
    private String releasedFlag;
    @ApiModelProperty(value = "基本数量")
    @Where
    private Double primaryQty;
    @ApiModelProperty(value = "自动升版本标识")
    @Where
    private String autoRevisionFlag;
    @ApiModelProperty(value = "是否按物料装配")
    @Where
    private String assembleAsMaterialFlag;
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
     * @return 装配清单头历史ID
     */
    public String getBomHisId() {
        return bomHisId;
    }

    public void setBomHisId(String bomHisId) {
        this.bomHisId = bomHisId;
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
     * @return 装配清单名称
     */
    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    /**
     * @return 版本
     */
    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * @return 装配清单类型，包括物料、WO、EO的BOM类型
     */
    public String getBomType() {
        return bomType;
    }

    public void setBomType(String bomType) {
        this.bomType = bomType;
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
     * @return 当前版本
     */
    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    /**
     * @return 物料清单描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 物料清单状态
     */
    public String getBomStatus() {
        return bomStatus;
    }

    public void setBomStatus(String bomStatus) {
        this.bomStatus = bomStatus;
    }

    /**
     * @return 引用的源物料清单ID
     */
    public String getCopiedFromBomId() {
        return copiedFromBomId;
    }

    public void setCopiedFromBomId(String copiedFromBomId) {
        this.copiedFromBomId = copiedFromBomId;
    }

    /**
     * @return 已经下达EO标识
     */
    public String getReleasedFlag() {
        return releasedFlag;
    }

    public void setReleasedFlag(String releasedFlag) {
        this.releasedFlag = releasedFlag;
    }

    /**
     * @return 基本数量
     */
    public Double getPrimaryQty() {
        return primaryQty;
    }

    public void setPrimaryQty(Double primaryQty) {
        this.primaryQty = primaryQty;
    }

    /**
     * @return 自动升版本标识
     */
    public String getAutoRevisionFlag() {
        return autoRevisionFlag;
    }

    public void setAutoRevisionFlag(String autoRevisionFlag) {
        this.autoRevisionFlag = autoRevisionFlag;
    }

    /**
     * @return 是否按物料装配
     */
    public String getAssembleAsMaterialFlag() {
        return assembleAsMaterialFlag;
    }

    public void setAssembleAsMaterialFlag(String assembleAsMaterialFlag) {
        this.assembleAsMaterialFlag = assembleAsMaterialFlag;
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
