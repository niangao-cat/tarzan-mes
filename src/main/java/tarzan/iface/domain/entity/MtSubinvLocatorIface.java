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
 * ERP货位接口表
 *
 * @author guichuan.li@hand-china.com 2019-09-24 10:33:46
 */
@ApiModel("ERP货位接口表")

@ModifyAudit

@Table(name = "mt_subinv_locator_iface")
@CustomPrimary
public class MtSubinvLocatorIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_SUBINV_CODE = "subinvCode";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_LOCATOR_CODE = "locatorCode";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
	private static final long serialVersionUID = -2345415545808884646L;

	//
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    @Where
    private String ifaceId;
    @ApiModelProperty(value = "工厂代码（EBS将ID转换成code写入)",required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "ERP库存地点代码",required = true)
    @NotBlank
    @Where
    private String subinvCode;
   @ApiModelProperty(value = "ERP货位ID")    
    @Where
    private String locatorId;
    @ApiModelProperty(value = "ERP仓库货位",required = true)
    @NotBlank
    @Where
    private String locatorCode;
   @ApiModelProperty(value = "有效性")    
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "ERP创建日期",required = true)
    @NotNull
    @Where
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人",required = true)
    @NotBlank
    @Where
    private String erpCreatedBy;
    @ApiModelProperty(value = "ERP更新日期",required = true)
    @NotNull
    @Where
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "批次id",required = true)
    @NotBlank
    @Where
    private Double batchId;
    @ApiModelProperty(value = "处理状态(N, 未处理，E处理错误，,S成功 ,P 处理中)",required = true)
    @NotBlank
    @Where
    private String status;
   @ApiModelProperty(value = "处理消息")    
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
     * @return 工厂代码（EBS将ID转换成code写入)
     */
	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
    /**
     * @return ERP库存地点代码
     */
	public String getSubinvCode() {
		return subinvCode;
	}

	public void setSubinvCode(String subinvCode) {
		this.subinvCode = subinvCode;
	}
    /**
     * @return ERP货位ID
     */
	public String getLocatorId() {
		return locatorId;
	}

	public void setLocatorId(String locatorId) {
		this.locatorId = locatorId;
	}
    /**
     * @return ERP仓库货位
     */
	public String getLocatorCode() {
		return locatorCode;
	}

	public void setLocatorCode(String locatorCode) {
		this.locatorCode = locatorCode;
	}
    /**
     * @return 有效性
     */
	public String getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
	}
    /**
     * @return ERP创建日期
     */
	public Date getErpCreationDate() {
		if (erpCreationDate == null) {
			return null;
		} else {
			return (Date) erpCreationDate.clone();
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
	public String getErpCreatedBy() {
		return erpCreatedBy;
	}

	public void setErpCreatedBy(String erpCreatedBy) {
		this.erpCreatedBy = erpCreatedBy;
	}
    /**
     * @return ERP更新日期
     */
	public Date getErpLastUpdateDate() {
		if (erpLastUpdateDate == null) {
			return null;
		} else {
			return (Date) erpLastUpdateDate.clone();
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
     * @return 批次id
     */
	public Double getBatchId() {
		return batchId;
	}

	public void setBatchId(Double batchId) {
		this.batchId = batchId;
	}

	/**
     * @return 处理状态(N, 未处理，E处理错误，,S成功 ,P 处理中)
     */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    /**
     * @return 处理消息
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
