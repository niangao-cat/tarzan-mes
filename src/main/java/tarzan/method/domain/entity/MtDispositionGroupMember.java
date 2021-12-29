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
 * 处置组分配
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
@ApiModel("处置组分配")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_disposition_group_member")
@CustomPrimary
public class MtDispositionGroupMember extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DISPOSITION_GROUP_MEMBER_ID = "dispositionGroupMemberId";
    public static final String FIELD_DISPOSITION_GROUP_ID = "dispositionGroupId";
    public static final String FIELD_DISPOSITION_FUNCTION_ID = "dispositionFunctionId";
    public static final String FIELD_SEQUENCE = "sequence";
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
    private String dispositionGroupMemberId;
    @ApiModelProperty(value = "处置组ID", required = true)
    @NotBlank
    @Where
    private String dispositionGroupId;
    @ApiModelProperty(value = "处置方法", required = true)
    @NotBlank
    @Where
    private String dispositionFunctionId;
    @ApiModelProperty(value = "序号", required = true)
    @NotNull
    @Where
    private Long sequence;
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
    public String getDispositionGroupMemberId() {
        return dispositionGroupMemberId;
    }

    public void setDispositionGroupMemberId(String dispositionGroupMemberId) {
        this.dispositionGroupMemberId = dispositionGroupMemberId;
    }

    /**
     * @return 处置组ID
     */
    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
    }

    /**
     * @return 处置方法
     */
    public String getDispositionFunctionId() {
        return dispositionFunctionId;
    }

    public void setDispositionFunctionId(String dispositionFunctionId) {
        this.dispositionFunctionId = dispositionFunctionId;
    }

    /**
     * @return 序号
     */
    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
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
