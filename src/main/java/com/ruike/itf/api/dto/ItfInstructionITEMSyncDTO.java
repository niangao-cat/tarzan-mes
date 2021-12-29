package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购订单头行DTO
 *
 * @author kejin.liu01@hand-china.com 2020/8/12 9:18
 */
@Data
public class ItfInstructionITEMSyncDTO {
    // 生产订单接口
    @ApiModelProperty(value = "")
    private String PUR_MAT;

    @ApiModelProperty(value = "retpo")
    private String RETPO;

    @ApiModelProperty(value = "站点")
    private String PLANT;

    @ApiModelProperty(value = "指令数量")
    private BigDecimal QUANTITY;

    @ApiModelProperty(value = "")
    private String CO_CODE;

    @ApiModelProperty(value = "")
    private String PURCH_ORG;

    @ApiModelProperty(value = "")
    private String ORDER_NO;

    @ApiModelProperty(value = "")
    private String TEL_NUMBER;

    @ApiModelProperty(value = "指令编号,采购凭证的项目编号")
    private String PO_ITEM;

    @ApiModelProperty(value = "单据类型")
    private String DOC_TYPE;

    @ApiModelProperty(value = "SAP状态")
    private String STATUS;

    @ApiModelProperty(value = "销售凭证项目")
    private String SDOC_ITEM;

    @ApiModelProperty(value = "")
    private String BUILDING;

    @ApiModelProperty(value = "需求日期")
    private Date EINDT;

    @ApiModelProperty(value = "")
    private String REL_STATUS;

    @ApiModelProperty(value = "")
    private String CNTRL_IND;

    @ApiModelProperty(value = "")
    private String DOC_CAT;

    @ApiModelProperty(value = "")
    private String PUR_GROUP;

    @ApiModelProperty(value = "")
    private String DELIV_QTY;

    @ApiModelProperty(value = "物料")
    private String MATERIAL;

    @ApiModelProperty(value = "")
    private String SCHED_LINE;

    @ApiModelProperty(value = "")
    private String CREATED_BY;

    @ApiModelProperty(value = "")
    private String DEL_COMPL;

    @ApiModelProperty(value = "")
    private String PREQ_NO;

    @ApiModelProperty(value = "")
    private String REL_IND;

    @ApiModelProperty(value = "")
    private String ITEM_CAT;

    @ApiModelProperty(value = "")
    private String PREQ_ITEM;

    @ApiModelProperty(value = "单位")
    private String UNIT;

    @ApiModelProperty(value = "")
    private String ACCTASSCAT;

    @ApiModelProperty(value = "")
    private String COST_CTR;

    @ApiModelProperty(value = "销售和分销凭证号")
    private String SD_DOC;

    @ApiModelProperty(value = "供应商")
    private String STORE_LOC;

    @ApiModelProperty(value = "备注")
    private String STREET;

    @ApiModelProperty(value = "")
    private String NAME1;

    @ApiModelProperty(value = "")
    private String MAT_GRP;

    @ApiModelProperty(value = "")
    private String FINAL_INV;

    @ApiModelProperty(value = "单据编号")
    private String PO_NUMBER;

    @ApiModelProperty(value = "")
    private String SHORT_TEXT;

    @ApiModelProperty(value = "")
    private String G_L_ACCT;

    @ApiModelProperty(value = "")
    private String REL_STRAT;

    @ApiModelProperty(value = "")
    private String UNLOAD_PT;

    @ApiModelProperty(value = "")
    private String CREATED_ON;

    @ApiModelProperty(value = "")
    private String REL_GROUP;

    @ApiModelProperty(value = "供应商")
    private String VENDOR;

    @ApiModelProperty(value = "")
    private String DELETE_IND_I;

    @ApiModelProperty(value = "")
    private String DELETE_IND_H;

    @ApiModelProperty(value = "")
    private String IV_QTY;

    @ApiModelProperty(value = "")
    private BigDecimal LAGMG;

    @ApiModelProperty(value = "")
    private String LMEIN;

    @ApiModelProperty(value = "")
    private String UMREN;

    @ApiModelProperty(value = "")
    private String UMREZ;

    @ApiModelProperty(value = "样品标示")
    private String UMSON;


}
