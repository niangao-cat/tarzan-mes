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
import java.math.BigDecimal;
import java.util.Date;

/**
 * FAC数据采集接口表
 *
 * @author yonghui.zhu@hand-china.com 2020-08-04 19:51:39
 */
@ApiModel("FAC数据采集接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_fac_collect_iface")
@CustomPrimary
public class ItfFacCollectIface extends AuditDomain {

    //exchenge by wenzhnag 2002.08.11 在字段前加设备类
    //exchenge by wenzhnag 2002.08.11 取消全两个字段设备类
    public static final String FIELD_INTERFACE_ID = "interfaceId";
    //public static final String FIELD_FAC_ASSET_ENCODING = "facAssetEncoding";
    //public static final String FIELD_FAC_SN = "facSn";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_FAC_LOT = "facLot";
    public static final String FIELD_FAC_GLUE_LOT = "facGlueLot";
    public static final String FIELD_FAC_GLUE_TYPE = "facGlueType";
    public static final String FIELD_FAC_GLUE_OPEN_DATE = "facGlueOpenDate";
    public static final String FIELD_FAC_HOT_START_DATE = "facHotStartDate";
    public static final String FIELD_FAC_IS_CHART = "facIsChart";
    public static final String FIELD_FAC_COS_POS = "facCosPos";
    public static final String FIELD_FAC_CURRENT = "facCurrent";
    public static final String FIELD_FAC_CENTER_Y = "facCenterY";
    public static final String FIELD_FAC_CENTER_X = "facCenterX";
    public static final String FIELD_FAC_WIDTH_Y = "facWidthY";
    public static final String FIELD_FAC_WIDTH_X = "facWidthX";
    public static final String FIELD_FAC_ERR = "facErr";
    public static final String FIELD_FAC_REMARK = "facRemark";
    public static final String FIELD_FAC_COS_TYPE = "facCosType";
    public static final String FIELD_FAC_PARA_SHIFT = "facParaShift";
    public static final String FIELD_FAC_RAY_ANGLE = "facRayAngle";
    public static final String FIELD_FAC_EXP_CODE = "facExpCode";
    public static final String FIELD_PRIMARY_KEY = "primaryKey";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_FAC_ATTRIBUTE1 = "facAttribute1";
    public static final String FIELD_FAC_ATTRIBUTE2 = "facAttribute2";
    public static final String FIELD_FAC_ATTRIBUTE3 = "facAttribute3";
    public static final String FIELD_FAC_ATTRIBUTE4 = "facAttribute4";
    public static final String FIELD_FAC_ATTRIBUTE5 = "facAttribute5";
    public static final String FIELD_FAC_ATTRIBUTE6 = "facAttribute6";
    public static final String FIELD_FAC_ATTRIBUTE7 = "facAttribute7";
    public static final String FIELD_FAC_ATTRIBUTE8 = "facAttribute8";
    public static final String FIELD_FAC_ATTRIBUTE9 = "facAttribute9";
    public static final String FIELD_FAC_ATTRIBUTE10 = "facAttribute10";
    public static final String FIELD_FAC_ATTRIBUTE11 = "facAttribute11";
    public static final String FIELD_FAC_ATTRIBUTE12 = "facAttribute12";
    public static final String FIELD_FAC_ATTRIBUTE13 = "facAttribute13";
    public static final String FIELD_FAC_ATTRIBUTE14 = "facAttribute14";
    public static final String FIELD_FAC_ATTRIBUTE15 = "facAttribute15";

