package tarzan.method.domain.entity;

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
 * 装配清单行参考点关系历史表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@ApiModel("装配清单行参考点关系历史表")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_bom_reference_point_his")
@CustomPrimary
public class MtBomReferencePointHis extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_BOM_REFERENCE_POINT_HIS_ID = "bomReferencePointHisId";
    public static final String FIELD_BOM_REFERENCE_POINT_ID = "bomReferencePointId";
    public static final String FIELD_REFERENCE_POINT = "referencePoint";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_BOM_COMPONENT_ID = "bomComponentId";
    public static final String FIELD_LINE_NUMBER = "lineNumber";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_COPIED_FROM_POINT_ID = "copiedFromPointId";
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
    @ApiModelProperty("历史表ID，主键，供其他表做外键")
    @Id
    @Where
    private String bomReferencePointHisId;
    @ApiModelProperty(value = "装配清单行参考点ID", required = true)
    @NotBlank
    @Where
    private String bomReferencePointId;
    @ApiModelProperty(value = "装配清单行参考点描述", required = true)
    @NotBlank
    @Where
    private String referencePoint;
    @ApiModelProperty(value = "参考点数量", required = true)
    @NotNull
    @Where
    private Double qty;
    @ApiModelProperty(value = "参考点关联的BOM行ID", required = true)
    @NotBlank
    @Where
    private String bomComponentId;
    @ApiModelProperty(value = "参考点序号", required = true)
    @NotNull
    @Where
    private Long lineNumber;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "复制的来源参考点属性ID")
    @Where
    private String copiedFromPointId;
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
     * @return 历史表ID，主键，供其他表做外键
     */
    public String getBomReferencePointHisId() {
        return bomReferencePointHisId;
    }

    public void setBomReferencePointHisId(String bomReferencePointHisId) {
        this.bomReferencePointHisId = bomReferencePointHisId;
    }

    /**
     * @return 装配清单行参考点ID
     */
    public String getBomReferencePointId() {
        return bomReferencePointId;
    }

    public void setBomReferencePointId(String bomReferencePointId) {
        this.bomReferencePointId = bomReferencePointId;
    }

    /**
     * @return 装配清单行参考点描述
     */
    public String getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(String referencePoint) {
        this.referencePoint = referencePoint;
    }

    /**
     * @return 参考点数量
     */
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    /**
     * @return 参考点关联的BOM行ID
     */
    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    /**
     * @return 参考点序号
     */
    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
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
     * @return 复制的来源参考点属性ID
     */
    public String getCopiedFromPointId() {
        return copiedFromPointId;
    }

    public void setCopiedFromPointId(String copiedFromPointId) {
        this.copiedFromPointId = copiedFromPointId;
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
