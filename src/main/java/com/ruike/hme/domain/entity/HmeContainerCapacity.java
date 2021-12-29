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
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 容器容量表
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-10 15:08:58
 */
@ApiModel("容器容量表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@CustomPrimary
@Table(name = "hme_container_capacity")
public class HmeContainerCapacity extends AuditDomain {

	public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CONTAINER_CAPACITY_ID = "containerCapacityId";
    public static final String FIELD_CONTAINER_TYPE_ID = "containerTypeId";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_COS_TYPE = "cosType";
    public static final String FIELD_CAPACITY = "capacity";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
	public static final String FIELD_LINE_NUM = "lineNum";
	public static final String FIELD_COLUMN_NUM = "columnNum";
    public static final String FIELD_ATTRIBUTE1 = "attribute1";
    public static final String FIELD_ATTRIBUTE2 = "attribute2";
    public static final String FIELD_ATTRIBUTE3 = "attribute3";
    public static final String FIELD_ATTRIBUTE4 = "attribute4";
    public static final String FIELD_ATTRIBUTE5 = "attribute5";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


	@ApiModelProperty(value = "租户ID",required = true)
	@NotNull
	private Long tenantId;
    @ApiModelProperty("表ID，主键")
    @Id
    private String containerCapacityId;
    @ApiModelProperty(value = "容器类型id",required = true)
    @NotBlank
    private String containerTypeId;
    @ApiModelProperty(value = "站点id")
    private String siteId;
    @ApiModelProperty(value = "工艺id")
    private String operationId;
    @ApiModelProperty(value = "COS类型")
    private String cosType;
    @ApiModelProperty(value = "芯片数")
    private Long capacity;
    @ApiModelProperty(value = "有效性",required = true)
    @NotBlank
    private String enableFlag;
	@ApiModelProperty(value = "行数")
	private Long lineNum;
	@ApiModelProperty(value = "列数")
	private Long columnNum;
    @ApiModelProperty(value = "装载规则")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "",required = true)
	@Cid
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
     * @return 表ID，主键
     */
	public String getContainerCapacityId() {
		return containerCapacityId;
	}

	public void setContainerCapacityId(String containerCapacityId) {
		this.containerCapacityId = containerCapacityId;
	}
    /**
     * @return 容器类型id
     */
	public String getContainerTypeId() {
		return containerTypeId;
	}

	public void setContainerTypeId(String containerTypeId) {
		this.containerTypeId = containerTypeId;
	}
    /**
     * @return 站点id
     */
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
    /**
     * @return 工艺id
     */
	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
    /**
     * @return COS类型
     */
	public String getCosType() {
		return cosType;
	}

	public void setCosType(String cosType) {
		this.cosType = cosType;
	}
    /**
     * @return 芯片数
     */
	public Long getCapacity() {
		return capacity;
	}

	public void setCapacity(Long capacity) {
		this.capacity = capacity;
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
	 * @return 行数
	 */
	public Long getLineNum() {
		return lineNum;
	}

	public void setLineNum(Long lineNum) {
		this.lineNum = lineNum;
	}
	/**
	 * @return 列数
	 */
	public Long getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(Long columnNum) {
		this.columnNum = columnNum;
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
	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

}
