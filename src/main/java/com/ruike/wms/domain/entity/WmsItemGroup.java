package com.ruike.wms.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.mybatis.common.query.Where;

/**
 * 物料组表
 *
 * @author jiangling.zheng@hand-china.com 2020-07-17 10:44:41
 */
@ApiModel("物料组表")
@Data
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_item_group")
public class WmsItemGroup extends AuditDomain {

	public static final String FIELD_ITEM_GROUP_ID = "itemGroupId";
	public static final String FIELD_ITEM_GROUP_CODE = "itemGroupCode";
	public static final String FIELD_ITEM_GROUP_DESCRIPTION = "itemGroupDescription";
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


	@ApiModelProperty("物料组id，主键ID，标识唯一一条记录")
	@Id
	@Where
	private String itemGroupId;
	@ApiModelProperty(value = "物料组编码",required = true)
	@NotBlank
	private String itemGroupCode;
	@ApiModelProperty(value = "物料组描述")
	private String itemGroupDescription;
	@ApiModelProperty(value = "租户id",required = true)
	@NotNull
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

}
