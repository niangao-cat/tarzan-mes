package tarzan.iface.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 成本中心
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:39:33
 */
@ApiModel("成本中心")
@ModifyAudit
@Table(name = "mt_costcenter")
@CustomPrimary
@Data
public class MtCostcenter extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_COSTCENTER_ID = "costcenterId";
    public static final String FIELD_COSTCENTER_CODE = "costcenterCode";
    public static final String FIELD_SITE_ID = "siteId";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DATE_FROM_TO = "dateFromTo";
    public static final String FIELD_DATE_END_TO = "dateEndTo";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_SOURCE_IDENTIFICATION_ID = "sourceIdentificationId";

    public static final String FIELD_COSTCENTER_TYPE = "costcenterType";
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
    private static final long serialVersionUID = -2817285094173937100L;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("唯一性主键标识")
    @Id
    @Where
    private String costcenterId;
    @ApiModelProperty(value = "成本中心(账户别名)", required = true)
    @NotBlank
    @Where
    private String costcenterCode;
    @ApiModelProperty(value = "站点ID", required = true)
    @NotBlank
    @Where
    private String siteId;
    @ApiModelProperty(value = "成本中心描述（账户别名描述）", required = true)
    @NotBlank
    @Where
    private String description;
    @ApiModelProperty(value = "生效时间", required = true)
    @NotNull
    @Where
    private Date dateFromTo;
    @ApiModelProperty(value = "失效时间")
    @Where
    private Date dateEndTo;
    @ApiModelProperty(value = "来源标识ID")
    @Where
    private Double sourceIdentificationId;

    @ApiModelProperty(value = "移动类型")
    @Where
    private String moveType;

    @ApiModelProperty(value = "移动原因")
    @Where
    private String moveReason;

    @Cid
    @Where
    private Long cid;

    @ApiModelProperty(value = "成本中心类型")
    private String costcenterType;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute1;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute2;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute3;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute4;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute5;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute6;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute7;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute8;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute9;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute10;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute11;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute12;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute13;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute14;
    @ApiModelProperty(value = "预留字段1,使用请修改备注")
    private String attribute15;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


}
