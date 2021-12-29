package com.ruike.hme.domain.entity;

import java.util.List;
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
 * 异常收集组基础数据表
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:24
 */
@ApiModel("异常收集组基础数据表")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hme_exception_group")
@CustomPrimary
public class HmeExceptionGroup extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_EXCEPTION_GROUP_ID = "exceptionGroupId";
    public static final String FIELD_EXCEPTION_GROUP_CODE = "exceptionGroupCode";
    public static final String FIELD_EXCEPTION_GROUP_NAME = "exceptionGroupName";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_EXCEPTION_GROUP_TYPE = "exceptionGroupType";
    public static final String FIELD_SOURCE_GROUP_ID = "sourceGroupId";
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
    private String exceptionGroupId;
    @ApiModelProperty(value = "异常收集组编码", required = true)
    private String exceptionGroupCode;
    @ApiModelProperty(value = "异常收集组描述")
    private String exceptionGroupName;
    @ApiModelProperty(value = "是否有效", required = true)
    private String enableFlag;
    @ApiModelProperty(value = "异常收集组类型", required = true)
    private String exceptionGroupType;
    @ApiModelProperty(value = "来源收集组ID")
    private String sourceGroupId;
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
    @ApiModelProperty("异常组类型名称")
    private String exceptionGroupTypeName;

    @Transient
    @ApiModelProperty("创建人")
    private String createdUserName;

    @Transient
    @ApiModelProperty("修改人")
    private String lastUpdatedUserName;

    @Transient
    @ApiModelProperty("异常项列表")
    private List<HmeExcGroupAssign> hmeExcGroupAssignList;

    @Transient
    @ApiModelProperty("异常工序关系列表")
    private List<HmeExcGroupWkcAssign> hmeExcGroupWkcAssignList;
}
