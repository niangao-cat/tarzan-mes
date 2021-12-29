package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 物料接口传入DTO
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
@Data
public class ItfBomComponentSyncDTO {

    @ApiModelProperty(value = "工厂编码")
    private String plantCode;
    @ApiModelProperty(value = "BOM编码")
    private String bomCode;
    @ApiModelProperty(value = "BOM说明")
    private String bomDescription;
    @ApiModelProperty(value = "BOM开始日期")
    private String bomStartDateStr;
    @ApiModelProperty(value = "BOM结束日期")
    private String bomEndDateStr;
    @ApiModelProperty(value = "BOM状态（Oracle可不写值，SAP将激活/未激活写入分别对应ACTIVE/UNACTIVE)")
    private String bomStatus;
    @ApiModelProperty(value = "BOM有效性（Oracle可不写值，SAP将删除标记勾上的写入N）")
    private String enableFlag;
    @ApiModelProperty(value = "BOM对象编码（物料编码或工单号）")
    private String bomObjectCode;
    @ApiModelProperty(value = "基准数量（Oracle物料BOM写入1，工单BOM可直接写入工单数量）")
    private Double standardQty;
    @ApiModelProperty(value = "组件行号")
    private Long componentLineNum;
    @ApiModelProperty(value = "组件物料编码")
    private String componentItemCode;
    @ApiModelProperty(value = "工序号")
    private String operationSequence;
    @ApiModelProperty(value = "组件单位用量")
    private Double bomUsage;
    @ApiModelProperty(value = "组件损耗率")
    private Double componentShrinkage;
    @ApiModelProperty(value = "组件发料类型（1推式，2，3拉式，6虚拟件）")
    private String wipSupplyType;
    @ApiModelProperty(value = "ERP创建日期")
    private String erpCreationDateStr;
    @ApiModelProperty(value = "ERP最后更新日期")
    private String erpLastUpdateDateStr;
    @ApiModelProperty(value = "")
    private String issueLocatorCode;
    @ApiModelProperty(value = "")
    private String assembleMethod;
    @ApiModelProperty(value = "")
    private String bomComponentType;
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
    @ApiModelProperty(value = "替代策略")
    private String lineAttribute15;
    @ApiModelProperty(value = "蝉联品")
    private String lineAttribute21;

    //wenzhang.yu for kang.wang 2020.09.28
    @ApiModelProperty(value = "替代组")
    private String substituteGroup;
    @ApiModelProperty(value = "使用百分比")
    private String lineAttribute17;
    @ApiModelProperty(value = "优先级")
    private String lineAttribute16;
    @ApiModelProperty(value = "组件开始日期")
    private Date componentStartDate;
    @ApiModelProperty(value = "组件结束日期")
    private Date componentEndDate;
    // add 刘克金，王康
    @ApiModelProperty(value = "基本单位")
    private String lineAttribute22;
    @ApiModelProperty(value = "损耗数量")
    private String lineAttribute23;
    // add 刘克金，王康 2020年11月03日15:22:49
    @ApiModelProperty(value = "联产品行号")
    private String lineAttribute24;

