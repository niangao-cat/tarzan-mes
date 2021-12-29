package com.ruike.itf.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
    * 成品出库指令信息接口表
    */
@ApiModel("成品出库指令信息接口表")
@Data
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_wcs_task_iface")
@CustomPrimary
public class ItfWcsTaskIface {
    @ApiModelProperty(value="主键")
    @Id
    private String ifaceId;
    @ApiModelProperty(value="租户ID")
    private Long tenantId;
    @ApiModelProperty(value="任务号，唯一性")
    private String taskNum;
    @ApiModelProperty(value="单据头id")
    private String docId;
    @ApiModelProperty(value="单据行id")
    private String docLineId;
    @ApiModelProperty(value="物料")
    private String materialCode;
    @ApiModelProperty(value="物料版本")
    private String materialVersion;
    @ApiModelProperty(value="数量")
    private String qty;
    @ApiModelProperty(value="销售订单号")
    private String soNum;
    @ApiModelProperty(value="销售订单行号")
    private String soLineNum;
    @ApiModelProperty(value="出口号")
    private String exitNum;
    @ApiModelProperty(value="仓库")
    private String warehouseCode;
    @ApiModelProperty(value="任务状态")
    private String taskStatus;
    @ApiModelProperty(value="数据处理状态")
    private String status;
    @ApiModelProperty(value="数据处理消息")
    private String message;
    @ApiModelProperty(value="CID")
    private Long cid;
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
}