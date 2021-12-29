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
 * 不良代码工艺分配
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@ApiModel("不良代码工艺分配")

@ModifyAudit

@Table(name = "mt_nc_valid_oper")
@CustomPrimary
public class MtNcValidOper extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NC_VALID_OPER_ID = "ncValidOperId";
    public static final String FIELD_NC_OBJECT_ID = "ncObjectId";
    public static final String FIELD_NC_OBJECT_TYPE = "ncObjectType";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_DISPOSITION_GROUP_ID = "dispositionGroupId";
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
    private String ncValidOperId;
    @ApiModelProperty(value = "不良代码或不良代码组", required = true)
    @NotBlank
    @Where
    private String ncObjectId;
    @ApiModelProperty(value = "类型", required = true)
    @NotBlank
    @Where
    private String ncObjectType;
    @ApiModelProperty(value = "工艺ID", required = true)
    @NotBlank
    @Where
    private String operationId;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "处置组", required = true)
    @NotBlank
    @Where
    private String dispositionGroupId;
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
    public String getNcValidOperId() {
        return ncValidOperId;
    }

    public void setNcValidOperId(String ncValidOperId) {
        this.ncValidOperId = ncValidOperId;
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
     * @return 工艺ID
     */
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
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
     * @return 处置组
     */
    public String getDispositionGroupId() {
        return dispositionGroupId;
    }

    public void setDispositionGroupId(String dispositionGroupId) {
        this.dispositionGroupId = dispositionGroupId;
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
