package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import java.math.BigDecimal;
import java.util.*;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报废记录表
 *
 * @author yuchao.wang@hand-china.com 2020-10-26 20:09:44
 */
@ApiModel("报废记录表")
@VersionAudit
@ModifyAudit
@CustomPrimary
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_cos_scrap")
public class HmeCosScrap extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COS_SCRAP_ID = "cosScrapId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_JOB_ID = "jobId";
    public static final String FIELD_DATE_TIME = "dateTime";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_DEFECT_COUNT = "defectCount";
    public static final String FIELD_NC_CODE_ID = "ncCodeId";
    public static final String FIELD_NC_TYPE = "ncType";
    public static final String FIELD_COMPONENT_MATERIAL_ID = "componentMaterialId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_LOAD_SEQUENCE = "loadSequence";
    public static final String FIELD_HOT_SINK_CODE = "hotSinkCode";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_WAFER_NUM = "waferNum";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_COMMENTS = "comments";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_ATTRIBUTE_CATEGORY = "attributeCategory";
    public static final String FIELD_ATTRIBUTE = "attribute1";
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


    @ApiModelProperty(value = "租户id",required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private String cosScrapId;
    @ApiModelProperty(value = "站点",required = true)
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "记录人")
    private Long userId;
    @ApiModelProperty(value = "EO_JOB_SN表主键")
    private String jobId;
    @ApiModelProperty(value = "报废时间",required = true)
    @NotNull
    private Date dateTime;
    @ApiModelProperty(value = "物料批")
    private String materialLotId;
    @ApiModelProperty(value = "缺陷数量")
    private BigDecimal defectCount;
    @ApiModelProperty(value = "不良代码ID")
    private String ncCodeId;
    @ApiModelProperty(value = "不良代码分类，缺陷/瑕疵/修复")
    private String ncType;
    @ApiModelProperty(value = "NC记录的组件")
    private String componentMaterialId;
    @ApiModelProperty(value = "工艺")
    private String operationId;
    @ApiModelProperty(value = "工作单元")
    private String workcellId;
    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;
    @ApiModelProperty(value = "热沉编号")
    private String hotSinkCode;
    @ApiModelProperty(value = "工单id")
    private String workOrderId;
    @ApiModelProperty(value = "")
    private String waferNum;
    @ApiModelProperty(value = "")
    private String cosType;
    @ApiModelProperty(value = "备注")
    private String comments;
    @ApiModelProperty(value = "CID",required = true)
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

	//
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

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
     * @return 主键
     */
	public String getCosScrapId() {
		return cosScrapId;
	}

	public void setCosScrapId(String cosScrapId) {
		this.cosScrapId = cosScrapId;
	}
    /**
     * @return 站点
     */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 记录人
     */
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
    /**
     * @return EO_JOB_SN表主键
     */
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
    /**
     * @return 报废时间
     */
	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
    /**
     * @return 物料批
     */
	public String getMaterialLotId() {
		return materialLotId;
	}

	public void setMaterialLotId(String materialLotId) {
		this.materialLotId = materialLotId;
	}
    /**
     * @return 缺陷数量
     */
	public BigDecimal getDefectCount() {
		return defectCount;
	}

	public void setDefectCount(BigDecimal defectCount) {
		this.defectCount = defectCount;
	}
    /**
     * @return 不良代码ID
     */
	public String getNcCodeId() {
		return ncCodeId;
	}

	public void setNcCodeId(String ncCodeId) {
		this.ncCodeId = ncCodeId;
	}
    /**
     * @return 不良代码分类，缺陷/瑕疵/修复
     */
	public String getNcType() {
		return ncType;
	}

	public void setNcType(String ncType) {
		this.ncType = ncType;
	}
    /**
     * @return NC记录的组件
     */
	public String getComponentMaterialId() {
		return componentMaterialId;
	}

	public void setComponentMaterialId(String componentMaterialId) {
		this.componentMaterialId = componentMaterialId;
	}
    /**
     * @return 工艺
     */
	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
    /**
     * @return 工作单元
     */
	public String getWorkcellId() {
		return workcellId;
	}

	public void setWorkcellId(String workcellId) {
		this.workcellId = workcellId;
	}
    /**
     * @return 芯片序列号
     */
	public String getLoadSequence() {
		return loadSequence;
	}

	public void setLoadSequence(String loadSequence) {
		this.loadSequence = loadSequence;
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
     * @return 工单id
     */
	public String getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(String workOrderId) {
		this.workOrderId = workOrderId;
	}
    /**
     * @return 
     */
	public String getWaferNum() {
		return waferNum;
	}

	public void setWaferNum(String waferNum) {
		this.waferNum = waferNum;
	}
    /**
     * @return 
     */
	public String getCosType() {
		return cosType;
	}

	public void setCosType(String cosType) {
		this.cosType = cosType;
	}
    /**
     * @return 备注
     */
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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
