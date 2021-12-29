package tarzan.method.domain.entity;

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
 * 装配清单行替代项
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@ApiModel("装配清单行替代项")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_bom_substitute")
@CustomPrimary
public class MtBomSubstitute extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_BOM_SUBSTITUTE_ID = "bomSubstituteId";
    public static final String FIELD_BOM_SUBSTITUTE_GROUP_ID = "bomSubstituteGroupId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_SUBSTITUTE_VALUE = "substituteValue";
    public static final String FIELD_SUBSTITUTE_USAGE = "substituteUsage";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_COPIED_FROM_SUBSTITUTE_ID = "copiedFromSubstituteId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -210587155555507910L;

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
    @ApiModelProperty("唯一性索引")
    @Id
    @Where
    private String bomSubstituteId;
    @ApiModelProperty(value = "关联的装配清单行替代组ID", required = true)
    @NotBlank
    @Where
    private String bomSubstituteGroupId;
    @ApiModelProperty(value = "替代物料ID", required = true)
    @NotBlank
    @Where
    private String materialId;
    @ApiModelProperty(value = "替代值（可能是百分比可能是优先级）", required = true)
    @NotNull
    @Where
    private Double substituteValue;
    @ApiModelProperty(value = "替代用量", required = true)
    @NotNull
    @Where
    private Double substituteUsage;
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull
    @Where
    private Date dateFrom;
    @ApiModelProperty(value = "失效生产")
    @Where
    private Date dateTo;
    @ApiModelProperty(value = "复制的来源替代项属性ID")
    @Where
    private String copiedFromSubstituteId;
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
     * @return 唯一性索引
     */
    public String getBomSubstituteId() {
        return bomSubstituteId;
    }

    public void setBomSubstituteId(String bomSubstituteId) {
        this.bomSubstituteId = bomSubstituteId;
    }

    /**
     * @return 关联的装配清单行替代组ID
     */
    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }

    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
    }

    /**
     * @return 替代物料ID
     */
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return 替代值（可能是百分比可能是优先级）
     */
    public Double getSubstituteValue() {
        return substituteValue;
    }

    public void setSubstituteValue(Double substituteValue) {
        this.substituteValue = substituteValue;
    }

    /**
     * @return 替代用量
     */
    public Double getSubstituteUsage() {
        return substituteUsage;
    }

    public void setSubstituteUsage(Double substituteUsage) {
        this.substituteUsage = substituteUsage;
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
     * @return 复制的来源替代项属性ID
     */
    public String getCopiedFromSubstituteId() {
        return copiedFromSubstituteId;
    }

    public void setCopiedFromSubstituteId(String copiedFromSubstituteId) {
        this.copiedFromSubstituteId = copiedFromSubstituteId;
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
