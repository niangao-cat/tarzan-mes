package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.transaction.Transaction;

import java.util.Date;

/**
 * 料废调换接口记录表
 *
 * @author kejin.liu01@hand-china.com 2020-09-21 11:05:25
 */
@ApiModel("料废调换接口记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_srm_material_waste_iface")
@Data
@CustomPrimary
public class ItfSrmMaterialWasteIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_VENDOR_CODE = "vendorCode";
    public static final String FIELD_ITEM_CODE = "itemCode";
    public static final String FIELD_PRIMAY_CHANGE_QTY = "primayChangeQty";
    public static final String FIELD_PRIMARY_UOM_QTY = "primaryUomQty";
    public static final String FIELD_PRIMARY_UOM = "primaryUom";
    public static final String FIELD_SHIP_TO_ORGANIZATION = "shipToOrganization";
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
    @ApiModelProperty(value = "供应商", required = true)
    @NotBlank
    private String vendorCode;
    @ApiModelProperty(value = "物料编码", required = true)
    @NotBlank
    private String itemCode;
    @ApiModelProperty(value = "未发出数量", required = true)
    @NotBlank
    private String primayChangeQty;
    @ApiModelProperty(value = "发出数量", required = true)
    @NotBlank
    private String primaryUomQty;
    @ApiModelProperty(value = "发出单位")
    private String primaryUom;
    @ApiModelProperty(value = "工厂", required = true)
    @NotBlank
    private String shipToOrganization;
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
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
}