    public ItfBomComponentSyncDTO(ItfSapIfaceDTO dto, List<ItfWorkOrderSyncDTO> workOrderList, List<LovValueDTO> itfNomControl) {
        // 若工单组件单位在值集ITF_NOM_CONTROL内，则按照以下逻辑计算单位用量：BDMNG-SAP总需求数量/[（1+AUSCH-损耗率%）*GAMNG-工单头计划数量]  > 1，则取整数位，
        // 其余情况均按照现有逻辑计算（ZZJXQ-SAP净需求数量/GAMNG-工单头计划数量的结果向下保留三位小数）。
        Map<String, ItfWorkOrderSyncDTO> workOrderSyncDTOMap = workOrderList.stream().collect(
                Collectors.toMap(ItfWorkOrderSyncDTO::getWorkOrderNum, ItfWorkOrderSyncDTO -> ItfWorkOrderSyncDTO));
        String workOrderNum = dto.getAUFNR();
        ItfWorkOrderSyncDTO itfWorkOrderSyncDTO = workOrderSyncDTOMap.get(workOrderNum);
        BigDecimal gamng = new BigDecimal(String.valueOf(itfWorkOrderSyncDTO.getQuantity()));
        boolean isNull = false;
        String baseUom = dto.getBASE_UOM();
        for (LovValueDTO lovValueDTO : itfNomControl) {
            if (lovValueDTO.getValue().equals(baseUom)) {
                isNull = true;
                break;
            }
        }
        BigDecimal bomUsage = BigDecimal.ZERO;
        BigDecimal zzjxq = new BigDecimal(dto.getZZJXQ());
        if (!isNull) {
            // add 于文璋，王康
            bomUsage = zzjxq.divide(gamng, 3, RoundingMode.FLOOR);
        } else {
            //BDMNG    行
            //AUSCH    行
            //GAMNG    工单
            //BDMNG / ((1+AUSCH/100)*GAMNG)
            BigDecimal ausch = new BigDecimal(dto.getAUSCH());
            BigDecimal bdmng = new BigDecimal(dto.getBDMNG());
            // 计算和向下取整
            bomUsage = bdmng.divide(BigDecimal.ONE.add(ausch.divide(new BigDecimal("100"))).multiply(gamng), BigDecimal.ROUND_DOWN);
            if (bomUsage.longValue() < 1L) {
                bomUsage = zzjxq.divide(gamng, 3, RoundingMode.FLOOR);
            }
        }
        if (bomUsage.doubleValue() < 0D) {
            bomUsage = new BigDecimal("-1");
        }
        //BigDecimal bomUsage = zzjxq.divide(gamng, 2, RoundingMode.FLOOR);
        this.plantCode = dto.getDWERK();
        this.bomCode = dto.getAUFNR();
        this.bomDescription = dto.getAUFNR();
        this.bomStartDateStr = dto.getERDAT();
        this.bomObjectCode = dto.getAUFNR();
        this.componentLineNum = Long.valueOf(dto.getRSPOS());
        this.componentItemCode = dto.getMATNR();
        this.operationSequence = dto.getVORNR();
        this.bomUsage = Double.valueOf(String.valueOf(bomUsage));
        this.componentShrinkage = dto.getAUSCH();
        this.issueLocatorCode = dto.getLGORT();
        this.wipSupplyType = dto.getRGEKZ();
        this.assembleMethod = dto.getRGEKZ();
        this.bomComponentType = dto.getDUMPS();
        this.lineAttribute2 = dto.getBAUGR();
        this.lineAttribute3 = dto.getABLAD();
        this.lineAttribute4 = dto.getZZJXQ();
        this.lineAttribute5 = dto.getBDMNG();
        this.lineAttribute6 = dto.getZZMXQDWYL();
        this.lineAttribute7 = dto.getPOTX1();
        this.lineAttribute8 = dto.getDUMPS();
        this.lineAttribute9 = dto.getZFLAG();
        this.lineAttribute10 = dto.getRSNUM();
        this.lineAttribute11 = dto.getSOBKZ();
        this.lineAttribute21 = dto.getKZKUP();

        //wenzhang.yu for kang.wang 2020.09.28
        this.substituteGroup = dto.getALPGR();
        this.lineAttribute15 = dto.getALPST();
        this.lineAttribute17 = dto.getEWAHR();
        this.lineAttribute16 = dto.getALPRF();
        this.componentStartDate = dto.getDATUV();
        this.componentEndDate = dto.getDATUB();
        // add 刘克金，王康
        this.lineAttribute22 = dto.getBASE_UOM();
        this.lineAttribute23 = dto.getZZMENGE();
        // add 刘克金，王康
        this.lineAttribute24 = dto.getORDER_ITEM_NUMBER();
    }
}
