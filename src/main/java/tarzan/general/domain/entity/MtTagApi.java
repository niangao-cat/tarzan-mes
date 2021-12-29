package tarzan.general.domain.entity;

import java.io.Serializable;

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
 * API转化表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@ApiModel("API转化表")

@ModifyAudit

@Table(name = "mt_tag_api")
@CustomPrimary
public class MtTagApi extends AuditDomain implements Serializable {

    private static final long serialVersionUID = 6639774224677021233L;
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_API_ID = "apiId";
    public static final String FIELD_API_CLASS = "apiClass";
    public static final String FIELD_API_NAME = "apiName";
    public static final String FIELD_API_FUNCTION = "apiFunction";
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
    private String apiId;
    @ApiModelProperty(value = "类名", required = true)
    @NotBlank
    @Where
    private String apiClass;
    @ApiModelProperty(value = "API名称", required = true)
    @NotBlank
    @Where
    private String apiName;
    @ApiModelProperty(value = "API函数", required = true)
    @NotBlank
    @Where
    private String apiFunction;
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
    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    /**
     * @return 类名
     */
    public String getApiClass() {
        return apiClass;
    }

    public void setApiClass(String apiClass) {
        this.apiClass = apiClass;
    }

    /**
     * @return API名称
     */
    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    /**
     * @return API函数
     */
    public String getApiFunction() {
        return apiFunction;
    }

    public void setApiFunction(String apiFunction) {
        this.apiFunction = apiFunction;
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
