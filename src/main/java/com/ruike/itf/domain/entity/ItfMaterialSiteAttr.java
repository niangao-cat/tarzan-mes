package com.ruike.itf.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 物料站点扩展表
 *
 * @author kejin.liu01@hand-china.com 2020-10-20 22:23:48
 */
@ApiModel("物料站点扩展表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_material_site_attr")
public class ItfMaterialSiteAttr extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTR_ID = "attrId";
    public static final String FIELD_MATERIAL_SITE_ID = "materialSiteId";
    public static final String FIELD_ATTR_NAME = "attrName";
    public static final String FIELD_ATTR_VALUE = "attrValue";
    public static final String FIELD_LANG = "lang";
    public static final String FIELD_CID = "cid";

//
// 业务方法(按public protected private顺序排列)
// ------------------------------------------------------------------------------

//
// 数据库字段
// ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("扩展表主键ID，唯一性标识")
    @Id
    @GeneratedValue
    private String attrId;
    @ApiModelProperty(value = "主表ID", required = true)
    @NotBlank
    private String materialSiteId;
    @ApiModelProperty(value = "扩展属性名", required = true)
    @NotBlank
    private String attrName;
    @ApiModelProperty(value = "扩展属性值")
    private String attrValue;
    @ApiModelProperty(value = "语言类型")
    private String lang;
    @ApiModelProperty(value = "", required = true)
    @NotNull
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
     * @return 扩展表主键ID，唯一性标识
     */
    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    /**
     * @return 主表ID
     */
    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    /**
     * @return 扩展属性名
     */
    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    /**
     * @return 扩展属性值
     */
    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    /**
     * @return 语言类型
     */
    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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
