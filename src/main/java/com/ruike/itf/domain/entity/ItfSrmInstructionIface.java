package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 送货单状态接口记录表
 *
 * @author kejin.liu01@hand-china.com 2020-09-09 10:05:04
 */
@ApiModel("送货单状态接口记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_srm_instruction_iface")
@Data
public class ItfSrmInstructionIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_INSTRUCTION_DOC_NUM = "instructionDocNum";
    public static final String FIELD_INSTRUCTION_DOC_TYPE = "instructionDocType";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_INSTRUCTION_DOC_STATUS = "instructionDocStatus";
    public static final String FIELD_ZFLAG = "zflag";
    public static final String FIELD_ZMESSAGE = "zmessage";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private String ifaceId;
    @ApiModelProperty(value = "送货单号", required = true)
    @NotBlank
    private String instructionDocNum;
    @ApiModelProperty(value = "送货单类型", required = true)
    @NotBlank
    private String instructionDocType;
    @ApiModelProperty(value = "供应商", required = true)
    @NotBlank
    private String supplierId;
    @ApiModelProperty(value = "工厂", required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "送货单状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "是否发送成功", required = true)
    @NotBlank
    private String zflag;
    @ApiModelProperty(value = "错误信息", required = true)
    @NotBlank
    private String zmessage;
    @ApiModelProperty(value = "CID")
    private Long cid;
    @ApiModelProperty(value = "行版本号，用来处理锁", required = true)
    @NotNull
    private Long objectVersionNumber;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Date creationDate;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long createdBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long lastUpdatedBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Date lastUpdateDate;
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

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    private String plantCode;

    @Transient
    private String supplierCode;

    @Transient
    private String instructionDocId;


}
