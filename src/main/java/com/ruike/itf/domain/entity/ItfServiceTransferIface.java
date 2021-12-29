package com.ruike.itf.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 售后大仓回调
 *
 * @author yonghui.zhu@hand-china.com 2021-04-01 14:05:32
 */
@ApiModel("售后大仓回调")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_service_transfer_iface")
@CustomPrimary
public class ItfServiceTransferIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_ACCOUNTING_DATE = "accountingDate";
    public static final String FIELD_LEDGER_DATE = "ledgerDate";
    public static final String FIELD_SN_NUM = "snNum";
    public static final String FIELD_GM_CODE = "gmCode";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_FROM_SITE_CODE = "fromSiteCode";
    public static final String FIELD_FROM_WAREHOUSE_CODE = "fromWarehouseCode";
    public static final String FIELD_MOVE_TYPE = "moveType";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_UOM_CODE = "uomCode";
    public static final String FIELD_TO_SITE_CODE = "toSiteCode";
    public static final String FIELD_TO_WAREHOUSE_CODE = "toWarehouseCode";
    public static final String FIELD_BARCODE = "barcode";
    public static final String FIELD_AREA_CODE = "areaCode";
    public static final String FIELD_RECEIVE_DATE = "receiveDate";
    public static final String FIELD_REAL_NAME = "realName";
    public static final String FIELD_BACK_TYPE = "backType";
    public static final String FIELD_LOT_CODE = "lotCode";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_CID = "cid";
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

    public static final String SEQUENCE = "itf_service_transfer_iface_s";
    public static final String CID_SEQUENCE = "itf_service_transfer_iface_cid_s";


    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("接口表ID，主键")
    @Id
    private String interfaceId;
    @ApiModelProperty(value = "过账日期", required = true)
    @NotNull
    private Date accountingDate;
    @ApiModelProperty(value = "凭证日期", required = true)
    @NotNull
    private Date ledgerDate;
    @ApiModelProperty(value = "抬头文本（sn）", required = true)
    @NotBlank
    private String snNum;
    @ApiModelProperty(value = "默认传‘04’", required = true)
    @NotBlank
    private String gmCode;
    @ApiModelProperty(value = "物料号", required = true)
    @NotBlank
    private String materialCode;
    @ApiModelProperty(value = "发出工厂编码", required = true)
    @NotBlank
    private String fromSiteCode;
    @ApiModelProperty(value = "库存地点", required = true)
    @NotBlank
    private String fromWarehouseCode;
    @ApiModelProperty(value = "移动类型", required = true)
    @NotBlank
    private String moveType;
    @ApiModelProperty(value = "数量", required = true)
    @NotNull
    private BigDecimal quantity;
    @ApiModelProperty(value = "单位", required = true)
    @NotBlank
    private String uomCode;
    @ApiModelProperty(value = "接收工厂编码", required = true)
    @NotBlank
    private String toSiteCode;
    @ApiModelProperty(value = "收货/发货库存地点", required = true)
    @NotBlank
    private String toWarehouseCode;
    @ApiModelProperty(value = "机器编码", required = true)
    @NotBlank
    private String barcode;
    @ApiModelProperty(value = "部门", required = true)
    @NotBlank
    private String areaCode;
    @ApiModelProperty(value = "登记日期", required = true)
    @NotNull
    private Date receiveDate;
    @ApiModelProperty(value = "操作人", required = true)
    @NotBlank
    private String realName;
    @ApiModelProperty(value = "实物返回属性", required = true)
    @NotBlank
    private String backType;
    @ApiModelProperty(value = "批次号")
    private String lotCode;
    @ApiModelProperty(value = "凭证号")
    private String document;
    @ApiModelProperty(value = "处理日期", required = true)
    @NotNull
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)", required = true)
    @NotBlank
    private String processStatus;
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
    @Cid
    private Long cid;
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
     * @return 过账日期
     */
    public Date getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(Date accountingDate) {
        this.accountingDate = accountingDate;
    }

    /**
     * @return 凭证日期
     */
    public Date getLedgerDate() {
        return ledgerDate;
    }

    public void setLedgerDate(Date ledgerDate) {
        this.ledgerDate = ledgerDate;
    }

    /**
     * @return 抬头文本（sn）
     */
    public String getSnNum() {
        return snNum;
    }

    public void setSnNum(String snNum) {
        this.snNum = snNum;
    }

    /**
     * @return 默认传‘04’
     */
    public String getGmCode() {
        return gmCode;
    }

    public void setGmCode(String gmCode) {
        this.gmCode = gmCode;
    }

    /**
     * @return 物料号
     */
    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    /**
     * @return 发出工厂编码
     */
    public String getFromSiteCode() {
        return fromSiteCode;
    }

    public void setFromSiteCode(String fromSiteCode) {
        this.fromSiteCode = fromSiteCode;
    }

    /**
     * @return 库存地点
     */
    public String getFromWarehouseCode() {
        return fromWarehouseCode;
    }

    public void setFromWarehouseCode(String fromWarehouseCode) {
        this.fromWarehouseCode = fromWarehouseCode;
    }

    /**
     * @return 移动类型
     */
    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    /**
     * @return 数量
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     * @return 单位
     */
    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    /**
     * @return 接收工厂编码
     */
    public String getToSiteCode() {
        return toSiteCode;
    }

    public void setToSiteCode(String toSiteCode) {
        this.toSiteCode = toSiteCode;
    }

    /**
     * @return 收货/发货库存地点
     */
    public String getToWarehouseCode() {
        return toWarehouseCode;
    }

    public void setToWarehouseCode(String toWarehouseCode) {
        this.toWarehouseCode = toWarehouseCode;
    }

    /**
     * @return 机器编码
     */
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return 部门
     */
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * @return 登记日期
     */
    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    /**
     * @return 操作人
     */
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * @return 实物返回属性
     */
    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    /**
     * @return 批次号
     */
    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    /**
     * @return 处理日期
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
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
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

    /**
     * @return
     */
    public String getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    /**
     * @return
     */
    public String getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    /**
     * @return
     */
    public String getAttribute13() {
        return attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    /**
     * @return
     */
    public String getAttribute14() {
        return attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    /**
     * @return
     */
    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
