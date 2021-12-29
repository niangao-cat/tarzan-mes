package tarzan.method.domain.entity;

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
 * 处置方法
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
@ApiModel("处置方法")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_disposition_function")
@CustomPrimary
public class MtDispositionFunction extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DISPOSITION_FUNCTION_ID = "dispositionFunctionId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_DISPOSITION_FUNCTION = "dispositionFunction";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ROUTER_ID = "routerId";
    public static final String FIELD_FUNCTION_TYPE = "functionType";
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
    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @Where
    private String dispositionFunctionId;
    @ApiModelProperty(value = "生产站点", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "处置方法", required = true)
    @NotBlank
    @Where
    private String dispositionFunction;
    @ApiModelProperty(value = "描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "工艺路线:与表ROUTER中字段ROUTER_ID对应。当FUNCTION_TYPE为.[NC_ROUTER]时必填")
    @Where
    private String routerId;
    @ApiModelProperty(value = "方法类型，包括以下类型（TYPE_GROUP:FUNCTION_TYPE）", required = true)
    @NotBlank
    @Where
    private String functionType;
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
     * @return 表ID，主键，供其他表做外键
     */
    public String getDispositionFunctionId() {
        return dispositionFunctionId;
    }

    public void setDispositionFunctionId(String dispositionFunctionId) {
        this.dispositionFunctionId = dispositionFunctionId;
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
     * @return 处置方法
     */
    public String getDispositionFunction() {
        return dispositionFunction;
    }

    public void setDispositionFunction(String dispositionFunction) {
        this.dispositionFunction = dispositionFunction;
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
     * @return 工艺路线:与表ROUTER中字段ROUTER_ID对应。当FUNCTION_TYPE为.[NC_ROUTER]时必填
     */
    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return 方法类型，包括以下类型 （TYPE_GROUP:FUNCTION_TYPE）： 1.[FUTURE_HOLD]未来保留; 2.[REWORK]原地重工;
     *         3.[NC_ROUTER]处置工艺路线（可以是NC）; 4.[IMIDIATE_HOLD]立即保留; 方法类型，包括以下类型
     *         （TYPE_GROUP:FUNCTION_TYPE）： 1.[FUTURE_HOLD]未来保留; 2.[REWORK]原地重工;
     *         3.[NC_ROUTER]处置工艺路线（可以是NC）; 4.[IMIDIATE_HOLD]立即保留;
     */
    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
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
