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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 光谱仪数据采集接口表
 *
 * @author yonghui.zhu@hand-china.com 2020-07-13 18:36:00
 */
@ApiModel("光谱仪数据采集接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_spec_collect_iface")
@CustomPrimary
public class ItfSpecCollectIface extends AuditDomain {

    public static final String FIELD_INTERFACE_ID = "interfaceId";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_EQUIPMENT_CATEGORY = "equipmentCategory";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_WORK_TYPE = "workType";
    public static final String FIELD_OPTICAL_MODULE = "opticalModule";
    public static final String FIELD_WAVEFORM_1080 = "waveform1080";
    public static final String FIELD_WAVEFORM_1135 = "waveform1135";
    public static final String FIELD_OPTICAL_NONLINEAR = "opticalNonlinear";
    public static final String FIELD_PULL_ON_THRO_13_WL = "pullOnThro13Wl";
    public static final String FIELD_PULL_OFF_THRO_13_WL = "pullOffThro13Wl";
    public static final String FIELD_ANNEAL_THRO_13_BD = "annealThro13Bd";
    public static final String FIELD_ANNEAL_THRO_DP = "annealThroDp";
    public static final String FIELD_ANNEAL_TIMES = "annealTimes";
    public static final String FIELD_ANNEALED_THRO_13_BD = "annealedThro13Bd";
    public static final String FIELD_ANNEALED_THRO_DP = "annealedThroDp";
    public static final String FIELD_ANNEALED_RE_3_BD = "annealedRe3Bd";
    public static final String FIELD_ANNEALED_RE_DP = "annealedReDp";
    public static final String FIELD_RECORD_TIME = "recordTime";
    public static final String FIELD_HYDROGEN_THRO_13_WL = "hydrogenThro13Wl";
    public static final String FIELD_HYDROGEN_THRO_13_BD = "hydrogenThro13Bd";
    public static final String FIELD_HYDROGEN_THRO_DP = "hydrogenThroDp";
    public static final String FIELD_HYDROGEN_THRO_3_WL = "hydrogenThro3Wl";
    public static final String FIELD_HYDROGEN_RE_3_BD = "hydrogenRe3Bd";
    public static final String FIELD_HYDROGEN_RE_DP = "hydrogenReDp";
    public static final String FIELD_TEST_TIME = "testTime";
    public static final String FIELD_PULL_ON_RE_3_WL = "pullOnRe3Wl";
    public static final String FIELD_PULL_OFF_RE_3_WL = "pullOffRe3Wl";
    public static final String FIELD_ANNEAL_RE_3_BD = "annealRe3Bd";
    public static final String FIELD_ANNEAL_RE_DP = "annealReDp";
    public static final String FIELD_ANNEALED_RE_3_WL = "annealedRe3Wl";
    public static final String FIELD_HYDROGEN_RE_3_WL = "hydrogenRe3Wl";
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


    @ApiModelProperty("接口表ID，主键")
    @Id
    private String interfaceId;
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;
    @ApiModelProperty(value = "产品SN")
    private String sn;
    @ApiModelProperty(value = "工作类型RECORD/TEST（刻写/测试）")
    private String workType;
    @ApiModelProperty(value = "1080nm波峰")
    private BigDecimal waveform1080;
    @ApiModelProperty(value = "1135nm波峰")
    private BigDecimal waveform1135;
    @ApiModelProperty(value = "光学模块")
    private String opticalModule;
    @ApiModelProperty(value = "出光非线性")
    private BigDecimal opticalNonlinear;
    @ApiModelProperty(value = "去拉力前，透射谱13db中心波长")
    private BigDecimal pullOnThro13Wl;
    @ApiModelProperty(value = "去拉力后，透射谱13db中心波长")
    private BigDecimal pullOffThro13Wl;
    @ApiModelProperty(value = "退火前，透射谱13db带宽")
    private BigDecimal annealThro13Bd;
    @ApiModelProperty(value = "退火前，透射谱深")
    private BigDecimal annealThroDp;
    @ApiModelProperty(value = "退火次数")
    private Integer annealTimes;
    @ApiModelProperty(value = "退火后，透射谱13db带宽")
    private BigDecimal annealedThro13Bd;
    @ApiModelProperty(value = "退火后，透射谱深")
    private BigDecimal annealedThroDp;
    @ApiModelProperty(value = "退火后，反射谱3db带宽")
    private BigDecimal annealedRe3Bd;
    @ApiModelProperty(value = "退火后，反射谱深")
    private BigDecimal annealedReDp;
    @ApiModelProperty(value = "刻写存图时间")
    private Date recordTime;
    @ApiModelProperty(value = "去氢后，透射谱13db中心波长")
    private BigDecimal hydrogenThro13Wl;
    @ApiModelProperty(value = "去氢后，透射谱13db带宽")
    private BigDecimal hydrogenThro13Bd;
    @ApiModelProperty(value = "去氢后，透射谱深")
    private BigDecimal hydrogenThroDp;
    @ApiModelProperty(value = "去氢后，透射谱3db中心波长")
    private BigDecimal hydrogenThro3Wl;
    @ApiModelProperty(value = "去氢后，反射谱3db带宽")
    private BigDecimal hydrogenRe3Bd;
    @ApiModelProperty(value = "去氢后，反射谱深")
    private BigDecimal hydrogenReDp;
    @ApiModelProperty(value = "测试存图时间")
    private Date testTime;
    @ApiModelProperty(value = "去拉力前，反射谱3db中心波长")
    private BigDecimal pullOnRe3Wl;
    @ApiModelProperty(value = "去拉力后，反射谱3db中心波长")
    private BigDecimal pullOffRe3Wl;
    @ApiModelProperty(value = "退火前，反射谱3db带宽")
    private BigDecimal annealRe3Bd;
    @ApiModelProperty(value = "退火前，反射谱深")
    private BigDecimal annealReDp;
    @ApiModelProperty(value = "退火后，反射谱3db中心波长")
    private BigDecimal annealedRe3Wl;
    @ApiModelProperty(value = "去氢后，反射谱3db中心波长")
    private BigDecimal hydrogenRe3Wl;
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
     * @return 工作类型RECORD/TEST（刻写/测试）
     */
    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    /**
     * @return 1080nm波峰
     */
    public BigDecimal getWaveform1080() {
        return waveform1080;
    }

