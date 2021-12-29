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
 * 反射镜数据采集接口表
 *
 * @author yonghui.zhu@hand-china.com 2020-08-04 19:51:39
 */
@ApiModel("反射镜数据采集接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_rmc_collect_iface")
@CustomPrimary
public class ItfRmcCollectIface extends AuditDomain {

    //exchenge by wenzhnag 2002.08.11 在字段前加设备类
    //exchenge by wenzhnag 2002.08.11 取消全两个字段设备类
    public static final String FIELD_INTERFACE_ID = "interfaceId";
    //public static final String FIELD_RMC_ASSET_ENCODING = "rmcAssetEncoding";
    //public static final String FIELD_RMC_SN = "rmcSn";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_RMC_LOT = "rmcLot";
    public static final String FIELD_RMC_GLUE_LOT = "rmcGlueLot";
    public static final String FIELD_RMC_GLUE_TYPE = "rmcGlueType";
    public static final String FIELD_RMC_GLUE_OPEN_DATE = "rmcGlueOpenDate";
    public static final String FIELD_RMC_HOT_START_DATE = "rmcHotStartDate";
    public static final String FIELD_RMC_HOT_END_DATE = "rmcHotEndDate";
    public static final String FIELD_RMC_COS_POS = "rmcCosPos";
    public static final String FIELD_RMC_CURRENT = "rmcCurrent";
    public static final String FIELD_RMC_AIR_TO_POWER = "rmcAirToPower";
    public static final String FIELD_RMC_BEFORE_CURING_POWER = "rmcBeforeCuringPower";
    public static final String FIELD_RMC_AFTER_CURING_POWER = "rmcAfterCuringPower";
    public static final String FIELD_RMC_COUPLING_EFFICIENCY = "rmcCouplingEfficiency";
    public static final String FIELD_RMC_ERR = "rmcErr";
    public static final String FIELD_RMC_REMARK = "rmcRemark";
    public static final String FIELD_RMC_PRO_STATUS = "rmcProStatus";
    public static final String FIELD_RMC_COS_TYPE = "rmcCosType";
    public static final String FIELD_RMC_EXP_CODE = "rmcExpCode";
    public static final String FIELD_PRIMARY_KEY = "primaryKey";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_RMC_ATTRIBUTE1 = "rmcAttribute1";
    public static final String FIELD_RMC_ATTRIBUTE2 = "rmcAttribute2";
    public static final String FIELD_RMC_ATTRIBUTE3 = "rmcAttribute3";
    public static final String FIELD_RMC_ATTRIBUTE4 = "rmcAttribute4";
    public static final String FIELD_RMC_ATTRIBUTE5 = "rmcAttribute5";
    public static final String FIELD_RMC_ATTRIBUTE6 = "rmcAttribute6";
    public static final String FIELD_RMC_ATTRIBUTE7 = "rmcAttribute7";
    public static final String FIELD_RMC_ATTRIBUTE8 = "rmcAttribute8";
    public static final String FIELD_RMC_ATTRIBUTE9 = "rmcAttribute9";
    public static final String FIELD_RMC_ATTRIBUTE10 = "rmcAttribute10";
    public static final String FIELD_RMC_ATTRIBUTE11 = "rmcAttribute11";
    public static final String FIELD_RMC_ATTRIBUTE12 = "rmcAttribute12";
    public static final String FIELD_RMC_ATTRIBUTE13 = "rmcAttribute13";
    public static final String FIELD_RMC_ATTRIBUTE14 = "rmcAttribute14";
    public static final String FIELD_RMC_ATTRIBUTE15 = "rmcAttribute15";


