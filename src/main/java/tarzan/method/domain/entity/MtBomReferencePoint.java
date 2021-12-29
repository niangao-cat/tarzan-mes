package tarzan.method.domain.entity;

import java.io.Serializable;

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
 * 装配清单行参考点关系
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@ApiModel("装配清单行参考点关系")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_bom_reference_point")
@CustomPrimary
public class MtBomReferencePoint extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_BOM_REFERENCE_POINT_ID = "bomReferencePointId";
    public static final String FIELD_BOM_COMPONENT_ID = "bomComponentId";
    public static final String FIELD_LINE_NUMBER = "lineNumber";
    public static final String FIELD_REFERENCE_POINT = "referencePoint";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_COPIED_FROM_POINT_ID = "copiedFromPointId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -5498866137822750192L;

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
    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @Where
    private String bomReferencePointId;
    @ApiModelProperty(value = "装配组件清单行ID", required = true)
    @NotBlank
    @Where
    private String bomComponentId;
    @ApiModelProperty(value = "参考点序号", required = true)
    @NotNull
    @Where
    private Long lineNumber;
    @ApiModelProperty(value = "参考点位置描述", required = true)
    @NotBlank
    @Where
    private String referencePoint;
    @ApiModelProperty(value = "参考点数量", required = true)
    @NotNull
    @Where
    private Double qty;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "复制的来源参考点属性ID")
    @Where
    private String copiedFromPointId;
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
     * @return 表ID，主键，供其他表做外键
     */
    public String getBomReferencePointId() {
        return bomReferencePointId;
    }

    public void setBomReferencePointId(String bomReferencePointId) {
        this.bomReferencePointId = bomReferencePointId;
    }

    /**
     * @return 装配组件清单行ID
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
     * @return 参考点位置描述
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
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
