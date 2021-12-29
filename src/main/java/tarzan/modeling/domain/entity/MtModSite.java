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
 * 站点
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@ApiModel("站点")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_mod_site")
@CustomPrimary
public class MtModSite extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_SITE_NAME = "siteName";
    public static final String FIELD_SITE_TYPE = "siteType";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_PROVINCE = "province";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_COUNTY = "county";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 8651580810301296270L;

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
    @ApiModelProperty("站点ID，主键唯一标识")
    @Id
    @Where
    private String siteId;
    @ApiModelProperty(value = "站点编码", required = true)
    @NotBlank
    @Where
    private String siteCode;
    @ApiModelProperty(value = "站点名称", required = true)
    @NotBlank
    @MultiLanguageField
    @Where
    private String siteName;
    @ApiModelProperty(value = "站点类型", required = true)
    @NotBlank
    @Where
    private String siteType;
    @ApiModelProperty(value = "有效性", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "国家")
    @MultiLanguageField
    @Where
    private String country;
    @ApiModelProperty(value = "省")
    @MultiLanguageField
    @Where
    private String province;
    @ApiModelProperty(value = "市")
    @MultiLanguageField
    @Where
    private String city;
    @ApiModelProperty(value = "县")
    @MultiLanguageField
    @Where
    private String county;
    @ApiModelProperty(value = "除国家省市县的详细地址")
    @MultiLanguageField
    @Where
    private String address;
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
     * @return 站点ID，主键唯一标识
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 站点编码
     */
    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    /**
     * @return 站点名称
     */
    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     * @return 站点类型
     */
    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    /**
     * @return 有效性
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 国家
     */
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return 省
     */
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return 市
     */
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return 县
     */
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * @return 除国家省市县的详细地址
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
