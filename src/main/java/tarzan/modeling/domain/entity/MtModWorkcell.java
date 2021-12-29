package tarzan.modeling.domain.entity;

import java.io.Serializable;

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
 * 工作单元
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@ApiModel("工作单元")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_mod_workcell")
@CustomPrimary
public class MtModWorkcell extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_WORKCELL_CODE = "workcellCode";
    public static final String FIELD_WORKCELL_NAME = "workcellName";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_WORKCELL_TYPE = "workcellType";
    public static final String FIELD_WORKCELL_LOCATION = "workcellLocation";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_WORKCELL_CATEGORY = "workcellCategory";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -1743895013316022675L;

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
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String workcellId;
    @ApiModelProperty(value = "工作单元编号", required = true)
    @NotBlank
    @Where
    private String workcellCode;
    @ApiModelProperty(value = "工作单元名称", required = true)
    @NotBlank
    @MultiLanguageField
    @Where
    private String workcellName;
    @ApiModelProperty(value = "工作单元描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "工作单元类型")
    @Where
    private String workcellType;
    @ApiModelProperty(value = "工作单元位置")
    @MultiLanguageField
    @Where
    private String workcellLocation;
    @ApiModelProperty(value = "是否有效", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "工作单元分类")
    @Where
    private String workcellCategory;
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 工作单元编号
     */
    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    /**
     * @return 工作单元名称
     */
    public String getWorkcellName() {
        return workcellName;
    }

    public void setWorkcellName(String workcellName) {
        this.workcellName = workcellName;
    }

    /**
     * @return 工作单元描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 工作单元类型
     */
    public String getWorkcellType() {
        return workcellType;
    }

    public void setWorkcellType(String workcellType) {
        this.workcellType = workcellType;
    }

    /**
     * @return 工作单元位置
     */
    public String getWorkcellLocation() {
        return workcellLocation;
    }

    public void setWorkcellLocation(String workcellLocation) {
        this.workcellLocation = workcellLocation;
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
     * @return 工作单元分类
     */
    public String getWorkcellCategory() {
        return workcellCategory;
    }

    public void setWorkcellCategory(String workcellCategory) {
        this.workcellCategory = workcellCategory;
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
