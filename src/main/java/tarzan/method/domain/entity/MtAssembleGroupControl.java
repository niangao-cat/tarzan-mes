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
 * 装配组控制，标识具体装配控制下对装配组的控制，装配组可安装的工作单元
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@ApiModel("装配组控制，标识具体装配控制下对装配组的控制，装配组可安装的工作单元")

@ModifyAudit

@Table(name = "mt_assemble_group_control")
@CustomPrimary
public class MtAssembleGroupControl extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ASSEMBLE_GROUP_CONTROL_ID = "assembleGroupControlId";
    public static final String FIELD_ASSEMBLE_CONTROL_ID = "assembleControlId";
    public static final String FIELD_ASSEMBLE_GROUP_ID = "assembleGroupId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
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
    private String assembleGroupControlId;
    @ApiModelProperty(value = "装配控制ID", required = true)
    @NotBlank
    @Where
    private String assembleControlId;
    @ApiModelProperty(value = "装配组ID", required = true)
    @NotBlank
    @Where
    private String assembleGroupId;
    @ApiModelProperty(value = "WKC", required = true)
    @NotBlank
    @Where
    private String workcellId;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
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
    public String getAssembleGroupControlId() {
        return assembleGroupControlId;
    }

    public void setAssembleGroupControlId(String assembleGroupControlId) {
        this.assembleGroupControlId = assembleGroupControlId;
    }

    /**
     * @return 装配控制ID
     */
    public String getAssembleControlId() {
        return assembleControlId;
    }

    public void setAssembleControlId(String assembleControlId) {
        this.assembleControlId = assembleControlId;
    }

    /**
     * @return 装配组ID
     */
    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    /**
     * @return WKC
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
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
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
