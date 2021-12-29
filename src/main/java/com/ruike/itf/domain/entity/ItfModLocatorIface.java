package com.ruike.itf.domain.entity;

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

/**
 * 仓库接口记录表
 *
 * @author kejin.liu01@hand-china.com 2020-09-07 15:05:17
 */
@ApiModel("仓库接口记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_mod_locator_iface")
@Data
public class ItfModLocatorIface extends AuditDomain {

    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_LGORT = "lgort";
    public static final String FIELD_LGOBE = "lgobe";
    public static final String FIELD_WERKS = "werks";
    public static final String FIELD_ZFLAG = "zflag";
    public static final String FIELD_ZMESSAGE = "zmessage";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_CID = "cid";
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

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private String ifaceId;
    @ApiModelProperty(value = "仓库编码")
    private String lgort;
    @ApiModelProperty(value = "仓库描述")
    private String lgobe;
    @ApiModelProperty(value = "工厂编码")
    private String werks;
    @ApiModelProperty(value = "是否成功")
    private String zflag;
    @ApiModelProperty(value = "信息")
    private String zmessage;
    @ApiModelProperty(value = "批次ID")
    private Long batchId;
    @ApiModelProperty(value = "CID", required = true)
    @NotBlank
    private String cid;
    @ApiModelProperty(value = "租户id", required = true)
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

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
