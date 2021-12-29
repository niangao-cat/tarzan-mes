package com.ruike.hme.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 芯片性能表
 *
 * @author wenzhang.yu@hand-china.com 2020-08-28 17:01:59
 */
@ApiModel("芯片性能表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_cos_function")
@CustomPrimary
public class HmeCosFunction extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COS_FUNCTION_ID = "cosFunctionId";
    public static final String FIELD_LOAD_SEQUENCE = "loadSequence";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_CURRENT = "current";
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
    public static final String FIELD_A27 = "a27";
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


    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    private String cosFunctionId;
    @ApiModelProperty(value = "芯片序列号", required = true)
    @NotNull
    private String loadSequence;
    @ApiModelProperty(value = "组织id", required = true)
    private String siteId;
    @ApiModelProperty(value = "电流", required = true)
    @NotBlank
    private String current;
    @ApiModelProperty(value = "功率等级")
    private String a01;
    @ApiModelProperty(value = "功率/w")
    private BigDecimal a02;
    @ApiModelProperty(value = "波长等级")
    private String a03;
    @ApiModelProperty(value = "波长/nm")
    private BigDecimal a04;
    @ApiModelProperty(value = "波长差/nm")
    private BigDecimal a05;
    @ApiModelProperty(value = "电压/V")
    private BigDecimal a06;
    @ApiModelProperty(value = "光谱宽度(单点)")
    private BigDecimal a07;
    @ApiModelProperty(value = "")
    private String a08;
    @ApiModelProperty(value = "")
    private String a09;
    @ApiModelProperty(value = "")
    private BigDecimal a010;
    @ApiModelProperty(value = "")
    private BigDecimal a011;
    @ApiModelProperty(value = "")
    private BigDecimal a012;
    @ApiModelProperty(value = "")
    private BigDecimal a013;
    @ApiModelProperty(value = "")
    private BigDecimal a014;
    @ApiModelProperty(value = "")
    private BigDecimal a15;
    @ApiModelProperty(value = "")
    private BigDecimal a16;
    @ApiModelProperty(value = "")
    private BigDecimal a17;
    @ApiModelProperty(value = "")
    private BigDecimal a18;
    @ApiModelProperty(value = "")
    private BigDecimal a19;
    @ApiModelProperty(value = "")
    private BigDecimal a20;
    @ApiModelProperty(value = "")
    private BigDecimal a21;
    @ApiModelProperty(value = "")
    private BigDecimal a22;
    @ApiModelProperty(value = "")
    private BigDecimal a23;
    @ApiModelProperty(value = "")
    private String a24;
    @ApiModelProperty(value = "")
    private String a25;
    @ApiModelProperty(value = "")
    private String a26;
    @ApiModelProperty(value = "")
    private String a27;
    @ApiModelProperty(value = "CID", required = true)
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
     * @return 主键ID，标识唯一一条记录
     */
    public String getCosFunctionId() {
        return cosFunctionId;
    }

    public void setCosFunctionId(String cosFunctionId) {
        this.cosFunctionId = cosFunctionId;
    }

    /**
     * @return 芯片序列号
     */
    public String getLoadSequence() {
        return loadSequence;
    }

    public void setLoadSequence(String loadSequence) {
        this.loadSequence = loadSequence;
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
     * @return 电流
     */
    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
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
     * @return 功率/w
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
     * @return 波长/nm
     */
    public BigDecimal getA04() {
        return a04;
    }

    public void setA04(BigDecimal a04) {
        this.a04 = a04;
    }

    /**
     * @return 波长差/nm
     */
    public BigDecimal getA05() {
        return a05;
    }

    public void setA05(BigDecimal a05) {
        this.a05 = a05;
    }

    /**
     * @return 电压/V
     */
    public BigDecimal getA06() {
        return a06;
    }

    public void setA06(BigDecimal a06) {
        this.a06 = a06;
    }

    /**
     * @return 光谱宽度(单点)
     */
    public BigDecimal getA07() {
        return a07;
    }

    public void setA07(BigDecimal a07) {
        this.a07 = a07;
    }

    /**
     * @return
     */
    public String getA08() {
        return a08;
    }

    public void setA08(String a08) {
        this.a08 = a08;
    }

    /**
     * @return
     */
    public String getA09() {
        return a09;
    }

    public void setA09(String a09) {
        this.a09 = a09;
    }

    /**
     * @return
     */
    public BigDecimal getA010() {
        return a010;
    }

    public void setA010(BigDecimal a010) {
        this.a010 = a010;
    }

    /**
     * @return
     */
    public BigDecimal getA011() {
        return a011;
    }

    public void setA011(BigDecimal a011) {
        this.a011 = a011;
    }

    /**
     * @return
     */
    public BigDecimal getA012() {
        return a012;
    }

    public void setA012(BigDecimal a012) {
        this.a012 = a012;
    }

    /**
     * @return
     */
    public BigDecimal getA013() {
        return a013;
    }

    public void setA013(BigDecimal a013) {
        this.a013 = a013;
    }

    /**
     * @return
     */
    public BigDecimal getA014() {
        return a014;
    }

    public void setA014(BigDecimal a014) {
        this.a014 = a014;
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

    public BigDecimal getA15() {
        return a15;
    }

    public void setA15(BigDecimal a15) {
        this.a15 = a15;
    }

    public BigDecimal getA16() {
        return a16;
    }

    public void setA16(BigDecimal a16) {
        this.a16 = a16;
    }

    public BigDecimal getA17() {
        return a17;
    }

    public void setA17(BigDecimal a17) {
        this.a17 = a17;
    }

    public BigDecimal getA18() {
        return a18;
    }

    public void setA18(BigDecimal a18) {
        this.a18 = a18;
    }

    public BigDecimal getA20() {
        return a20;
    }

    public void setA20(BigDecimal a20) {
        this.a20 = a20;
    }

    public BigDecimal getA19() {
        return a19;
    }

    public void setA19(BigDecimal a19) {
        this.a19 = a19;
    }

    public BigDecimal getA22() {
        return a22;
    }

    public void setA22(BigDecimal a22) {
        this.a22 = a22;
    }

    public BigDecimal getA21() {
        return a21;
    }

    public void setA21(BigDecimal a21) {
        this.a21 = a21;
    }

    public BigDecimal getA23() {
        return a23;
    }

    public void setA23(BigDecimal a23) {
        this.a23 = a23;
    }

    public String getA24() {
        return a24;
    }

    public void setA24(String a24) {
        this.a24 = a24;
    }

    public String getA25() {
        return a25;
    }

    public void setA25(String a25) {
        this.a25 = a25;
    }

    public String getA26() {
        return a26;
    }

    public void setA26(String a26) {
        this.a26 = a26;
    }

    public String getA27() {
        return a27;
    }

    public void setA27(String a27) {
        this.a27 = a27;
    }
}
