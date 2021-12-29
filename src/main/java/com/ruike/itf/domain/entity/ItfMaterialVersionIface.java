package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import com.ruike.itf.api.dto.ItfMaterialVersionSyncDTO;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 物料版本接口记录表
 *
 * @author kejin.liu01@hand-china.com 2020-09-28 15:14:35
 */
@ApiModel("物料版本接口记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_material_version_iface")
@Data
public class ItfMaterialVersionIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_MATERIAL_VERSION = "materialVersion";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_DATE_FROM = "dateFrom";
    public static final String FIELD_DATE_TO = "dateTo";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
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

//
// 业务方法(按public protected private顺序排列)
// ------------------------------------------------------------------------------

//
// 数据库字段
// ------------------------------------------------------------------------------


    @ApiModelProperty("租户ID")
    @Id
    @GeneratedValue
    private Long tenantId;
    @ApiModelProperty(value = "主键ID，表示唯一一条记录", required = true)
    @NotBlank
    private String ifaceId;
    @ApiModelProperty(value = "关系ID")
    private String materialCode;
    @ApiModelProperty(value = "工厂ID")
    private String materialVersion;
    @ApiModelProperty(value = "工单编码")
    private String description;
    @ApiModelProperty(value = "SN编号")
    private String siteCode;
    @ApiModelProperty(value = "重试次数")
    private Date dateFrom;
    @ApiModelProperty(value = "数据批次ID")
    private Date dateTo;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P")
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    private String message;
    @ApiModelProperty(value = "", required = true)
    @NotNull
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

    public ItfMaterialVersionIface(Long tenantId, String ifaceId, ItfMaterialVersionSyncDTO dto, Date date) {
        this.tenantId = tenantId;
        this.ifaceId = ifaceId;
        this.materialCode = dto.getMATNR().replaceAll("^(0+)", "");
        this.materialVersion = dto.getVERID();
        this.description = dto.getTEXT1();
        this.siteCode = dto.getWERKS();
        this.dateFrom = dto.getBDATU();
        this.dateTo = dto.getADATU();
        this.setCreatedBy(-1L);
        this.setLastUpdatedBy(-1L);
        this.setCreationDate(date);
        this.setLastUpdateDate(date);
        this.cid = -1L;
    }

    public ItfMaterialVersionIface() {
    }
}