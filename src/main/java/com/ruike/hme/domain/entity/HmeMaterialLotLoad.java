package com.ruike.hme.domain.entity;

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
import java.util.Date;

/**
 * 来料装载位置表
 *
 * @author wenzhang.yu@hand-china.com 2020-08-13 20:22:29
 */
@ApiModel("来料装载位置表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_material_lot_load")
@CustomPrimary
public class HmeMaterialLotLoad extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_MATERIAL_LOT_LOAD_ID = "materialLotLoadId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_LOAD_SEQUENCE = "loadSequence";
    public static final String FIELD_LOAD_ROW = "loadRow";
    public static final String FIELD_LOAD_COLUMN = "loadColumn";
    public static final String FIELD_COS_NUM = "cosNum";
    public static final String FIELD_HOT_SINK_CODE = "hotSinkCode";
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_SOURCE_MATERIAL_LOT_ID = "sourceMaterialLotId";
    public static final String FIELD_SOURCE_LOAD_ROW = "sourceLoadRow";
    public static final String FIELD_SOURCE_LOAD_COLUMN = "sourceLoadColumn";
	public static final String FIELD_TEST_DATE = "testDate";
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
	public static final String FIELD_ATTRIBUTE16 = "attribute16";
	public static final String FIELD_ATTRIBUTE17 = "attribute17";
	public static final String FIELD_ATTRIBUTE18 = "attribute18";
	public static final String FIELD_ATTRIBUTE19 = "attribute19";
	public static final String FIELD_ATTRIBUTE20 = "attribute20";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID，表示唯一一条记录")
    @Id
    private String materialLotLoadId;
    @ApiModelProperty(value = "物料批id",required = true)
    @NotBlank
    private String materialLotId;
   @ApiModelProperty(value = "行序号")    
    private String loadSequence;
   @ApiModelProperty(value = "行")    
    private Long loadRow;
   @ApiModelProperty(value = "列")    
    private Long loadColumn;
   @ApiModelProperty(value = "芯片数")    
    private Long cosNum;
   @ApiModelProperty(value = "热沉编号")    
    private String hotSinkCode;
	@ApiModelProperty(value = "状态")
	private String status;
   @ApiModelProperty(value = "来源物料批ID")    
    private String sourceMaterialLotId;
   @ApiModelProperty(value = "来源行")    
    private Long sourceLoadRow;
   @ApiModelProperty(value = "来源列")    
    private Long sourceLoadColumn;
	@ApiModelProperty(value = "测试时间")
	private Date testDate;
    @ApiModelProperty(value = "",required = true)
    @NotNull
	@Cid
    private Long cid;
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
     * @return 主键ID，表示唯一一条记录
     */
	public String getMaterialLotLoadId() {
		return materialLotLoadId;
	}

	public void setMaterialLotLoadId(String materialLotLoadId) {
		this.materialLotLoadId = materialLotLoadId;
	}
    /**
     * @return 物料批id
     */
	public String getMaterialLotId() {
		return materialLotId;
	}

	public void setMaterialLotId(String materialLotId) {
		this.materialLotId = materialLotId;
	}
    /**
     * @return 行序号
     */
	public String getLoadSequence() {
		return loadSequence;
	}

	public void setLoadSequence(String loadSequence) {
		this.loadSequence = loadSequence;
	}
    /**
     * @return 行
     */
	public Long getLoadRow() {
		return loadRow;
	}

	public void setLoadRow(Long loadRow) {
		this.loadRow = loadRow;
	}
    /**
     * @return 列
     */
	public Long getLoadColumn() {
		return loadColumn;
	}

	public void setLoadColumn(Long loadColumn) {
		this.loadColumn = loadColumn;
	}
    /**
     * @return 芯片数
     */
	public Long getCosNum() {
		return cosNum;
	}

	public void setCosNum(Long cosNum) {
		this.cosNum = cosNum;
	}
    /**
     * @return 热沉编号
     */
	public String getHotSinkCode() {
		return hotSinkCode;
	}

	public void setHotSinkCode(String hotSinkCode) {
		this.hotSinkCode = hotSinkCode;
	}
	/**
	 * @return 状态
	 */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    /**
     * @return 来源物料批ID
     */
	public String getSourceMaterialLotId() {
		return sourceMaterialLotId;
	}

	public void setSourceMaterialLotId(String sourceMaterialLotId) {
		this.sourceMaterialLotId = sourceMaterialLotId;
	}
    /**
     * @return 来源行
     */
	public Long getSourceLoadRow() {
		return sourceLoadRow;
	}

	public void setSourceLoadRow(Long sourceLoadRow) {
		this.sourceLoadRow = sourceLoadRow;
	}
    /**
     * @return 来源列
     */
	public Long getSourceLoadColumn() {
		return sourceLoadColumn;
	}

	public void setSourceLoadColumn(Long sourceLoadColumn) {
		this.sourceLoadColumn = sourceLoadColumn;
	}
    /**
     * @return 
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

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public String getAttribute16() {
		return attribute16;
	}

	public void setAttribute16(String attribute16) {
		this.attribute16 = attribute16;
	}

	public String getAttribute17() {
		return attribute17;
	}

	public void setAttribute17(String attribute17) {
		this.attribute17 = attribute17;
	}

	public String getAttribute18() {
		return attribute18;
	}

	public void setAttribute18(String attribute18) {
		this.attribute18 = attribute18;
	}

	public String getAttribute19() {
		return attribute19;
	}

	public void setAttribute19(String attribute19) {
		this.attribute19 = attribute19;
	}

	public String getAttribute20() {
		return attribute20;
	}

	public void setAttribute20(String attribute20) {
		this.attribute20 = attribute20;
	}
}
