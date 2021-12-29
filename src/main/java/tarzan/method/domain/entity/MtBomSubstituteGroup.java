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
 * 装配清单行替代组
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@ApiModel("装配清单行替代组")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_bom_substitute_group")
@CustomPrimary
public class MtBomSubstituteGroup extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_BOM_SUBSTITUTE_GROUP_ID = "bomSubstituteGroupId";
    public static final String FIELD_BOM_COMPONENT_ID = "bomComponentId";
    public static final String FIELD_SUBSTITUTE_GROUP = "substituteGroup";
    public static final String FIELD_SUBSTITUTE_POLICY = "substitutePolicy";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_COPIED_FROM_GROUP_ID = "copiedFromGroupId";
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
    @ApiModelProperty("装配清单行替代组ID，唯一性标识")
    @Id
    @Where
    private String bomSubstituteGroupId;
    @ApiModelProperty(value = "关联的装配清单行ID", required = true)
    @NotBlank
    @Where
    private String bomComponentId;
    @ApiModelProperty(value = "替代组编码", required = true)
    @NotBlank
    @Where
    private String substituteGroup;
    @ApiModelProperty(value = "替代策略编码，1表示百分比，2表示优先级", required = true)
    @NotBlank
    @Where
    private String substitutePolicy;
    @ApiModelProperty(value = "替代物料有效标识", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "复制的来源替代组属性ID")
    @Where
    private String copiedFromGroupId;
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
     * @return 装配清单行替代组ID，唯一性标识
     */
    public String getBomSubstituteGroupId() {
        return bomSubstituteGroupId;
    }

    public void setBomSubstituteGroupId(String bomSubstituteGroupId) {
        this.bomSubstituteGroupId = bomSubstituteGroupId;
    }

    /**
     * @return 关联的装配清单行ID
     */
    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    /**
     * @return 替代组编码
     */
    public String getSubstituteGroup() {
        return substituteGroup;
    }

    public void setSubstituteGroup(String substituteGroup) {
        this.substituteGroup = substituteGroup;
    }

    /**
     * @return 替代策略编码，1表示百分比，2表示优先级
     */
    public String getSubstitutePolicy() {
        return substitutePolicy;
    }

    public void setSubstitutePolicy(String substitutePolicy) {
        this.substitutePolicy = substitutePolicy;
    }

    /**
     * @return 替代物料有效标识
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 复制的来源替代组属性ID
     */
    public String getCopiedFromGroupId() {
        return copiedFromGroupId;
    }

    public void setCopiedFromGroupId(String copiedFromGroupId) {
        this.copiedFromGroupId = copiedFromGroupId;
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
