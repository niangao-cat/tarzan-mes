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
 * 标准工序接口表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@ApiModel("标准工序接口表")

@ModifyAudit

@Table(name = "mt_standard_operation_iface")
@CustomPrimary
public class MtStandardOperationIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_OPERATION_CODE = "operationCode";
    public static final String FIELD_OPERATION_DESCRIPTION = "operationDescription";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 7190323351908253384L;

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
    @ApiModelProperty(value = "工序CODE", required = true)
    @NotBlank
    @Where
    private String operationCode;
    @ApiModelProperty(value = "工序说明", required = true)
    @NotBlank
    @Where
    private String operationDescription;
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
     * @return 工序CODE
     */
    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
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
     * @return ERP创建日期
     */
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
