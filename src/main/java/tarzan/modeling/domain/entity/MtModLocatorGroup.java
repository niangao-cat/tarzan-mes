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
 * 库位组
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@ApiModel("库位组")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_mod_locator_group")
@CustomPrimary
public class MtModLocatorGroup extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_LOCATOR_GROUP_ID = "locatorGroupId";
    public static final String FIELD_LOCATOR_GROUP_CODE = "locatorGroupCode";
    public static final String FIELD_LOCATOR_GROUP_NAME = "locatorGroupName";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 1634781957782828230L;

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
    private String locatorGroupId;
    @ApiModelProperty(value = "库位组编码", required = true)
    @NotBlank
    @Where
    private String locatorGroupCode;
    @ApiModelProperty(value = "库位组名称", required = true)
    @NotBlank
    @MultiLanguageField
    @Where
    private String locatorGroupName;
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getLocatorGroupId() {
        return locatorGroupId;
    }

    public void setLocatorGroupId(String locatorGroupId) {
        this.locatorGroupId = locatorGroupId;
    }

    /**
     * @return 库位组编码
     */
    public String getLocatorGroupCode() {
        return locatorGroupCode;
    }

    public void setLocatorGroupCode(String locatorGroupCode) {
        this.locatorGroupCode = locatorGroupCode;
    }

    /**
     * @return 库位组名称
     */
    public String getLocatorGroupName() {
        return locatorGroupName;
    }

    public void setLocatorGroupName(String locatorGroupName) {
        this.locatorGroupName = locatorGroupName;
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
