package tarzan.general.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.*;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 对象类型定义
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
@ApiModel("对象类型定义")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_event_object_type")
@CustomPrimary
public class MtEventObjectType extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OBJECT_TYPE_ID = "objectTypeId";
    public static final String FIELD_OBJECT_TYPE_CODE = "objectTypeCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_TABLE_NAME = "tableName";
    public static final String FIELD_WHERE_CLAUSE = "whereClause";
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
    @ApiModelProperty("对象类型ID")
    @Id
    @Where
    private String objectTypeId;
    @ApiModelProperty(value = "对象类型编码", required = true)
    @NotBlank
    @Where
    private String objectTypeCode;
    @ApiModelProperty(value = "对象类型描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "表名")
    @Where
    private String tableName;
    @ApiModelProperty(value = "查询条件")
    @Where
    private String whereClause;
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
     * @return 对象类型ID
     */
    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    /**
     * @return 对象类型编码
     */
    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    /**
     * @return 对象类型描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
     * @return 表名
     */
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return 查询条件
     */
    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
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
