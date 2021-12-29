package com.ruike.hme.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import com.ruike.hme.api.dto.HmeNcDisposePlatformDTO11;
import com.ruike.hme.domain.vo.HmeProcessNcDetailVO2;
import com.ruike.hme.domain.vo.HmeTagNcVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
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
 * 工序作业平台-SN作业
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 00:04:39
 */
@ApiModel("工序作业平台-SN作业")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "hme_eo_job_sn")
@CustomPrimary
public class HmeEoJobSn extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_JOB_ID = "jobId";
    public static final String FIELD_SITE_IN_DATE = "siteInDate";
    public static final String FIELD_SITE_OUT_DATE = "siteOutDate";
    public static final String FIELD_SHIFT_ID = "shiftId";
    public static final String FIELD_IN_SITE_BY = "inSiteBy";
    public static final String FIELD_OUT_SITE_BY = "outSiteBy";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_WORK_ORDER_ID = "workOrderId";
    public static final String FIELD_OPERATION_ID = "operationId";
    public static final String FIELD_SN_MATERIAL_ID = "snMaterialId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_SN_QTY = "snQty";
    public static final String FIELD_EO_ID = "eoId";
    public static final String FIELD_EO_STEP_ID = "eoStepId";
    public static final String FIELD_EO_STEP_NUM = "eoStepNum";
    public static final String FIELD_JOB_CONTAINER_ID = "jobContainerId";
    public static final String FIELD_SOURCE_JOB_ID = "sourceJobId";
    public static final String FIELD_REWORK_FLAG = "reworkFlag";
    public static final String FIELD_JOB_TYPE = "jobType";

    public static final String FIELD_CID = "cid";
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

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String jobId;
    @ApiModelProperty(value = "进站日期", required = true)
    @NotNull
    private Date siteInDate;
    @ApiModelProperty(value = "出站日期")
    private Date siteOutDate;
    @ApiModelProperty(value = "班次ID", required = true)
    private String shiftId;
    @ApiModelProperty(value = "进站人ID", required = true)
    @NotNull
    private Long siteInBy;
    @ApiModelProperty(value = "出站人ID")
    private Long siteOutBy;
    @ApiModelProperty(value = "工位ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    @ApiModelProperty(value = "SN在制品ID",required = true)
    @NotBlank
    private String snMaterialId;
    @ApiModelProperty(value = "条码ID",required = true)
    @NotBlank
    private String materialLotId;
    @ApiModelProperty(value = "SN数量")
    private BigDecimal snQty;
    @ApiModelProperty(value = "EO")
    private String eoId;
    @ApiModelProperty(value = "工艺步骤ID")
    private String eoStepId;
    @ApiModelProperty(value = "工艺步骤加工次数")
    private Integer eoStepNum;
    @ApiModelProperty(value = "作业容器ID")
    private String jobContainerId;
    @ApiModelProperty(value = "是否返修标识")
    private String reworkFlag;
    @ApiModelProperty(value = "作业平台类型")
    private String jobType;
    @ApiModelProperty(value = "来源容器ID")
    private String sourceContainerId;
    @ApiModelProperty(value = "来源作业ID")
    private String sourceJobId;
    @ApiModelProperty(value = "",required = true)
    @NotNull
    @Cid
    private Long cid;
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

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @ApiModelProperty("当前步骤识别码")
    @Transient
    private String currentStepName;
    @ApiModelProperty("下一步骤识别码")
    @Transient
    private String nextStepName;
    @ApiModelProperty("SN物料编码")
    @Transient
    private String snMaterialCode;
    @ApiModelProperty("SN物料名称")
    @Transient
    private String snMaterialName;
    @ApiModelProperty("工单号")
    @Transient
    private String workOrderNum;
    @ApiModelProperty("特殊要求")
    @Transient
    private String remark;
    @ApiModelProperty("错误编码")
    @Transient
    private String errorCode;
    @ApiModelProperty("错误消息")
    @Transient
    private String errorMessage;
    @ApiModelProperty("不良判定标识")
    @Transient
    private String tagNcFlag;
    @ApiModelProperty("不良判定")
    @Transient
    List<HmeTagNcVO> hmeTagNcVOList;

    @ApiModelProperty("完工事务明细")
    @Transient
    private List<WmsObjectTransactionResponseVO> woCompleteTransactionResponseVOList;

    @ApiModelProperty("报工事务明细")
    @Transient
    private List<WmsObjectTransactionResponseVO> woReportTransactionResponseVOList;

    @ApiModelProperty("工序不良")
    @Transient
    private HmeNcDisposePlatformDTO11 hmeNcDisposePlatformDTO;

    @ApiModelProperty("工序不良集合")
    @Transient
    List<HmeProcessNcDetailVO2> processNcDetailList;
}
