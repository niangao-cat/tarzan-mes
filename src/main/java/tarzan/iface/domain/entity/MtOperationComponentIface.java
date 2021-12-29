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
 * 工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@ApiModel("工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）")

@ModifyAudit

@Table(name = "mt_operation_component_iface")
@CustomPrimary
public class MtOperationComponentIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_ROUTER_OBJECT_TYPE = "routerObjectType";
    public static final String FIELD_ROUTER_CODE = "routerCode";
    public static final String FIELD_ROUTER_ALTERNATE = "routerAlternate";
    public static final String FIELD_BOM_OBJECT_TYPE = "bomObjectType";
    public static final String FIELD_BOM_CODE = "bomCode";
    public static final String FIELD_BOM_ALTERNATE = "bomAlternate";
    public static final String FIELD_LINE_NUM = "lineNum";
    public static final String FIELD_OPERATION_SEQ_NUM = "operationSeqNum";
    public static final String FIELD_COMPONENT_ITEM_ID = "componentItemId";
    public static final String FIELD_COMPONENT_ITEM_NUM = "componentItemNum";
    public static final String FIELD_BOM_USAGE = "bomUsage";
    public static final String FIELD_UOM = "uom";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_COMPONENT_START_DATE = "componentStartDate";
    public static final String FIELD_COMPONENT_END_DATE = "componentEndDate";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_UPDATE_METHOD = "updateMethod";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 3589494125577786072L;

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
    @ApiModelProperty(value = "工厂CODE", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "工艺类型")
    @Where
    private String routerObjectType;
    @ApiModelProperty(value = "（Oracle：把相同装配件相同替代项的router_id写入）")
    @Where
    private String routerCode;
    @ApiModelProperty(value = "（Oracle：把相同装配件相同替代项的alternate写入）")
    @Where
    private String routerAlternate;
    @ApiModelProperty(value = "BOM类型")
    @Where
    private String bomObjectType;
    @ApiModelProperty(value = "（Oracle：把相同装配件相同替代项的BOM_ID写入）")
    @Where
    private String bomCode;
    @ApiModelProperty(value = "（Oracle：把相同装配件相同替代项的alternate写入）")
    @Where
    private String bomAlternate;
    @ApiModelProperty(value = "行号")
    @Where
    private Long lineNum;
    @ApiModelProperty(value = "工序号", required = true)
    @NotBlank
    @Where
    private String operationSeqNum;
    @ApiModelProperty(value = "组件物料编码：oracle要将组件物料ID转化成code写入")
    @Where
    private String componentItemNum;
    @ApiModelProperty(value = "BOM用量")
    @Where
    private Double bomUsage;
    @ApiModelProperty(value = "组件物料单位")
    @Where
    private String uom;
    @ApiModelProperty(value = "oracle为空，SAP将删除标志转化为是否有效传入")
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "ERP组件开始日期", required = true)
    @NotNull
    @Where
    private Date componentStartDate;
    @ApiModelProperty(value = "ERP组件结束日期")
    @Where
    private Date componentEndDate;
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
    @ApiModelProperty(value = "更新方式（ 允许有两个值UPDATE，ALL)")
    @Where
    private String updateMethod;
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
    @Cid
    @Where
    private Long cid;

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
     * @return 工厂CODE
     */
    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    /**
     * @return 工艺类型
     */
    public String getRouterObjectType() {
        return routerObjectType;
    }

    public void setRouterObjectType(String routerObjectType) {
        this.routerObjectType = routerObjectType;
    }

    /**
     * @return （Oracle：把相同装配件相同替代项的router_id写入）
     */
    public String getRouterCode() {
        return routerCode;
    }

    public void setRouterCode(String routerCode) {
        this.routerCode = routerCode;
    }

    /**
     * @return （Oracle：把相同装配件相同替代项的alternate写入）
     */
    public String getRouterAlternate() {
        return routerAlternate;
    }

    public void setRouterAlternate(String routerAlternate) {
        this.routerAlternate = routerAlternate;
    }

    /**
     * @return BOM类型
     */
    public String getBomObjectType() {
        return bomObjectType;
    }

    public void setBomObjectType(String bomObjectType) {
        this.bomObjectType = bomObjectType;
    }

    /**
     * @return （Oracle：把相同装配件相同替代项的BOM_ID写入）
     */
    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }

    /**
     * @return （Oracle：把相同装配件相同替代项的alternate写入）
     */
    public String getBomAlternate() {
        return bomAlternate;
    }

    public void setBomAlternate(String bomAlternate) {
        this.bomAlternate = bomAlternate;
    }

    /**
     * @return 行号
     */
    public Long getLineNum() {
        return lineNum;
    }

    public void setLineNum(Long lineNum) {
        this.lineNum = lineNum;
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
     * @return 组件物料编码：oracle要将组件物料ID转化成code写入
     */
    public String getComponentItemNum() {
        return componentItemNum;
    }

    public void setComponentItemNum(String componentItemNum) {
        this.componentItemNum = componentItemNum;
    }

    /**
     * @return BOM用量
     */
    public Double getBomUsage() {
        return bomUsage;
    }

    public void setBomUsage(Double bomUsage) {
        this.bomUsage = bomUsage;
    }

    /**
     * @return 组件物料单位
     */
    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    /**
     * @return oracle为空，SAP将删除标志转化为是否有效传入
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return ERP组件开始日期
     */
    public Date getComponentStartDate() {
        if (componentStartDate != null) {
            return (Date) componentStartDate.clone();
        } else {
            return null;
        }
    }

    public void setComponentStartDate(Date componentStartDate) {
        if (componentStartDate == null) {
            this.componentStartDate = null;
        } else {
            this.componentStartDate = (Date) componentStartDate.clone();
        }
    }

    public Date getComponentEndDate() {
        if (componentEndDate != null) {
            return (Date) componentEndDate.clone();
        } else {
            return null;
        }
    }

    public void setComponentEndDate(Date componentEndDate) {
        if (componentEndDate == null) {
            this.componentEndDate = null;
        } else {
            this.componentEndDate = (Date) componentEndDate.clone();
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

    public String getUpdateMethod() {
        return updateMethod;
    }

    public void setUpdateMethod(String updateMethod) {
        this.updateMethod = updateMethod;
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

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
