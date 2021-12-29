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
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 六型芯片导入临时表
 *
 * @author chaonan.hu@hand-china.com 2021-01-25 13:56:00
 */
@ApiModel("六型芯片导入临时表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_chip_import_data")
@CustomPrimary
public class HmeChipImportData extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_KID = "kid";
    public static final String FIELD_PRINT_FLAG = "printFlag";
    public static final String FIELD_WORK_NUM = "workNum";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_WORKCELL = "workcell";
    public static final String FIELD_IMPORT_LOT = "importLot";
    public static final String FIELD_TARGET_BARCODE = "targetBarcode";
    public static final String FIELD_SOURCE_BARCODE = "sourceBarcode";
    public static final String FIELD_FOX_NUM = "foxNum";
    public static final String FIELD_WAFER = "wafer";
    public static final String FIELD_CONTAINER_TYPE = "containerType";
    public static final String FIELD_LOTNO = "lotno";
    public static final String FIELD_AVG_WAVELENGHT = "avgWavelenght";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_POSITION = "position";
    public static final String FIELD_BAR_NUM = "barNum";
    public static final String FIELD_QTY = "qty";
    public static final String FIELD_A001 = "a001";
    public static final String FIELD_A002 = "a002";
    public static final String FIELD_A003 = "a003";
    public static final String FIELD_A004 = "a004";
    public static final String FIELD_A005 = "a005";
    public static final String FIELD_A006 = "a006";
    public static final String FIELD_A007 = "a007";
    public static final String FIELD_A008 = "a008";
    public static final String FIELD_A009 = "a009";
    public static final String FIELD_A010 = "a010";
    public static final String FIELD_A011 = "a011";
    public static final String FIELD_A012 = "a012";
    public static final String FIELD_A013 = "a013";
    public static final String FIELD_A014 = "a014";
    public static final String FIELD_A015 = "a015";
    public static final String FIELD_A016 = "a016";
    public static final String FIELD_A017 = "a017";
    public static final String FIELD_A018 = "a018";
    public static final String FIELD_A019 = "a019";
    public static final String FIELD_A020 = "a020";
    public static final String FIELD_A021 = "a021";
    public static final String FIELD_A022 = "a022";
    public static final String FIELD_A023 = "a023";
    public static final String FIELD_A024 = "a024";
    public static final String FIELD_A025 = "a025";
    public static final String FIELD_A026 = "a026";
    public static final String FIELD_A027 = "a027";
    public static final String FIELD_A028 = "a028";
    public static final String FIELD_A029 = "a029";
    public static final String FIELD_A030 = "a030";
    public static final String FIELD_A031 = "a031";
    public static final String FIELD_A032 = "a032";
    public static final String FIELD_A033 = "a033";
    public static final String FIELD_A034 = "a034";
    public static final String FIELD_A035 = "a035";
    public static final String FIELD_A036 = "a036";
    public static final String FIELD_A037 = "a037";
    public static final String FIELD_A038 = "a038";
    public static final String FIELD_A039 = "a039";
    public static final String FIELD_A040 = "a040";
    public static final String FIELD_A041 = "a041";
    public static final String FIELD_A042 = "a042";
    public static final String FIELD_A043 = "a043";
    public static final String FIELD_A044 = "a044";
    public static final String FIELD_A045 = "a045";
    public static final String FIELD_A046 = "a046";
    public static final String FIELD_A047 = "a047";
    public static final String FIELD_A048 = "a048";
    public static final String FIELD_A049 = "a049";
    public static final String FIELD_A050 = "a050";
    public static final String FIELD_A051 = "a051";
    public static final String FIELD_A052 = "a052";
    public static final String FIELD_A053 = "a053";
    public static final String FIELD_A054 = "a054";
    public static final String FIELD_A055 = "a055";
    public static final String FIELD_A056 = "a056";
    public static final String FIELD_A057 = "a057";
    public static final String FIELD_A058 = "a058";
    public static final String FIELD_A059 = "a059";
    public static final String FIELD_A060 = "a060";
    public static final String FIELD_A061 = "a061";
    public static final String FIELD_A062 = "a062";
    public static final String FIELD_A063 = "a063";
    public static final String FIELD_A064 = "a064";
    public static final String FIELD_A065 = "a065";
    public static final String FIELD_A066 = "a066";
    public static final String FIELD_A067 = "a067";
    public static final String FIELD_A068 = "a068";
    public static final String FIELD_A069 = "a069";
    public static final String FIELD_A070 = "a070";
    public static final String FIELD_A071 = "a071";
    public static final String FIELD_A072 = "a072";
    public static final String FIELD_A073 = "a073";
    public static final String FIELD_A074 = "a074";
    public static final String FIELD_A075 = "a075";
    public static final String FIELD_A076 = "a076";
    public static final String FIELD_A077 = "a077";
    public static final String FIELD_A078 = "a078";
    public static final String FIELD_A079 = "a079";
    public static final String FIELD_A080 = "a080";
    public static final String FIELD_A081 = "a081";
    public static final String FIELD_A082 = "a082";
    public static final String FIELD_A083 = "a083";
    public static final String FIELD_A084 = "a084";
    public static final String FIELD_A085 = "a085";
    public static final String FIELD_A086 = "a086";
    public static final String FIELD_A087 = "a087";
    public static final String FIELD_A088 = "a088";
    public static final String FIELD_A089 = "a089";
    public static final String FIELD_A090 = "a090";
    public static final String FIELD_A091 = "a091";
    public static final String FIELD_A092 = "a092";
    public static final String FIELD_A093 = "a093";
    public static final String FIELD_A094 = "a094";
    public static final String FIELD_A095 = "a095";
    public static final String FIELD_A096 = "a096";
    public static final String FIELD_A097 = "a097";
    public static final String FIELD_A098 = "a098";
    public static final String FIELD_A099 = "a099";
    public static final String FIELD_A100 = "a100";
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
    @ApiModelProperty("主键")
    @Id
    private String kid;
    @ApiModelProperty(value = "是否打印",required = true)
    @NotBlank
    private String printFlag;
    @ApiModelProperty(value = "工单",required = true)
    @NotBlank
    private String workNum;
    @ApiModelProperty(value = "COS类型",required = true)
    @NotBlank
    private String cosType;
    @ApiModelProperty(value = "工位",required = true)
    @NotBlank
    private String workcell;
    @ApiModelProperty(value = "导入批次",required = true)
    @NotBlank
    private String importLot;
    @ApiModelProperty(value = "目标条码",required = true)
    @NotBlank
    private String targetBarcode;
    @ApiModelProperty(value = "来料条码",required = true)
    @NotBlank
    private String sourceBarcode;
    @ApiModelProperty(value = "盒号",required = true)
    @NotBlank
    private String foxNum;
    @ApiModelProperty(value = "WAFER",required = true)
    @NotBlank
    private String wafer;
    @ApiModelProperty(value = "容器类型",required = true)
    @NotBlank
    private String containerType;
   @ApiModelProperty(value = "LOTNO")    
    private String lotno;
   @ApiModelProperty(value = "Avg (nm)")    
    private String avgWavelenght;
   @ApiModelProperty(value = "TYPE")    
    private String type;
   @ApiModelProperty(value = "备注")    
    private String remark;
    @ApiModelProperty(value = "位置",required = true)
    @NotBlank
    private String position;
    @ApiModelProperty(value = "Bar条数",required = true)
    @NotNull
    private Long barNum;
    @ApiModelProperty(value = "合格芯片数",required = true)
    @NotNull
    private Long qty;
    @ApiModelProperty(value = "",required = true)
    @NotNull
    @Cid
    private Long cid;
   @ApiModelProperty(value = "")    
    private Long a001;
   @ApiModelProperty(value = "")    
    private Long a002;
   @ApiModelProperty(value = "")    
    private Long a003;
   @ApiModelProperty(value = "")    
    private Long a004;
   @ApiModelProperty(value = "")    
    private Long a005;
   @ApiModelProperty(value = "")    
    private Long a006;
   @ApiModelProperty(value = "")    
    private Long a007;
   @ApiModelProperty(value = "")    
    private Long a008;
   @ApiModelProperty(value = "")    
    private Long a009;
   @ApiModelProperty(value = "")    
    private Long a010;
   @ApiModelProperty(value = "")    
    private Long a011;
   @ApiModelProperty(value = "")    
    private Long a012;
   @ApiModelProperty(value = "")    
    private Long a013;
   @ApiModelProperty(value = "")    
    private Long a014;
   @ApiModelProperty(value = "")    
    private Long a015;
   @ApiModelProperty(value = "")    
    private Long a016;
   @ApiModelProperty(value = "")    
    private Long a017;
   @ApiModelProperty(value = "")    
    private Long a018;
   @ApiModelProperty(value = "")    
    private Long a019;
   @ApiModelProperty(value = "")    
    private Long a020;
   @ApiModelProperty(value = "")    
    private Long a021;
   @ApiModelProperty(value = "")    
    private Long a022;
   @ApiModelProperty(value = "")    
    private Long a023;
   @ApiModelProperty(value = "")    
    private Long a024;
   @ApiModelProperty(value = "")    
    private Long a025;
   @ApiModelProperty(value = "")    
    private Long a026;
   @ApiModelProperty(value = "")    
    private Long a027;
   @ApiModelProperty(value = "")    
    private Long a028;
   @ApiModelProperty(value = "")    
    private Long a029;
   @ApiModelProperty(value = "")    
    private Long a030;
   @ApiModelProperty(value = "")    
    private Long a031;
   @ApiModelProperty(value = "")    
    private Long a032;
   @ApiModelProperty(value = "")    
    private Long a033;
   @ApiModelProperty(value = "")    
    private Long a034;
   @ApiModelProperty(value = "")    
    private Long a035;
   @ApiModelProperty(value = "")    
    private Long a036;
   @ApiModelProperty(value = "")    
    private Long a037;
   @ApiModelProperty(value = "")    
    private Long a038;
   @ApiModelProperty(value = "")    
    private Long a039;
   @ApiModelProperty(value = "")    
    private Long a040;
   @ApiModelProperty(value = "")    
    private Long a041;
   @ApiModelProperty(value = "")    
    private Long a042;
   @ApiModelProperty(value = "")    
    private Long a043;
   @ApiModelProperty(value = "")    
    private Long a044;
   @ApiModelProperty(value = "")    
    private Long a045;
   @ApiModelProperty(value = "")    
    private Long a046;
   @ApiModelProperty(value = "")    
    private Long a047;
   @ApiModelProperty(value = "")    
    private Long a048;
   @ApiModelProperty(value = "")    
    private Long a049;
   @ApiModelProperty(value = "")    
    private Long a050;
   @ApiModelProperty(value = "")    
    private Long a051;
   @ApiModelProperty(value = "")    
    private Long a052;
   @ApiModelProperty(value = "")    
    private Long a053;
   @ApiModelProperty(value = "")    
    private Long a054;
   @ApiModelProperty(value = "")    
    private Long a055;
   @ApiModelProperty(value = "")    
    private Long a056;
   @ApiModelProperty(value = "")    
    private Long a057;
   @ApiModelProperty(value = "")    
    private Long a058;
   @ApiModelProperty(value = "")    
    private Long a059;
   @ApiModelProperty(value = "")    
    private Long a060;
   @ApiModelProperty(value = "")    
    private Long a061;
   @ApiModelProperty(value = "")    
    private Long a062;
   @ApiModelProperty(value = "")    
    private Long a063;
   @ApiModelProperty(value = "")    
    private Long a064;
   @ApiModelProperty(value = "")    
    private Long a065;
   @ApiModelProperty(value = "")    
    private Long a066;
   @ApiModelProperty(value = "")    
    private Long a067;
   @ApiModelProperty(value = "")    
    private Long a068;
   @ApiModelProperty(value = "")    
    private Long a069;
   @ApiModelProperty(value = "")    
    private Long a070;
   @ApiModelProperty(value = "")    
    private Long a071;
   @ApiModelProperty(value = "")    
    private Long a072;
   @ApiModelProperty(value = "")    
    private Long a073;
   @ApiModelProperty(value = "")    
    private Long a074;
   @ApiModelProperty(value = "")    
    private Long a075;
   @ApiModelProperty(value = "")    
    private Long a076;
   @ApiModelProperty(value = "")    
    private Long a077;
   @ApiModelProperty(value = "")    
    private Long a078;
   @ApiModelProperty(value = "")    
    private Long a079;
   @ApiModelProperty(value = "")    
    private Long a080;
   @ApiModelProperty(value = "")    
    private Long a081;
   @ApiModelProperty(value = "")    
    private Long a082;
   @ApiModelProperty(value = "")    
    private Long a083;
   @ApiModelProperty(value = "")    
    private Long a084;
   @ApiModelProperty(value = "")    
    private Long a085;
   @ApiModelProperty(value = "")    
    private Long a086;
   @ApiModelProperty(value = "")    
    private Long a087;
   @ApiModelProperty(value = "")    
    private Long a088;
   @ApiModelProperty(value = "")    
    private Long a089;
   @ApiModelProperty(value = "")    
    private Long a090;
   @ApiModelProperty(value = "")    
    private Long a091;
   @ApiModelProperty(value = "")    
    private Long a092;
   @ApiModelProperty(value = "")    
    private Long a093;
   @ApiModelProperty(value = "")    
    private Long a094;
   @ApiModelProperty(value = "")    
    private Long a095;
   @ApiModelProperty(value = "")    
    private Long a096;
   @ApiModelProperty(value = "")    
    private Long a097;
   @ApiModelProperty(value = "")    
    private Long a098;
   @ApiModelProperty(value = "")    
    private Long a099;
   @ApiModelProperty(value = "")    
    private Long a100;
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
     * @return 租户id
     */
	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
    /**
     * @return 主键
     */
	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}
    /**
     * @return 是否打印
     */
	public String getPrintFlag() {
		return printFlag;
	}

	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}
    /**
     * @return 工单
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
	public String getAvgWavelenght() {
		return avgWavelenght;
	}

	public void setAvgWavelenght(String avgWavelenght) {
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
     * @return Bar条数
     */
	public Long getBarNum() {
		return barNum;
	}

	public void setBarNum(Long barNum) {
		this.barNum = barNum;
	}
    /**
     * @return 合格芯片数
     */
	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}
    /**
     * @return 
     */
	public Long getA001() {
		return a001;
	}

	public void setA001(Long a001) {
		this.a001 = a001;
	}
    /**
     * @return 
     */
	public Long getA002() {
		return a002;
	}

	public void setA002(Long a002) {
		this.a002 = a002;
	}
    /**
     * @return 
     */
	public Long getA003() {
		return a003;
	}

	public void setA003(Long a003) {
		this.a003 = a003;
	}
    /**
     * @return 
     */
	public Long getA004() {
		return a004;
	}

	public void setA004(Long a004) {
		this.a004 = a004;
	}
    /**
     * @return 
     */
	public Long getA005() {
		return a005;
	}

	public void setA005(Long a005) {
		this.a005 = a005;
	}
    /**
     * @return 
     */
	public Long getA006() {
		return a006;
	}

	public void setA006(Long a006) {
		this.a006 = a006;
	}
    /**
     * @return 
     */
	public Long getA007() {
		return a007;
	}

	public void setA007(Long a007) {
		this.a007 = a007;
	}
    /**
     * @return 
     */
	public Long getA008() {
		return a008;
	}

	public void setA008(Long a008) {
		this.a008 = a008;
	}
    /**
     * @return 
     */
	public Long getA009() {
		return a009;
	}

	public void setA009(Long a009) {
		this.a009 = a009;
	}
    /**
     * @return 
     */
	public Long getA010() {
		return a010;
	}

	public void setA010(Long a010) {
		this.a010 = a010;
	}
    /**
     * @return 
     */
	public Long getA011() {
		return a011;
	}

	public void setA011(Long a011) {
		this.a011 = a011;
	}
    /**
     * @return 
     */
	public Long getA012() {
		return a012;
	}

	public void setA012(Long a012) {
		this.a012 = a012;
	}
    /**
     * @return 
     */
	public Long getA013() {
		return a013;
	}

	public void setA013(Long a013) {
		this.a013 = a013;
	}
    /**
     * @return 
     */
	public Long getA014() {
		return a014;
	}

	public void setA014(Long a014) {
		this.a014 = a014;
	}
    /**
     * @return 
     */
	public Long getA015() {
		return a015;
	}

	public void setA015(Long a015) {
		this.a015 = a015;
	}
    /**
     * @return 
     */
	public Long getA016() {
		return a016;
	}

	public void setA016(Long a016) {
		this.a016 = a016;
	}
    /**
     * @return 
     */
	public Long getA017() {
		return a017;
	}

	public void setA017(Long a017) {
		this.a017 = a017;
	}
    /**
     * @return 
     */
	public Long getA018() {
		return a018;
	}

	public void setA018(Long a018) {
		this.a018 = a018;
	}
    /**
     * @return 
     */
	public Long getA019() {
		return a019;
	}

	public void setA019(Long a019) {
		this.a019 = a019;
	}
    /**
     * @return 
     */
	public Long getA020() {
		return a020;
	}

	public void setA020(Long a020) {
		this.a020 = a020;
	}
    /**
     * @return 
     */
	public Long getA021() {
		return a021;
	}

	public void setA021(Long a021) {
		this.a021 = a021;
	}
    /**
     * @return 
     */
	public Long getA022() {
		return a022;
	}

	public void setA022(Long a022) {
		this.a022 = a022;
	}
    /**
     * @return 
     */
	public Long getA023() {
		return a023;
	}

	public void setA023(Long a023) {
		this.a023 = a023;
	}
    /**
     * @return 
     */
	public Long getA024() {
		return a024;
	}

	public void setA024(Long a024) {
		this.a024 = a024;
	}
    /**
     * @return 
     */
	public Long getA025() {
		return a025;
	}

	public void setA025(Long a025) {
		this.a025 = a025;
	}
    /**
     * @return 
     */
	public Long getA026() {
		return a026;
	}

	public void setA026(Long a026) {
		this.a026 = a026;
	}
    /**
     * @return 
     */
	public Long getA027() {
		return a027;
	}

	public void setA027(Long a027) {
		this.a027 = a027;
	}
    /**
     * @return 
     */
	public Long getA028() {
		return a028;
	}

	public void setA028(Long a028) {
		this.a028 = a028;
	}
    /**
     * @return 
     */
	public Long getA029() {
		return a029;
	}

	public void setA029(Long a029) {
		this.a029 = a029;
	}
    /**
     * @return 
     */
	public Long getA030() {
		return a030;
	}

	public void setA030(Long a030) {
		this.a030 = a030;
	}
    /**
     * @return 
     */
	public Long getA031() {
		return a031;
	}

	public void setA031(Long a031) {
		this.a031 = a031;
	}
    /**
     * @return 
     */
	public Long getA032() {
		return a032;
	}

	public void setA032(Long a032) {
		this.a032 = a032;
	}
    /**
     * @return 
     */
	public Long getA033() {
		return a033;
	}

	public void setA033(Long a033) {
		this.a033 = a033;
	}
    /**
     * @return 
     */
	public Long getA034() {
		return a034;
	}

	public void setA034(Long a034) {
		this.a034 = a034;
	}
    /**
     * @return 
     */
	public Long getA035() {
		return a035;
	}

	public void setA035(Long a035) {
		this.a035 = a035;
	}
    /**
     * @return 
     */
	public Long getA036() {
		return a036;
	}

	public void setA036(Long a036) {
		this.a036 = a036;
	}
    /**
     * @return 
     */
	public Long getA037() {
		return a037;
	}

	public void setA037(Long a037) {
		this.a037 = a037;
	}
    /**
     * @return 
     */
	public Long getA038() {
		return a038;
	}

	public void setA038(Long a038) {
		this.a038 = a038;
	}
    /**
     * @return 
     */
	public Long getA039() {
		return a039;
	}

	public void setA039(Long a039) {
		this.a039 = a039;
	}
    /**
     * @return 
     */
	public Long getA040() {
		return a040;
	}

	public void setA040(Long a040) {
		this.a040 = a040;
	}
    /**
     * @return 
     */
	public Long getA041() {
		return a041;
	}

	public void setA041(Long a041) {
		this.a041 = a041;
	}
    /**
     * @return 
     */
	public Long getA042() {
		return a042;
	}

	public void setA042(Long a042) {
		this.a042 = a042;
	}
    /**
     * @return 
     */
	public Long getA043() {
		return a043;
	}

	public void setA043(Long a043) {
		this.a043 = a043;
	}
    /**
     * @return 
     */
	public Long getA044() {
		return a044;
	}

	public void setA044(Long a044) {
		this.a044 = a044;
	}
    /**
     * @return 
     */
	public Long getA045() {
		return a045;
	}

	public void setA045(Long a045) {
		this.a045 = a045;
	}
    /**
     * @return 
     */
	public Long getA046() {
		return a046;
	}

	public void setA046(Long a046) {
		this.a046 = a046;
	}
    /**
     * @return 
     */
	public Long getA047() {
		return a047;
	}

	public void setA047(Long a047) {
		this.a047 = a047;
	}
    /**
     * @return 
     */
	public Long getA048() {
		return a048;
	}

	public void setA048(Long a048) {
		this.a048 = a048;
	}
    /**
     * @return 
     */
	public Long getA049() {
		return a049;
	}

	public void setA049(Long a049) {
		this.a049 = a049;
	}
    /**
     * @return 
     */
	public Long getA050() {
		return a050;
	}

	public void setA050(Long a050) {
		this.a050 = a050;
	}
    /**
     * @return 
     */
	public Long getA051() {
		return a051;
	}

	public void setA051(Long a051) {
		this.a051 = a051;
	}
    /**
     * @return 
     */
	public Long getA052() {
		return a052;
	}

	public void setA052(Long a052) {
		this.a052 = a052;
	}
    /**
     * @return 
     */
	public Long getA053() {
		return a053;
	}

	public void setA053(Long a053) {
		this.a053 = a053;
	}
    /**
     * @return 
     */
	public Long getA054() {
		return a054;
	}

	public void setA054(Long a054) {
		this.a054 = a054;
	}
    /**
     * @return 
     */
	public Long getA055() {
		return a055;
	}

	public void setA055(Long a055) {
		this.a055 = a055;
	}
    /**
     * @return 
     */
	public Long getA056() {
		return a056;
	}

	public void setA056(Long a056) {
		this.a056 = a056;
	}
    /**
     * @return 
     */
	public Long getA057() {
		return a057;
	}

	public void setA057(Long a057) {
		this.a057 = a057;
	}
    /**
     * @return 
     */
	public Long getA058() {
		return a058;
	}

	public void setA058(Long a058) {
		this.a058 = a058;
	}
    /**
     * @return 
     */
	public Long getA059() {
		return a059;
	}

	public void setA059(Long a059) {
		this.a059 = a059;
	}
    /**
     * @return 
     */
	public Long getA060() {
		return a060;
	}

	public void setA060(Long a060) {
		this.a060 = a060;
	}
    /**
     * @return 
     */
	public Long getA061() {
		return a061;
	}

	public void setA061(Long a061) {
		this.a061 = a061;
	}
    /**
     * @return 
     */
	public Long getA062() {
		return a062;
	}

	public void setA062(Long a062) {
		this.a062 = a062;
	}
    /**
     * @return 
     */
	public Long getA063() {
		return a063;
	}

	public void setA063(Long a063) {
		this.a063 = a063;
	}
    /**
     * @return 
     */
	public Long getA064() {
		return a064;
	}

	public void setA064(Long a064) {
		this.a064 = a064;
	}
    /**
     * @return 
     */
	public Long getA065() {
		return a065;
	}

	public void setA065(Long a065) {
		this.a065 = a065;
	}
    /**
     * @return 
     */
	public Long getA066() {
		return a066;
	}

	public void setA066(Long a066) {
		this.a066 = a066;
	}
    /**
     * @return 
     */
	public Long getA067() {
		return a067;
	}

	public void setA067(Long a067) {
		this.a067 = a067;
	}
    /**
     * @return 
     */
	public Long getA068() {
		return a068;
	}

	public void setA068(Long a068) {
		this.a068 = a068;
	}
    /**
     * @return 
     */
	public Long getA069() {
		return a069;
	}

	public void setA069(Long a069) {
		this.a069 = a069;
	}
    /**
     * @return 
     */
	public Long getA070() {
		return a070;
	}

	public void setA070(Long a070) {
		this.a070 = a070;
	}
    /**
     * @return 
     */
	public Long getA071() {
		return a071;
	}

	public void setA071(Long a071) {
		this.a071 = a071;
	}
    /**
     * @return 
     */
	public Long getA072() {
		return a072;
	}

	public void setA072(Long a072) {
		this.a072 = a072;
	}
    /**
     * @return 
     */
	public Long getA073() {
		return a073;
	}

	public void setA073(Long a073) {
		this.a073 = a073;
	}
    /**
     * @return 
     */
	public Long getA074() {
		return a074;
	}

	public void setA074(Long a074) {
		this.a074 = a074;
	}
    /**
     * @return 
     */
	public Long getA075() {
		return a075;
	}

	public void setA075(Long a075) {
		this.a075 = a075;
	}
    /**
     * @return 
     */
	public Long getA076() {
		return a076;
	}

	public void setA076(Long a076) {
		this.a076 = a076;
	}
    /**
     * @return 
     */
	public Long getA077() {
		return a077;
	}

	public void setA077(Long a077) {
		this.a077 = a077;
	}
    /**
     * @return 
     */
	public Long getA078() {
		return a078;
	}

	public void setA078(Long a078) {
		this.a078 = a078;
	}
    /**
     * @return 
     */
	public Long getA079() {
		return a079;
	}

	public void setA079(Long a079) {
		this.a079 = a079;
	}
    /**
     * @return 
     */
	public Long getA080() {
		return a080;
	}

	public void setA080(Long a080) {
		this.a080 = a080;
	}
    /**
     * @return 
     */
	public Long getA081() {
		return a081;
	}

	public void setA081(Long a081) {
		this.a081 = a081;
	}
    /**
     * @return 
     */
	public Long getA082() {
		return a082;
	}

	public void setA082(Long a082) {
		this.a082 = a082;
	}
    /**
     * @return 
     */
	public Long getA083() {
		return a083;
	}

	public void setA083(Long a083) {
		this.a083 = a083;
	}
    /**
     * @return 
     */
	public Long getA084() {
		return a084;
	}

	public void setA084(Long a084) {
		this.a084 = a084;
	}
    /**
     * @return 
     */
	public Long getA085() {
		return a085;
	}

	public void setA085(Long a085) {
		this.a085 = a085;
	}
    /**
     * @return 
     */
	public Long getA086() {
		return a086;
	}

	public void setA086(Long a086) {
		this.a086 = a086;
	}
    /**
     * @return 
     */
	public Long getA087() {
		return a087;
	}

	public void setA087(Long a087) {
		this.a087 = a087;
	}
    /**
     * @return 
     */
	public Long getA088() {
		return a088;
	}

	public void setA088(Long a088) {
		this.a088 = a088;
	}
    /**
     * @return 
     */
	public Long getA089() {
		return a089;
	}

	public void setA089(Long a089) {
		this.a089 = a089;
	}
    /**
     * @return 
     */
	public Long getA090() {
		return a090;
	}

	public void setA090(Long a090) {
		this.a090 = a090;
	}
    /**
     * @return 
     */
	public Long getA091() {
		return a091;
	}

	public void setA091(Long a091) {
		this.a091 = a091;
	}
    /**
     * @return 
     */
	public Long getA092() {
		return a092;
	}

	public void setA092(Long a092) {
		this.a092 = a092;
	}
    /**
     * @return 
     */
	public Long getA093() {
		return a093;
	}

	public void setA093(Long a093) {
		this.a093 = a093;
	}
    /**
     * @return 
     */
	public Long getA094() {
		return a094;
	}

	public void setA094(Long a094) {
		this.a094 = a094;
	}
    /**
     * @return 
     */
	public Long getA095() {
		return a095;
	}

	public void setA095(Long a095) {
		this.a095 = a095;
	}
    /**
     * @return 
     */
	public Long getA096() {
		return a096;
	}

	public void setA096(Long a096) {
		this.a096 = a096;
	}
    /**
     * @return 
     */
	public Long getA097() {
		return a097;
	}

	public void setA097(Long a097) {
		this.a097 = a097;
	}
    /**
     * @return 
     */
	public Long getA098() {
		return a098;
	}

	public void setA098(Long a098) {
		this.a098 = a098;
	}
    /**
     * @return 
     */
	public Long getA099() {
		return a099;
	}

	public void setA099(Long a099) {
		this.a099 = a099;
	}
    /**
     * @return 
     */
	public Long getA100() {
		return a100;
	}

	public void setA100(Long a100) {
		this.a100 = a100;
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

}
