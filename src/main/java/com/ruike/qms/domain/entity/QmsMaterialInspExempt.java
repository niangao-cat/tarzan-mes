package com.ruike.qms.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料免检表
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:06:18
 */
@ApiModel("物料免检表")
@VersionAudit
@ModifyAudit
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "qms_material_insp_exempt")
@CustomPrimary
public class QmsMaterialInspExempt extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EXEMPTION_ID = "exemptionId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_SITE_ID = "supplierSiteId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_EXEMPTION_FLAG = "exemptionFlag";
    public static final String FIELD_ENABLE_FALG = "enableFlag";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_ATTRIBUTE6 = "attribute6";
    public static final String FIELD_ATTRIBUTE7 = "attribute7";
    public static final String FIELD_ATTRIBUTE8 = "attribute8";
    public static final String FIELD_ATTRIBUTE9 = "attribute9";
    public static final String FIELD_ATTRIBUTE10 = "attribute10";
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    @GeneratedValue
    private String exemptionId;
    @ApiModelProperty(value = "组织ID",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "供应商ID",required = true)
    @NotBlank
    private String supplierId;
    @ApiModelProperty(value = "供应商地址ID",required = true)
    @NotBlank
    private String supplierSiteId;
    @ApiModelProperty(value = "物料ID",required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "免检类型")
    private String type;
    @ApiModelProperty(value = "免检标识",required = true)
    @NotBlank
    private String exemptionFlag;
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    private String enableFlag;
    @ApiModelProperty(value = "CID",required = true)
    @NotNull
	@Cid
    private Long cid;
   @ApiModelProperty(value = "")    
    private String attributeCategory;
   @ApiModelProperty(value = "")    
    private String attribute1;
   @ApiModelProperty(value = "")    
    private String attribute2;
   @ApiModelProperty(value = "")    
    private String attribute3;
   @ApiModelProperty(value = "")    
    private String attribute4;
   @ApiModelProperty(value = "")    
    private String attribute5;
   @ApiModelProperty(value = "")    
    private String attribute6;
   @ApiModelProperty(value = "")    
    private String attribute7;
   @ApiModelProperty(value = "")    
    private String attribute8;
   @ApiModelProperty(value = "")    
    private String attribute9;
   @ApiModelProperty(value = "")    
    private String attribute10;
   @ApiModelProperty(value = "")    
    private String attribute11;
   @ApiModelProperty(value = "")    
    private String attribute12;
   @ApiModelProperty(value = "")    
    private String attribute13;
   @ApiModelProperty(value = "")    
    private String attribute14;
   @ApiModelProperty(value = "")    
    private String attribute15;

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
