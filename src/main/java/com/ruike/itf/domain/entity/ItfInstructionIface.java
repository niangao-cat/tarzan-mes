package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import com.ruike.itf.api.dto.ItfInstructionRESBSyncDTO;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.math.BigDecimal;
import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

/**
 * 仓储物流指令内容表
 *
 * @author kejin.liu01@hand-china.com 2020-08-11 11:24:54
 */
@ApiModel("销售订单明细接口记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_instruction_iface")
@Data
@CustomPrimary
public class ItfInstructionIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INSTRUCTION_ID = "instructionId";
    public static final String FIELD_INSTRUCTION_NUM = "instructionNum";
    public static final String FIELD_INSTRUCTION_TYPE = "instructionType";
    public static final String FIELD_INSTRUCTION_STATUS = "instructionStatus";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_MATERIAL = "material";
    public static final String FIELD_UOM_CODE = "uomCode";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_RSNUM = "rsnum";
    public static final String FIELD_RSPOS = "rspos";
    public static final String FIELD_PO_NUMBER = "poNumber";
    public static final String FIELD_PO_ITEM = "poItem";
    public static final String FIELD_ERROR_MSG = "errorMsg";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键ID ,表示唯一一条记录")
    @Id
    @GeneratedValue
    private String instructionId;
    @ApiModelProperty(value = "单据编号")
    private String instructionNum;
    @ApiModelProperty(value = "供应商接收")
    private String instructionType;
    @ApiModelProperty(value = "状态（NEW，CANCEL，PRINTERD，RELEASED，COMPLETE，COMPLETE_CANCEL）")
    private String instructionStatus;
    @ApiModelProperty(value = "批次ID")
    private Long batchId;
    @ApiModelProperty(value = "批次插入时间")
    private String batchDate;
    @ApiModelProperty(value = "站点")
    private String siteCode;
    @ApiModelProperty(value = "物料")
    private String material;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "指令数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "预留/相关需求的编号")
    private String rsnum;
    @ApiModelProperty(value = "预留/相关需求的项目编号")
    private String rspos;
    @ApiModelProperty(value = "项目编号")
    private String poNumber;
    @ApiModelProperty(value = "项目行编号")
    private String poItem;
    @ApiModelProperty(value = "异常信息")
    private String errorMsg;
    private Long cid;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long objectVersionNumber;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long createdBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Date creationDate;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Long lastUpdatedBy;
    @ApiModelProperty(value = "", required = true)
    @NotNull
    private Date lastUpdateDate;

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


    public ItfInstructionIface(ItfInstructionRESBSyncDTO dto, Long tenantId, Long batchId, String batchDate,String errorMsg) {
        this.tenantId = tenantId;
        this.instructionNum = Strings.isEmpty(dto.getRSPOS()) ? null : dto.getRSPOS();
        this.instructionType = "RECEIVE_FROM_SUPPLIER";
        this.instructionStatus = "NEW";
        this.batchId = batchId;
        this.batchDate = batchDate;
        this.siteCode = Strings.isEmpty(dto.getWERKS()) ? null : dto.getWERKS();
        this.material = Strings.isEmpty(dto.getMATNR()) ? null : Long.valueOf(dto.getMATNR()).toString();
        this.uomCode = Strings.isEmpty(dto.getMEINS()) ? null : dto.getMEINS();
        this.quantity = Strings.isEmpty(dto.getBDMNG().toString()) ? null : dto.getBDMNG();
        this.rsnum = Strings.isEmpty(dto.getRSNUM()) ? null : dto.getRSNUM();
        this.rspos = Strings.isEmpty(dto.getRSPOS()) ? null : dto.getRSPOS();
        this.poNumber = Strings.isEmpty(dto.getPO_NUMBER()) ? null : dto.getPO_NUMBER();
        this.poItem = Strings.isEmpty(dto.getPO_ITEM()) ? null : dto.getPO_ITEM();
        this.errorMsg = Strings.isEmpty(errorMsg) ? null : errorMsg;
        this.attribute1 = Strings.isEmpty(dto.getLGORT()) ? null : dto.getLGORT();
    }
}
