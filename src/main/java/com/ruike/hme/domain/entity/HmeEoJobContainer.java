package com.ruike.hme.domain.entity;

import java.io.Serializable;
import java.util.Date;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hzero.mybatis.common.query.Where;

/**
 * 工序作业平台-容器
 *
 * @author liyuan.lv@hand-china.com 2020-03-23 12:48:53
 */
@ApiModel("工序作业平台-容器")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "hme_eo_job_container")
@CustomPrimary
public class HmeEoJobContainer extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_JOB_CONTAINER_ID = "jobContainerId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_CONTAINER_ID = "containerId";
    public static final String FIELD_CONTAINER_CODE = "containerCode";
    public static final String FIELD_SITE_IN_DATE = "siteInDate";
    public static final String FIELD_SITE_OUT_DATE = "siteOutDate";
    public static final String FIELD_SITE_IN_BY = "siteInBy";
    public static final String FIELD_SITE_OUT_BY = "siteOutBy";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String jobContainerId;
    @ApiModelProperty(value = "工位ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "容器ID")
    private String containerId;
    @ApiModelProperty(value = "容器条码")
    private String containerCode;
    @ApiModelProperty(value = "进站日期", required = true)
    @NotNull
    private Date siteInDate;
    @ApiModelProperty(value = "出站日期")
    private Date siteOutDate;
    @ApiModelProperty(value = "进站操作人ID", required = true)
    @NotNull
    private Long siteInBy;
    @ApiModelProperty(value = "出站操作人ID")
    @NotNull
    private Long siteOutBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    @Cid
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
