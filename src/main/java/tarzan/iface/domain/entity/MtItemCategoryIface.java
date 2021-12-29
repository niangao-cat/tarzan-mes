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
 * 物料类别数据接口
 *
 * @author mingjie.chen@hand-china.com 2019-09-09 17:05:24
 */
@ApiModel("物料类别数据接口")

@ModifyAudit

@Table(name = "mt_item_category_iface")
@CustomPrimary
public class MtItemCategoryIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
	public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_CATEGORY_SET_CODE = "categorySetCode";
    public static final String FIELD_CATEGORY_SET_DESCRIPTION = "categorySetDescription";
    public static final String FIELD_CATEGORY_CODE = "categoryCode";
    public static final String FIELD_CATEGORY_DESCRIPTION = "categoryDescription";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
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
	private static final long serialVersionUID = 9221797194475533440L;

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
	@ApiModelProperty(value = "工厂代码")
	@Where
	private String plantCode;
    @ApiModelProperty(value = "类别集代码（ Oracle 要将ID转换成代码）",required = true)
    @NotBlank
    @Where
    private String categorySetCode;
    @ApiModelProperty(value = "类别集描述",required = true)
    @NotBlank
    @Where
    private String categorySetDescription;
    @ApiModelProperty(value = "类别代码（ Oracle 要将ID转换成代码）",required = true)
    @NotBlank
    @Where
    private String categoryCode;
    @ApiModelProperty(value = "类别描述",required = true)
    @NotBlank
    @Where
    private String categoryDescription;
    @ApiModelProperty(value = "启用标识:Oracle的失效日期转换为是否有效的Y/N sap全部为Y",required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "ERP创建日期",required = true)
    @NotNull
    @Where
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人",required = true)
    @NotNull
    @Where
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人",required = true)
    @NotNull
    @Where
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期",required = true)
    @NotNull
    @Where
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "数据批次ID",required = true)
    @NotNull
    @Where
    private Double batchId;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P",required = true)
    @NotBlank
    @Where
    private String status;
   @ApiModelProperty(value = "数据处理消息返回")
    @Where
    private String message;
    @Cid
    @Where
    private Long cid;
   @ApiModelProperty(value = "")
    @Where
    private String attributeCategory;
   @ApiModelProperty(value = "")
    @Where
    private String attribute1;
   @ApiModelProperty(value = "")
    @Where
    private String attribute2;
   @ApiModelProperty(value = "")
    @Where
    private String attribute3;
   @ApiModelProperty(value = "")
    @Where
    private String attribute4;
   @ApiModelProperty(value = "")
    @Where
    private String attribute5;
   @ApiModelProperty(value = "")
    @Where
    private String attribute6;
   @ApiModelProperty(value = "")
    @Where
    private String attribute7;
   @ApiModelProperty(value = "")
    @Where
    private String attribute8;
   @ApiModelProperty(value = "")
    @Where
    private String attribute9;
   @ApiModelProperty(value = "")
    @Where
    private String attribute10;
   @ApiModelProperty(value = "")
    @Where
    private String attribute11;
   @ApiModelProperty(value = "")
    @Where
    private String attribute12;
   @ApiModelProperty(value = "")
    @Where
    private String attribute13;
   @ApiModelProperty(value = "")
    @Where
    private String attribute14;
   @ApiModelProperty(value = "")
    @Where
    private String attribute15;

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
	 * @return 工厂代码
	 */
	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	/**
     * @return 类别集代码（ Oracle 要将ID转换成代码）
     */
	public String getCategorySetCode() {
		return categorySetCode;
	}

	public void setCategorySetCode(String categorySetCode) {
		this.categorySetCode = categorySetCode;
	}
    /**
     * @return 类别集描述
     */
	public String getCategorySetDescription() {
		return categorySetDescription;
	}

	public void setCategorySetDescription(String categorySetDescription) {
		this.categorySetDescription = categorySetDescription;
	}
    /**
     * @return 类别代码（ Oracle 要将ID转换成代码）
     */
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
    /**
     * @return 类别描述
     */
	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
    /**
     * @return 启用标识:Oracle的失效日期转换为是否有效的Y/N sap全部为Y
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
