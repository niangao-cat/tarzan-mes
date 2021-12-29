package com.ruike.hme.domain.entity;

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

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * COS复测导入数据临时表
 *
 * @author sanfeng.zhang@hand-china.com 2021-01-22 17:55:27
 */
@ApiModel("COS复测导入数据临时表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_retest_import_data")
public class HmeRetestImportData extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_RETEST_IMPORT_DATA_ID = "retestImportDataId";
    public static final String FIELD_WORK_NUM = "workNum";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_WORKCELL = "workcell";
    public static final String FIELD_IMPORT_LOT = "importLot";
    public static final String FIELD_IMPORT_TYPE = "importType";
    public static final String FIELD_TARGET_BARCODE = "targetBarcode";
    public static final String FIELD_SOURCE_BARCODE = "sourceBarcode";
    public static final String FIELD_FOX_NUM = "foxNum";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_RC_NUM = "rcNum";
    public static final String FIELD_WAFER = "wafer";
    public static final String FIELD_CONTAINER_TYPE = "containerType";
    public static final String FIELD_LOTNO = "lotno";
    public static final String FIELD_AVG_WAVELENGHT = "avgWavelenght";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_POSITION = "position";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_CURRENT = "current";
    public static final String FIELD_PRINT_FLAG = "printFlag";
    public static final String FIELD_A01 = "a01";
    public static final String FIELD_A02 = "a02";
    public static final String FIELD_A03 = "a03";
    public static final String FIELD_A04 = "a04";
    public static final String FIELD_A05 = "a05";
    public static final String FIELD_A06 = "a06";
    public static final String FIELD_A07 = "a07";
    public static final String FIELD_A08 = "a08";
    public static final String FIELD_A09 = "a09";
    public static final String FIELD_A010 = "a010";
    public static final String FIELD_A011 = "a011";
    public static final String FIELD_A012 = "a012";
    public static final String FIELD_A013 = "a013";
    public static final String FIELD_A014 = "a014";
    public static final String FIELD_A15 = "a15";
    public static final String FIELD_A16 = "a16";
    public static final String FIELD_A17 = "a17";
    public static final String FIELD_A18 = "a18";
    public static final String FIELD_A19 = "a19";
    public static final String FIELD_A20 = "a20";
    public static final String FIELD_A21 = "a21";
    public static final String FIELD_A22 = "a22";
    public static final String FIELD_A23 = "a23";
    public static final String FIELD_A24 = "a24";
    public static final String FIELD_A25 = "a25";
    public static final String FIELD_A26 = "a26";
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


    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("表ID，主键")
    @Id
    private String retestImportDataId;
    @ApiModelProperty(value = "工单号")
    private String workNum;
    @ApiModelProperty(value = "COS类型")
    private String cosType;
    @ApiModelProperty(value = "工位")
    private String workcell;
    @ApiModelProperty(value = "导入批次")
    private String importLot;
    @ApiModelProperty(value = "导入类型")
    private String importType;
    @ApiModelProperty(value = "目标条码")
    private String targetBarcode;
    @ApiModelProperty(value = "来料条码")
    private String sourceBarcode;
    @ApiModelProperty(value = "盒号")
    private String foxNum;
    @ApiModelProperty(value = "供应商")
    private String supplierCode;
    @ApiModelProperty(value = "热沉号")
    private String rcNum;
    @ApiModelProperty(value = "WAFER")
    private String wafer;
    @ApiModelProperty(value = "容器类型")
    private String containerType;
    @ApiModelProperty(value = "LOTNO")
    private String lotno;
    @ApiModelProperty(value = "Avg (nm)")
    private BigDecimal avgWavelenght;
    @ApiModelProperty(value = "TYPE")
    private String type;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "位置")
    private String position;
    @ApiModelProperty(value = "合格芯片数")
    private BigDecimal qty;
    @ApiModelProperty(value = "电流")
    private String current;
    @ApiModelProperty(value = "是否打印 Y-是 N-否", required = true)
    @NotBlank
    private String printFlag;
    @ApiModelProperty(value = "功率等级")
    private String a01;
    @ApiModelProperty(value = "功率")
    private BigDecimal a02;
    @ApiModelProperty(value = "波长等级")
    private String a03;
    @ApiModelProperty(value = "波长")
    private BigDecimal a04;
    @ApiModelProperty(value = "波长差")
    private BigDecimal a05;
    @ApiModelProperty(value = "电压")
    private BigDecimal a06;
    @ApiModelProperty(value = "光谱宽度（单点）")
    private BigDecimal a07;
    @ApiModelProperty(value = "设备")
    private String a08;
    @ApiModelProperty(value = "测试模式")
    private String a09;
    @ApiModelProperty(value = "阈值电流")
    private BigDecimal a010;
    @ApiModelProperty(value = "阈值电压")
    private BigDecimal a011;
    @ApiModelProperty(value = "SE")
    private BigDecimal a012;
    @ApiModelProperty(value = "线宽")
    private BigDecimal a013;
    @ApiModelProperty(value = "光电能转换效率")
    private BigDecimal a014;
    @ApiModelProperty(value = "偏振度")
    private BigDecimal a15;
    @ApiModelProperty(value = "X半宽高")
    private BigDecimal a16;
    @ApiModelProperty(value = "Y半宽高")
    private BigDecimal a17;
    @ApiModelProperty(value = "X86能量宽")
    private BigDecimal a18;
    @ApiModelProperty(value = "Y86能量宽")
    private BigDecimal a19;
    @ApiModelProperty(value = "X95能量宽")
    private BigDecimal a20;
    @ApiModelProperty(value = "Y95能量宽")
    private BigDecimal a21;
    @ApiModelProperty(value = "透镜功率")
    private BigDecimal a22;
    @ApiModelProperty(value = "PBS功率")
    private BigDecimal a23;
    @ApiModelProperty(value = "不良代码")
    private String a24;
    @ApiModelProperty(value = "操作者")
    private String a25;
    @ApiModelProperty(value = "备注")
    private String a26;
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

    //
    // getter/setter
    // ------------------------------------------------------------------------------

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
     * @return 表ID，主键
     */
    public String getRetestImportDataId() {
        return retestImportDataId;
    }

    public void setRetestImportDataId(String retestImportDataId) {
        this.retestImportDataId = retestImportDataId;
    }

    /**
     * @return 工单号
     */
    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }

    /**
     * @return COS类型
     */
    public String getCosType() {
        return cosType;
    }

    public void setCosType(String cosType) {
        this.cosType = cosType;
    }

    /**
     * @return 工位
     */
    public String getWorkcell() {
        return workcell;
    }

    public void setWorkcell(String workcell) {
        this.workcell = workcell;
    }

    /**
     * @return 导入批次
     */
    public String getImportLot() {
        return importLot;
    }

    public void setImportLot(String importLot) {
        this.importLot = importLot;
    }

    /**
     * @return 导入类型
     */
    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    /**
     * @return 目标条码
     */
    public String getTargetBarcode() {
        return targetBarcode;
    }

    public void setTargetBarcode(String targetBarcode) {
        this.targetBarcode = targetBarcode;
    }

    /**
     * @return 来料条码
     */
    public String getSourceBarcode() {
        return sourceBarcode;
    }

    public void setSourceBarcode(String sourceBarcode) {
        this.sourceBarcode = sourceBarcode;
    }

    /**
     * @return 盒号
     */
    public String getFoxNum() {
        return foxNum;
    }

    public void setFoxNum(String foxNum) {
        this.foxNum = foxNum;
    }

    /**
     * @return 供应商
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * @return 热沉号
     */
    public String getRcNum() {
        return rcNum;
    }

    public void setRcNum(String rcNum) {
        this.rcNum = rcNum;
    }

    /**
     * @return WAFER
     */
    public String getWafer() {
        return wafer;
    }

    public void setWafer(String wafer) {
        this.wafer = wafer;
    }

    /**
     * @return 容器类型
     */
    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    /**
     * @return LOTNO
     */
    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    /**
     * @return Avg (nm)
     */
    public BigDecimal getAvgWavelenght() {
        return avgWavelenght;
    }

    public void setAvgWavelenght(BigDecimal avgWavelenght) {
        this.avgWavelenght = avgWavelenght;
    }

    /**
     * @return TYPE
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
     * @return 位置
     */
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return 合格芯片数
     */
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * @return 电流
     */
    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    /**
     * @return 是否打印 Y-是 N-否
     */
    public String getPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(String printFlag) {
        this.printFlag = printFlag;
    }

    /**
     * @return 功率等级
     */
    public String getA01() {
        return a01;
    }

    public void setA01(String a01) {
        this.a01 = a01;
    }

    /**
     * @return 功率
     */
    public BigDecimal getA02() {
        return a02;
    }

    public void setA02(BigDecimal a02) {
        this.a02 = a02;
    }

    /**
     * @return 波长等级
     */
    public String getA03() {
        return a03;
    }

    public void setA03(String a03) {
        this.a03 = a03;
    }

    /**
     * @return 波长
     */
    public BigDecimal getA04() {
        return a04;
    }

    public void setA04(BigDecimal a04) {
        this.a04 = a04;
    }

    /**
     * @return 波长差
     */
    public BigDecimal getA05() {
        return a05;
    }

    public void setA05(BigDecimal a05) {
        this.a05 = a05;
    }

    /**
     * @return 电压
     */
    public BigDecimal getA06() {
        return a06;
    }

    public void setA06(BigDecimal a06) {
        this.a06 = a06;
    }

    /**
     * @return 光谱宽度（单点）
     */
    public BigDecimal getA07() {
        return a07;
    }

    public void setA07(BigDecimal a07) {
        this.a07 = a07;
    }

    /**
     * @return 设备
     */
    public String getA08() {
        return a08;
    }

    public void setA08(String a08) {
        this.a08 = a08;
    }

    /**
     * @return 测试模式
     */
    public String getA09() {
        return a09;
    }

    public void setA09(String a09) {
        this.a09 = a09;
    }

    /**
     * @return 阈值电流
     */
    public BigDecimal getA010() {
        return a010;
    }

    public void setA010(BigDecimal a010) {
        this.a010 = a010;
    }

    /**
     * @return 阈值电压
     */
    public BigDecimal getA011() {
        return a011;
    }

    public void setA011(BigDecimal a011) {
        this.a011 = a011;
    }

    /**
     * @return SE
     */
    public BigDecimal getA012() {
        return a012;
    }

    public void setA012(BigDecimal a012) {
        this.a012 = a012;
    }

    /**
     * @return 线宽
     */
    public BigDecimal getA013() {
        return a013;
    }

    public void setA013(BigDecimal a013) {
        this.a013 = a013;
    }

    /**
     * @return 光电能转换效率
     */
    public BigDecimal getA014() {
        return a014;
    }

    public void setA014(BigDecimal a014) {
        this.a014 = a014;
    }

    /**
     * @return 偏振度
     */
    public BigDecimal getA15() {
        return a15;
    }

    public void setA15(BigDecimal a15) {
        this.a15 = a15;
    }

    /**
     * @return X半宽高
     */
    public BigDecimal getA16() {
        return a16;
    }

    public void setA16(BigDecimal a16) {
        this.a16 = a16;
    }

    /**
     * @return Y半宽高
     */
    public BigDecimal getA17() {
        return a17;
    }

    public void setA17(BigDecimal a17) {
        this.a17 = a17;
    }

    /**
     * @return X86能量宽
     */
    public BigDecimal getA18() {
        return a18;
    }

    public void setA18(BigDecimal a18) {
        this.a18 = a18;
    }

    /**
     * @return Y86能量宽
     */
    public BigDecimal getA19() {
        return a19;
    }

    public void setA19(BigDecimal a19) {
        this.a19 = a19;
    }

    /**
     * @return X95能量宽
     */
    public BigDecimal getA20() {
        return a20;
    }

    public void setA20(BigDecimal a20) {
        this.a20 = a20;
    }

    /**
     * @return Y95能量宽
     */
    public BigDecimal getA21() {
        return a21;
    }

    public void setA21(BigDecimal a21) {
        this.a21 = a21;
    }

    /**
     * @return 透镜功率
     */
    public BigDecimal getA22() {
        return a22;
    }

    public void setA22(BigDecimal a22) {
        this.a22 = a22;
    }

    /**
     * @return PBS功率
     */
    public BigDecimal getA23() {
        return a23;
    }

    public void setA23(BigDecimal a23) {
        this.a23 = a23;
    }

    /**
     * @return 不良代码
     */
    public String getA24() {
        return a24;
    }

    public void setA24(String a24) {
        this.a24 = a24;
    }

    /**
     * @return 操作者
     */
    public String getA25() {
        return a25;
    }

    public void setA25(String a25) {
        this.a25 = a25;
    }

    /**
     * @return 备注
     */
    public String getA26() {
        return a26;
    }

    public void setA26(String a26) {
        this.a26 = a26;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
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

}
