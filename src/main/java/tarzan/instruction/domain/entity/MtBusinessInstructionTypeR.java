package tarzan.instruction.domain.entity;

import java.io.Serializable;

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
 * 业务类型与指令移动类型关系表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:36:42
 */
@ApiModel("业务类型与指令移动类型关系表")

@ModifyAudit

@Table(name = "mt_business_instruction_type_r")
@CustomPrimary
public class MtBusinessInstructionTypeR extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_RELATION_ID = "relationId";
    public static final String FIELD_BUSINESS_TYPE = "businessType";
    public static final String FIELD_INSTRUCTION_TYPE = "instructionType";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 5964644229493397883L;

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
    @ApiModelProperty("关系ID")
    @Id
    @Where
    private String relationId;
    @ApiModelProperty(value = "业务类型", required = true)
    @NotBlank
    @Where
    private String businessType;
    @ApiModelProperty(value = "指令移动类型", required = true)
    @NotBlank
    @Where
    private String instructionType;
    @Cid
    @Where
    private Long cid;
    @ApiModelProperty(value = "是否有效", required = true)
    @Where
    private String enableFlag;

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
     * @return 关系ID
     */
    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    /**
     * @return 业务类型
     */
    public String getBusinessType() {
        return businessType;
    }

    public void setBussinessType(String bussinessType) {
        this.businessType = bussinessType;
    }

    /**
     * @return 指令移动类型
     */
    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
