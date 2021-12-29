package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 物料接口传入DTO
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
@Data
public class ItfRoutingOperationSyncDTO {

    @ApiModelProperty(value = "ROUTER对象编码（物料编码或工单号）")
    private String routerObjectCode;
    @ApiModelProperty(value = "工厂编码")
    private String plantCode;
    @ApiModelProperty(value = "ROUTER说明")
    private String routerDescription;
    @ApiModelProperty(value = "ROUTER编码")
    private String routerCode;
    @ApiModelProperty(value = "ROUTER开始日期")
    private String routerStartDate;
    @ApiModelProperty(value = "ROUTER结束日期")
    private String routerEndDate;
    @ApiModelProperty(value = "ROUTER有效性（Oracle可不写值，SAP将删除标记勾上的写入N）")
    private String enableFlag;
    @ApiModelProperty(value = "工序号")
    private String operationSeqNum;
    @ApiModelProperty(value = "标准工序编码")
    private String standardOperationCode;
    @ApiModelProperty(value = "工序描述")
    private String operationDescription;
    @ApiModelProperty(value = "ERP创建日期")
    private String erpCreationDateStr;
    @ApiModelProperty(value = "ERP最后更新日期")
    private String erpLastUpdateDateStr;
    @ApiModelProperty(value = "")
    private String headAttribute1;
    @ApiModelProperty(value = "")
    private String headAttribute2;
    @ApiModelProperty(value = "")
    private String headAttribute3;
    @ApiModelProperty(value = "")
    private String headAttribute4;
    @ApiModelProperty(value = "")
    private String headAttribute5;
    @ApiModelProperty(value = "")
    private String headAttribute6;
    @ApiModelProperty(value = "")
    private String headAttribute7;
    @ApiModelProperty(value = "")
    private String headAttribute8;
    @ApiModelProperty(value = "")
    private String headAttribute9;
    @ApiModelProperty(value = "")
    private String headAttribute10;
    @ApiModelProperty(value = "")
    private String headAttribute11;
    @ApiModelProperty(value = "")
    private String headAttribute12;
    @ApiModelProperty(value = "")
    private String headAttribute13;
    @ApiModelProperty(value = "")
    private String headAttribute14;
    @ApiModelProperty(value = "")
    private String headAttribute15;
    @ApiModelProperty(value = "")
    private String lineAttribute1;
    @ApiModelProperty(value = "")
    private String lineAttribute2;
    @ApiModelProperty(value = "")
    private String lineAttribute3;
    @ApiModelProperty(value = "")
    private String lineAttribute4;
    @ApiModelProperty(value = "")
    private String lineAttribute5;
    @ApiModelProperty(value = "")
    private String lineAttribute6;
    @ApiModelProperty(value = "")
    private String lineAttribute7;
    @ApiModelProperty(value = "")
    private String lineAttribute8;
    @ApiModelProperty(value = "")
    private String lineAttribute9;
    @ApiModelProperty(value = "")
    private String lineAttribute10;
    @ApiModelProperty(value = "")
    private String lineAttribute11;
    @ApiModelProperty(value = "")
    private String lineAttribute12;
    @ApiModelProperty(value = "")
    private String lineAttribute13;
    @ApiModelProperty(value = "")
    private String lineAttribute14;
    @ApiModelProperty(value = "")
    private String lineAttribute15;


    public ItfRoutingOperationSyncDTO(ItfSapIfaceDTO dto) {
        this.routerObjectCode = dto.getAUFNR();
        this.plantCode = dto.getDWERK();
        this.routerDescription = dto.getAUFNR();
        this.routerCode = dto.getAUFNR();
        this.routerStartDate = dto.getERDAT();
        this.operationSeqNum = dto.getVORNR();
        this.standardOperationCode = dto.getKTSCH();
        this.operationDescription = dto.getLTXA1();
        this.lineAttribute1 = dto.getVGE01();
        this.lineAttribute2 = dto.getVGW01();
        this.lineAttribute3 = dto.getVGE02();
        this.lineAttribute4 = dto.getVGW02();
        this.lineAttribute5 = dto.getVGE03();
        this.lineAttribute6 = dto.getVGW03();
        this.lineAttribute7 = dto.getVGE04();
        this.lineAttribute8 = dto.getVGW04();
        this.lineAttribute9 = dto.getVGE05();
        this.lineAttribute10 = dto.getVGW05();
        this.lineAttribute11 = dto.getVGE06();
        this.lineAttribute12 = dto.getVGW06();


    }
}