    public static final String FIELD_RMC_COS_NC_CODE = "rmcCosNcCode";
    public static final String FIELD_RMC_NC_CODE = "rmcNcCode";

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
    @ApiModelProperty(value = "反射镜批次")
    private String rmcLot;
    @ApiModelProperty(value = "胶水批次")
    private String rmcGlueLot;
    @ApiModelProperty(value = "胶水型号")
    private String rmcGlueType;
    @ApiModelProperty(value = "胶水打开时间")
    private Date rmcGlueOpenDate;
    @ApiModelProperty(value = "烘烤开始时间")
    private Date rmcHotStartDate;
    @ApiModelProperty(value = "烘烤结束时间")
    private Date rmcHotEndDate;
    @ApiModelProperty(value = "COS路数")
    private String rmcCosPos;
    @ApiModelProperty(value = "电流")
    private BigDecimal rmcCurrent;
    @ApiModelProperty(value = "空对功率",required = true)
    @NotNull
    private BigDecimal rmcAirToPower;
    @ApiModelProperty(value = "固化前功率",required = true)
    @NotNull
    private BigDecimal rmcBeforeCuringPower;
    @ApiModelProperty(value = "固化后功率",required = true)
    @NotNull
    private BigDecimal rmcAfterCuringPower;
    @ApiModelProperty(value = "耦合效率")
    private BigDecimal rmcCouplingEfficiency;
    @ApiModelProperty(value = "不良类型")
    private String rmcErr;
    @ApiModelProperty(value = "备注")
    private String rmcRemark;
    @ApiModelProperty(value = "工序状态")
    private String rmcProStatus;
    @ApiModelProperty(value = "COS类型")
    private String rmcCosType;
    @ApiModelProperty(value = "实验代码")
    private String rmcExpCode;
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
    private String rmcAttribute1;
    @ApiModelProperty(value = "")
    private String rmcAttribute2;
    @ApiModelProperty(value = "")
    private String rmcAttribute3;
    @ApiModelProperty(value = "")
    private String rmcAttribute4;
    @ApiModelProperty(value = "")
    private String rmcAttribute5;
    @ApiModelProperty(value = "")
    private String rmcAttribute6;
    @ApiModelProperty(value = "")
    private String rmcAttribute7;
    @ApiModelProperty(value = "")
    private String rmcAttribute8;
    @ApiModelProperty(value = "")
    private String rmcAttribute9;
    @ApiModelProperty(value = "")
    private String rmcAttribute10;
    @ApiModelProperty(value = "")
    private String rmcAttribute11;
    @ApiModelProperty(value = "")
    private String rmcAttribute12;
    @ApiModelProperty(value = "")
    private String rmcAttribute13;
    @ApiModelProperty(value = "")
    private String rmcAttribute14;
    @ApiModelProperty(value = "")
    private String rmcAttribute15;

    @ApiModelProperty(value = "COS不良")
    private String rmcCosNcCode;
    @ApiModelProperty(value = "器件不良")
    private String rmcNcCode;
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
     * @return 反射镜批次
     */
    public String getRmcLot() {
        return rmcLot;
    }

    public void setRmcLot(String rmcLot) {
        this.rmcLot = rmcLot;
    }
    /**
     * @return 胶水批次
     */
    public String getRmcGlueLot() {
        return rmcGlueLot;
    }

    public void setRmcGlueLot(String rmcGlueLot) {
        this.rmcGlueLot = rmcGlueLot;
    }
    /**
     * @return 胶水型号
     */
    public String getRmcGlueType() {
        return rmcGlueType;
    }

    public void setRmcGlueType(String rmcGlueType) {
        this.rmcGlueType = rmcGlueType;
    }
    /**
     * @return 胶水打开时间
     */
    public Date getRmcGlueOpenDate() {
        return rmcGlueOpenDate;
    }

    public void setRmcGlueOpenDate(Date rmcGlueOpenDate) {
        this.rmcGlueOpenDate = rmcGlueOpenDate;
    }
    /**
     * @return 烘烤开始时间
     */
    public Date getRmcHotStartDate() {
        return rmcHotStartDate;
    }

    public void setRmcHotStartDate(Date rmcHotStartDate) {
        this.rmcHotStartDate = rmcHotStartDate;
    }
    /**
     * @return 烘烤结束时间
     */
    public Date getRmcHotEndDate() {
        return rmcHotEndDate;
    }

    public void setRmcHotEndDate(Date rmcHotEndDate) {
        this.rmcHotEndDate = rmcHotEndDate;
    }
    /**
     * @return COS路数
     */
    public String getRmcCosPos() {
        return rmcCosPos;
    }

    public void setRmcCosPos(String rmcCosPos) {
        this.rmcCosPos = rmcCosPos;
    }
    /**
     * @return 电流
     */
    public BigDecimal getRmcCurrent() {
        return rmcCurrent;
    }

    public void setRmcCurrent(BigDecimal rmcCurrent) {
        this.rmcCurrent = rmcCurrent;
    }
    /**
     * @return 空对功率
     */
    public BigDecimal getRmcAirToPower() {
        return rmcAirToPower;
    }

    public void setRmcAirToPower(BigDecimal rmcAirToPower) {
        this.rmcAirToPower = rmcAirToPower;
    }
    /**
     * @return 固化前功率
     */
    public BigDecimal getRmcBeforeCuringPower() {
        return rmcBeforeCuringPower;
    }

    public void setRmcBeforeCuringPower(BigDecimal rmcBeforeCuringPower) {
        this.rmcBeforeCuringPower = rmcBeforeCuringPower;
    }
    /**
     * @return 固化后功率
     */
    public BigDecimal getRmcAfterCuringPower() {
        return rmcAfterCuringPower;
    }

    public void setRmcAfterCuringPower(BigDecimal rmcAfterCuringPower) {
        this.rmcAfterCuringPower = rmcAfterCuringPower;
    }
    /**
     * @return 耦合效率
     */
    public BigDecimal getRmcCouplingEfficiency() {
        return rmcCouplingEfficiency;
    }

    public void setRmcCouplingEfficiency(BigDecimal rmcCouplingEfficiency) {
        this.rmcCouplingEfficiency = rmcCouplingEfficiency;
    }
    /**
     * @return 不良类型
     */
    public String getRmcErr() {
        return rmcErr;
    }

