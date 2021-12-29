package tarzan.general.domain.entity;

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
 * 
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:57:18
 */
@ApiModel("")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_class_rel")
@CustomPrimary
public class MtClassRel extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_REL_ID = "relId";
    public static final String FIELD_CLASS_CODE = "classCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_SERVICE = "service";
    public static final String FIELD_BEFORE_FUN = "beforeFun";
    public static final String FIELD_AFTER_FUN = "afterFun";
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
    @ApiModelProperty("主键")
    @Id
    @Where
    private String relId;
    @ApiModelProperty(value = "编码", required = true)
    @NotBlank
    @Where
    private String classCode;
    @ApiModelProperty(value = "描述")
    @Where
    private String description;
    @ApiModelProperty(value = "服务API", required = true)
    @NotBlank
    @Where
    private String service;
    @ApiModelProperty(value = "预操作")
    @Where
    private String beforeFun;
    @ApiModelProperty(value = "完结操作")
    @Where
    private String afterFun;
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
     * @return 主键
     */
    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    /**
     * @return 编码
     */
    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    /**
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 服务API
     */
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return 预操作
     */
    public String getBeforeFun() {
        return beforeFun;
    }

    public void setBeforeFun(String beforeFun) {
        this.beforeFun = beforeFun;
    }

    /**
     * @return 完结操作
     */
    public String getAfterFun() {
        return afterFun;
    }

    public void setAfterFun(String afterFun) {
        this.afterFun = afterFun;
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
