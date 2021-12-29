package com.ruike.itf.domain.vo;

import com.ruike.itf.domain.repository.ItfObjectTransactionIfaceRepository;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * <p>
 * 售后大仓回调 调用值对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/1 14:23
 */
public class ServiceTransferIfaceInvokeVO {

    public static final String SAP_ACCOUNTING_DATE = "PDATE";
    public static final String SAP_LEDGER_DATE = "DDATE";
    public static final String SAP_SN_NUM = "HTEXT";
    public static final String SAP_GM_CODE = "GMCODE";
    public static final String SAP_MATERIAL_CODE = "MATERIAL";
    public static final String SAP_FROM_SITE_CODE = "PLANT";
    public static final String SAP_FROM_WAREHOUSE_CODE = "STGE_LOC";
    public static final String SAP_MOVE_TYPE = "MOVE_TYPE";
    public static final String SAP_QUANTITY = "ENTRY_QNT";
    public static final String SAP_UOM_CODE = "ENTRY_UOM";
    public static final String SAP_TO_SITE_CODE = "MOVE_PLANT";
    public static final String SAP_TO_WAREHOUSE_CODE = "MOVE_STLOC";
    public static final String SAP_BARCODE = "SERNR1";
    public static final String SAP_AREA_CODE = "PRCTR";
    public static final String SAP_RECEIVE_DATE = "DATUM";
    public static final String SAP_REAL_NAME = "USER";
    public static final String SAP_BACK_TYPE = "ZSTATU";
    public static final String SAP_INTERFACE_ID = "IFACEID";


    private static final String DEFAULT_GM_CODE = "04";
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
    @ApiModelProperty(value = "租户", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "接口ID", required = true)
    @NotBlank
    private String interfaceId;

    public ServiceTransferIfaceInvokeVO(@NotNull Date accountingDate, @NotNull Date ledgerDate, @NotBlank String snNum, @NotBlank String gmCode, @NotBlank String materialCode, @NotBlank String fromSiteCode, @NotBlank String fromWarehouseCode, @NotBlank String moveType, @NotNull BigDecimal quantity, @NotBlank String uomCode, @NotBlank String toSiteCode, @NotBlank String toWarehouseCode, @NotBlank String barcode, @NotBlank String areaCode, @NotNull Date receiveDate, @NotBlank String realName, @NotBlank String backType, String lotCode, @NotNull Long tenantId, String interfaceId) {
        this.accountingDate = accountingDate;
        this.ledgerDate = ledgerDate;
        this.snNum = snNum;
        this.gmCode = gmCode;
        this.materialCode = materialCode;
        this.fromSiteCode = fromSiteCode;
        this.fromWarehouseCode = fromWarehouseCode;
        this.moveType = moveType;
        this.quantity = quantity;
        this.uomCode = uomCode;
        this.toSiteCode = toSiteCode;
        this.toWarehouseCode = toWarehouseCode;
        this.barcode = barcode;
        this.areaCode = areaCode;
        this.receiveDate = receiveDate;
        this.realName = realName;
        this.backType = backType;
        this.lotCode = lotCode;
        this.interfaceId = interfaceId;
    }

    private ServiceTransferIfaceInvokeVO() {

    }

    public static ServiceTransferIfaceInvokeVO newInstance(ItfObjectTransactionIfaceRepository objectTransactionIfaceRepository, @NotNull Date accountingDate, @NotBlank String snNum, @NotBlank String materialCode, @NotBlank String fromSiteCode, @NotBlank String fromWarehouseCode, @NotBlank String moveType, @NotNull BigDecimal quantity, @NotBlank String uomCode, @NotBlank String toSiteCode, @NotBlank String toWarehouseCode, @NotBlank String barcode, @NotBlank String areaCode, @NotNull Date receiveDate, @NotBlank String realName, @NotBlank String backType, String lotCode, @NotNull Long tenantId) {
        Date silentAccountDate = objectTransactionIfaceRepository.querySilentAccountSet(tenantId);
        ServiceTransferIfaceInvokeVO obj = new ServiceTransferIfaceInvokeVO();
        obj.accountingDate = Optional.ofNullable(silentAccountDate).orElse(accountingDate);
        obj.ledgerDate = obj.accountingDate;
        obj.snNum = snNum;
        obj.gmCode = DEFAULT_GM_CODE;
        obj.materialCode = materialCode;
        obj.fromSiteCode = fromSiteCode;
        obj.fromWarehouseCode = fromWarehouseCode;
        obj.moveType = moveType;
        obj.quantity = quantity;
        obj.uomCode = uomCode;
        obj.toSiteCode = toSiteCode;
        obj.toWarehouseCode = toWarehouseCode;
        obj.barcode = barcode;
        obj.areaCode = areaCode;
        obj.receiveDate = receiveDate;
        obj.realName = realName;
        obj.backType = backType;
        obj.lotCode = lotCode;
        return obj;
    }

    public Date getAccountingDate() {
        return accountingDate;
    }

    public Date getLedgerDate() {
        return ledgerDate;
    }

    public String getSnNum() {
        return snNum;
    }

    public String getGmCode() {
        return gmCode;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public String getFromSiteCode() {
        return fromSiteCode;
    }

    public String getFromWarehouseCode() {
        return fromWarehouseCode;
    }

    public String getMoveType() {
        return moveType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUomCode() {
        return uomCode;
    }

    public String getToSiteCode() {
        return toSiteCode;
    }

    public String getToWarehouseCode() {
        return toWarehouseCode;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public String getRealName() {
        return realName;
    }

    public String getBackType() {
        return backType;
    }

    public String getLotCode() {
        return lotCode;
    }

    public Long getTenantId() {
        return tenantId;
    }

    @Override
    public String toString() {
        return "ServiceTransferIfaceInvokeVO{" +
                "accountingDate=" + accountingDate +
                ", ledgerDate=" + ledgerDate +
                ", snNum='" + snNum + '\'' +
                ", gmCode='" + gmCode + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", fromSiteCode='" + fromSiteCode + '\'' +
                ", fromWarehouseCode='" + fromWarehouseCode + '\'' +
                ", moveType='" + moveType + '\'' +
                ", quantity=" + quantity +
                ", uomCode='" + uomCode + '\'' +
                ", toSiteCode='" + toSiteCode + '\'' +
                ", toWarehouseCode='" + toWarehouseCode + '\'' +
                ", barcode='" + barcode + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", receiveDate=" + receiveDate +
                ", realName='" + realName + '\'' +
                ", backType='" + backType + '\'' +
                ", lotCode='" + lotCode + '\'' +
                '}';
    }
}
