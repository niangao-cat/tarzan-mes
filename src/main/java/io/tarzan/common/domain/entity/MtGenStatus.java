package io.tarzan.common.domain.entity;

import java.io.Serializable;

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
 * 状态
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@ApiModel("状态")
@ModifyAudit
@MultiLanguage
@Table(name = "mt_gen_status")
@CustomPrimary
public class MtGenStatus extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_GEN_STATUS_ID = "genStatusId";
    public static final String FIELD_MODULE = "module";
    public static final String FIELD_STATUS_GROUP = "statusGroup";
    public static final String FIELD_STATUS_CODE = "statusCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DEFAULT_FLAG = "defaultFlag";
    public static final String FIELD_RELATION_TABLE = "relationTable";
    public static final String FIELD_INITIAL_FLAG = "initialFlag";
    public static final String FIELD_SEQUENCE = "sequence";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 4531842764344804825L;

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
    @ApiModelProperty("通用状态主键")
    @Id
    @Where
    private String genStatusId;
    @ApiModelProperty(value = "服务包", required = true)
    @NotBlank
    @Where
    private String module;
    @ApiModelProperty(value = "状态组编码", required = true)
    @NotBlank
    @Where
    private String statusGroup;
    @ApiModelProperty(value = "状态编码", required = true)
    @NotBlank
    @Where
    private String statusCode;
    @ApiModelProperty(value = "备注")
    @Where
    @MultiLanguageField
    private String description;
    @ApiModelProperty(value = "默认状态，Y/N")
    @Where
    private String defaultFlag;
    @ApiModelProperty(value = "关联对象")
    @Where
    private String relationTable;
    @ApiModelProperty(value = "初始数据标识")
    @Where
    private String initialFlag;
    @ApiModelProperty(value = "顺序")
    @Where
    private Double sequence;
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
     * @return 通用状态主键
     */
    public String getGenStatusId() {
        return genStatusId;
    }

    public void setGenStatusId(String genStatusId) {
        this.genStatusId = genStatusId;
    }

    /**
     * @return 服务包
     */
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    /**
     * @return 状态组编码
     */
    public String getStatusGroup() {
        return statusGroup;
    }

    public void setStatusGroup(String statusGroup) {
        this.statusGroup = statusGroup;
    }

    /**
     * @return 状态编码
     */
    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return 备注
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 默认状态，Y/N
     */
    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    /**
     * @return 关联对象
     */
    public String getRelationTable() {
        return relationTable;
    }

    public void setRelationTable(String relationTable) {
        this.relationTable = relationTable;
    }

    /**
     * @return 初始数据标识
     */
    public String getInitialFlag() {
        return initialFlag;
    }

    public void setInitialFlag(String initialFlag) {
        this.initialFlag = initialFlag;
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

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }
}
