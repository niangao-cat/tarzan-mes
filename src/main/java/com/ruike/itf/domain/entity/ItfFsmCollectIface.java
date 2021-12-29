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
 * 熔接机数据采集接口表
 *
 * @author yonghui.zhu@hand-china.com 2020-07-13 18:36:01
 */
@ApiModel("熔接机数据采集接口表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_fsm_collect_iface")
@CustomPrimary
public class ItfFsmCollectIface extends AuditDomain {

    //exchenge by wenzhnag 2002.08.11 在字段前加设备类
    //exchenge by wenzhnag 2002.08.11 取消全两个字段设备类
    public static final String FIELD_INTERFACE_ID = "interfaceId";
    //public static final String FIELD_FSM_ASSET_ENCODING = "fsmAssetEncoding";
    public static final String FIELD_ASSET_ENCODING = "assetEncoding";
    public static final String FIELD_EQUIPMENT_CATEGORY = "equipmentCategory";
    //public static final String FIELD_FSM_SN = "fsmSn";
    public static final String FIELD_SN = "sn";
    public static final String FIELD_FSM_FUSION_SPOT = "fsmFusionSpot";
    public static final String FIELD_FSM_FUSION_TIME = "fsmFusionTime";
    public static final String FIELD_FSM_DISCHARGE_POWER = "fsmDischargePower";
    public static final String FIELD_FSM_DISCHARGE_TIMES = "fsmDischargeTimes";
    public static final String FIELD_FSM_FUSION_LOSS = "fsmFusionLoss";
    public static final String FIELD_FSM_CUTANGLE_LEFT = "fsmCutangleLeft";
    public static final String FIELD_FSM_CUTANGLE_RIGHT = "fsmCutangleRight";
    public static final String FIELD_FSM_AXIAL_OFFSET = "fsmAxialOffset";
    public static final String FIELD_FSM_FIBER_SPACE = "fsmFiberSpace";
    public static final String FIELD_FSM_FIBER_LEFT = "fsmFiberLeft";
    public static final String FIELD_FSM_FIBER_RIGHT = "fsmFiberRight";
    public static final String FIELD_FSM_L_R = "fsmLR";
    public static final String FIELD_FSM_ERR_MSG = "fsmErrMsg";
    public static final String FIELD_PRIMARY_KEY = "primaryKey";
    public static final String FIELD_PROCESS_DATE = "processDate";
    public static final String FIELD_PROCESS_MESSAGE = "processMessage";
    public static final String FIELD_PROCESS_STATUS = "processStatus";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_FSM_ATTRIBUTE1 = "fsmAttribute1";
    public static final String FIELD_FSM_ATTRIBUTE2 = "fsmAttribute2";
    public static final String FIELD_FSM_ATTRIBUTE3 = "fsmAttribute3";
    public static final String FIELD_FSM_ATTRIBUTE4 = "fsmAttribute4";
    public static final String FIELD_FSM_ATTRIBUTE5 = "fsmAttribute5";
    public static final String FIELD_FSM_ATTRIBUTE6 = "fsmAttribute6";
    public static final String FIELD_FSM_ATTRIBUTE7 = "fsmAttribute7";
    public static final String FIELD_FSM_ATTRIBUTE8 = "fsmAttribute8";
    public static final String FIELD_FSM_ATTRIBUTE9 = "fsmAttribute9";
    public static final String FIELD_FSM_ATTRIBUTE10 = "fsmAttribute10";
    public static final String FIELD_FSM_ATTRIBUTE11 = "fsmAttribute11";
    public static final String FIELD_FSM_ATTRIBUTE12 = "fsmAttribute12";
    public static final String FIELD_FSM_ATTRIBUTE13 = "fsmAttribute13";
    public static final String FIELD_FSM_ATTRIBUTE14 = "fsmAttribute14";
    public static final String FIELD_FSM_ATTRIBUTE15 = "fsmAttribute15";

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
    @ApiModelProperty(value = "熔接点")
    private Integer fsmFusionSpot;
    @ApiModelProperty(value = "熔接时间(yyyy-MM-dd HH:mm:ss)")
    private Date fsmFusionTime;
    @ApiModelProperty(value = "放电功率")
    private BigDecimal fsmDischargePower;
    @ApiModelProperty(value = "放电次数")
    private Integer fsmDischargeTimes;
    @ApiModelProperty(value = "熔接损耗")
    private String fsmFusionLoss;
    @ApiModelProperty(value = "左光纤切割角")
    private BigDecimal fsmCutangleLeft;
    @ApiModelProperty(value = "右光纤切割角")
    private BigDecimal fsmCutangleRight;
    @ApiModelProperty(value = "轴向偏移")
    private BigDecimal fsmAxialOffset;
    @ApiModelProperty(value = "光纤端面间隔")
    private BigDecimal fsmFiberSpace;
    @ApiModelProperty(value = "左光纤端面")
    private String fsmFiberLeft;
    @ApiModelProperty(value = "右光纤端面")
    private String fsmFiberRight;
    @ApiModelProperty(value = "L-R角")
    private BigDecimal fsmLR;
    @ApiModelProperty(value = "异常报警信息")
    private String fsmErrMsg;
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
    private String fsmAttribute1;
    @ApiModelProperty(value = "")
    private String fsmAttribute2;
    @ApiModelProperty(value = "")
    private String fsmAttribute3;
    @ApiModelProperty(value = "")
    private String fsmAttribute4;
    @ApiModelProperty(value = "")
    private String fsmAttribute5;
    @ApiModelProperty(value = "")
    private String fsmAttribute6;
    @ApiModelProperty(value = "")
    private String fsmAttribute7;
    @ApiModelProperty(value = "")
    private String fsmAttribute8;
    @ApiModelProperty(value = "")
    private String fsmAttribute9;
    @ApiModelProperty(value = "")
    private String fsmAttribute10;
    @ApiModelProperty(value = "")
    private String fsmAttribute11;
    @ApiModelProperty(value = "")
    private String fsmAttribute12;
    @ApiModelProperty(value = "")
    private String fsmAttribute13;
    @ApiModelProperty(value = "")
    private String fsmAttribute14;
    @ApiModelProperty(value = "")
    private String fsmAttribute15;

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
     * @return 设备类别
     */
    public String getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(String equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
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
     * @return 熔接点
     */
    public Integer getFsmFusionSpot() {
        return fsmFusionSpot;
    }

    public void setFsmFusionSpot(Integer fsmFusionSpot) {
        this.fsmFusionSpot = fsmFusionSpot;
    }
    /**
     * @return 熔接时间(yyyy-MM-dd HH:mm:ss)
     */
    public Date getFsmFusionTime() {
        return fsmFusionTime;
    }

    public void setFsmFusionTime(Date fsmFusionTime) {
        this.fsmFusionTime = fsmFusionTime;
    }
    /**
     * @return 放电功率
     */
    public BigDecimal getFsmDischargePower() {
        return fsmDischargePower;
    }

    public void setFsmDischargePower(BigDecimal fsmDischargePower) {
        this.fsmDischargePower = fsmDischargePower;
    }
    /**
     * @return 放电次数
     */
    public Integer getFsmDischargeTimes() {
        return fsmDischargeTimes;
    }

    public void setFsmDischargeTimes(Integer fsmDischargeTimes) {
        this.fsmDischargeTimes = fsmDischargeTimes;
    }
    /**
     * @return 熔接损耗
     */
    public String getFsmFusionLoss() {
        return fsmFusionLoss;
    }

    public void setFsmFusionLoss(String fsmFusionLoss) {
        this.fsmFusionLoss = fsmFusionLoss;
    }
    /**
     * @return 左光纤切割角
     */
    public BigDecimal getFsmCutangleLeft() {
        return fsmCutangleLeft;
    }

    public void setFsmCutangleLeft(BigDecimal fsmCutangleLeft) {
        this.fsmCutangleLeft = fsmCutangleLeft;
    }
    /**
     * @return 右光纤切割角
     */
    public BigDecimal getFsmCutangleRight() {
        return fsmCutangleRight;
    }

    public void setFsmCutangleRight(BigDecimal fsmCutangleRight) {
        this.fsmCutangleRight = fsmCutangleRight;
    }
    /**
     * @return 轴向偏移
     */
    public BigDecimal getFsmAxialOffset() {
        return fsmAxialOffset;
    }

    public void setFsmAxialOffset(BigDecimal fsmAxialOffset) {
        this.fsmAxialOffset = fsmAxialOffset;
    }
    /**
     * @return 光纤端面间隔
     */
    public BigDecimal getFsmFiberSpace() {
        return fsmFiberSpace;
    }

    public void setFsmFiberSpace(BigDecimal fsmFiberSpace) {
        this.fsmFiberSpace = fsmFiberSpace;
    }
    /**
     * @return 左光纤端面
     */
    public String getFsmFiberLeft() {
        return fsmFiberLeft;
    }

    public void setFsmFiberLeft(String fsmFiberLeft) {
        this.fsmFiberLeft = fsmFiberLeft;
    }
    /**
     * @return 右光纤端面
     */
    public String getFsmFiberRight() {
        return fsmFiberRight;
    }

    public void setFsmFiberRight(String fsmFiberRight) {
        this.fsmFiberRight = fsmFiberRight;
    }
    /**
     * @return L-R角
     */
    public BigDecimal getFsmLR() {
        return fsmLR;
    }

    public void setFsmLR(BigDecimal fsmLR) {
        this.fsmLR = fsmLR;
    }
    /**
     * @return 异常报警信息
     */
    public String getFsmErrMsg() {
        return fsmErrMsg;
    }

    public void setFsmErrMsg(String fsmErrMsg) {
        this.fsmErrMsg = fsmErrMsg;
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
    public String getFsmAttribute1() {
        return fsmAttribute1;
    }

    public void setFsmAttribute1(String fsmAttribute1) {
        this.fsmAttribute1 = fsmAttribute1;
    }
    /**
     * @return
     */
    public String getFsmAttribute2() {
        return fsmAttribute2;
    }

    public void setFsmAttribute2(String fsmAttribute2) {
        this.fsmAttribute2 = fsmAttribute2;
    }
    /**
     * @return
     */
    public String getFsmAttribute3() {
        return fsmAttribute3;
    }

    public void setFsmAttribute3(String fsmAttribute3) {
        this.fsmAttribute3 = fsmAttribute3;
    }
    /**
     * @return
     */
    public String getFsmAttribute4() {
        return fsmAttribute4;
    }

    public void setFsmAttribute4(String fsmAttribute4) {
        this.fsmAttribute4 = fsmAttribute4;
    }
    /**
     * @return
     */
    public String getFsmAttribute5() {
        return fsmAttribute5;
    }

    public void setFsmAttribute5(String fsmAttribute5) {
        this.fsmAttribute5 = fsmAttribute5;
    }
    /**
     * @return
     */
    public String getFsmAttribute6() {
        return fsmAttribute6;
    }

    public void setFsmAttribute6(String fsmAttribute6) {
        this.fsmAttribute6 = fsmAttribute6;
    }
    /**
     * @return
     */
    public String getFsmAttribute7() {
        return fsmAttribute7;
    }

    public void setFsmAttribute7(String fsmAttribute7) {
        this.fsmAttribute7 = fsmAttribute7;
    }
    /**
     * @return
     */
    public String getFsmAttribute8() {
        return fsmAttribute8;
    }

    public void setFsmAttribute8(String fsmAttribute8) {
        this.fsmAttribute8 = fsmAttribute8;
    }
    /**
     * @return
     */
    public String getFsmAttribute9() {
        return fsmAttribute9;
    }

    public void setFsmAttribute9(String fsmAttribute9) {
        this.fsmAttribute9 = fsmAttribute9;
    }
    /**
     * @return
     */
    public String getFsmAttribute10() {
        return fsmAttribute10;
    }

    public void setFsmAttribute10(String fsmAttribute10) {
        this.fsmAttribute10 = fsmAttribute10;
    }
    /**
     * @return
     */
    public String getFsmAttribute11() {
        return fsmAttribute11;
    }

    public void setFsmAttribute11(String fsmAttribute11) {
        this.fsmAttribute11 = fsmAttribute11;
    }
    /**
     * @return
     */
    public String getFsmAttribute12() {
        return fsmAttribute12;
    }

    public void setFsmAttribute12(String fsmAttribute12) {
        this.fsmAttribute12 = fsmAttribute12;
    }
    /**
     * @return
     */
    public String getFsmAttribute13() {
        return fsmAttribute13;
    }

    public void setFsmAttribute13(String fsmAttribute13) {
        this.fsmAttribute13 = fsmAttribute13;
    }
    /**
     * @return
     */
    public String getFsmAttribute14() {
        return fsmAttribute14;
    }

    public void setFsmAttribute14(String fsmAttribute14) {
        this.fsmAttribute14 = fsmAttribute14;
    }
    /**
     * @return
     */
    public String getFsmAttribute15() {
        return fsmAttribute15;
    }

    public void setFsmAttribute15(String fsmAttribute15) {
        this.fsmAttribute15 = fsmAttribute15;
    }



}
