package com.ruike.wms.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 仓库物料每日进销存表
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 09:53:55
 */
@ApiModel("仓库物料每日进销存表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_inv_onhand_qty_record")
@CustomPrimary
@Data
public class WmsInvOnhandQtyRecord extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ONHAND_QTY_RECORD_ID = "onhandQtyRecordId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_LOT = "lot";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_OUT_QTY = "outQty";
    public static final String FIELD_IN_QTY = "inQty";
    public static final String FIELD_INV_RECORD_QTY = "invRecordQty";
    public static final String FIELD_SHOT_DATE = "shotDate";
    public static final String FIELD_SHOW_DATE = "showDate";
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


    @ApiModelProperty(value = "租户id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键id")
    @Id
    @GeneratedValue
    private String onhandQtyRecordId;
    @ApiModelProperty(value = "站点ID",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "货位ID",required = true)
    @NotBlank
    private String locatorId;
   @ApiModelProperty(value = "批次")    
    private String lot;
    @ApiModelProperty(value = "物料id",required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "出库数量")
    private BigDecimal outQty;
   @ApiModelProperty(value = "入库数量")    
    private BigDecimal inQty;
   @ApiModelProperty(value = "当前库存")    
    private BigDecimal invRecordQty;
   @ApiModelProperty(value = "快照时间")    
    private Date shotDate;
   @ApiModelProperty(value = "记录日期")    
    private Date showDate;
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
