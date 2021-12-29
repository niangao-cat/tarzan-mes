package io.tarzan.common.domain.entity;

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
 * 编码对象属性
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@ApiModel("编码对象属性")
@ModifyAudit

@Table(name = "mt_numrange_object_column")
@CustomPrimary
@MultiLanguage
public class MtNumrangeObjectColumn extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OBJECT_COLUMN_ID = "objectColumnId";
    public static final String FIELD_OBJECT_ID = "objectId";
    public static final String FIELD_OBJECT_COLUMN_CODE = "objectColumnCode";
    public static final String FIELD_OBJECT_COLUMN_NAME = "objectColumnName";
    public static final String TYPE_GROUP = "typeGroup";
    public static final String MODULE = "module";
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
    @ApiModelProperty("编码对象列ID")
    @Id
    @Where
    private String objectColumnId;
    @ApiModelProperty(value = "编码对象ID", required = true)
    @NotBlank
    @Where
    private String objectId;
    @ApiModelProperty(value = "列参数名", required = true)
    @NotBlank
    @Where
    private String objectColumnCode;
    @ApiModelProperty(value = "列名称")
    @Where
    @MultiLanguageField
    private String objectColumnName;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "类型组")
    @Where
    private String typeGroup;
    @ApiModelProperty(value = "服务包")
    @Where
    private String module;
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
     * @return 编码对象列ID
     */
    public String getObjectColumnId() {
        return objectColumnId;
    }

    public void setObjectColumnId(String objectColumnId) {
        this.objectColumnId = objectColumnId;
    }

    /**
     * @return 编码对象ID
     */
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * @return 列参数名
     */
    public String getObjectColumnCode() {
        return objectColumnCode;
    }

    public void setObjectColumnCode(String objectColumnCode) {
        this.objectColumnCode = objectColumnCode;
    }

    /**
     * @return 列名称
     */
    public String getObjectColumnName() {
        return objectColumnName;
    }

    public void setObjectColumnName(String objectColumnName) {
        this.objectColumnName = objectColumnName;
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

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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
