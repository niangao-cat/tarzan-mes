package com.ruike.wms.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.domain.AuditDomain;
import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料全局替代关系表
 *
 * @author yapeng.yao@hand-china.com 2020-08-19 16:54:02
 */
@ApiModel("物料全局替代关系表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_material_substitute_rel")
@Data
public class WmsMaterialSubstituteRel extends AuditDomain {

	public static final String FIELD_TENANT_ID = "tenantId";
	public static final String FIELD_SUBSTITUTE_ID = "substituteId";
	public static final String FIELD_SEQUENCE = "sequence";
	public static final String FIELD_SITE_ID = "siteId";
	public static final String FIELD_PLANT = "plant";
	public static final String FIELD_SUBSTITUTE_GROUP = "substituteGroup";
	public static final String FIELD_MATERIAL_ID = "materialId";
	public static final String FIELD_MATERIAL_CODE = "materialCode";
	public static final String FIELD_MAIN_MATERIAL_ID = "mainMaterialId";
	public static final String FIELD_MAIN_MATERIAL_CODE = "mainMaterialCode";
	public static final String FIELD_START_DATE = "startDate";
	public static final String FIELD_END_DATE = "endDate";
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
	public static final String FIELD_BATCH_ID = "batchId";
	public static final String FIELD_CID = "cid";
	public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
	public static final String FIELD_CREATED_BY = "createdBy";
	public static final String FIELD_CREATION_DATE = "creationDate";
	public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
	public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";

	//
	// 业务方法(按public protected private顺序排列)
	// ------------------------------------------------------------------------------

	//
	// 数据库字段
	// ------------------------------------------------------------------------------


	@ApiModelProperty(value = "租户ID",required = true)
	@NotNull
	private Long tenantId;
	@ApiModelProperty("主键")
	@Id
	@GeneratedValue
	private String substituteId;
	@ApiModelProperty(value = "项目编号")
	private String sequence;
	@ApiModelProperty(value = "工厂标识")
	private String siteId;
	@ApiModelProperty(value = "工厂")
	private String plant;
	@ApiModelProperty(value = "替代组")
	private String substituteGroup;
	@ApiModelProperty(value = "物料标识")
	private String materialId;
	@ApiModelProperty(value = "物料编码")
	private String materialCode;
	@ApiModelProperty(value = "替代组主料标识")
	private String mainMaterialId;
	@ApiModelProperty(value = "替代组主料编码")
	private String mainMaterialCode;
	@ApiModelProperty(value = "可替换零件的有效开始日期 ")
	private Date startDate;
	@ApiModelProperty(value = "失效时间")
	private Date endDate;
	@ApiModelProperty(value = "备用字段1")
	private String attribute1;
	@ApiModelProperty(value = "备用字段2")
	private String attribute2;
	@ApiModelProperty(value = "备用字段3")
	private String attribute3;
	@ApiModelProperty(value = "备用字段4")
	private String attribute4;
	@ApiModelProperty(value = "备用字段5")
	private String attribute5;
	@ApiModelProperty(value = "备用字段6")
	private String attribute6;
	@ApiModelProperty(value = "备用字段7")
	private String attribute7;
	@ApiModelProperty(value = "备用字段8")
	private String attribute8;
	@ApiModelProperty(value = "备用字段9")
	private String attribute9;
	@ApiModelProperty(value = "备用字段10")
	private String attribute10;
	@ApiModelProperty(value = "备用字段11")
	private String attribute11;
	@ApiModelProperty(value = "备用字段12")
	private String attribute12;
	@ApiModelProperty(value = "备用字段13")
	private String attribute13;
	@ApiModelProperty(value = "备用字段14")
	private String attribute14;
	@ApiModelProperty(value = "备用字段15")
	private String attribute15;
	@ApiModelProperty(value = "数据批次ID")
	private BigDecimal batchId;
	@ApiModelProperty(value = "CID")
	private Long cid;
	@ApiModelProperty(value = "")
	private Long objectVersionNumber;
	@ApiModelProperty(value = "")
	private Long createdBy;
	@ApiModelProperty(value = "")
	private Date creationDate;
	@ApiModelProperty(value = "")
	private Long lastUpdatedBy;
	@ApiModelProperty(value = "")
	private Date lastUpdateDate;

}
