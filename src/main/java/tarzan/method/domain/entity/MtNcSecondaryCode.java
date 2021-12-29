package tarzan.method.domain.entity;

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
 * 次级不良代码
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@ApiModel("次级不良代码")

@ModifyAudit

@Table(name = "mt_nc_secondary_code")
@CustomPrimary
public class MtNcSecondaryCode extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NC_SECONDARY_CODE_ID = "ncSecondaryCodeId";
    public static final String FIELD_NC_OBJECT_ID = "ncObjectId";
    public static final String FIELD_NC_OBJECT_TYPE = "ncObjectType";
    public static final String FIELD_NC_CODE_ID = "ncCodeId";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_REQUIRED_FLAG = "requiredFlag";
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
    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @Where
    private String ncSecondaryCodeId;
    @ApiModelProperty(value = "不良代码或不良代码组", required = true)
    @NotBlank
    @Where
    private String ncObjectId;
    @ApiModelProperty(value = "类型", required = true)
    @NotBlank
    @Where
    private String ncObjectType;
    @ApiModelProperty(value = "不良代码", required = true)
    @NotBlank
    @Where
    private String ncCodeId;
    @ApiModelProperty(value = "顺序", required = true)
    @NotNull
    @Where
    private Long sequence;
    @ApiModelProperty(value = "关闭是否需要", required = true)
    @NotBlank
    @Where
    private String requiredFlag;
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
    public String getNcSecondaryCodeId() {
        return ncSecondaryCodeId;
    }

    public void setNcSecondaryCodeId(String ncSecondaryCodeId) {
        this.ncSecondaryCodeId = ncSecondaryCodeId;
    }

    /**
     * @return 不良代码或不良代码组
     */
    public String getNcObjectId() {
        return ncObjectId;
    }

    public void setNcObjectId(String ncObjectId) {
        this.ncObjectId = ncObjectId;
    }

    /**
     * @return 类型
     */
    public String getNcObjectType() {
        return ncObjectType;
    }

    public void setNcObjectType(String ncObjectType) {
        this.ncObjectType = ncObjectType;
    }

    /**
     * @return 不良代码
     */
    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
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
     * @return 关闭是否需要
     */
    public String getRequiredFlag() {
        return requiredFlag;
    }

    public void setRequiredFlag(String requiredFlag) {
        this.requiredFlag = requiredFlag;
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
