package com.ruike.itf.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 物料条码接口表
 *
 * @author yapeng.yao@hand-china.com 2020-09-02 14:30:57
 */
@ApiModel("物料条码接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_material_lot_iface")
@Data
public class ItfMaterialLotIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_MATERIAL_LOT_CODE = "materialLotCode";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_QUALITY_STATUS = "qualityStatus";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_PRIMARY_UOM_ID = "primaryUomId";
    public static final String FIELD_PRIMARY_UOM_CODE = "primaryUomCode";
    public static final String FIELD_PRIMARY_UOM_QTY = "primaryUomQty";
    public static final String FIELD_SUPPLIER_ID = "supplierId";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_SUPPLIER_SITE_CODE = "supplierSiteCode";
    public static final String FIELD_OUTER_BOX = "outerBox";
    public static final String FIELD_SUPPLIER_LOT = "supplierLot";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_PRODUCT_DATE = "productDate";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
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


    @ApiModelProperty("接口表ID，主键")
    @Id
    @GeneratedValue
    private String interfaceId;
    @ApiModelProperty(value = "条码号")
    private String materialLotCode;
    @Transient
    @ApiModelProperty(value = "站点Id")
    private String siteId;
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "可用标识")
    private String enableFlag;
    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;
    @Transient
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @Transient
    @ApiModelProperty(value = "主单位ID")
    private String primaryUomId;
    @ApiModelProperty(value = "主单位编码")
    private String primaryUomCode;
    @ApiModelProperty(value = "主单位数量")
    private BigDecimal primaryUomQty;
    @Transient
    @ApiModelProperty(value = "供应商Id")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商地点编码")
    private String supplierSiteCode;
    @ApiModelProperty(value = "外箱条码")
    private String outerBox;
    @ApiModelProperty(value = "供应商条码批次")
    private String supplierLot;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "生产日期")
    private String productDate;
    @ApiModelProperty(value = "处理时间", required = true)
    @NotNull
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)", required = true)
    @NotBlank
    private String processStatus;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
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

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

}
