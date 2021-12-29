package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;

@ExcelSheet(zh = "MES与SAP现有量对比")
public class MesOnHandVO {

    @ExcelColumn(zh = "物料编码", order = 1)
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ExcelColumn(zh = "仓库编码", order = 2)
    @ApiModelProperty(value = "仓库编码")
    private String locatorCode;
    @ExcelColumn(zh = "MES库存", order = 3)
    @ApiModelProperty(value = "MES库存")
    private BigDecimal mesOnHandQty;
    @ExcelColumn(zh = "261移动类型库存", order = 4)
    @ApiModelProperty(value = "261移动类型库存")
    private String qty261;
    @ExcelColumn(zh = "262移动类型库存", order = 5)
    @ApiModelProperty(value = "262移动类型库存")
    private String qty262;
    @ExcelColumn(zh = "102移动类型库存", order = 6)
    @ApiModelProperty(value = "102移动类型库存")
    private String qty102;
    @ExcelColumn(zh = "101移动类型库存", order = 7)
    @ApiModelProperty(value = "101移动类型库存")
    private String qty101;
    @ExcelColumn(zh = "接口库存汇总", order = 8)
    @ApiModelProperty(value = "接口库存汇总")
    private String interfaceQty;
    @ExcelColumn(zh = "MES所有库存汇总", order = 9)
    @ApiModelProperty(value = "MES所有库存汇总")
    private String mesCountQty;
    @ExcelColumn(zh = "SAP所有库存汇总", order = 10)
    @ApiModelProperty(value = "SAP所有库存汇总")
    private String sapCountQty;
    @ExcelColumn(zh = "MES库存减去SAP库存差异", order = 11)
    @ApiModelProperty(value = "MES库存减去SAP库存差异")
    private String difference;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public BigDecimal getMesOnHandQty() {
        return mesOnHandQty;
    }

    public void setMesOnHandQty(BigDecimal mesOnHandQty) {
        this.mesOnHandQty = mesOnHandQty;
    }

    public String getQty261() {
        return qty261;
    }

    public void setQty261(String qty261) {
        this.qty261 = qty261;
    }

    public String getQty262() {
        return qty262;
    }

    public void setQty262(String qty262) {
        this.qty262 = qty262;
    }

    public String getQty102() {
        return qty102;
    }

    public void setQty102(String qty102) {
        this.qty102 = qty102;
    }

    public String getQty101() {
        return qty101;
    }

    public void setQty101(String qty101) {
        this.qty101 = qty101;
    }

    public String getInterfaceQty() {
        return interfaceQty;
    }

    public void setInterfaceQty(String interfaceQty) {
        this.interfaceQty = interfaceQty;
    }

    public String getMesCountQty() {
        return mesCountQty;
    }

    public void setMesCountQty(String mesCountQty) {
        this.mesCountQty = mesCountQty;
    }

    public String getSapCountQty() {
        return sapCountQty;
    }

    public void setSapCountQty(String sapCountQty) {
        BigDecimal sapCount = new BigDecimal(sapCountQty);
        BigDecimal mesCount = new BigDecimal(getMesCountQty());
        String difference = String.valueOf(mesCount.subtract(sapCount));
        setDifference(difference);
        this.sapCountQty = sapCountQty;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    // 构造有参无参

    public MesOnHandVO() {
    }

    public MesOnHandVO(SapOnHandVO sapOnHandVO) {
        this.materialCode = sapOnHandVO.getMATNR();
        this.locatorCode = sapOnHandVO.getLGORT();
        this.mesOnHandQty = new BigDecimal("0");
        this.qty261 = "0";
        this.qty262 = "0";
        this.qty102 = "0";
        this.qty101 = "0";
        this.interfaceQty = "0";
        this.mesCountQty = "0";
        this.sapCountQty = sapOnHandVO.getLABST();
        BigDecimal sapCountQty = new BigDecimal(sapOnHandVO.getLABST());
        BigDecimal mesCountQty = new BigDecimal("0");
        BigDecimal difference = mesCountQty.subtract(sapCountQty);
        this.difference = String.valueOf(difference);
    }
}
