package tarzan.iface.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 工艺路线接口表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@ApiModel("工艺路线接口表")

@ModifyAudit

@Table(name = "mt_routing_operation_iface")
@CustomPrimary
public class MtRoutingOperationIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_ROUTER_OBEJCT_TYPE = "routerObjectType";
    public static final String FIELD_ROUTER_OBJECT_CODE = "routerObjectCode";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_ROUTER_DESCRIPTION = "routerDescription";
    public static final String FIELD_ROUTING_SEQUENCE_ID = "routingSequenceId";
    public static final String FIELD_ROUTER_CODE = "routerCode";
    public static final String FIELD_ROUTER_START_DATE = "routerStartDate";
    public static final String FIELD_ROUTER_END_DATE = "routerEndDate";
    public static final String FIELD_ROUTER_STATUS = "routerStatus";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_ROUTING_ALTERNATE = "routingAlternate";
    public static final String FIELD_OPERATION_SEQ_NUM = "operationSeqNum";
    public static final String FIELD_STANDARD_OPERATION_CODE = "standardOperationCode";
    public static final String FIELD_OPERATION_DESCRIPTION = "operationDescription";
    public static final String FIELD_OPERATION_START_DATE = "operationStartDate";
    public static final String FIELD_OPERATION_END_DATE = "operationEndDate";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_UPDATE_METHOD = "updateMethod";
    public static final String FIELD_PROCESS_TIME = "processTime";
    public static final String FIELD_SPECIAL_INTRUCTION = "specialIntruction";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_HEAD_ATTRIBUTE1 = "headAttribute1";
    public static final String FIELD_HEAD_ATTRIBUTE2 = "headAttribute2";
    public static final String FIELD_HEAD_ATTRIBUTE3 = "headAttribute3";
    public static final String FIELD_HEAD_ATTRIBUTE4 = "headAttribute4";
    public static final String FIELD_HEAD_ATTRIBUTE5 = "headAttribute5";
    public static final String FIELD_HEAD_ATTRIBUTE6 = "headAttribute6";
    public static final String FIELD_HEAD_ATTRIBUTE7 = "headAttribute7";
    public static final String FIELD_HEAD_ATTRIBUTE8 = "headAttribute8";
    public static final String FIELD_HEAD_ATTRIBUTE9 = "headAttribute9";
    public static final String FIELD_HEAD_ATTRIBUTE10 = "headAttribute10";
    public static final String FIELD_HEAD_ATTRIBUTE11 = "headAttribute11";
    public static final String FIELD_HEAD_ATTRIBUTE12 = "headAttribute12";
    public static final String FIELD_HEAD_ATTRIBUTE13 = "headAttribute13";
    public static final String FIELD_HEAD_ATTRIBUTE14 = "headAttribute14";
    public static final String FIELD_HEAD_ATTRIBUTE15 = "headAttribute15";
    public static final String FIELD_LINE_ATTRIBUTE1 = "lineAttribute1";
    public static final String FIELD_LINE_ATTRIBUTE2 = "lineAttribute2";
    public static final String FIELD_LINE_ATTRIBUTE3 = "lineAttribute3";
    public static final String FIELD_LINE_ATTRIBUTE4 = "lineAttribute4";
    public static final String FIELD_LINE_ATTRIBUTE5 = "lineAttribute5";
    public static final String FIELD_LINE_ATTRIBUTE6 = "lineAttribute6";
    public static final String FIELD_LINE_ATTRIBUTE7 = "lineAttribute7";
    public static final String FIELD_LINE_ATTRIBUTE8 = "lineAttribute8";
    public static final String FIELD_LINE_ATTRIBUTE9 = "lineAttribute9";
    public static final String FIELD_LINE_ATTRIBUTE10 = "lineAttribute10";
    public static final String FIELD_LINE_ATTRIBUTE11 = "lineAttribute11";
    public static final String FIELD_LINE_ATTRIBUTE12 = "lineAttribute12";
    public static final String FIELD_LINE_ATTRIBUTE13 = "lineAttribute13";
    public static final String FIELD_LINE_ATTRIBUTE14 = "lineAttribute14";
    public static final String FIELD_LINE_ATTRIBUTE15 = "lineAttribute15";
    private static final long serialVersionUID = 5753010988071899508L;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    @Where
    private String ifaceId;
    @ApiModelProperty("对象类型")
    @Where
    private String routerObjectType;
    @ApiModelProperty("对象编码")
    @Where
    private String routerObjectCode;
    @ApiModelProperty(value = "工厂CODE", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "工艺路线描述 ", required = true)
    @NotBlank
    @Where
    private String routerDescription;
    @ApiModelProperty(value = "工艺路线代码", required = true)
    @NotBlank
    @Where
    private String routerCode;
    @ApiModelProperty(value = "生效日期", required = true)
    @NotNull
    @Where
    private Date routerStartDate;
    @ApiModelProperty(value = "失效日期")
    @Where
    private Date routerEndDate;
    @ApiModelProperty(value = "工艺路线状态")
    @Where
    private String routerStatus;
    @ApiModelProperty(value = "是否有效（SAP删除标记）")
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "替代项（考虑SAP将计数器放在此字段）")
    @Where
    private String routingAlternate;
    @ApiModelProperty(value = "工序号")
    @Where
    private String operationSeqNum;
    @ApiModelProperty(value = "标准工序代码 （Oracle为空）", required = true)
    @NotBlank
    @Where
    private String standardOperationCode;
    @ApiModelProperty(value = "工序说明")
    @Where
    private String operationDescription;
    @ApiModelProperty(value = "生效日期")
    @Where
    private Date operationStartDate;
    @ApiModelProperty(value = "失效日期")
    @Where
    private Date operationEndDate;
    @ApiModelProperty(value = "ERP创建日期", required = true)
    @NotNull
    @Where
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人", required = true)
    @NotNull
    @Where
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人", required = true)
    @NotNull
    @Where
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期", required = true)
    @NotNull
    @Where
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    @Where
    private Double batchId;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    @Where
    private String message;
    @ApiModelProperty(value = "更新方式（ 允许有两个值UPDATE，ALL)")
    @Where
    private String updateMethod;
    @ApiModelProperty(value = "工艺加工所需时间")
    @Where
    private Double processTime;
    @ApiModelProperty(value = "特殊指令")
    @Where
    private String specialIntruction;
    @Cid
    @Where
    private Long cid;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute1;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute2;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute3;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute4;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute5;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute6;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute7;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute8;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute9;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute10;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute11;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute12;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute13;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute14;
    @ApiModelProperty(value = "")
    @Where
    private String headAttribute15;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute1;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute2;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute3;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute4;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute5;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute6;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute7;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute8;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute9;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute10;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute11;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute12;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute13;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute14;
    @ApiModelProperty(value = "")
    @Where
    private String lineAttribute15;
    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
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
    public String getIfaceId() {
        return ifaceId;
    }

    public void setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
    }

    /**
     * @return 对象编码
     */
    public String getRouterObjectType() {
        return routerObjectType;
    }

    public void setRouterObjectType(String routerObjectType) {
        this.routerObjectType = routerObjectType;
    }

    /**
     * @return 对象类型
     */
    public String getRouterObjectCode() {
        return routerObjectCode;
    }

    public void setRouterObjectCode(String routerObjectCode) {
        this.routerObjectCode = routerObjectCode;
    }

    /**
     * @return 工厂CODE
     */
    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    /**
     * @return 工艺路线描述
     */
    public String getRouterDescription() {
        return routerDescription;
    }

    public void setRouterDescription(String routerDescription) {
        this.routerDescription = routerDescription;
    }

    /**
     * @return 工艺路线代码
     */
    public String getRouterCode() {
        return routerCode;
    }

    public void setRouterCode(String routerCode) {
        this.routerCode = routerCode;
    }

    /**
     * @return 生效日期
     */
    public Date getRouterStartDate() {
        if (routerStartDate != null) {
            return (Date) routerStartDate.clone();
        } else {
            return null;
        }
    }

    public void setRouterStartDate(Date routerStartDate) {
        if (routerStartDate == null) {
            this.routerStartDate = null;
        } else {
            this.routerStartDate = (Date) routerStartDate.clone();
        }
    }

    public Date getRouterEndDate() {
        if (routerEndDate != null) {
            return (Date) routerEndDate.clone();
        } else {
            return null;
        }
    }

    public void setRouterEndDate(Date routerEndDate) {
        if (routerEndDate == null) {
            this.routerEndDate = null;
        } else {
            this.routerEndDate = (Date) routerEndDate.clone();
        }
    }

    /**
     * @return 工艺路线状态
     */
    public String getRouterStatus() {
        return routerStatus;
    }

    public void setRouterStatus(String routerStatus) {
        this.routerStatus = routerStatus;
    }

    /**
     * @return 是否有效（SAP删除标记）
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 替代项（考虑SAP将计数器放在此字段）
     */
    public String getRoutingAlternate() {
        return routingAlternate;
    }

    public void setRoutingAlternate(String routingAlternate) {
        this.routingAlternate = routingAlternate;
    }

    /**
     * @return 工序号
     */
    public String getOperationSeqNum() {
        return operationSeqNum;
    }

    public void setOperationSeqNum(String operationSeqNum) {
        this.operationSeqNum = operationSeqNum;
    }

    /**
     * @return 标准工序代码 （Oracle为空）
     */
    public String getStandardOperationCode() {
        return standardOperationCode;
    }

    public void setStandardOperationCode(String standardOperationCode) {
        this.standardOperationCode = standardOperationCode;
    }

    /**
     * @return 工序说明
     */
    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    /**
     * @return 生效日期
     */
    public Date getOperationStartDate() {
        if (operationStartDate != null) {
            return (Date) operationStartDate.clone();
        } else {
            return null;
        }
    }

    public void setOperationStartDate(Date operationStartDate) {
        if (operationStartDate == null) {
            this.operationStartDate = null;
        } else {
            this.operationStartDate = (Date) operationStartDate.clone();
        }
    }

    public Date getOperationEndDate() {
        if (operationEndDate != null) {
            return (Date) operationEndDate.clone();
        } else {
            return null;
        }
    }

    public void setOperationEndDate(Date operationEndDate) {
        if (operationEndDate == null) {
            this.operationEndDate = null;
        } else {
            this.operationEndDate = (Date) operationEndDate.clone();
        }
    }

    public Date getErpCreationDate() {
        if (erpCreationDate != null) {
            return (Date) erpCreationDate.clone();
        } else {
            return null;
        }
    }

    public void setErpCreationDate(Date erpCreationDate) {
        if (erpCreationDate == null) {
            this.erpCreationDate = null;
        } else {
            this.erpCreationDate = (Date) erpCreationDate.clone();
        }
    }

    /**
     * @return ERP创建人
     */
    public Long getErpCreatedBy() {
        return erpCreatedBy;
    }

    public void setErpCreatedBy(Long erpCreatedBy) {
        this.erpCreatedBy = erpCreatedBy;
    }

    /**
     * @return ERP最后更新人
     */
    public Long getErpLastUpdatedBy() {
        return erpLastUpdatedBy;
    }

    public void setErpLastUpdatedBy(Long erpLastUpdatedBy) {
        this.erpLastUpdatedBy = erpLastUpdatedBy;
    }

    /**
     * @return ERP最后更新日期
     */
    public Date getErpLastUpdateDate() {
        if (erpLastUpdateDate != null) {
            return (Date) erpLastUpdateDate.clone();
        } else {
            return null;
        }
    }

    public void setErpLastUpdateDate(Date erpLastUpdateDate) {
        if (erpLastUpdateDate == null) {
            this.erpLastUpdateDate = null;
        } else {
            this.erpLastUpdateDate = (Date) erpLastUpdateDate.clone();
        }
    }

    /**
     * @return 数据批次ID
     */
    public Double getBatchId() {
        return batchId;
    }

    public void setBatchId(Double batchId) {
        this.batchId = batchId;
    }

    /**
     * @return 数据处理状态，初始为N，失败为E，成功S，处理中P
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 数据处理消息返回
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUpdateMethod() {
        return updateMethod;
    }

    public void setUpdateMethod(String updateMethod) {
        this.updateMethod = updateMethod;
    }

    public Double getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Double processTime) {
        this.processTime = processTime;
    }

    public String getSpecialIntruction() {
        return specialIntruction;
    }

    public void setSpecialIntruction(String specialIntruction) {
        this.specialIntruction = specialIntruction;
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

    public String getHeadAttribute1() {
        return headAttribute1;
    }

    public void setHeadAttribute1(String headAttribute1) {
        this.headAttribute1 = headAttribute1;
    }

    public String getHeadAttribute2() {
        return headAttribute2;
    }

    public void setHeadAttribute2(String headAttribute2) {
        this.headAttribute2 = headAttribute2;
    }

    public String getHeadAttribute3() {
        return headAttribute3;
    }

    public void setHeadAttribute3(String headAttribute3) {
        this.headAttribute3 = headAttribute3;
    }

    public String getHeadAttribute4() {
        return headAttribute4;
    }

    public void setHeadAttribute4(String headAttribute4) {
        this.headAttribute4 = headAttribute4;
    }

    public String getHeadAttribute5() {
        return headAttribute5;
    }

    public void setHeadAttribute5(String headAttribute5) {
        this.headAttribute5 = headAttribute5;
    }

    public String getHeadAttribute6() {
        return headAttribute6;
    }

    public void setHeadAttribute6(String headAttribute6) {
        this.headAttribute6 = headAttribute6;
    }

    public String getHeadAttribute7() {
        return headAttribute7;
    }

    public void setHeadAttribute7(String headAttribute7) {
        this.headAttribute7 = headAttribute7;
    }

    public String getHeadAttribute8() {
        return headAttribute8;
    }

    public void setHeadAttribute8(String headAttribute8) {
        this.headAttribute8 = headAttribute8;
    }

    public String getHeadAttribute9() {
        return headAttribute9;
    }

    public void setHeadAttribute9(String headAttribute9) {
        this.headAttribute9 = headAttribute9;
    }

    public String getHeadAttribute10() {
        return headAttribute10;
    }

    public void setHeadAttribute10(String headAttribute10) {
        this.headAttribute10 = headAttribute10;
    }

    public String getHeadAttribute11() {
        return headAttribute11;
    }

    public void setHeadAttribute11(String headAttribute11) {
        this.headAttribute11 = headAttribute11;
    }

    public String getHeadAttribute12() {
        return headAttribute12;
    }

    public void setHeadAttribute12(String headAttribute12) {
        this.headAttribute12 = headAttribute12;
    }

    public String getHeadAttribute13() {
        return headAttribute13;
    }

    public void setHeadAttribute13(String headAttribute13) {
        this.headAttribute13 = headAttribute13;
    }

    public String getHeadAttribute14() {
        return headAttribute14;
    }

    public void setHeadAttribute14(String headAttribute14) {
        this.headAttribute14 = headAttribute14;
    }

    public String getHeadAttribute15() {
        return headAttribute15;
    }

    public void setHeadAttribute15(String headAttribute15) {
        this.headAttribute15 = headAttribute15;
    }

    public String getLineAttribute1() {
        return lineAttribute1;
    }

    public void setLineAttribute1(String lineAttribute1) {
        this.lineAttribute1 = lineAttribute1;
    }

    public String getLineAttribute2() {
        return lineAttribute2;
    }

    public void setLineAttribute2(String lineAttribute2) {
        this.lineAttribute2 = lineAttribute2;
    }

    public String getLineAttribute3() {
        return lineAttribute3;
    }

    public void setLineAttribute3(String lineAttribute3) {
        this.lineAttribute3 = lineAttribute3;
    }

    public String getLineAttribute4() {
        return lineAttribute4;
    }

    public void setLineAttribute4(String lineAttribute4) {
        this.lineAttribute4 = lineAttribute4;
    }

    public String getLineAttribute5() {
        return lineAttribute5;
    }

    public void setLineAttribute5(String lineAttribute5) {
        this.lineAttribute5 = lineAttribute5;
    }

    public String getLineAttribute6() {
        return lineAttribute6;
    }

    public void setLineAttribute6(String lineAttribute6) {
        this.lineAttribute6 = lineAttribute6;
    }

    public String getLineAttribute7() {
        return lineAttribute7;
    }

    public void setLineAttribute7(String lineAttribute7) {
        this.lineAttribute7 = lineAttribute7;
    }

    public String getLineAttribute8() {
        return lineAttribute8;
    }

    public void setLineAttribute8(String lineAttribute8) {
        this.lineAttribute8 = lineAttribute8;
    }

    public String getLineAttribute9() {
        return lineAttribute9;
    }

    public void setLineAttribute9(String lineAttribute9) {
        this.lineAttribute9 = lineAttribute9;
    }

    public String getLineAttribute10() {
        return lineAttribute10;
    }

    public void setLineAttribute10(String lineAttribute10) {
        this.lineAttribute10 = lineAttribute10;
    }

    public String getLineAttribute11() {
        return lineAttribute11;
    }

    public void setLineAttribute11(String lineAttribute11) {
        this.lineAttribute11 = lineAttribute11;
    }

    public String getLineAttribute12() {
        return lineAttribute12;
    }

    public void setLineAttribute12(String lineAttribute12) {
        this.lineAttribute12 = lineAttribute12;
    }

    public String getLineAttribute13() {
        return lineAttribute13;
    }

    public void setLineAttribute13(String lineAttribute13) {
        this.lineAttribute13 = lineAttribute13;
    }

    public String getLineAttribute14() {
        return lineAttribute14;
    }

    public void setLineAttribute14(String lineAttribute14) {
        this.lineAttribute14 = lineAttribute14;
    }

    public String getLineAttribute15() {
        return lineAttribute15;
    }

    public void setLineAttribute15(String lineAttribute15) {
        this.lineAttribute15 = lineAttribute15;
    }
}