    public static final String FIELD_FAC_COS_NC_CODE = "facCosNcCode";
    public static final String FIELD_FAC_NC_CODE = "facNcCode";
    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("接口表ID，主键")
    @Id
    private String interfaceId;
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "产品SN")
    private String sn;
    @ApiModelProperty(value = "FAC批次")
    private String facLot;
    @ApiModelProperty(value = "胶水批次")
    private String facGlueLot;
    @ApiModelProperty(value = "胶水类型")
    private String facGlueType;
    @ApiModelProperty(value = "胶水打开时间")
    private Date facGlueOpenDate;
    @ApiModelProperty(value = "烘烤开始时间")
    private Date facHotStartDate;
    @ApiModelProperty(value = "是否存图")
    private String facIsChart;
    @ApiModelProperty(value = "COS路数")
    private String facCosPos;
    @ApiModelProperty(value = "电流")
    private BigDecimal facCurrent;
    @ApiModelProperty(value = "质心Y")
    private BigDecimal facCenterY;
    @ApiModelProperty(value = "质心X")
    private BigDecimal facCenterX;
    @ApiModelProperty(value = "Y宽")
    private BigDecimal facWidthY;
    @ApiModelProperty(value = "X宽")
    private BigDecimal facWidthX;
    @ApiModelProperty(value = "不良类型")
    private String facErr;
    @ApiModelProperty(value = "备注")
    private String facRemark;
    @ApiModelProperty(value = "COS类型")
    private String facCosType;
    @ApiModelProperty(value = "平行光偏移")
    private String facParaShift;
    @ApiModelProperty(value = "光斑角度")
    private String facRayAngle;
    @ApiModelProperty(value = "实验代码")
    private String facExpCode;
    @ApiModelProperty(value = "外围系统唯一标识")
    private String primaryKey;
    @ApiModelProperty(value = "处理时间", required = true)
    @NotNull
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)", required = true)
    @NotBlank
    private String processStatus;
    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty(value = "")
    private String attributeCategory;
    @ApiModelProperty(value = "")
    private String facAttribute1;
    @ApiModelProperty(value = "")
    private String facAttribute2;
    @ApiModelProperty(value = "")
    private String facAttribute3;
    @ApiModelProperty(value = "")
    private String facAttribute4;
    @ApiModelProperty(value = "")
    private String facAttribute5;
    @ApiModelProperty(value = "")
    private String facAttribute6;
    @ApiModelProperty(value = "")
    private String facAttribute7;
    @ApiModelProperty(value = "")
    private String facAttribute8;
    @ApiModelProperty(value = "")
    private String facAttribute9;
    @ApiModelProperty(value = "")
    private String facAttribute10;
    @ApiModelProperty(value = "")
    private String facAttribute11;
    @ApiModelProperty(value = "")
    private String facAttribute12;
    @ApiModelProperty(value = "")
    private String facAttribute13;
    @ApiModelProperty(value = "")
    private String facAttribute14;
    @ApiModelProperty(value = "")
    private String facAttribute15;

    @ApiModelProperty(value = "COS不良")
    private String facCosNcCode;
    @ApiModelProperty(value = "器件不良")
    private String facNcCode;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    @ApiModelProperty(value = "设备类别")
    @Transient
    private String equipmentCategory;
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
     * @return 设备资产编码
     */
    public String getAssetEncoding() {
        return assetEncoding;
    }

    public void setAssetEncoding(String assetEncoding) {
        this.assetEncoding = assetEncoding;
    }
    /**
     * @return 产品SN
     */
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
    /**
     * @return FAC批次
     */
    public String getFacLot() {
        return facLot;
    }

    public void setFacLot(String facLot) {
        this.facLot = facLot;
    }
    /**
     * @return 胶水批次
     */
    public String getFacGlueLot() {
        return facGlueLot;
    }

    public void setFacGlueLot(String facGlueLot) {
        this.facGlueLot = facGlueLot;
    }
    /**
     * @return 胶水类型
     */
    public String getFacGlueType() {
        return facGlueType;
    }

    public void setFacGlueType(String facGlueType) {
        this.facGlueType = facGlueType;
    }
    /**
     * @return 胶水打开时间
     */
    public Date getFacGlueOpenDate() {
        return facGlueOpenDate;
    }

    public void setFacGlueOpenDate(Date facGlueOpenDate) {
        this.facGlueOpenDate = facGlueOpenDate;
    }
    /**
     * @return 烘烤开始时间
     */
    public Date getFacHotStartDate() {
        return facHotStartDate;
    }

    public void setFacHotStartDate(Date facHotStartDate) {
        this.facHotStartDate = facHotStartDate;
    }
    /**
     * @return 是否存图
     */
    public String getFacIsChart() {
        return facIsChart;
    }

    public void setFacIsChart(String facIsChart) {
        this.facIsChart = facIsChart;
    }
    /**
     * @return COS路数
     */
    public String getFacCosPos() {
        return facCosPos;
    }

    public void setFacCosPos(String facCosPos) {
        this.facCosPos = facCosPos;
    }
    /**
     * @return 电流
     */
    public BigDecimal getFacCurrent() {
        return facCurrent;
    }

    public void setFacCurrent(BigDecimal facCurrent) {
        this.facCurrent = facCurrent;
    }
    /**
     * @return 质心Y
     */
    public BigDecimal getFacCenterY() {
        return facCenterY;
    }

    public void setFacCenterY(BigDecimal facCenterY) {
        this.facCenterY = facCenterY;
    }
    /**
     * @return 质心X
     */
    public BigDecimal getFacCenterX() {
        return facCenterX;
    }

    public void setFacCenterX(BigDecimal facCenterX) {
        this.facCenterX = facCenterX;
    }
    /**
     * @return Y宽
     */
    public BigDecimal getFacWidthY() {
        return facWidthY;
    }

    public void setFacWidthY(BigDecimal facWidthY) {
        this.facWidthY = facWidthY;
    }
    /**
     * @return X宽
     */
    public BigDecimal getFacWidthX() {
        return facWidthX;
    }

    public void setFacWidthX(BigDecimal facWidthX) {
        this.facWidthX = facWidthX;
    }
    /**
     * @return 不良类型
     */
    public String getFacErr() {
        return facErr;
    }

    public void setFacErr(String facErr) {
        this.facErr = facErr;
    }
    /**
     * @return 备注
     */
    public String getFacRemark() {
        return facRemark;
    }

    public void setFacRemark(String facRemark) {
        this.facRemark = facRemark;
    }
    /**
     * @return COS类型
     */
    public String getFacCosType() {
        return facCosType;
    }

    public void setFacCosType(String facCosType) {
        this.facCosType = facCosType;
    }
    /**
     * @return 平行光偏移
     */
    public String getFacParaShift() {
        return facParaShift;
    }

    public void setFacParaShift(String facParaShift) {
        this.facParaShift = facParaShift;
    }
    /**
     * @return 光斑角度
     */
    public String getFacRayAngle() {
        return facRayAngle;
    }

    public void setFacRayAngle(String facRayAngle) {
        this.facRayAngle = facRayAngle;
    }
    /**
     * @return 实验代码
     */
    public String getFacExpCode() {
        return facExpCode;
    }

    public void setFacExpCode(String facExpCode) {
        this.facExpCode = facExpCode;
    }
    /**
     * @return 外围系统唯一标识
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
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
    public String getFacAttribute1() {
        return facAttribute1;
    }

    public void setFacAttribute1(String facAttribute1) {
        this.facAttribute1 = facAttribute1;
    }
    /**
     * @return
     */
    public String getFacAttribute2() {
        return facAttribute2;
    }

    public void setFacAttribute2(String facAttribute2) {
        this.facAttribute2 = facAttribute2;
    }
    /**
     * @return
     */
    public String getFacAttribute3() {
        return facAttribute3;
    }

    public void setFacAttribute3(String facAttribute3) {
        this.facAttribute3 = facAttribute3;
    }
    /**
     * @return
     */
    public String getFacAttribute4() {
        return facAttribute4;
    }

    public void setFacAttribute4(String facAttribute4) {
        this.facAttribute4 = facAttribute4;
    }
    /**
     * @return
     */
    public String getFacAttribute5() {
        return facAttribute5;
    }

    public void setFacAttribute5(String facAttribute5) {
        this.facAttribute5 = facAttribute5;
    }
    /**
     * @return
     */
    public String getFacAttribute6() {
        return facAttribute6;
    }

    public void setFacAttribute6(String facAttribute6) {
        this.facAttribute6 = facAttribute6;
    }
    /**
     * @return
     */
    public String getFacAttribute7() {
        return facAttribute7;
    }

    public void setFacAttribute7(String facAttribute7) {
        this.facAttribute7 = facAttribute7;
    }
    /**
     * @return
     */
    public String getFacAttribute8() {
        return facAttribute8;
    }

    public void setFacAttribute8(String facAttribute8) {
        this.facAttribute8 = facAttribute8;
    }
    /**
     * @return
     */
    public String getFacAttribute9() {
        return facAttribute9;
    }

    public void setFacAttribute9(String facAttribute9) {
        this.facAttribute9 = facAttribute9;
    }
    /**
     * @return
     */
    public String getFacAttribute10() {
        return facAttribute10;
    }

    public void setFacAttribute10(String facAttribute10) {
        this.facAttribute10 = facAttribute10;
    }
    /**
     * @return
     */
    public String getFacAttribute11() {
        return facAttribute11;
    }

    public void setFacAttribute11(String facAttribute11) {
        this.facAttribute11 = facAttribute11;
    }
    /**
     * @return
     */
    public String getFacAttribute12() {
        return facAttribute12;
    }

    public void setFacAttribute12(String facAttribute12) {
        this.facAttribute12 = facAttribute12;
    }
    /**
     * @return
     */
    public String getFacAttribute13() {
        return facAttribute13;
    }

    public void setFacAttribute13(String facAttribute13) {
        this.facAttribute13 = facAttribute13;
    }
    /**
     * @return
     */
    public String getFacAttribute14() {
        return facAttribute14;
    }

    public void setFacAttribute14(String facAttribute14) {
        this.facAttribute14 = facAttribute14;
    }
    /**
     * @return
     */
    public String getFacAttribute15() {
        return facAttribute15;
    }

    public void setFacAttribute15(String facAttribute15) {
        this.facAttribute15 = facAttribute15;
    }

    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }


    public String getFacNcCode() {
        return facNcCode;
    }

    public void setFacNcCode(String facNcCode) {
        this.facNcCode = facNcCode;
    }

    public String getFacCosNcCode() {
        return facCosNcCode;
    }

    public void setFacCosNcCode(String facCosNcCode) {
        this.facCosNcCode = facCosNcCode;
    }
}
