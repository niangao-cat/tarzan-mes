package com.ruike.qms.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.common.query.Where;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 物料检验计划
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:43
 */
@ApiModel("物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据")
@ModifyAudit
@Table(name = "qms_material_insp_scheme")
@CustomPrimary
@Getter
@Setter
public class QmsMaterialInspScheme extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INSPECTION_SCHEME_ID = "inspectionSchemeId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_MATERIAL_CATEGORY_ID = "materialCategoryId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_INSPECTION_TYPE = "inspectionType";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_INSPECTION_FILE = "inspectionFile";
    public static final String FIELD_FILE_VERSION = "fileVersion";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_PUBLISH_FLAG = "publishFlag";
    public static final String FIELD_CID = "cid";
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

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


   @ApiModelProperty(value = "租户id")
   @Where
    private Long tenantId;
    @ApiModelProperty("主键id，标识唯一一条记录")
    @Id
	@Where
    private String inspectionSchemeId;
    @ApiModelProperty(value = "组织",required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "物料类别",required = true)
    @Where
    private String materialCategoryId;
    @ApiModelProperty(value = "物料id",required = true)
    @Where
    private String materialId;
    @ApiModelProperty(value = "物料版本",required = true)
    @NotBlank
    @Where
    private String materialVersion;
    @ApiModelProperty(value = "检验类型",required = true)
    @NotBlank
    @Where
    @LovValue(value = "QMS.INSPECTION_TYPE",meaningField = "inspectionTypeMeaning")
    private String inspectionType;
   @ApiModelProperty(value = "状态")
   @NotBlank
   @Where
   @LovValue(value = "QMS.MATERIAL_INSPECTION_STATUS",meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "检验文件号",required = true)
    @NotBlank
    @Where
    private String inspectionFile;
    @ApiModelProperty(value = "文件版本号",required = true)
    @NotBlank
    @Where
    private String fileVersion;
    @ApiModelProperty(value = "是否有效",required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @Where
    @ApiModelProperty(value = "发布标识",required = true)
    @NotBlank
    private String publishFlag;
    @Where
    @ApiModelProperty(value = "cid",required = true)
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



}
