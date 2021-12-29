package com.ruike.itf.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;


import com.ruike.itf.api.dto.ItfInstructionITEMSyncDTO;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 指令单据头表
 *
 * @author kejin.liu01@hand-china.com 2020-08-11 11:24:54
 */
@ApiModel("销售订单头接口记录表")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_instruction_doc_iface")
@Data
@CustomPrimary
public class ItfInstructionDocIface extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INSTRUCTION_DOC_ID = "instructionDocId";
    public static final String FIELD_INSTRUCTION_DOC_NUM = "instructionDocNum";
    public static final String FIELD_INSTRUCTION_DOC_TYPE = "instructionDocType";
    public static final String FIELD_INSTRUCTION_DOC_STATUS = "instructionDocStatus";
    public static final String FIELD_SITE_CODE = "siteCode";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_INSTRUCTION = "instruction";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_SAP_STATUS = "sapStatus";
    public static final String FIELD_INSTRUCTION_NUM = "instructionNum";
    public static final String FIELD_INSTRUCTION_TYPE = "instructionType";
    public static final String FIELD_INSTRUCTION_STATUS = "instructionStatus";
    public static final String FIELD_MATERIAL = "material";
    public static final String FIELD_UOM_CODE = "uomCode";
    public static final String FIELD_TO_LOCATOR_CODE = "toLocatorCode";
    public static final String FIELD_QUANTITY = "quantity";
    public static final String FIELD_DEMAND_TIME = "demandTime";
    public static final String FIELD_PO_ITEM = "poItem";
    public static final String FIELD_DELETE_IND_H = "deleteIndH";
    public static final String FIELD_DELETE_IND_I = "deleteIndI";
    public static final String FIELD_SD_DOC = "sdDoc";
    public static final String FIELD_SDOC_ITEM = "sdocItem";
    public static final String FIELD_PO_TYPE = "poType";
    public static final String FIELD_PO_DISTR_TYPE = "poDistrType";
    public static final String FIELD_ERROR_MSG = "errorMsg";
    public static final String FIELD_RETPO = "retpo";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("主键")
    @Id
    @GeneratedValue
    private String instructionDocId;
    @ApiModelProperty(value = "单据编号")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型（业务类型，由项目定义）")
    private String instructionDocType;
    @ApiModelProperty(value = "状态（NEW，CANCEL，PRINTERD，RELEASED，COMPLETE，COMPLETE_CANCEL）")
    private String instructionDocStatus;
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "供应商ID")
    private String supplierCode;
    @ApiModelProperty(value = "实际业务需要的单据编号")
    private String instruction;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "批次ID")
    private Long batchId;
    @ApiModelProperty(value = "批次插入时间")
    private String batchDate;
    @ApiModelProperty(value = "SAP状态")
    private String sapStatus;
    @ApiModelProperty(value = "单据编号")
    private String instructionNum;
    @ApiModelProperty(value = "供应商接收")
    private String instructionType;
    @ApiModelProperty(value = "指令状态")
    private String instructionStatus;
    @ApiModelProperty(value = "物料")
    private String material;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "目标库位")
    private String toLocatorCode;
    @ApiModelProperty(value = "指令数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "需求日期")
    private Date demandTime;
    @ApiModelProperty(value = "采购凭证的项目编号")
    private String poItem;
    @ApiModelProperty(value = "采购凭证头的删除标识")
    private String deleteIndH;
    @ApiModelProperty(value = "采购凭证行的删除标识")
    private String deleteIndI;
    @ApiModelProperty(value = "销售和分销凭证号")
    private String sdDoc;
    @ApiModelProperty(value = "销售凭证项目")
    private String sdocItem;
    @ApiModelProperty(value = "采购订单行类别")
    private String poType;
    @ApiModelProperty(value = "科目分配类别")
    private String poDistrType;
    @ApiModelProperty(value = "异常信息")
    private String errorMsg;
    @ApiModelProperty(value = "退货采购订单标识")
    private String retpo;
    @ApiModelProperty(value = "CID")
    private Long cid;
    @ApiModelProperty(value = "")
    private Long objectVersionNumber;
    @ApiModelProperty(value = "")
    private Long createdBy;
    @ApiModelProperty(value = "")
    private Date creationDate;
    @ApiModelProperty(value = "")
    private Long lastUpdatedBy;
    @ApiModelProperty(value = "")
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


    public ItfInstructionDocIface(ItfInstructionITEMSyncDTO dto, Long tenantId, Long batchId, String batchDate, String errorMsg) {
        this.tenantId = tenantId;
        this.instructionDocNum = Strings.isEmpty(dto.getPO_NUMBER()) ? null : dto.getPO_NUMBER();
        this.instructionDocType = Strings.isEmpty(dto.getDOC_TYPE()) ? null : dto.getDOC_TYPE();
        this.instructionDocStatus = "RELEASED";
        this.siteCode = Strings.isEmpty(dto.getPLANT()) ? null : dto.getPLANT();
        this.supplierCode = Strings.isEmpty(dto.getVENDOR()) ? null : dto.getVENDOR().replaceAll("^(0+)", "");
        this.remark = Strings.isEmpty(dto.getSTREET()) ? null : dto.getSTREET();
        this.sapStatus = Strings.isEmpty(dto.getSTATUS()) ? null : dto.getSTATUS();
        this.batchId = batchId;
        this.batchDate = batchDate;
        this.instructionNum = Strings.isEmpty(dto.getPO_ITEM()) ? null : dto.getPO_ITEM();
        this.instructionType = "RECEIVE_FROM_SUPPLIER";
        this.instructionStatus = "RELEASED";
        this.material = Strings.isEmpty(dto.getMATERIAL()) ? null : dto.getMATERIAL().replaceAll("^(0+)", "");
        this.uomCode = Strings.isEmpty(dto.getLMEIN()) ? null : dto.getLMEIN();
        this.toLocatorCode = Strings.isEmpty(dto.getSTORE_LOC()) ? null : dto.getSTORE_LOC();
        this.quantity = Objects.isNull(dto.getLAGMG()) ? null : dto.getLAGMG();
        this.demandTime = dto.getEINDT();
        this.poItem = Strings.isEmpty(dto.getPO_ITEM()) ? null : dto.getPO_ITEM();
        this.deleteIndH = Strings.isEmpty(dto.getDELETE_IND_H()) ? null : dto.getDELETE_IND_H();
        this.deleteIndI = Strings.isEmpty(dto.getDELETE_IND_I()) ? null : dto.getDELETE_IND_I();
        this.sdDoc = "0".equals(Strings.isEmpty(dto.getSD_DOC()) ? null : dto.getSD_DOC()) ? null : dto.getSD_DOC();
        this.sdocItem = "0".equals(Strings.isEmpty(dto.getSDOC_ITEM()) ? null : dto.getSDOC_ITEM()) ? null : dto.getSDOC_ITEM();
        this.poType = Strings.isEmpty(dto.getITEM_CAT()) ? null : dto.getITEM_CAT();
        this.poDistrType = Strings.isEmpty(dto.getACCTASSCAT()) ? null : dto.getACCTASSCAT();
        this.errorMsg = Strings.isEmpty(errorMsg) ? null : errorMsg;
        this.retpo = Strings.isEmpty(dto.getRETPO()) ? null : dto.getRETPO();
        this.attribute1 = Strings.isEmpty(dto.getUNIT()) ? null : dto.getUNIT();
        this.attribute2 = Objects.isNull(dto.getQUANTITY()) ? null : String.valueOf(dto.getQUANTITY());
        this.attribute3 = Strings.isEmpty(dto.getUMREN()) ? null : dto.getUMREN();
        this.attribute4 = Strings.isEmpty(dto.getUMREZ()) ? null : dto.getUMREZ();
        this.attribute5 = Strings.isEmpty(dto.getUMSON()) ? null : dto.getUMSON();
    }
}
