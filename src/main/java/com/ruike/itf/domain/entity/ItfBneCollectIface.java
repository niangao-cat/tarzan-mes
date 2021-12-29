package com.ruike.itf.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * BNE数据采集接口表
 *
 * @author wenzhang.yu@hand-china.com 22-9-12 13:59:43
 */
@ApiModel("BNE数据采集接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_bne_collect_iface")
@CustomPrimary
public class ItfBneCollectIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_A1 = "a1";
    public static final String FIELD_A2 = "a2";
    public static final String FIELD_A3 = "a3";
    public static final String FIELD_A4 = "a4";
    public static final String FIELD_A5 = "a5";
    public static final String FIELD_A6 = "a6";
    public static final String FIELD_A7 = "a7";
    public static final String FIELD_A8 = "a8";
    public static final String FIELD_B1 = "b1";
    public static final String FIELD_B2 = "b2";
    public static final String FIELD_B3 = "b3";
    public static final String FIELD_B4 = "b4";
    public static final String FIELD_B5 = "b5";
    public static final String FIELD_B6 = "b6";
    public static final String FIELD_B7 = "b7";
    public static final String FIELD_B8 = "b8";
    public static final String FIELD_C1 = "c1";
    public static final String FIELD_C2 = "c2";
    public static final String FIELD_C3 = "c3";
    public static final String FIELD_C4 = "c4";
    public static final String FIELD_C5 = "c5";
    public static final String FIELD_C6 = "c6";
    public static final String FIELD_C7 = "c7";
    public static final String FIELD_C8 = "c8";
    public static final String FIELD_D1 = "d1";
    public static final String FIELD_D2 = "d2";
    public static final String FIELD_D3 = "d3";
    public static final String FIELD_D4 = "d4";
    public static final String FIELD_D5 = "d5";
    public static final String FIELD_D6 = "d6";
    public static final String FIELD_D7 = "d7";
    public static final String FIELD_D8 = "d8";
    public static final String FIELD_E1 = "e1";
    public static final String FIELD_E2 = "e2";
    public static final String FIELD_E3 = "e3";
    public static final String FIELD_E4 = "e4";
    public static final String FIELD_E5 = "e5";
    public static final String FIELD_E6 = "e6";
    public static final String FIELD_E7 = "e7";
    public static final String FIELD_E8 = "e8";
    public static final String FIELD_F1 = "f1";
    public static final String FIELD_F2 = "f2";
    public static final String FIELD_F3 = "f3";
    public static final String FIELD_F4 = "f4";
    public static final String FIELD_F5 = "f5";
    public static final String FIELD_F6 = "f6";
    public static final String FIELD_F7 = "f7";
    public static final String FIELD_F8 = "f8";
    public static final String FIELD_G1 = "g1";
    public static final String FIELD_G2 = "g2";
    public static final String FIELD_G3 = "g3";
    public static final String FIELD_G4 = "g4";
    public static final String FIELD_G5 = "g5";
    public static final String FIELD_G6 = "g6";
    public static final String FIELD_G7 = "g7";
    public static final String FIELD_G8 = "g8";
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

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    private String interfaceId;
   @ApiModelProperty(value = "设备编码")    
    private String assetEncoding;
   @ApiModelProperty(value = "SN")    
    private String sn;
   @ApiModelProperty(value = "")    
    private String a1;
   @ApiModelProperty(value = "")    
    private String a2;
   @ApiModelProperty(value = "")    
    private String a3;
   @ApiModelProperty(value = "")    
    private String a4;
   @ApiModelProperty(value = "")    
    private String a5;
   @ApiModelProperty(value = "")    
    private String a6;
   @ApiModelProperty(value = "")    
    private String a7;
   @ApiModelProperty(value = "")    
    private String a8;
   @ApiModelProperty(value = "")    
    private String b1;
   @ApiModelProperty(value = "")    
    private String b2;
   @ApiModelProperty(value = "")    
    private String b3;
   @ApiModelProperty(value = "")    
    private String b4;
   @ApiModelProperty(value = "")    
    private String b5;
   @ApiModelProperty(value = "")    
    private String b6;
   @ApiModelProperty(value = "")    
    private String b7;
   @ApiModelProperty(value = "")    
    private String b8;
   @ApiModelProperty(value = "")    
    private String c1;
   @ApiModelProperty(value = "")    
    private String c2;
   @ApiModelProperty(value = "")    
    private String c3;
   @ApiModelProperty(value = "")    
    private String c4;
   @ApiModelProperty(value = "")    
    private String c5;
   @ApiModelProperty(value = "")    
    private String c6;
   @ApiModelProperty(value = "")    
    private String c7;
   @ApiModelProperty(value = "")    
    private String c8;
   @ApiModelProperty(value = "")    
    private String d1;
   @ApiModelProperty(value = "")    
    private String d2;
   @ApiModelProperty(value = "")    
    private String d3;
   @ApiModelProperty(value = "")    
    private String d4;
   @ApiModelProperty(value = "")    
    private String d5;
   @ApiModelProperty(value = "")    
    private String d6;
   @ApiModelProperty(value = "")    
    private String d7;
   @ApiModelProperty(value = "")    
    private String d8;
   @ApiModelProperty(value = "")    
    private String e1;
   @ApiModelProperty(value = "")    
    private String e2;
   @ApiModelProperty(value = "")    
    private String e3;
   @ApiModelProperty(value = "")    
    private String e4;
   @ApiModelProperty(value = "")    
    private String e5;
   @ApiModelProperty(value = "")    
    private String e6;
   @ApiModelProperty(value = "")    
    private String e7;
   @ApiModelProperty(value = "")    
    private String e8;
   @ApiModelProperty(value = "")    
    private String f1;
   @ApiModelProperty(value = "")    
    private String f2;
   @ApiModelProperty(value = "")    
    private String f3;
   @ApiModelProperty(value = "")    
    private String f4;
   @ApiModelProperty(value = "")    
    private String f5;
   @ApiModelProperty(value = "")    
    private String f6;
   @ApiModelProperty(value = "")    
    private String f7;
   @ApiModelProperty(value = "")    
    private String f8;
   @ApiModelProperty(value = "")    
    private String g1;
   @ApiModelProperty(value = "")    
    private String g2;
   @ApiModelProperty(value = "")    
    private String g3;
   @ApiModelProperty(value = "")    
    private String g4;
   @ApiModelProperty(value = "")    
    private String g5;
   @ApiModelProperty(value = "")    
    private String g6;
   @ApiModelProperty(value = "")    
    private String g7;
   @ApiModelProperty(value = "")    
    private String g8;
    @ApiModelProperty(value = "处理时间",required = true)
    @NotNull
    private Date processDate;
   @ApiModelProperty(value = "处理消息")    
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)",required = true)
    @NotBlank
    private String processStatus;
   @ApiModelProperty(value = "租户id")    
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
    @Transient
    @ApiModelProperty(value = "设备类")
    private String equipmentCategory;
    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键
     */
	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
    /**
     * @return 设备编码
     */
	public String getAssetEncoding() {
		return assetEncoding;
	}

	public void setAssetEncoding(String assetEncoding) {
		this.assetEncoding = assetEncoding;
	}
    /**
     * @return SN
     */
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
    /**
     * @return 
     */
	public String getA1() {
		return a1;
	}

	public void setA1(String a1) {
		this.a1 = a1;
	}
    /**
     * @return 
     */
	public String getA2() {
		return a2;
	}

	public void setA2(String a2) {
		this.a2 = a2;
	}
    /**
     * @return 
     */
	public String getA3() {
		return a3;
	}

	public void setA3(String a3) {
		this.a3 = a3;
	}
    /**
     * @return 
     */
	public String getA4() {
		return a4;
	}

	public void setA4(String a4) {
		this.a4 = a4;
	}
    /**
     * @return 
     */
	public String getA5() {
		return a5;
	}

	public void setA5(String a5) {
		this.a5 = a5;
	}
    /**
     * @return 
     */
	public String getA6() {
		return a6;
	}

	public void setA6(String a6) {
		this.a6 = a6;
	}
    /**
     * @return 
     */
	public String getA7() {
		return a7;
	}

	public void setA7(String a7) {
		this.a7 = a7;
	}
    /**
     * @return 
     */
	public String getA8() {
		return a8;
	}

	public void setA8(String a8) {
		this.a8 = a8;
	}
    /**
     * @return 
     */
	public String getB1() {
		return b1;
	}

	public void setB1(String b1) {
		this.b1 = b1;
	}
    /**
     * @return 
     */
	public String getB2() {
		return b2;
	}

	public void setB2(String b2) {
		this.b2 = b2;
	}
    /**
     * @return 
     */
	public String getB3() {
		return b3;
	}

	public void setB3(String b3) {
		this.b3 = b3;
	}
    /**
     * @return 
     */
	public String getB4() {
		return b4;
	}

	public void setB4(String b4) {
		this.b4 = b4;
	}
    /**
     * @return 
     */
	public String getB5() {
		return b5;
	}

	public void setB5(String b5) {
		this.b5 = b5;
	}
    /**
     * @return 
     */
	public String getB6() {
		return b6;
	}

	public void setB6(String b6) {
		this.b6 = b6;
	}
    /**
     * @return 
     */
	public String getB7() {
		return b7;
	}

	public void setB7(String b7) {
		this.b7 = b7;
	}
    /**
     * @return 
     */
	public String getB8() {
		return b8;
	}

	public void setB8(String b8) {
		this.b8 = b8;
	}
    /**
     * @return 
     */
	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}
    /**
     * @return 
     */
	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}
    /**
     * @return 
     */
	public String getC3() {
		return c3;
	}

	public void setC3(String c3) {
		this.c3 = c3;
	}
    /**
     * @return 
     */
	public String getC4() {
		return c4;
	}

	public void setC4(String c4) {
		this.c4 = c4;
	}
    /**
     * @return 
     */
	public String getC5() {
		return c5;
	}

	public void setC5(String c5) {
		this.c5 = c5;
	}
    /**
     * @return 
     */
	public String getC6() {
		return c6;
	}

	public void setC6(String c6) {
		this.c6 = c6;
	}
    /**
     * @return 
     */
	public String getC7() {
		return c7;
	}

	public void setC7(String c7) {
		this.c7 = c7;
	}
    /**
     * @return 
     */
	public String getC8() {
		return c8;
	}

	public void setC8(String c8) {
		this.c8 = c8;
	}
    /**
     * @return 
     */
	public String getD1() {
		return d1;
	}

	public void setD1(String d1) {
		this.d1 = d1;
	}
    /**
     * @return 
     */
	public String getD2() {
		return d2;
	}

	public void setD2(String d2) {
		this.d2 = d2;
	}
    /**
     * @return 
     */
	public String getD3() {
		return d3;
	}

	public void setD3(String d3) {
		this.d3 = d3;
	}
    /**
     * @return 
     */
	public String getD4() {
		return d4;
	}

	public void setD4(String d4) {
		this.d4 = d4;
	}
    /**
     * @return 
     */
	public String getD5() {
		return d5;
	}

	public void setD5(String d5) {
		this.d5 = d5;
	}
    /**
     * @return 
     */
	public String getD6() {
		return d6;
	}

	public void setD6(String d6) {
		this.d6 = d6;
	}
    /**
     * @return 
     */
	public String getD7() {
		return d7;
	}

	public void setD7(String d7) {
		this.d7 = d7;
	}
    /**
     * @return 
     */
	public String getD8() {
		return d8;
	}

	public void setD8(String d8) {
		this.d8 = d8;
	}
    /**
     * @return 
     */
	public String getE1() {
		return e1;
	}

	public void setE1(String e1) {
		this.e1 = e1;
	}
    /**
     * @return 
     */
	public String getE2() {
		return e2;
	}

	public void setE2(String e2) {
		this.e2 = e2;
	}
    /**
     * @return 
     */
	public String getE3() {
		return e3;
	}

	public void setE3(String e3) {
		this.e3 = e3;
	}
    /**
     * @return 
     */
	public String getE4() {
		return e4;
	}

	public void setE4(String e4) {
		this.e4 = e4;
	}
    /**
     * @return 
     */
	public String getE5() {
		return e5;
	}

	public void setE5(String e5) {
		this.e5 = e5;
	}
    /**
     * @return 
     */
	public String getE6() {
		return e6;
	}

	public void setE6(String e6) {
		this.e6 = e6;
	}
    /**
     * @return 
     */
	public String getE7() {
		return e7;
	}

	public void setE7(String e7) {
		this.e7 = e7;
	}
    /**
     * @return 
     */
	public String getE8() {
		return e8;
	}

	public void setE8(String e8) {
		this.e8 = e8;
	}
    /**
     * @return 
     */
	public String getF1() {
		return f1;
	}

	public void setF1(String f1) {
		this.f1 = f1;
	}
    /**
     * @return 
     */
	public String getF2() {
		return f2;
	}

	public void setF2(String f2) {
		this.f2 = f2;
	}
    /**
     * @return 
     */
	public String getF3() {
		return f3;
	}

	public void setF3(String f3) {
		this.f3 = f3;
	}
    /**
     * @return 
     */
	public String getF4() {
		return f4;
	}

	public void setF4(String f4) {
		this.f4 = f4;
	}
    /**
     * @return 
     */
	public String getF5() {
		return f5;
	}

	public void setF5(String f5) {
		this.f5 = f5;
	}
    /**
     * @return 
     */
	public String getF6() {
		return f6;
	}

	public void setF6(String f6) {
		this.f6 = f6;
	}
    /**
     * @return 
     */
	public String getF7() {
		return f7;
	}

	public void setF7(String f7) {
		this.f7 = f7;
	}
    /**
     * @return 
     */
	public String getF8() {
		return f8;
	}

	public void setF8(String f8) {
		this.f8 = f8;
	}
    /**
     * @return 
     */
	public String getG1() {
		return g1;
	}

	public void setG1(String g1) {
		this.g1 = g1;
	}
    /**
     * @return 
     */
	public String getG2() {
		return g2;
	}

	public void setG2(String g2) {
		this.g2 = g2;
	}
    /**
     * @return 
     */
	public String getG3() {
		return g3;
	}

	public void setG3(String g3) {
		this.g3 = g3;
	}
    /**
     * @return 
     */
	public String getG4() {
		return g4;
	}

	public void setG4(String g4) {
		this.g4 = g4;
	}
    /**
     * @return 
     */
	public String getG5() {
		return g5;
	}

	public void setG5(String g5) {
		this.g5 = g5;
	}
    /**
     * @return 
     */
	public String getG6() {
		return g6;
	}

	public void setG6(String g6) {
		this.g6 = g6;
	}
    /**
     * @return 
     */
	public String getG7() {
		return g7;
	}

	public void setG7(String g7) {
		this.g7 = g7;
	}
    /**
     * @return 
     */
	public String getG8() {
		return g8;
	}

	public void setG8(String g8) {
		this.g8 = g8;
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
     * @return 处理状态(N/P/E/S:正常/处理中/错误/成功)
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
	public String getAttribute10() {
		return attribute10;
	}

	public void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
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
	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
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

    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }
}
