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
 * 质量文件解析接口头
 *
 * @author yonghui.zhu@hand-china.com 2021-04-06 10:04:45
 */
@ApiModel("质量文件解析接口头")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_quality_analyze_line_iface")
@CustomPrimary
public class ItfQualityAnalyzeLineIface extends AuditDomain {

    public static final String FIELD_LINE_INTERFACE_ID = "lineInterfaceId";
    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_MATERIAL_LOT_CODE = "materialLotCode";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_TEST1 = "test1";
    public static final String FIELD_TEST2 = "test2";
    public static final String FIELD_TEST3 = "test3";
    public static final String FIELD_TEST4 = "test4";
    public static final String FIELD_TEST5 = "test5";
    public static final String FIELD_TEST6 = "test6";
    public static final String FIELD_TEST7 = "test7";
    public static final String FIELD_TEST8 = "test8";
    public static final String FIELD_TEST9 = "test9";
    public static final String FIELD_TEST10 = "test10";
    public static final String FIELD_TEST11 = "test11";
    public static final String FIELD_TEST12 = "test12";
    public static final String FIELD_TEST13 = "test13";
    public static final String FIELD_TEST14 = "test14";
    public static final String FIELD_TEST15 = "test15";
    public static final String FIELD_TEST16 = "test16";
    public static final String FIELD_TEST17 = "test17";
    public static final String FIELD_TEST18 = "test18";
    public static final String FIELD_TEST19 = "test19";
    public static final String FIELD_TEST20 = "test20";
    public static final String FIELD_TEST21 = "test21";
    public static final String FIELD_TEST22 = "test22";
    public static final String FIELD_TEST23 = "test23";
    public static final String FIELD_TEST24 = "test24";
    public static final String FIELD_TEST25 = "test25";
    public static final String FIELD_TEST26 = "test26";
    public static final String FIELD_TEST27 = "test27";
    public static final String FIELD_TEST28 = "test28";
    public static final String FIELD_TEST29 = "test29";
    public static final String FIELD_TEST30 = "test30";
    public static final String FIELD_TEST31 = "test31";
    public static final String FIELD_TEST32 = "test32";
    public static final String FIELD_TEST33 = "test33";
    public static final String FIELD_TEST34 = "test34";
    public static final String FIELD_TEST35 = "test35";
    public static final String FIELD_TEST36 = "test36";
    public static final String FIELD_TEST37 = "test37";
    public static final String FIELD_TEST38 = "test38";
    public static final String FIELD_TEST39 = "test39";
    public static final String FIELD_TEST40 = "test40";
    public static final String FIELD_TEST41 = "test41";
    public static final String FIELD_TEST42 = "test42";
    public static final String FIELD_TEST43 = "test43";
    public static final String FIELD_TEST44 = "test44";
    public static final String FIELD_TEST45 = "test45";
    public static final String FIELD_TEST46 = "test46";
    public static final String FIELD_TEST47 = "test47";
    public static final String FIELD_TEST48 = "test48";
    public static final String FIELD_TEST49 = "test49";
    public static final String FIELD_TEST50 = "test50";
    public static final String FIELD_TEST51 = "test51";
    public static final String FIELD_TEST52 = "test52";
    public static final String FIELD_TEST53 = "test53";
    public static final String FIELD_TEST54 = "test54";
    public static final String FIELD_TEST55 = "test55";
    public static final String FIELD_TEST56 = "test56";
    public static final String FIELD_TEST57 = "test57";
    public static final String FIELD_TEST58 = "test58";
    public static final String FIELD_TEST59 = "test59";
    public static final String FIELD_TEST60 = "test60";
    public static final String FIELD_TEST61 = "test61";
    public static final String FIELD_TEST62 = "test62";
    public static final String FIELD_TEST63 = "test63";
    public static final String FIELD_TEST64 = "test64";
    public static final String FIELD_TEST65 = "test65";
    public static final String FIELD_TEST66 = "test66";
    public static final String FIELD_TEST67 = "test67";
    public static final String FIELD_TEST68 = "test68";
    public static final String FIELD_TEST69 = "test69";
    public static final String FIELD_TEST70 = "test70";
    public static final String FIELD_TEST71 = "test71";
    public static final String FIELD_TEST72 = "test72";
    public static final String FIELD_TEST73 = "test73";
    public static final String FIELD_TEST74 = "test74";
    public static final String FIELD_TEST75 = "test75";
    public static final String FIELD_TEST76 = "test76";
    public static final String FIELD_TEST77 = "test77";
    public static final String FIELD_TEST78 = "test78";
    public static final String FIELD_TEST79 = "test79";
    public static final String FIELD_TEST80 = "test80";
    public static final String FIELD_TEST81 = "test81";
    public static final String FIELD_TEST82 = "test82";
    public static final String FIELD_TEST83 = "test83";
    public static final String FIELD_TEST84 = "test84";
    public static final String FIELD_TEST85 = "test85";
    public static final String FIELD_TEST86 = "test86";
    public static final String FIELD_TEST87 = "test87";
    public static final String FIELD_TEST88 = "test88";
    public static final String FIELD_TEST89 = "test89";
    public static final String FIELD_TEST90 = "test90";
    public static final String FIELD_TEST91 = "test91";
    public static final String FIELD_TEST92 = "test92";
    public static final String FIELD_TEST93 = "test93";
    public static final String FIELD_TEST94 = "test94";
    public static final String FIELD_TEST95 = "test95";
    public static final String FIELD_TEST96 = "test96";
    public static final String FIELD_TEST97 = "test97";
    public static final String FIELD_TEST98 = "test98";
    public static final String FIELD_TEST99 = "test99";
    public static final String FIELD_TEST100 = "test100";
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
    public static final String FIELD_ATTRIBUTE16 = "attribute16";
    public static final String FIELD_ATTRIBUTE17 = "attribute17";
    public static final String FIELD_ATTRIBUTE18 = "attribute18";
    public static final String FIELD_ATTRIBUTE19 = "attribute19";
    public static final String FIELD_ATTRIBUTE20 = "attribute20";
    public static final String FIELD_ATTRIBUTE21 = "attribute21";
    public static final String FIELD_ATTRIBUTE22 = "attribute22";
    public static final String FIELD_ATTRIBUTE23 = "attribute23";
    public static final String FIELD_ATTRIBUTE24 = "attribute24";
    public static final String FIELD_ATTRIBUTE25 = "attribute25";
    public static final String FIELD_ATTRIBUTE26 = "attribute26";
    public static final String FIELD_ATTRIBUTE27 = "attribute27";
    public static final String FIELD_ATTRIBUTE28 = "attribute28";
    public static final String FIELD_ATTRIBUTE29 = "attribute29";
    public static final String FIELD_ATTRIBUTE30 = "attribute30";
    public static final String FIELD_ATTRIBUTE31 = "attribute31";
    public static final String FIELD_ATTRIBUTE32 = "attribute32";
    public static final String FIELD_ATTRIBUTE33 = "attribute33";
    public static final String FIELD_ATTRIBUTE34 = "attribute34";
    public static final String FIELD_ATTRIBUTE35 = "attribute35";
    public static final String FIELD_ATTRIBUTE36 = "attribute36";
    public static final String FIELD_ATTRIBUTE37 = "attribute37";
    public static final String FIELD_ATTRIBUTE38 = "attribute38";
    public static final String FIELD_ATTRIBUTE39 = "attribute39";
    public static final String FIELD_ATTRIBUTE40 = "attribute40";
    public static final String FIELD_ATTRIBUTE41 = "attribute41";
    public static final String FIELD_ATTRIBUTE42 = "attribute42";
    public static final String FIELD_ATTRIBUTE43 = "attribute43";
    public static final String FIELD_ATTRIBUTE44 = "attribute44";
    public static final String FIELD_ATTRIBUTE45 = "attribute45";
    public static final String FIELD_ATTRIBUTE46 = "attribute46";
    public static final String FIELD_ATTRIBUTE47 = "attribute47";
    public static final String FIELD_ATTRIBUTE48 = "attribute48";
    public static final String FIELD_ATTRIBUTE49 = "attribute49";
    public static final String FIELD_ATTRIBUTE50 = "attribute50";

