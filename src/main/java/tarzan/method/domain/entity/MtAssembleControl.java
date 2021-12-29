package tarzan.method.domain.entity;

import java.util.Date;

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
 * 装配控制，定义一组装配控制要求，包括装配组可安装位置和装配点可装载物料
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@ApiModel("装配控制，定义一组装配控制要求，包括装配组可安装位置和装配点可装载物料")

@ModifyAudit

@Table(name = "mt_assemble_control")
@CustomPrimary
public class MtAssembleControl extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ASSEMBLE_CONTROL_ID = "assembleControlId";
    public static final String FIELD_OBJECT_TYPE = "objectType";
    public static final String FIELD_OBJECT_ID = "objectId";
    public static final String FIELD_ORGANIZATION_TYPE = "organizationType";
    public static final String FIELD_ORGANIZATION_ID = "organizationId";
    public static final String FIELD_REFERENCE_AREA = "referenceArea";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
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
    @ApiModelProperty("主键ID,表示唯一一条记录")
    @Id
    @Where
    private String assembleControlId;
    @ApiModelProperty(value = "对象类型，包括MATERIAL、WO、EO", required = true)
    @NotBlank
    @Where
    private String objectType;
    @ApiModelProperty(value = "对象的具体值", required = true)
    @NotBlank
    @Where
    private String objectId;
    @ApiModelProperty(value = "组织类型，包括SITE/PRODUCTION_LINE/WKC")
    @Where
    private String organizationType;
    @ApiModelProperty(value = "组织的具体值")
    @Where
    private String organizationId;
    @ApiModelProperty(value = "装配参考区域，针对多个产品一起装配或者一个产品拆分为多次装配的情况")
    @Where
    private String referenceArea;
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull
    @Where
    private Date dateFrom;
    @ApiModelProperty(value = "失效生产")
    @Where
    private Date dateTo;
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
     * @return 主键ID,表示唯一一条记录
     */
    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    /**
     * @return 对象类型，包括MATERIAL、WO、EO
     */
    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    /**
     * @return 对象的具体值
     */
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * @return 组织类型，包括SITE/PRODUCTION_LINE/WKC
     */
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return 组织的具体值
     */
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return 装配参考区域，针对多个产品一起装配或者一个产品拆分为多次装配的情况
     */
    public String getReferenceArea() {
        return referenceArea;
    }

    public void setReferenceArea(String referenceArea) {
        this.referenceArea = referenceArea;
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
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
