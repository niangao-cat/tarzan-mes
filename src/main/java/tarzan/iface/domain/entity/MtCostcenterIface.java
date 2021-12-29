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
 * 成本中心数据接口
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@ApiModel("成本中心数据接口")

@ModifyAudit

@Table(name = "mt_costcenter_iface")
@CustomPrimary
@Data
public class MtCostcenterIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_COSTCENTER_CODE = "costcenterCode";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DATE_FROM_TO = "dateFromTo";
    public static final String FIELD_DATE_END_TO = "dateEndTo";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_DISPOSITION_ID = "dispositionId";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -6131478203608504145L;

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
    @ApiModelProperty("主键")
    @Id
    @Where
    private String ifaceId;
    @ApiModelProperty(value = "Oracle转换库存组织代码写入, SAP将公司代码转换成工厂代码写入", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "SAP：成本中心、Oracle账户别名", required = true)
    @NotBlank
    @Where
    private String costcenterCode;
    @ApiModelProperty(value = "SAP：成本中心描述、Oracle：账户别名描述", required = true)
    @NotBlank
    @Where
    private String description;
    @ApiModelProperty(value = "地点生效日期", required = true)
    @NotNull
    @Where
    private Date dateFromTo;
    @ApiModelProperty(value = "地点失效日期")
    @Where
    private Date dateEndTo;
    @ApiModelProperty(value = "ERP创建日期", required = true)
    @NotNull
    @Where
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人", required = true)
    @NotNull
    @Where
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人", required = true)
    @NotNull
    @Where
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期", required = true)
    @NotNull
    @Where
    private Date erpLastUpdateDate;
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    @Where
    private Double batchId;
    @ApiModelProperty(value = "账户别名ID")
    @Where
    private Double dispositionId;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P", required = true)
    @NotBlank
    @Where
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    @Where
    private String message;
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
