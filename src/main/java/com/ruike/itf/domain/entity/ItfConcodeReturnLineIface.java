package com.ruike.itf.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 成品出入库容器信息返回行表接口表
 *
 * @author taowen.wang@hand-china.com 2021/6/30 13:40
 */
@ApiModel("成品出入库容器信息返回行表接口表")
@Data
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_concode_return_line_iface")
@CustomPrimary
public class ItfConcodeReturnLineIface extends AuditDomain {

    public static final String FIELD_LINE_ID = "lineId";
    public static final String FIELD_HEADER_ID = "headerId";
    public static final String FIELD_MATERIAL_LOT_CODE= "materialLotCode";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_CREATED_BY = "createdDy";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
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

    @ApiModelProperty(value="主键")
    @Id
    private String lineId;
    @ApiModelProperty(value="头表主键")
    private String headerId;
    @ApiModelProperty(value="SN清单")
    private String materialLotCode;
    @ApiModelProperty(value="")
    private Long cid;
    @ApiModelProperty(value="租户id")
    private Long tenantId;
    @ApiModelProperty(value="行版本号，用来处理锁")
    private Long objectVersionNumber;
    @ApiModelProperty(value="")
    private Date creationDate;
    @ApiModelProperty(value="")
    private Long createdBy;
    @ApiModelProperty(value="")
    private Long lastUpdatedBy;
    @ApiModelProperty(value="")
    private Date lastUpdateDate;
    @ApiModelProperty(value="")
    private String attributeCategory;
    @ApiModelProperty(value="")
    private String attribute1;
    @ApiModelProperty(value="")
    private String attribute2;
    @ApiModelProperty(value="")
    private String attribute3;
    @ApiModelProperty(value="")
    private String attribute4;
    @ApiModelProperty(value="")
    private String attribute5;
    @ApiModelProperty(value="")
    private String attribute6;
    @ApiModelProperty(value="")
    private String attribute7;
    @ApiModelProperty(value="")
    private String attribute8;
    @ApiModelProperty(value="")
    private String attribute9;
    @ApiModelProperty(value="")
    private String attribute10;
    @ApiModelProperty(value="")
    private String attribute11;
    @ApiModelProperty(value="")
    private String attribute12;
    @ApiModelProperty(value="")
    private String attribute13;
    @ApiModelProperty(value="")
    private String attribute14;
    @ApiModelProperty(value="")
    private String attribute15;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

}