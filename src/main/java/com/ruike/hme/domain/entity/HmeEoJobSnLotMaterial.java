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

import java.io.Serializable;
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
 * 工序作业平台-产品SN批次投料
 *
 * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
 */
@ApiModel("工序作业平台-产品SN批次投料")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "hme_eo_job_sn_lot_material")
@CustomPrimary
public class HmeEoJobSnLotMaterial extends AuditDomain implements Serializable {
    private static final long serialVersionUID = 4164593602010741939L;

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_JOB_MATERIAL_ID = "jobMaterialId";
    public static final String FIELD_LOT_MATERIAL_ID = "lotMaterialId";
    public static final String FIELD_TIME_MATERIAL_ID = "timeMaterialId";
    public static final String FIELD_MATERIAL_TYPE = "materialType";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_JOB_ID = "jobId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_RELEASE_QTY = "releaseQty";
    public static final String FIELD_IS_RELEASED = "isReleased";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_LOT_CODE = "lotCode";
    public static final String FIELD_PRODUCTION_VERSION = "productionVersion";
    public static final String FIELD_VIRTUAL_FLAG = "virtualFlag";
    public static final String FIELD_PARENT_MATERIAL_LOT_ID = "parentMaterialLotId";
    public static final String FIELD_REMAIN_QTY = "remainQty";
    public static final String FIELD_IS_SCRAP = "isScrap";
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

    @ApiModelProperty(value = "表ID，主键", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String jobMaterialId;
    @ApiModelProperty(value = "批次物料ID")
    private String lotMaterialId;
    @ApiModelProperty(value = "时效物料ID")
    private String timeMaterialId;
    @ApiModelProperty(value = "物料类型")
    private String materialType;
    @ApiModelProperty(value = "工位ID", required = true)
    @NotBlank
    private String workcellId;
    @ApiModelProperty(value = "工序作业ID")
    @NotBlank
    private String jobId;
    @ApiModelProperty(value = "组件物料ID", required = true)
    @NotBlank
    private String materialId;
    @ApiModelProperty(value = "组件条码", required = true)
    @NotBlank
    private String materialLotId;
    @ApiModelProperty(value = "条码投料量")
    private BigDecimal releaseQty;
    @ApiModelProperty(value = "是否已投")
    private Integer isReleased;
    @ApiModelProperty(value = "装配库位")
    private String locatorId;
    @ApiModelProperty(value = "库存批次")
    private String lotCode;
    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
    @ApiModelProperty(value = "虚拟机标识")
    private String virtualFlag;
    @ApiModelProperty(value = "上层虚拟件物料批ID")
    private String parentMaterialLotId;
    @ApiModelProperty(value = "条码剩余数量")
    private BigDecimal remainQty;
    @ApiModelProperty(value = "是否报废")
    private Integer isScrap;
    @ApiModelProperty(value = "", required = true)
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
    @Transient
    @ApiModelProperty(value = "条码消耗量")
    private BigDecimal costQty;
    @Transient
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @Transient
    @ApiModelProperty(value = "单位ID")
    private String primaryUomId;
    @Transient
    @ApiModelProperty(value = "单位编码")
    private String primaryUomCode;
    @Transient
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @Transient
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @Transient
    @ApiModelProperty(value = "库位编码")
    private String locatorCode;
    @Transient
    @ApiModelProperty(value = "区域库位ID")
    private String areaLocatorId;
    @Transient
    @ApiModelProperty(value = "区域库位编码")
    private String areaLocatorCode;
}
