package com.ruike.hme.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

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
 * 异常收集组分配WKC关系表
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
@ApiModel("异常收集组分配WKC关系表")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_exc_group_wkc_assign")
@CustomPrimary
public class HmeExcGroupWkcAssign extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EXC_GROUP_WKC_ASSIGN_ID = "excGroupWkcAssignId";
    public static final String FIELD_EXCEPTION_GROUP_ID = "exceptionGroupId";
    public static final String FIELD_SERIAL_NUMBER = "serialNumber";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
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

    @ApiModelProperty("租户id")
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty(value = "主键ID，标识唯一一条记录", required = true)
    @NotBlank
    @Id
    @Where
    private String excGroupWkcAssignId;
    @ApiModelProperty(value = "异常收集组id")
    private String exceptionGroupId;
    @ApiModelProperty(value = "序号")
    private Long serialNumber;
    @ApiModelProperty(value = "工序工位id")
    private String workcellId;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
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

    @Transient
    @ApiModelProperty("工序工位编码")
    private String workcellCode;
    @Transient
    @ApiModelProperty("工序工位名称")
    private String workcellName;
    @Transient
    @ApiModelProperty("创建人")
    private String createdUserName;

    @Transient
    @ApiModelProperty("修改人")
    private String lastUpdatedUserName;
}
