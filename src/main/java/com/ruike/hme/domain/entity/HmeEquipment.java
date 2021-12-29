package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
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
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.mybatis.common.query.Where;

/**
 * 设备表
 *
 * @author xu.deng01@hand-china.com 2020-06-03 18:27:09
 */
@ApiModel("设备表")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_equipment")
@CustomPrimary
public class HmeEquipment extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EQUIPMENT_ID = "equipmentId";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_ASSET_NAME = "assetName";
    public static final String FIELD_ASSET_CLASS = "assetClass";
    public static final String FIELD_DESCRIPTIONS = "descriptions";
    public static final String FIELD_SAP_NUM = "sapNum";
    public static final String FIELD_EQUIPMENT_BODY_NUM = "equipmentBodyNum";
    public static final String FIELD_EQUIPMENT_CONFIG = "equipmentConfig";
    public static final String FIELD_OA_CHECK_NUM = "oaCheckNum";
    public static final String FIELD_EQUIPMENT_CATEGORY = "equipmentCategory";
    public static final String FIELD_EQUIPMENT_TYPE = "equipmentType";
    public static final String FIELD_APPLY_TYPE = "applyType";
    public static final String FIELD_EQUIPMENT_STATUS = "equipmentStatus";
    public static final String FIELD_DEAL_NUM = "dealNum";
    public static final String FIELD_DEAL_REASON = "dealReason";
    public static final String FIELD_BUSINESS_ID = "businessId";
    public static final String FIELD_USER = "user";
    public static final String FIELD_PRESERVER = "preserver";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_MEASURE_FLAG = "measureFlag";
    public static final String FIELD_FREQUENCY = "frequency";
    public static final String FIELD_BELONG_TO = "belongTo";
    public static final String FIELD_POSTING_DATE = "postingDate";
    public static final String FIELD_SUPPLIER = "supplier";
    public static final String FIELD_BRAND = "brand";
    public static final String FIELD_MODEL = "model";
    public static final String FIELD_UNIT = "unit";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_AMOUNT = "amount";
    public static final String FIELD_CURRENCY = "currency";
    public static final String FIELD_CONTRACT_NUM = "contractNum";
    public static final String FIELD_RECRUITEMNT = "recruitemnt";
    public static final String FIELD_RECRUITEMENT_NUM = "recruitementNum";
    public static final String FIELD_WARRANTY_DATE = "warrantyDate";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_REMARK = "remark";
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


	@ApiModelProperty("租户id")
	@NotNull
	@Where
	private Long tenantId;
	@ApiModelProperty(value = "主键ID，标识唯一一条记录", required = true)
	@NotBlank
	@Id
	@Where
    private String equipmentId;
    @ApiModelProperty(value = "资产编码",required = true)
    @NotBlank
    private String assetEncoding;
   @ApiModelProperty(value = "资产名称",required = true)
    private String assetName;
   @ApiModelProperty(value = "资产类别",required = true)
   @LovValue(value = "HME.ASSET_CLASS", meaningField = "assetClassDes")
    private String assetClass;
   @ApiModelProperty(value = "设备描述")    
    private String descriptions;
   @ApiModelProperty(value = "SAP流水号")    
    private String sapNum;
   @ApiModelProperty(value = "机身序列号")    
    private String equipmentBodyNum;
   @ApiModelProperty(value = "配置")    
    private String equipmentConfig;
   @ApiModelProperty(value = "OA验收单号")    
    private String oaCheckNum;
   @ApiModelProperty(value = "设备类别")
   @LovValue(value = "HME.EQUIPMENT_CATEGORY", meaningField = "equipmentCategoryDes")
    private String equipmentCategory;
    @ApiModelProperty(value = "设备类型")
    @LovValue(value = "HME.EQUIPEMNT_TYPE", meaningField = "equipmentTypeDes")
    private String equipmentType;
   @ApiModelProperty(value = "应用类型")
   @LovValue(value = "HME.APPLY_TYPE", meaningField = "applyTypeDes")
    private String applyType;
   @ApiModelProperty(value = "设备状态",required = true)
   @LovValue(value = "HME.EQUIPMENT_STATUS", meaningField = "equipmentStatusDes")
    private String equipmentStatus;
   @ApiModelProperty(value = "处置单号")    
    private String dealNum;
   @ApiModelProperty(value = "处置原因")    
    private String dealReason;
   @ApiModelProperty(value = "保管部门ID")    
    private String businessId;
   @ApiModelProperty(value = "使用人")    
    private String user;
   @ApiModelProperty(value = "保管人")    
    private String preserver;
   @ApiModelProperty(value = "存放地点")    
    private String location;
   @ApiModelProperty(value = "是否计量")
   @LovValue(value = "HME.MEASURE_FLAG", meaningField = "measureFlagDes")
    private String measureFlag;
   @ApiModelProperty(value = "使用频次")
   @LovValue(value = "HME.USE_FREQUENCY", meaningField = "frequencyDes")
    private String frequency;
   @ApiModelProperty(value = "归属权" )
    private String belongTo;
   @ApiModelProperty(value = "入账日期")    
    private Date postingDate;
   @ApiModelProperty(value = "销售商")    
    private String supplier;
   @ApiModelProperty(value = "品牌")    
    private String brand;
   @ApiModelProperty(value = "型号")    
    private String model;
   @ApiModelProperty(value = "单位")    
    private String unit;
   @ApiModelProperty(value = "数量")    
    private Long quantity;
   @ApiModelProperty(value = "金额")    
    private BigDecimal amount;
   @ApiModelProperty(value = "币种")
   @LovValue(value = "HME.CURRENCY", meaningField = "currencySymbol")
    private String currency;
   @ApiModelProperty(value = "合同编号")    
    private String contractNum;
   @ApiModelProperty(value = "募投")    
    private String recruitement;
   @ApiModelProperty(value = "募投编号")    
    private String recruitementNum;
   @ApiModelProperty(value = "质保期")    
    private Date warrantyDate;
    @ApiModelProperty(value = "组织ID")
    private String siteId;
   @ApiModelProperty(value = "备注")    
    private String remark;
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

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------



}