    public void setRmcErr(String rmcErr) {
        this.rmcErr = rmcErr;
    }
    /**
     * @return 备注
     */
    public String getRmcRemark() {
        return rmcRemark;
    }

    public void setRmcRemark(String rmcRemark) {
        this.rmcRemark = rmcRemark;
    }
    /**
     * @return 工序状态
     */
    public String getRmcProStatus() {
        return rmcProStatus;
    }

    public void setRmcProStatus(String rmcProStatus) {
        this.rmcProStatus = rmcProStatus;
    }
    /**
     * @return COS类型
     */
    public String getRmcCosType() {
        return rmcCosType;
    }

    public void setRmcCosType(String rmcCosType) {
        this.rmcCosType = rmcCosType;
    }
    /**
     * @return 实验代码
     */
    public String getRmcExpCode() {
        return rmcExpCode;
    }

    public void setRmcExpCode(String rmcExpCode) {
        this.rmcExpCode = rmcExpCode;
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
    public String getRmcAttribute1() {
        return rmcAttribute1;
    }

    public void setRmcAttribute1(String rmcAttribute1) {
        this.rmcAttribute1 = rmcAttribute1;
    }
    /**
     * @return
     */
    public String getRmcAttribute2() {
        return rmcAttribute2;
    }

    public void setRmcAttribute2(String rmcAttribute2) {
        this.rmcAttribute2 = rmcAttribute2;
    }
    /**
     * @return
     */
    public String getRmcAttribute3() {
        return rmcAttribute3;
    }

    public void setRmcAttribute3(String rmcAttribute3) {
        this.rmcAttribute3 = rmcAttribute3;
    }
    /**
     * @return
     */
    public String getRmcAttribute4() {
        return rmcAttribute4;
    }

    public void setRmcAttribute4(String rmcAttribute4) {
        this.rmcAttribute4 = rmcAttribute4;
    }
    /**
     * @return
     */
    public String getRmcAttribute5() {
        return rmcAttribute5;
    }

    public void setRmcAttribute5(String rmcAttribute5) {
        this.rmcAttribute5 = rmcAttribute5;
    }
    /**
     * @return
     */
    public String getRmcAttribute6() {
        return rmcAttribute6;
    }

    public void setRmcAttribute6(String rmcAttribute6) {
        this.rmcAttribute6 = rmcAttribute6;
    }
    /**
     * @return
     */
    public String getRmcAttribute7() {
        return rmcAttribute7;
    }

    public void setRmcAttribute7(String rmcAttribute7) {
        this.rmcAttribute7 = rmcAttribute7;
    }
    /**
     * @return
     */
    public String getRmcAttribute8() {
        return rmcAttribute8;
    }

    public void setRmcAttribute8(String rmcAttribute8) {
        this.rmcAttribute8 = rmcAttribute8;
    }
    /**
     * @return
     */
    public String getRmcAttribute9() {
        return rmcAttribute9;
    }

    public void setRmcAttribute9(String rmcAttribute9) {
        this.rmcAttribute9 = rmcAttribute9;
    }
    /**
     * @return
     */
    public String getRmcAttribute10() {
        return rmcAttribute10;
    }

    public void setRmcAttribute10(String rmcAttribute10) {
        this.rmcAttribute10 = rmcAttribute10;
    }
    /**
     * @return
     */
    public String getRmcAttribute11() {
        return rmcAttribute11;
    }

    public void setRmcAttribute11(String rmcAttribute11) {
        this.rmcAttribute11 = rmcAttribute11;
    }
    /**
     * @return
     */
    public String getRmcAttribute12() {
        return rmcAttribute12;
    }

    public void setRmcAttribute12(String rmcAttribute12) {
        this.rmcAttribute12 = rmcAttribute12;
    }
    /**
     * @return
     */
    public String getRmcAttribute13() {
        return rmcAttribute13;
    }

    public void setRmcAttribute13(String rmcAttribute13) {
        this.rmcAttribute13 = rmcAttribute13;
    }
    /**
     * @return
     */
    public String getRmcAttribute14() {
        return rmcAttribute14;
    }

    public void setRmcAttribute14(String rmcAttribute14) {
        this.rmcAttribute14 = rmcAttribute14;
    }
    /**
     * @return
     */
    public String getRmcAttribute15() {
        return rmcAttribute15;
    }

    public void setRmcAttribute15(String rmcAttribute15) {
        this.rmcAttribute15 = rmcAttribute15;
    }

    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }


    public String getRmcCosNcCode() {
        return rmcCosNcCode;
    }

    public void setRmcCosNcCode(String rmcCosNcCode) {
        this.rmcCosNcCode = rmcCosNcCode;
    }

    public String getRmcNcCode() {
        return rmcNcCode;
    }

    public void setRmcNcCode(String rmcNcCode) {
        this.rmcNcCode = rmcNcCode;
    }
}
