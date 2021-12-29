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
 * 区域
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@ApiModel("区域")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@MultiLanguage
@Table(name = "mt_mod_area")
@CustomPrimary
public class MtModArea extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_AREA_ID = "areaId";
    public static final String FIELD_AREA_CODE = "areaCode";
    public static final String FIELD_AREA_NAME = "areaName";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_PROVINCE = "province";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_COUNTY = "county";
    public static final String FIELD_ADDRESS = "address";
    public static final String FIELD_AREA_CATEGORY = "areaCategory";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 7695727720342887834L;

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
    @ApiModelProperty("主键ID，标示唯一一条记录")
    @Id
    @Where
    private String areaId;
    @ApiModelProperty(value = "区域编号", required = true)
    @NotBlank
    @Where
    private String areaCode;
    @ApiModelProperty(value = "区域名称", required = true)
    @NotBlank
    @MultiLanguageField
    @Where
    private String areaName;
    @ApiModelProperty(value = "区域描述")
    @MultiLanguageField
    @Where
    private String description;
    @ApiModelProperty(value = "是否有效", required = true)
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
    @ApiModelProperty(value = "区域分类，自定义类型，后续考虑基于类型拓展对应属性表")
    @Where
    private String areaCategory;
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
     * @return 主键ID，标示唯一一条记录
     */
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @return 区域编号
     */
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * @return 区域名称
     */
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /**
     * @return 区域描述
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
     * @return 区域分类，自定义类型，后续考虑基于类型拓展对应属性表
     */
    public String getAreaCategory() {
        return areaCategory;
    }

    public void setAreaCategory(String areaCategory) {
        this.areaCategory = areaCategory;
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
