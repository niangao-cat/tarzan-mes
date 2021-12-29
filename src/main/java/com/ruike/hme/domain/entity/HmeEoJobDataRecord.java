package com.ruike.hme.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hzero.mybatis.common.query.Where;

/**
 * 工序作业平台-数据采集
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:48:09
 */
@ApiModel("工序作业平台-数据采集")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "hme_eo_job_data_record")
@CustomPrimary
public class HmeEoJobDataRecord extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_JOB_RECORD_ID = "jobRecordId";
    public static final String FIELD_JOB_ID = "jobId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_TAG_GROUP_ID = "tagGroupId";
    public static final String FIELD_TAG_ID = "tagId";
    public static final String FIELD_MINIMUM_VALUE = "minimumValue";
    public static final String FIELD_MAXIMAL_VALUE = "maximalValue";
    public static final String FIELD_GROUP_PURPOSE = "groupPurpose";
    public static final String FIELD_RESULT = "result";
    public static final String FIELD_DATA_RECORD_ID = "dataRecordId";
    public static final String FIELD_IS_SUPPLEMENT = "isSupplement";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "表ID，主键", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String jobRecordId;
    @ApiModelProperty(value = "工位ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "EO")
    private String eoId;
    @ApiModelProperty(value = "SN作业ID", required = true)
    @NotBlank
    private String jobId;
    @ApiModelProperty(value = "数据采集组ID", required = true)
    @NotBlank
    private String tagGroupId;
    @ApiModelProperty(value = "数据采集项ID", required = true)
    @NotBlank
    private String tagId;
    @ApiModelProperty(value = "下限值")
    private BigDecimal minimumValue;
    @ApiModelProperty(value = "上限值")
    private BigDecimal maximalValue;
    @ApiModelProperty(value = "采集组用途", required = true)
    @NotBlank
    private String groupPurpose;
    @ApiModelProperty(value = "采集结果")
    private String result;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "数据采集实绩ID")
    private String dataRecordId;
    @ApiModelProperty(value = "补充数据采集标识")
    private String isSupplement;
    @NotNull
    @Cid
    private Long cid;
    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
}