    public void setWaveform1080(BigDecimal waveform1080) {
        this.waveform1080 = waveform1080;
    }

    /**
     * @return 1135nm波峰
     */
    public BigDecimal getWaveform1135() {
        return waveform1135;
    }

    public void setWaveform1135(BigDecimal waveform1135) {
        this.waveform1135 = waveform1135;
    }

    /**
     * @return 去拉力前，透射谱13db中心波长
     */
    public BigDecimal getPullOnThro13Wl() {
        return pullOnThro13Wl;
    }

    public void setPullOnThro13Wl(BigDecimal pullOnThro13Wl) {
        this.pullOnThro13Wl = pullOnThro13Wl;
    }

    /**
     * @return 去拉力后，透射谱13db中心波长
     */
    public BigDecimal getPullOffThro13Wl() {
        return pullOffThro13Wl;
    }

    public void setPullOffThro13Wl(BigDecimal pullOffThro13Wl) {
        this.pullOffThro13Wl = pullOffThro13Wl;
    }

    /**
     * @return 退火前，透射谱13db带宽
     */
    public BigDecimal getAnnealThro13Bd() {
        return annealThro13Bd;
    }

    public void setAnnealThro13Bd(BigDecimal annealThro13Bd) {
        this.annealThro13Bd = annealThro13Bd;
    }

    /**
     * @return 退火前，透射谱深
     */
    public BigDecimal getAnnealThroDp() {
        return annealThroDp;
    }

    public void setAnnealThroDp(BigDecimal annealThroDp) {
        this.annealThroDp = annealThroDp;
    }

    /**
     * @return 退火次数
     */
    public Integer getAnnealTimes() {
        return annealTimes;
    }

    public void setAnnealTimes(Integer annealTimes) {
        this.annealTimes = annealTimes;
    }

    /**
     * @return 退火后，透射谱13db带宽
     */
    public BigDecimal getAnnealedThro13Bd() {
        return annealedThro13Bd;
    }

    public void setAnnealedThro13Bd(BigDecimal annealedThro13Bd) {
        this.annealedThro13Bd = annealedThro13Bd;
    }

    /**
     * @return 退火后，透射谱深
     */
    public BigDecimal getAnnealedThroDp() {
        return annealedThroDp;
    }

    public void setAnnealedThroDp(BigDecimal annealedThroDp) {
        this.annealedThroDp = annealedThroDp;
    }

    /**
     * @return 退火后，反射谱3db带宽
     */
    public BigDecimal getAnnealedRe3Bd() {
        return annealedRe3Bd;
    }

    public void setAnnealedRe3Bd(BigDecimal annealedRe3Bd) {
        this.annealedRe3Bd = annealedRe3Bd;
    }

    /**
     * @return 退火后，反射谱深
     */
    public BigDecimal getAnnealedReDp() {
        return annealedReDp;
    }

