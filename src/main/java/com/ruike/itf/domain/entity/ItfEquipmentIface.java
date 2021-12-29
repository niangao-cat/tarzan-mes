package com.ruike.itf.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruike.hme.domain.entity.HmeEquipment;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.ruike.itf.infra.constant.ItfConstant.ConstantValue.BLANK;

/**
 * 设备台帐接口表
 *
 * @author yonghui.zhu@hand-china.com 2021-01-08 14:11:29
 */
@ApiModel("设备台帐接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_equipment_iface")
@CustomPrimary
public class ItfEquipmentIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_ASSET_NAME = "assetName";
    public static final String FIELD_ASSET_CLASS = "assetClass";
    public static final String FIELD_DESCRIPTIONS = "descriptions";
    public static final String FIELD_SAP_NUM = "sapNum";
    public static final String FIELD_EQUIPMENT_BODY_NUM = "equipmentBodyNum";
    public static final String FIELD_EQUIPMENT_CONFIG = "equipmentConfig";
    public static final String FIELD_OA_CHECK_NUM = "oaCheckNum";
    public static final String FIELD_EQUIPMENT_TYPE = "equipmentType";
    public static final String FIELD_EQUIPMENT_CATEGORY = "equipmentCategory";
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
    public static final String FIELD_RECRUITEMENT = "recruitement";
    public static final String FIELD_RECRUITEMENT_NUM = "recruitementNum";
    public static final String FIELD_WARRANTY_DATE = "warrantyDate";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_REMARK = "remark";
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
    public static final String FIELD_ATTRIBUTE11 = "attribute11";
    public static final String FIELD_ATTRIBUTE12 = "attribute12";
    public static final String FIELD_ATTRIBUTE13 = "attribute13";
    public static final String FIELD_ATTRIBUTE14 = "attribute14";
    public static final String FIELD_ATTRIBUTE15 = "attribute15";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "台账类型")
    @Transient
    private String accountType;

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("接口表ID，主键")
    @Id
    private String interfaceId;
    @ApiModelProperty(value = "资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "资产名称")
    private String assetName;
    @ApiModelProperty(value = "资产类别")
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
    @ApiModelProperty(value = "设备类型")
    private String equipmentType;
    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;
    @ApiModelProperty(value = "应用类型")
    private String applyType;
    @ApiModelProperty(value = "设备状态")
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
    private String measureFlag;
    @ApiModelProperty(value = "使用频次")
    private String frequency;
    @ApiModelProperty(value = "归属权")
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
    private String currency;
    @ApiModelProperty(value = "合同编号")
    private String contractNum;
    @ApiModelProperty(value = "募投")
    private String recruitement;
    @ApiModelProperty(value = "募投编号")
    private String recruitementNum;
    @ApiModelProperty(value = "质保期")
    private Date warrantyDate;
    @ApiModelProperty(value = "组织id")
    private String siteId;
    @ApiModelProperty(value = "备注")
    private String remark;
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

    public HmeEquipment toEntity(boolean updateFlag) {
        String[] ignoreAttrs = new String[]{ItfEquipmentIface.FIELD_ATTRIBUTE1, ItfEquipmentIface.FIELD_ATTRIBUTE2, ItfEquipmentIface.FIELD_ATTRIBUTE3, ItfEquipmentIface.FIELD_ATTRIBUTE4, ItfEquipmentIface.FIELD_ATTRIBUTE5, ItfEquipmentIface.FIELD_ATTRIBUTE6, ItfEquipmentIface.FIELD_ATTRIBUTE7, ItfEquipmentIface.FIELD_ATTRIBUTE8, ItfEquipmentIface.FIELD_ATTRIBUTE9, ItfEquipmentIface.FIELD_ATTRIBUTE10, ItfEquipmentIface.FIELD_ATTRIBUTE11, ItfEquipmentIface.FIELD_ATTRIBUTE12, ItfEquipmentIface.FIELD_ATTRIBUTE13, ItfEquipmentIface.FIELD_ATTRIBUTE14, ItfEquipmentIface.FIELD_ATTRIBUTE15, ItfEquipmentIface.FIELD_BELONG_TO, ItfEquipmentIface.FIELD_SITE_ID};
        HmeEquipment hmeEquipment = new HmeEquipment();
        BeanUtils.copyProperties(this, hmeEquipment, ignoreAttrs);
        hmeEquipment.setTenantId(tenantId);
        hmeEquipment.setAttribute1(this.getAttribute7());
        hmeEquipment.setAttribute2(this.getAccountType());
        if (updateFlag) {
            hmeEquipment.setAssetName(BLANK.equals(hmeEquipment.getAssetName()) ? null : hmeEquipment.getAssetName());
            hmeEquipment.setEquipmentBodyNum(BLANK.equals(hmeEquipment.getEquipmentBodyNum()) ? null : hmeEquipment.getEquipmentBodyNum());
            hmeEquipment.setEquipmentConfig(BLANK.equals(hmeEquipment.getEquipmentConfig()) ? null : hmeEquipment.getEquipmentConfig());
            hmeEquipment.setEquipmentType(BLANK.equals(hmeEquipment.getEquipmentType()) ? null : hmeEquipment.getEquipmentType());
            hmeEquipment.setEquipmentStatus(BLANK.equals(hmeEquipment.getEquipmentStatus()) ? null : hmeEquipment.getEquipmentStatus());
            hmeEquipment.setEquipmentCategory(BLANK.equals(hmeEquipment.getEquipmentCategory()) ? null : hmeEquipment.getEquipmentCategory());
            hmeEquipment.setDealNum(BLANK.equals(hmeEquipment.getDealNum()) ? null : hmeEquipment.getDealNum());
            hmeEquipment.setDealReason(BLANK.equals(hmeEquipment.getDealReason()) ? null : hmeEquipment.getDealReason());
            hmeEquipment.setApplyType(BLANK.equals(hmeEquipment.getApplyType()) ? null : hmeEquipment.getApplyType());
            hmeEquipment.setBusinessId(BLANK.equals(hmeEquipment.getBusinessId()) ? null : hmeEquipment.getBusinessId());
            hmeEquipment.setPreserver(BLANK.equals(hmeEquipment.getPreserver()) ? null : hmeEquipment.getPreserver());
            hmeEquipment.setLocation(BLANK.equals(hmeEquipment.getLocation()) ? null : hmeEquipment.getLocation());
            hmeEquipment.setFrequency(BLANK.equals(hmeEquipment.getFrequency()) ? null : hmeEquipment.getFrequency());
            hmeEquipment.setModel(BLANK.equals(hmeEquipment.getModel()) ? null : hmeEquipment.getModel());
            hmeEquipment.setAttribute1(BLANK.equals(hmeEquipment.getAttribute1()) ? null : hmeEquipment.getAttribute1());
        }
        return hmeEquipment;
    }

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 接口表ID，主键
     */
    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    /**
     * @return 资产编码
     */
    public String getAssetEncoding() {
        return assetEncoding;
    }

    public void setAssetEncoding(String assetEncoding) {
        this.assetEncoding = assetEncoding;
    }

    /**
     * @return 资产名称
     */
    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    /**
     * @return 资产类别
     */
    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    /**
     * @return 设备描述
     */
    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * @return SAP流水号
     */
    public String getSapNum() {
        return sapNum;
    }

    public void setSapNum(String sapNum) {
        this.sapNum = sapNum;
    }

    /**
     * @return 机身序列号
     */
    public String getEquipmentBodyNum() {
        return equipmentBodyNum;
    }

    public void setEquipmentBodyNum(String equipmentBodyNum) {
        this.equipmentBodyNum = equipmentBodyNum;
    }

    /**
     * @return 配置
     */
    public String getEquipmentConfig() {
        return equipmentConfig;
    }

    public void setEquipmentConfig(String equipmentConfig) {
        this.equipmentConfig = equipmentConfig;
    }

    /**
     * @return OA验收单号
     */
    public String getOaCheckNum() {
        return oaCheckNum;
    }

    public void setOaCheckNum(String oaCheckNum) {
        this.oaCheckNum = oaCheckNum;
    }

    /**
     * @return 设备类型
     */
    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    /**
     * @return 设备类别
     */
    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }

    /**
     * @return 应用类型
     */
    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    /**
     * @return 设备状态
     */
    public String getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(String equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    /**
     * @return 处置单号
     */
    public String getDealNum() {
        return dealNum;
    }

    public void setDealNum(String dealNum) {
        this.dealNum = dealNum;
    }

    /**
     * @return 处置原因
     */
    public String getDealReason() {
        return dealReason;
    }

    public void setDealReason(String dealReason) {
        this.dealReason = dealReason;
    }

    /**
     * @return 保管部门ID
     */
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    /**
     * @return 使用人
     */
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return 保管人
     */
    public String getPreserver() {
        return preserver;
    }

    public void setPreserver(String preserver) {
        this.preserver = preserver;
    }

    /**
     * @return 存放地点
     */
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return 是否计量
     */
    public String getMeasureFlag() {
        return measureFlag;
    }

    public void setMeasureFlag(String measureFlag) {
        this.measureFlag = measureFlag;
    }

    /**
     * @return 使用频次
     */
    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * @return 归属权
     */
    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    /**
     * @return 入账日期
     */
    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    /**
     * @return 销售商
     */
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    /**
     * @return 品牌
     */
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return 型号
     */
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return 单位
     */
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return 数量
     */
    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    /**
     * @return 金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return 币种
     */
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return 合同编号
     */
    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    /**
     * @return 募投
     */
    public String getRecruitement() {
        return recruitement;
    }

    public void setRecruitement(String recruitement) {
        this.recruitement = recruitement;
    }

    /**
     * @return 募投编号
     */
    public String getRecruitementNum() {
        return recruitementNum;
    }

    public void setRecruitementNum(String recruitementNum) {
        this.recruitementNum = recruitementNum;
    }

    /**
     * @return 质保期
     */
    public Date getWarrantyDate() {
        return warrantyDate;
    }

    public void setWarrantyDate(Date warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    /**
     * @return 组织id
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return 处理时间
     */
    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    /**
     * @return 处理消息
     */
    public String getProcessMessage() {
        return processMessage;
    }

    public void setProcessMessage(String processMessage) {
        this.processMessage = processMessage;
    }

    /**
     * @return 处理状态(N / P / E / S : 正常 / 处理中 / 错误 / 成功)
     */
    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    /**
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return
     */
    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    /**
     * @return
     */
    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    /**
     * @return
     */
    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    /**
     * @return
     */
    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    /**
     * @return
     */
    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    /**
     * @return
     */
    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    /**
     * @return
     */
    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    /**
     * @return
     */
    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    /**
     * @return
     */
    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    /**
     * @return
     */
    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    /**
     * @return
     */
    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    public String getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    public String getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    public String getAttribute13() {
        return attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    public String getAttribute14() {
        return attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
