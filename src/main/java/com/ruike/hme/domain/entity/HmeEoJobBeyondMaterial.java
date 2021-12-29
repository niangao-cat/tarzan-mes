package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import java.math.BigDecimal;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hzero.mybatis.common.query.Where;

/**
 * 工序作业平台-计划外物料
 *
 * @author liyuan.lv@hand-china.com 2020-07-15 15:27:06
 */
@ApiModel("工序作业平台-计划外物料")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "hme_eo_job_beyond_material")
@CustomPrimary
public class HmeEoJobBeyondMaterial extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_BYD_MATERIAL_ID = "bydMaterialId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_TYPE = "materialType";
    public static final String FIELD_AVAILABLE_TIME = "availableTime";
    public static final String FIELD_UNIT_QTY = "unitQty";
    public static final String FIELD_ASSEMBLE_LOCATION = "assembleLocation";
    public static final String FIELD_IS_BEYOND = "isBeyond";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("表ID，主键")
    @Where
    private Long tenantId;
    @ApiModelProperty(value = "主键ID，标识唯一一条记录", required = true)
    @NotBlank
    @Where
    @Id
    private String bydMaterialId;
    @ApiModelProperty(value = "工位ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "物料ID", required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "物料类型", required = true)
    @NotBlank
    private String materialType;
    @ApiModelProperty(value = "时效")
    private String availableTime;
    @ApiModelProperty(value = "单位用量")
    private BigDecimal unitQty;
    @ApiModelProperty(value = "装配位置")
    private String assembleLocation;
    @ApiModelProperty(value = "计划外投料标识")
    private String isBeyond;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    @Cid
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty(value = "物料编码")
    @Transient
	private String materialCode;
	@ApiModelProperty(value = "物料描述")
    @Transient
	private String materialName;
}
