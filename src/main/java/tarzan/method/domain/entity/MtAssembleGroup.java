package tarzan.method.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 装配组，标识一个装载设备或一类装配关系
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@ApiModel("装配组，标识一个装载设备或一类装配关系")

@ModifyAudit

@Table(name = "mt_assemble_group")
@MultiLanguage
@CustomPrimary
public class MtAssembleGroup extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ASSEMBLE_GROUP_ID = "assembleGroupId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_ASSEMBLE_GROUP_CODE = "assembleGroupCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_AUTO_INSTALL_POINT_FLAG = "autoInstallPointFlag";
    public static final String FIELD_ASSEMBLE_CONTROL_FLAG = "assembleControlFlag";
    public static final String FIELD_ASSEMBLE_SEQUENCE_FLAG = "assembleSequenceFlag";
    public static final String FIELD_ASSEMBLE_GROUP_STATUS = "assembleGroupStatus";
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
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    @Where
    private String assembleGroupId;
    @ApiModelProperty(value = "生产站点", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "装配组代码", required = true)
    @NotBlank
    @Where
    private String assembleGroupCode;
    @ApiModelProperty(value = "装配组描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "每次上料自动安装装配点，如果启用则ASSEMBLE_CONTROL_FLAG和ASSEMBLE_SEQUENCE_FLAG无效")
    @Where
    private String autoInstallPointFlag;
    @ApiModelProperty(value = "装配限制标识，如果启用则ASSEMBLE_SEQUENCE_FLAG无效，如果装配控制明细为空，则不校验")
    @Where
    private String assembleControlFlag;
    @ApiModelProperty(value = "如果为是，则严格按照装配点在装配组的顺序装载物料")
    @Where
    private String assembleSequenceFlag;
    @ApiModelProperty(value = "状态，包括新建NEW、下达RELEASED、运行WORKING、保留HOLD、关闭CLOSED", required = true)
    @NotBlank
    @Where
    private String assembleGroupStatus;
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
     * @return 主键ID，表示唯一一条记录
     */
    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    /**
     * @return 生产站点
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 装配组代码
     */
    public String getAssembleGroupCode() {
        return assembleGroupCode;
    }

    public void setAssembleGroupCode(String assembleGroupCode) {
        this.assembleGroupCode = assembleGroupCode;
    }

    /**
     * @return 装配组描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 每次上料自动安装装配点，如果启用则ASSEMBLE_CONTROL_FLAG和ASSEMBLE_SEQUENCE_FLAG无效
     */
    public String getAutoInstallPointFlag() {
        return autoInstallPointFlag;
    }

    public void setAutoInstallPointFlag(String autoInstallPointFlag) {
        this.autoInstallPointFlag = autoInstallPointFlag;
    }

    /**
     * @return 装配限制标识，如果启用则ASSEMBLE_SEQUENCE_FLAG无效，如果装配控制明细为空，则不校验
     */
    public String getAssembleControlFlag() {
        return assembleControlFlag;
    }

    public void setAssembleControlFlag(String assembleControlFlag) {
        this.assembleControlFlag = assembleControlFlag;
    }

    /**
     * @return 如果为是，则严格按照装配点在装配组的顺序装载物料
     */
    public String getAssembleSequenceFlag() {
        return assembleSequenceFlag;
    }

    public void setAssembleSequenceFlag(String assembleSequenceFlag) {
        this.assembleSequenceFlag = assembleSequenceFlag;
    }

    /**
     * @return 状态，包括新建NEW、下达RELEASED、运行WORKING、保留HOLD、关闭CLOSED
     */
    public String getAssembleGroupStatus() {
        return assembleGroupStatus;
    }

    public void setAssembleGroupStatus(String assembleGroupStatus) {
        this.assembleGroupStatus = assembleGroupStatus;
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