    public void setAnnealedReDp(BigDecimal annealedReDp) {
        this.annealedReDp = annealedReDp;
    }

    /**
     * @return 刻写存图时间
     */
    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    /**
     * @return 去氢后，透射谱13db中心波长
     */
    public BigDecimal getHydrogenThro13Wl() {
        return hydrogenThro13Wl;
    }

    public void setHydrogenThro13Wl(BigDecimal hydrogenThro13Wl) {
        this.hydrogenThro13Wl = hydrogenThro13Wl;
    }

    /**
     * @return 去氢后，透射谱13db带宽
     */
    public BigDecimal getHydrogenThro13Bd() {
        return hydrogenThro13Bd;
    }

    public void setHydrogenThro13Bd(BigDecimal hydrogenThro13Bd) {
        this.hydrogenThro13Bd = hydrogenThro13Bd;
    }

    /**
     * @return 去氢后，透射谱深
     */
    public BigDecimal getHydrogenThroDp() {
        return hydrogenThroDp;
    }

    public void setHydrogenThroDp(BigDecimal hydrogenThroDp) {
        this.hydrogenThroDp = hydrogenThroDp;
    }

    /**
     * @return 去氢后，透射谱3db中心波长
     */
    public BigDecimal getHydrogenThro3Wl() {
        return hydrogenThro3Wl;
    }

    public void setHydrogenThro3Wl(BigDecimal hydrogenThro3Wl) {
        this.hydrogenThro3Wl = hydrogenThro3Wl;
    }

    /**
     * @return 去氢后，反射谱3db带宽
     */
    public BigDecimal getHydrogenRe3Bd() {
        return hydrogenRe3Bd;
    }

    public void setHydrogenRe3Bd(BigDecimal hydrogenRe3Bd) {
        this.hydrogenRe3Bd = hydrogenRe3Bd;
    }

    /**
     * @return 去氢后，反射谱深
     */
    public BigDecimal getHydrogenReDp() {
        return hydrogenReDp;
    }

    public void setHydrogenReDp(BigDecimal hydrogenReDp) {
        this.hydrogenReDp = hydrogenReDp;
    }

    /**
     * @return 测试存图时间
     */
    public Date getTestTime() {
        return testTime;
    }

    public void setTestTime(Date testTime) {
        this.testTime = testTime;
    }

    /**
     * @return 去拉力前，反射谱3db中心波长
     */
    public BigDecimal getPullOnRe3Wl() {
        return pullOnRe3Wl;
    }

    public void setPullOnRe3Wl(BigDecimal pullOnRe3Wl) {
        this.pullOnRe3Wl = pullOnRe3Wl;
    }

    /**
     * @return 去拉力后，反射谱3db中心波长
     */
    public BigDecimal getPullOffRe3Wl() {
        return pullOffRe3Wl;
    }

    public void setPullOffRe3Wl(BigDecimal pullOffRe3Wl) {
        this.pullOffRe3Wl = pullOffRe3Wl;
    }

    /**
     * @return 退火前，反射谱3db带宽
     */
    public BigDecimal getAnnealRe3Bd() {
        return annealRe3Bd;
    }

    public void setAnnealRe3Bd(BigDecimal annealRe3Bd) {
        this.annealRe3Bd = annealRe3Bd;
    }

    /**
     * @return 退火前，反射谱深
     */
    public BigDecimal getAnnealReDp() {
        return annealReDp;
    }

    public void setAnnealReDp(BigDecimal annealReDp) {
        this.annealReDp = annealReDp;
    }

    /**
     * @return 退火后，反射谱3db中心波长
     */
    public BigDecimal getAnnealedRe3Wl() {
        return annealedRe3Wl;
    }

    public void setAnnealedRe3Wl(BigDecimal annealedRe3Wl) {
        this.annealedRe3Wl = annealedRe3Wl;
    }

    /**
     * @return 去氢后，反射谱3db中心波长
     */
    public BigDecimal getHydrogenRe3Wl() {
        return hydrogenRe3Wl;
    }

    public void setHydrogenRe3Wl(BigDecimal hydrogenRe3Wl) {
        this.hydrogenRe3Wl = hydrogenRe3Wl;
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
     * @return 处理状态(N / P / E / S : 正常 / 处理中 / 错误 / 成功)
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

    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }

    public String getOpticalModule() {
        return opticalModule;
    }

    public void setOpticalModule(String opticalModule) {
        this.opticalModule = opticalModule;
    }

    public BigDecimal getOpticalNonlinear() {
        return opticalNonlinear;
    }

    public void setOpticalNonlinear(BigDecimal opticalNonlinear) {
        this.opticalNonlinear = opticalNonlinear;
    }
}