    public static final int MAX_COLUMN_COUNT = 154;
    public static final int ATTRIBUTE_COLUMN_COUNT = 104;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("接口表行ID，主键")
    @Id
    private String lineInterfaceId;
    @ApiModelProperty(value = "接口表ID", required = true)
    @NotBlank
    private String interfaceId;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "检验项目1")
    private String test1;
    @ApiModelProperty(value = "检验项目2")
    private String test2;
    @ApiModelProperty(value = "检验项目3")
    private String test3;
    @ApiModelProperty(value = "检验项目4")
    private String test4;
    @ApiModelProperty(value = "检验项目5")
    private String test5;
    @ApiModelProperty(value = "检验项目6")
    private String test6;
    @ApiModelProperty(value = "检验项目7")
    private String test7;
    @ApiModelProperty(value = "检验项目8")
    private String test8;
    @ApiModelProperty(value = "检验项目9")
    private String test9;
    @ApiModelProperty(value = "检验项目10")
    private String test10;
    @ApiModelProperty(value = "检验项目11")
    private String test11;
    @ApiModelProperty(value = "检验项目12")
    private String test12;
    @ApiModelProperty(value = "检验项目13")
    private String test13;
    @ApiModelProperty(value = "检验项目14")
    private String test14;
    @ApiModelProperty(value = "检验项目15")
    private String test15;
    @ApiModelProperty(value = "检验项目16")
    private String test16;
    @ApiModelProperty(value = "检验项目17")
    private String test17;
    @ApiModelProperty(value = "检验项目18")
    private String test18;
    @ApiModelProperty(value = "检验项目19")
    private String test19;
    @ApiModelProperty(value = "检验项目20")
    private String test20;
    @ApiModelProperty(value = "检验项目21")
    private String test21;
    @ApiModelProperty(value = "检验项目22")
    private String test22;
    @ApiModelProperty(value = "检验项目23")
    private String test23;
    @ApiModelProperty(value = "检验项目24")
    private String test24;
    @ApiModelProperty(value = "检验项目25")
    private String test25;
    @ApiModelProperty(value = "检验项目26")
    private String test26;
    @ApiModelProperty(value = "检验项目27")
    private String test27;
    @ApiModelProperty(value = "检验项目28")
    private String test28;
    @ApiModelProperty(value = "检验项目29")
    private String test29;
    @ApiModelProperty(value = "检验项目30")
    private String test30;
    @ApiModelProperty(value = "检验项目31")
    private String test31;
    @ApiModelProperty(value = "检验项目32")
    private String test32;
    @ApiModelProperty(value = "检验项目33")
    private String test33;
    @ApiModelProperty(value = "检验项目34")
    private String test34;
    @ApiModelProperty(value = "检验项目35")
    private String test35;
    @ApiModelProperty(value = "检验项目36")
    private String test36;
    @ApiModelProperty(value = "检验项目37")
    private String test37;
    @ApiModelProperty(value = "检验项目38")
    private String test38;
    @ApiModelProperty(value = "检验项目39")
    private String test39;
    @ApiModelProperty(value = "检验项目40")
    private String test40;
    @ApiModelProperty(value = "检验项目41")
    private String test41;
    @ApiModelProperty(value = "检验项目42")
    private String test42;
    @ApiModelProperty(value = "检验项目43")
    private String test43;
    @ApiModelProperty(value = "检验项目44")
    private String test44;
    @ApiModelProperty(value = "检验项目45")
    private String test45;
    @ApiModelProperty(value = "检验项目46")
    private String test46;
    @ApiModelProperty(value = "检验项目47")
    private String test47;
    @ApiModelProperty(value = "检验项目48")
    private String test48;
    @ApiModelProperty(value = "检验项目49")
    private String test49;
    @ApiModelProperty(value = "检验项目50")
    private String test50;
    @ApiModelProperty(value = "检验项目51")
    private String test51;
    @ApiModelProperty(value = "检验项目52")
    private String test52;
    @ApiModelProperty(value = "检验项目53")
    private String test53;
    @ApiModelProperty(value = "检验项目54")
    private String test54;
    @ApiModelProperty(value = "检验项目55")
    private String test55;
    @ApiModelProperty(value = "检验项目56")
    private String test56;
    @ApiModelProperty(value = "检验项目57")
    private String test57;
    @ApiModelProperty(value = "检验项目58")
    private String test58;
    @ApiModelProperty(value = "检验项目59")
    private String test59;
    @ApiModelProperty(value = "检验项目60")
    private String test60;
    @ApiModelProperty(value = "检验项目61")
    private String test61;
    @ApiModelProperty(value = "检验项目62")
    private String test62;
    @ApiModelProperty(value = "检验项目63")
    private String test63;
    @ApiModelProperty(value = "检验项目64")
    private String test64;
    @ApiModelProperty(value = "检验项目65")
    private String test65;
    @ApiModelProperty(value = "检验项目66")
    private String test66;
    @ApiModelProperty(value = "检验项目67")
    private String test67;
    @ApiModelProperty(value = "检验项目68")
    private String test68;
    @ApiModelProperty(value = "检验项目69")
    private String test69;
    @ApiModelProperty(value = "检验项目70")
    private String test70;
    @ApiModelProperty(value = "检验项目71")
    private String test71;
    @ApiModelProperty(value = "检验项目72")
    private String test72;
    @ApiModelProperty(value = "检验项目73")
    private String test73;
    @ApiModelProperty(value = "检验项目74")
    private String test74;
    @ApiModelProperty(value = "检验项目75")
    private String test75;
    @ApiModelProperty(value = "检验项目76")
    private String test76;
    @ApiModelProperty(value = "检验项目77")
    private String test77;
    @ApiModelProperty(value = "检验项目78")
    private String test78;
    @ApiModelProperty(value = "检验项目79")
    private String test79;
    @ApiModelProperty(value = "检验项目80")
    private String test80;
    @ApiModelProperty(value = "检验项目81")
    private String test81;
    @ApiModelProperty(value = "检验项目82")
    private String test82;
    @ApiModelProperty(value = "检验项目83")
    private String test83;
    @ApiModelProperty(value = "检验项目84")
    private String test84;
    @ApiModelProperty(value = "检验项目85")
    private String test85;
    @ApiModelProperty(value = "检验项目86")
    private String test86;
    @ApiModelProperty(value = "检验项目87")
    private String test87;
    @ApiModelProperty(value = "检验项目88")
    private String test88;
    @ApiModelProperty(value = "检验项目89")
    private String test89;
    @ApiModelProperty(value = "检验项目90")
    private String test90;
    @ApiModelProperty(value = "检验项目91")
    private String test91;
    @ApiModelProperty(value = "检验项目92")
    private String test92;
    @ApiModelProperty(value = "检验项目93")
    private String test93;
    @ApiModelProperty(value = "检验项目94")
    private String test94;
    @ApiModelProperty(value = "检验项目95")
    private String test95;
    @ApiModelProperty(value = "检验项目96")
    private String test96;
    @ApiModelProperty(value = "检验项目97")
    private String test97;
    @ApiModelProperty(value = "检验项目98")
    private String test98;
    @ApiModelProperty(value = "检验项目99")
    private String test99;
    @ApiModelProperty(value = "检验项目100")
    private String test100;
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
    @ApiModelProperty(value = "")
    private String attribute16;
    @ApiModelProperty(value = "")
    private String attribute17;
    @ApiModelProperty(value = "")
    private String attribute18;
    @ApiModelProperty(value = "")
    private String attribute19;
    @ApiModelProperty(value = "")
    private String attribute20;
    @ApiModelProperty(value = "")
    private String attribute21;
    @ApiModelProperty(value = "")
    private String attribute22;
    @ApiModelProperty(value = "")
    private String attribute23;
    @ApiModelProperty(value = "")
    private String attribute24;
    @ApiModelProperty(value = "")
    private String attribute25;
    @ApiModelProperty(value = "")
    private String attribute26;
    @ApiModelProperty(value = "")
    private String attribute27;
    @ApiModelProperty(value = "")
    private String attribute28;
    @ApiModelProperty(value = "")
    private String attribute29;
    @ApiModelProperty(value = "")
    private String attribute30;
    @ApiModelProperty(value = "")
    private String attribute31;
    @ApiModelProperty(value = "")
    private String attribute32;
    @ApiModelProperty(value = "")
    private String attribute33;
    @ApiModelProperty(value = "")
    private String attribute34;
    @ApiModelProperty(value = "")
    private String attribute35;
    @ApiModelProperty(value = "")
    private String attribute36;
    @ApiModelProperty(value = "")
    private String attribute37;
    @ApiModelProperty(value = "")
    private String attribute38;
    @ApiModelProperty(value = "")
    private String attribute39;
    @ApiModelProperty(value = "")
    private String attribute40;
    @ApiModelProperty(value = "")
    private String attribute41;
    @ApiModelProperty(value = "")
    private String attribute42;
    @ApiModelProperty(value = "")
    private String attribute43;
    @ApiModelProperty(value = "")
    private String attribute44;
    @ApiModelProperty(value = "")
    private String attribute45;
    @ApiModelProperty(value = "")
    private String attribute46;
    @ApiModelProperty(value = "")
    private String attribute47;
    @ApiModelProperty(value = "")
    private String attribute48;
    @ApiModelProperty(value = "")
    private String attribute49;
    @ApiModelProperty(value = "")
    private String attribute50;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 接口表行ID，主键
     */
    public String getLineInterfaceId() {
        return lineInterfaceId;
    }

    public void setLineInterfaceId(String lineInterfaceId) {
        this.lineInterfaceId = lineInterfaceId;
    }

    /**
     * @return 接口表ID
     */
    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    /**
     * @return 类型
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return 物料批编码
     */
    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
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
     * @return 物料编码
     */
    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    /**
     * @return 检验项目1
     */
    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    /**
     * @return 检验项目2
     */
    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    /**
     * @return 检验项目3
     */
    public String getTest3() {
        return test3;
    }

    public void setTest3(String test3) {
        this.test3 = test3;
    }

    /**
     * @return 检验项目4
     */
    public String getTest4() {
        return test4;
    }

    public void setTest4(String test4) {
        this.test4 = test4;
    }

    /**
     * @return 检验项目5
     */
    public String getTest5() {
        return test5;
    }

    public void setTest5(String test5) {
        this.test5 = test5;
    }

    /**
     * @return 检验项目6
     */
    public String getTest6() {
        return test6;
    }

    public void setTest6(String test6) {
        this.test6 = test6;
    }

    /**
     * @return 检验项目7
     */
    public String getTest7() {
        return test7;
    }

    public void setTest7(String test7) {
        this.test7 = test7;
    }

    /**
     * @return 检验项目8
     */
    public String getTest8() {
        return test8;
    }

    public void setTest8(String test8) {
        this.test8 = test8;
    }

    /**
     * @return 检验项目9
     */
    public String getTest9() {
        return test9;
    }

    public void setTest9(String test9) {
        this.test9 = test9;
    }

    /**
     * @return 检验项目10
     */
    public String getTest10() {
        return test10;
    }

    public void setTest10(String test10) {
        this.test10 = test10;
    }

    /**
     * @return 检验项目11
     */
    public String getTest11() {
        return test11;
    }

    public void setTest11(String test11) {
        this.test11 = test11;
    }

    /**
     * @return 检验项目12
     */
    public String getTest12() {
        return test12;
    }

    public void setTest12(String test12) {
        this.test12 = test12;
    }

    /**
     * @return 检验项目13
     */
    public String getTest13() {
        return test13;
    }

    public void setTest13(String test13) {
        this.test13 = test13;
    }

    /**
     * @return 检验项目14
     */
    public String getTest14() {
        return test14;
    }

    public void setTest14(String test14) {
        this.test14 = test14;
    }

    /**
     * @return 检验项目15
     */
    public String getTest15() {
        return test15;
    }

    public void setTest15(String test15) {
        this.test15 = test15;
    }

    /**
     * @return 检验项目16
     */
    public String getTest16() {
        return test16;
    }

    public void setTest16(String test16) {
        this.test16 = test16;
    }

    /**
     * @return 检验项目17
     */
    public String getTest17() {
        return test17;
    }

    public void setTest17(String test17) {
        this.test17 = test17;
    }

    /**
     * @return 检验项目18
     */
    public String getTest18() {
        return test18;
    }

    public void setTest18(String test18) {
        this.test18 = test18;
    }

    /**
     * @return 检验项目19
     */
    public String getTest19() {
        return test19;
    }

    public void setTest19(String test19) {
        this.test19 = test19;
    }

    /**
     * @return 检验项目20
     */
    public String getTest20() {
        return test20;
    }

    public void setTest20(String test20) {
        this.test20 = test20;
    }

    /**
     * @return 检验项目21
     */
    public String getTest21() {
        return test21;
    }

    public void setTest21(String test21) {
        this.test21 = test21;
    }

    /**
     * @return 检验项目22
     */
    public String getTest22() {
        return test22;
    }

    public void setTest22(String test22) {
        this.test22 = test22;
    }

    /**
     * @return 检验项目23
     */
    public String getTest23() {
        return test23;
    }

    public void setTest23(String test23) {
        this.test23 = test23;
    }

    /**
     * @return 检验项目24
     */
    public String getTest24() {
        return test24;
    }

    public void setTest24(String test24) {
        this.test24 = test24;
    }

    /**
     * @return 检验项目25
     */
    public String getTest25() {
        return test25;
    }

    public void setTest25(String test25) {
        this.test25 = test25;
    }

    /**
     * @return 检验项目26
     */
    public String getTest26() {
        return test26;
    }

    public void setTest26(String test26) {
        this.test26 = test26;
    }

    /**
     * @return 检验项目27
     */
    public String getTest27() {
        return test27;
    }

    public void setTest27(String test27) {
        this.test27 = test27;
    }

    /**
     * @return 检验项目28
     */
    public String getTest28() {
        return test28;
    }

    public void setTest28(String test28) {
        this.test28 = test28;
    }

    /**
     * @return 检验项目29
     */
    public String getTest29() {
        return test29;
    }

    public void setTest29(String test29) {
        this.test29 = test29;
    }

    /**
     * @return 检验项目30
     */
    public String getTest30() {
        return test30;
    }

    public void setTest30(String test30) {
        this.test30 = test30;
    }

    /**
     * @return 检验项目31
     */
    public String getTest31() {
        return test31;
    }

    public void setTest31(String test31) {
        this.test31 = test31;
    }

    /**
     * @return 检验项目32
     */
    public String getTest32() {
        return test32;
    }

    public void setTest32(String test32) {
        this.test32 = test32;
    }

    /**
     * @return 检验项目33
     */
    public String getTest33() {
        return test33;
    }

    public void setTest33(String test33) {
        this.test33 = test33;
    }

    /**
     * @return 检验项目34
     */
    public String getTest34() {
        return test34;
    }

    public void setTest34(String test34) {
        this.test34 = test34;
    }

    /**
     * @return 检验项目35
     */
    public String getTest35() {
        return test35;
    }

    public void setTest35(String test35) {
        this.test35 = test35;
    }

    /**
     * @return 检验项目36
     */
    public String getTest36() {
        return test36;
    }

    public void setTest36(String test36) {
        this.test36 = test36;
    }

    /**
     * @return 检验项目37
     */
    public String getTest37() {
        return test37;
    }

    public void setTest37(String test37) {
        this.test37 = test37;
    }

    /**
     * @return 检验项目38
     */
    public String getTest38() {
        return test38;
    }

    public void setTest38(String test38) {
        this.test38 = test38;
    }

    /**
     * @return 检验项目39
     */
    public String getTest39() {
        return test39;
    }

    public void setTest39(String test39) {
        this.test39 = test39;
    }

    /**
     * @return 检验项目40
     */
    public String getTest40() {
        return test40;
    }

    public void setTest40(String test40) {
        this.test40 = test40;
    }

    /**
     * @return 检验项目41
     */
    public String getTest41() {
        return test41;
    }

    public void setTest41(String test41) {
        this.test41 = test41;
    }

    /**
     * @return 检验项目42
     */
    public String getTest42() {
        return test42;
    }

    public void setTest42(String test42) {
        this.test42 = test42;
    }

    /**
     * @return 检验项目43
     */
    public String getTest43() {
        return test43;
    }

    public void setTest43(String test43) {
        this.test43 = test43;
    }

    /**
     * @return 检验项目44
     */
    public String getTest44() {
        return test44;
    }

    public void setTest44(String test44) {
        this.test44 = test44;
    }

    /**
     * @return 检验项目45
     */
    public String getTest45() {
        return test45;
    }

    public void setTest45(String test45) {
        this.test45 = test45;
    }

    /**
     * @return 检验项目46
     */
    public String getTest46() {
        return test46;
    }

    public void setTest46(String test46) {
        this.test46 = test46;
    }

    /**
     * @return 检验项目47
     */
    public String getTest47() {
        return test47;
    }

    public void setTest47(String test47) {
        this.test47 = test47;
    }

    /**
     * @return 检验项目48
     */
    public String getTest48() {
        return test48;
    }

    public void setTest48(String test48) {
        this.test48 = test48;
    }

    /**
     * @return 检验项目49
     */
    public String getTest49() {
        return test49;
    }

    public void setTest49(String test49) {
        this.test49 = test49;
    }

    /**
     * @return 检验项目50
     */
    public String getTest50() {
        return test50;
    }

    public void setTest50(String test50) {
        this.test50 = test50;
    }

    /**
     * @return 检验项目51
     */
    public String getTest51() {
        return test51;
    }

    public void setTest51(String test51) {
        this.test51 = test51;
    }

    /**
     * @return 检验项目52
     */
    public String getTest52() {
        return test52;
    }

    public void setTest52(String test52) {
        this.test52 = test52;
    }

    /**
     * @return 检验项目53
     */
    public String getTest53() {
        return test53;
    }

    public void setTest53(String test53) {
        this.test53 = test53;
    }

    /**
     * @return 检验项目54
     */
    public String getTest54() {
        return test54;
    }

    public void setTest54(String test54) {
        this.test54 = test54;
    }

    /**
     * @return 检验项目55
     */
    public String getTest55() {
        return test55;
    }

    public void setTest55(String test55) {
        this.test55 = test55;
    }

    /**
     * @return 检验项目56
     */
    public String getTest56() {
        return test56;
    }

    public void setTest56(String test56) {
        this.test56 = test56;
    }

    /**
     * @return 检验项目57
     */
    public String getTest57() {
        return test57;
    }

    public void setTest57(String test57) {
        this.test57 = test57;
    }

    /**
     * @return 检验项目58
     */
    public String getTest58() {
        return test58;
    }

    public void setTest58(String test58) {
        this.test58 = test58;
    }

    /**
     * @return 检验项目59
     */
    public String getTest59() {
        return test59;
    }

    public void setTest59(String test59) {
        this.test59 = test59;
    }

    /**
     * @return 检验项目60
     */
    public String getTest60() {
        return test60;
    }

    public void setTest60(String test60) {
        this.test60 = test60;
    }

    /**
     * @return 检验项目61
     */
    public String getTest61() {
        return test61;
    }

    public void setTest61(String test61) {
        this.test61 = test61;
    }

    /**
     * @return 检验项目62
     */
    public String getTest62() {
        return test62;
    }

    public void setTest62(String test62) {
        this.test62 = test62;
    }

    /**
     * @return 检验项目63
     */
    public String getTest63() {
        return test63;
    }

    public void setTest63(String test63) {
        this.test63 = test63;
    }

    /**
     * @return 检验项目64
     */
    public String getTest64() {
        return test64;
    }

    public void setTest64(String test64) {
        this.test64 = test64;
    }

    /**
     * @return 检验项目65
     */
    public String getTest65() {
        return test65;
    }

    public void setTest65(String test65) {
        this.test65 = test65;
    }

    /**
     * @return 检验项目66
     */
    public String getTest66() {
        return test66;
    }

    public void setTest66(String test66) {
        this.test66 = test66;
    }

    /**
     * @return 检验项目67
     */
    public String getTest67() {
        return test67;
    }

    public void setTest67(String test67) {
        this.test67 = test67;
    }

    /**
     * @return 检验项目68
     */
    public String getTest68() {
        return test68;
    }

    public void setTest68(String test68) {
        this.test68 = test68;
    }

    /**
     * @return 检验项目69
     */
    public String getTest69() {
        return test69;
    }

    public void setTest69(String test69) {
        this.test69 = test69;
    }

    /**
     * @return 检验项目70
     */
    public String getTest70() {
        return test70;
    }

    public void setTest70(String test70) {
        this.test70 = test70;
    }

    /**
     * @return 检验项目71
     */
    public String getTest71() {
        return test71;
    }

    public void setTest71(String test71) {
        this.test71 = test71;
    }

    /**
     * @return 检验项目72
     */
    public String getTest72() {
        return test72;
    }

    public void setTest72(String test72) {
        this.test72 = test72;
    }

    /**
     * @return 检验项目73
     */
    public String getTest73() {
        return test73;
    }

    public void setTest73(String test73) {
        this.test73 = test73;
    }

    /**
     * @return 检验项目74
     */
    public String getTest74() {
        return test74;
    }

    public void setTest74(String test74) {
        this.test74 = test74;
    }

    /**
     * @return 检验项目75
     */
    public String getTest75() {
        return test75;
    }

    public void setTest75(String test75) {
        this.test75 = test75;
    }

    /**
     * @return 检验项目76
     */
    public String getTest76() {
        return test76;
    }

    public void setTest76(String test76) {
        this.test76 = test76;
    }

    /**
     * @return 检验项目77
     */
    public String getTest77() {
        return test77;
    }

    public void setTest77(String test77) {
        this.test77 = test77;
    }

    /**
     * @return 检验项目78
     */
    public String getTest78() {
        return test78;
    }

    public void setTest78(String test78) {
        this.test78 = test78;
    }

    /**
     * @return 检验项目79
     */
    public String getTest79() {
        return test79;
    }

    public void setTest79(String test79) {
        this.test79 = test79;
    }

    /**
     * @return 检验项目80
     */
    public String getTest80() {
        return test80;
    }

    public void setTest80(String test80) {
        this.test80 = test80;
    }

    /**
     * @return 检验项目81
     */
    public String getTest81() {
        return test81;
    }

    public void setTest81(String test81) {
        this.test81 = test81;
    }

    /**
     * @return 检验项目82
     */
    public String getTest82() {
        return test82;
    }

    public void setTest82(String test82) {
        this.test82 = test82;
    }

    /**
     * @return 检验项目83
     */
    public String getTest83() {
        return test83;
    }

    public void setTest83(String test83) {
        this.test83 = test83;
    }

    /**
     * @return 检验项目84
     */
    public String getTest84() {
        return test84;
    }

    public void setTest84(String test84) {
        this.test84 = test84;
    }

    /**
     * @return 检验项目85
     */
    public String getTest85() {
        return test85;
    }

    public void setTest85(String test85) {
        this.test85 = test85;
    }

    /**
     * @return 检验项目86
     */
    public String getTest86() {
        return test86;
    }

    public void setTest86(String test86) {
        this.test86 = test86;
    }

    /**
     * @return 检验项目87
     */
    public String getTest87() {
        return test87;
    }

    public void setTest87(String test87) {
        this.test87 = test87;
    }

    /**
     * @return 检验项目88
     */
    public String getTest88() {
        return test88;
    }

    public void setTest88(String test88) {
        this.test88 = test88;
    }

    /**
     * @return 检验项目89
     */
    public String getTest89() {
        return test89;
    }

    public void setTest89(String test89) {
        this.test89 = test89;
    }

    /**
     * @return 检验项目90
     */
    public String getTest90() {
        return test90;
    }

    public void setTest90(String test90) {
        this.test90 = test90;
    }

    /**
     * @return 检验项目91
     */
    public String getTest91() {
        return test91;
    }

    public void setTest91(String test91) {
        this.test91 = test91;
    }

    /**
     * @return 检验项目92
     */
    public String getTest92() {
        return test92;
    }

    public void setTest92(String test92) {
        this.test92 = test92;
    }

    /**
     * @return 检验项目93
     */
    public String getTest93() {
        return test93;
    }

    public void setTest93(String test93) {
        this.test93 = test93;
    }

    /**
     * @return 检验项目94
     */
    public String getTest94() {
        return test94;
    }

    public void setTest94(String test94) {
        this.test94 = test94;
    }

    /**
     * @return 检验项目95
     */
    public String getTest95() {
        return test95;
    }

    public void setTest95(String test95) {
        this.test95 = test95;
    }

    /**
     * @return 检验项目96
     */
    public String getTest96() {
        return test96;
    }

    public void setTest96(String test96) {
        this.test96 = test96;
    }

    /**
     * @return 检验项目97
     */
    public String getTest97() {
        return test97;
    }

    public void setTest97(String test97) {
        this.test97 = test97;
    }

    /**
     * @return 检验项目98
     */
    public String getTest98() {
        return test98;
    }

    public void setTest98(String test98) {
        this.test98 = test98;
    }

    /**
     * @return 检验项目99
     */
    public String getTest99() {
        return test99;
    }

    public void setTest99(String test99) {
        this.test99 = test99;
    }

    /**
     * @return 检验项目100
     */
    public String getTest100() {
        return test100;
    }

    public void setTest100(String test100) {
        this.test100 = test100;
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

    /**
     * @return
     */
    public String getAttribute16() {
        return attribute16;
    }

    public void setAttribute16(String attribute16) {
        this.attribute16 = attribute16;
    }

    /**
     * @return
     */
    public String getAttribute17() {
        return attribute17;
    }

    public void setAttribute17(String attribute17) {
        this.attribute17 = attribute17;
    }

    /**
     * @return
     */
    public String getAttribute18() {
        return attribute18;
    }

    public void setAttribute18(String attribute18) {
        this.attribute18 = attribute18;
    }

    /**
     * @return
     */
    public String getAttribute19() {
        return attribute19;
    }

    public void setAttribute19(String attribute19) {
        this.attribute19 = attribute19;
    }

    /**
     * @return
     */
    public String getAttribute20() {
        return attribute20;
    }

    public void setAttribute20(String attribute20) {
        this.attribute20 = attribute20;
    }

    /**
     * @return
     */
    public String getAttribute21() {
        return attribute21;
    }

    public void setAttribute21(String attribute21) {
        this.attribute21 = attribute21;
    }

    /**
     * @return
     */
    public String getAttribute22() {
        return attribute22;
    }

    public void setAttribute22(String attribute22) {
        this.attribute22 = attribute22;
    }

    /**
     * @return
     */
    public String getAttribute23() {
        return attribute23;
    }

    public void setAttribute23(String attribute23) {
        this.attribute23 = attribute23;
    }

    /**
     * @return
     */
    public String getAttribute24() {
        return attribute24;
    }

    public void setAttribute24(String attribute24) {
        this.attribute24 = attribute24;
    }

    /**
     * @return
     */
    public String getAttribute25() {
        return attribute25;
    }

    public void setAttribute25(String attribute25) {
        this.attribute25 = attribute25;
    }

    /**
     * @return
     */
    public String getAttribute26() {
        return attribute26;
    }

    public void setAttribute26(String attribute26) {
        this.attribute26 = attribute26;
    }

    /**
     * @return
     */
    public String getAttribute27() {
        return attribute27;
    }

    public void setAttribute27(String attribute27) {
        this.attribute27 = attribute27;
    }

    /**
     * @return
     */
    public String getAttribute28() {
        return attribute28;
    }

    public void setAttribute28(String attribute28) {
        this.attribute28 = attribute28;
    }

    /**
     * @return
     */
    public String getAttribute29() {
        return attribute29;
    }

    public void setAttribute29(String attribute29) {
        this.attribute29 = attribute29;
    }

    /**
     * @return
     */
    public String getAttribute30() {
        return attribute30;
    }

    public void setAttribute30(String attribute30) {
        this.attribute30 = attribute30;
    }

    /**
     * @return
     */
    public String getAttribute31() {
        return attribute31;
    }

    public void setAttribute31(String attribute31) {
        this.attribute31 = attribute31;
    }

    /**
     * @return
     */
    public String getAttribute32() {
        return attribute32;
    }

    public void setAttribute32(String attribute32) {
        this.attribute32 = attribute32;
    }

    /**
     * @return
     */
    public String getAttribute33() {
        return attribute33;
    }

    public void setAttribute33(String attribute33) {
        this.attribute33 = attribute33;
    }

    /**
     * @return
     */
    public String getAttribute34() {
        return attribute34;
    }

    public void setAttribute34(String attribute34) {
        this.attribute34 = attribute34;
    }

    /**
     * @return
     */
    public String getAttribute35() {
        return attribute35;
    }

    public void setAttribute35(String attribute35) {
        this.attribute35 = attribute35;
    }

    /**
     * @return
     */
    public String getAttribute36() {
        return attribute36;
    }

    public void setAttribute36(String attribute36) {
        this.attribute36 = attribute36;
    }

    /**
     * @return
     */
    public String getAttribute37() {
        return attribute37;
    }

    public void setAttribute37(String attribute37) {
        this.attribute37 = attribute37;
    }

    /**
     * @return
     */
    public String getAttribute38() {
        return attribute38;
    }

    public void setAttribute38(String attribute38) {
        this.attribute38 = attribute38;
    }

    /**
     * @return
     */
    public String getAttribute39() {
        return attribute39;
    }

    public void setAttribute39(String attribute39) {
        this.attribute39 = attribute39;
    }

    /**
     * @return
     */
    public String getAttribute40() {
        return attribute40;
    }

    public void setAttribute40(String attribute40) {
        this.attribute40 = attribute40;
    }

    /**
     * @return
     */
    public String getAttribute41() {
        return attribute41;
    }

    public void setAttribute41(String attribute41) {
        this.attribute41 = attribute41;
    }

    /**
     * @return
     */
    public String getAttribute42() {
        return attribute42;
    }

    public void setAttribute42(String attribute42) {
        this.attribute42 = attribute42;
    }

    /**
     * @return
     */
    public String getAttribute43() {
        return attribute43;
    }

    public void setAttribute43(String attribute43) {
        this.attribute43 = attribute43;
    }

    /**
     * @return
     */
    public String getAttribute44() {
        return attribute44;
    }

    public void setAttribute44(String attribute44) {
        this.attribute44 = attribute44;
    }

    /**
     * @return
     */
    public String getAttribute45() {
        return attribute45;
    }

    public void setAttribute45(String attribute45) {
        this.attribute45 = attribute45;
    }

    /**
     * @return
     */
    public String getAttribute46() {
        return attribute46;
    }

    public void setAttribute46(String attribute46) {
        this.attribute46 = attribute46;
    }

    /**
     * @return
     */
    public String getAttribute47() {
        return attribute47;
    }

    public void setAttribute47(String attribute47) {
        this.attribute47 = attribute47;
    }

    /**
     * @return
     */
    public String getAttribute48() {
        return attribute48;
    }

    public void setAttribute48(String attribute48) {
        this.attribute48 = attribute48;
    }

    /**
     * @return
     */
    public String getAttribute49() {
        return attribute49;
    }

    public void setAttribute49(String attribute49) {
        this.attribute49 = attribute49;
    }

    /**
     * @return
     */
    public String getAttribute50() {
        return attribute50;
    }

    public void setAttribute50(String attribute50) {
        this.attribute50 = attribute50;
    }

}
