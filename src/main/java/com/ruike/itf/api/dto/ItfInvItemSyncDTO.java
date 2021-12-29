package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;

/**
 * 物料接口传入DTO
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
@Data
public class ItfInvItemSyncDTO {

    @ApiModelProperty(value = "工厂CODE")
    private String plantCode;
    @ApiModelProperty(value = "物料Id")
    private Double itemId;
    @ApiModelProperty(value = "物料编码")
    private String itemCode;
    @ApiModelProperty(value = "旧物料号")
    private String oldItemCode;
    @ApiModelProperty(value = "主单位")
    private String primaryUom;
    @ApiModelProperty(value = "物料描述")
    private String descriptions;
    @ApiModelProperty(value = "长描述")
    private String descriptionMir;
    @ApiModelProperty(value = "有效标识")
    private String enableFlag;
    @ApiModelProperty(value = "物料类型")
    private String itemType;
    @ApiModelProperty(value = "制造/采购标志")
    private String planningMakeBuyCode;
    @ApiModelProperty(value = "批次管理标志")
    private String lotControlCode;
    @ApiModelProperty(value = "前处理提前期")
    private Double prerpocessingLeadTime;
    @ApiModelProperty(value = "采购提前期")
    private Double purchaseLeadtime;
    @ApiModelProperty(value = "制造提前期")
    private Double wipLeadTime;
    @ApiModelProperty(value = "后处理提前期")
    private Double instockLeadTime;
    @ApiModelProperty(value = "最小包装")
    private Double minPackQty;
    @ApiModelProperty(value = "最小起订量")
    private Double minimumOrderQuantity;
    @ApiModelProperty(value = "采购员CODE")
    private String buyerCode;
    @ApiModelProperty(value = "检验标识")
    private String qcFlag;
    @ApiModelProperty(value = "接收方式")
    private String receivingRoutingId;
    @ApiModelProperty(value = "wip发料类型")
    private String wipSupplyType;
    @ApiModelProperty(value = "寄售vmi标识")
    private String vmiFlag;
    @ApiModelProperty(value = "物料组")
    private String itemGroup;
    @ApiModelProperty(value = "产品组")
    private String productGroup;
    @ApiModelProperty(value = "ERP创建日期")
    private String erpCreationDate;
    @ApiModelProperty(value = "ERP最后更新日期")
    private String erpLastUpdateDate;
    @ApiModelProperty(value = "物料图号")
    private String materialDesignCode;
    @ApiModelProperty(value = "物料简称")
    private String materialIdentifyCode;
    @ApiModelProperty(value = "尺寸单位编码")
    private String sizeUomCode;
    @ApiModelProperty(value = "材质/型号")
    private String model;
    @ApiModelProperty(value = "长")
    private Double length;
    @ApiModelProperty(value = "宽")
    private Double width;
    @ApiModelProperty(value = "高")
    private Double height;
    @ApiModelProperty(value = "体积")
    private Double volume;
    @ApiModelProperty(value = "重量")
    private Double weight;
    @ApiModelProperty(value = "保质期")
    private Double shelfLife;
    @ApiModelProperty(value = "主辅单位转换比例：基本计量单位/辅助单位")
    private Double conversionRate;
    @ApiModelProperty(value = "体积单位编码")
    private String volumeUomCode;
    @ApiModelProperty(value = "重量单位编码")
    private String weightUomCode;
    @ApiModelProperty(value = "保质期单位编码")
    private String shelfLifeUomCode;
    @ApiModelProperty(value = "辅助单位编码")
    private String secondaryUomCode;
    @ApiModelProperty(value = "默认存储位置")
    private String stockLocatorCode;
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
    @ApiModelProperty(value = "")
    private String attribute16;
    @ApiModelProperty(value = "")
    private String attribute17;
    @ApiModelProperty(value = "")
    private String attribute18;
    @ApiModelProperty(value = "")
    private String attribute19;
    @ApiModelProperty(value = "")
    private String attribute20;
    @ApiModelProperty(value = "")
    private String attribute21;
    @ApiModelProperty(value = "")
    private String attribute22;
    @ApiModelProperty(value = "")
    private String attribute23;
    @ApiModelProperty(value = "")
    private String attribute24;
    @ApiModelProperty(value = "")
    private String attribute25;
    @ApiModelProperty(value = "")
    private String attribute26;
    @ApiModelProperty(value = "")
    private String attribute27;
    @ApiModelProperty(value = "")
    private String attribute28;
    @ApiModelProperty(value = "")
    private String attribute29;
    @ApiModelProperty(value = "")
    private String attribute30;

    public ItfInvItemSyncDTO(ItfSapIfaceDTO dto) {
        this.plantCode = Strings.isEmpty(dto.getWERKS()) ? null : dto.getWERKS();
        this.itemCode = Strings.isEmpty(dto.getMATNR()) ? null : Long.valueOf(dto.getMATNR()).toString();
        this.oldItemCode = Strings.isEmpty(dto.getBISMT()) ? null : dto.getBISMT();
        this.primaryUom = Strings.isEmpty(dto.getMEINS()) ? null : dto.getMEINS();
        this.descriptions = Strings.isEmpty(dto.getMAKTX()) ? null : dto.getMAKTX();
        this.enableFlag = Strings.isEmpty(dto.getLVORM2()) ? null : dto.getLVORM2();
        this.itemType = Strings.isEmpty(dto.getMTART()) ? null : dto.getMTART();
        this.lotControlCode = Strings.isEmpty(dto.getXCHPF()) ? null : dto.getXCHPF();
        this.qcFlag = Strings.isEmpty(dto.getQMATV()) ? null : dto.getQMATV();
        this.itemGroup = Strings.isEmpty(dto.getMATKL()) ? null : dto.getMATKL();
        this.productGroup = Strings.isEmpty(dto.getSPART()) ? null : dto.getSPART();
        this.erpLastUpdateDate = Strings.isEmpty(dto.getLAEDA()) ? null : dto.getLAEDA();
        this.shelfLife = Double.valueOf(Strings.isEmpty(dto.getMAXLZ()) ? null : dto.getMAXLZ());
        this.shelfLifeUomCode = Strings.isEmpty(dto.getLZEIH()) ? null : dto.getLZEIH();
        this.attribute1 = Strings.isEmpty(dto.getRGEKZ()) ? null : dto.getRGEKZ();
        this.attribute2 = Strings.isEmpty(dto.getZGNW()) ? null : dto.getZGNW();
        this.attribute4 = Strings.isEmpty(dto.getZGL()) ? null : dto.getZGL();
        this.attribute5 = Strings.isEmpty(dto.getZCPXH()) ? null : dto.getZCPXH();
        this.attribute6 = Strings.isEmpty(dto.getZSYTM()) ? null : dto.getZSYTM();
        this.attribute7 = Strings.isEmpty(dto.getZTMFS()) ? null : dto.getZTMFS();
        this.attribute8 = Strings.isEmpty(dto.getZZIBZ()) ? null : dto.getZZIBZ();
        this.attribute9 = Strings.isEmpty(dto.getZZKFYX()) ? null : dto.getZZKFYX();
        this.attribute10 = Strings.isEmpty(dto.getZZKFYXDW()) ? null : dto.getZZKFYXDW();
        this.attribute11 = Strings.isEmpty(dto.getSERNP()) ? null : dto.getSERNP();
        this.attribute12 = Strings.isEmpty(dto.getZZGUQLX()) ? null : dto.getZZGUQLX();
        this.attribute13 = Strings.isEmpty(dto.getZZWLFL()) ? null : dto.getZZWLFL();
        this.attribute14 = Strings.isEmpty(dto.getZZSCLX()) ? null : dto.getZZSCLX();
        this.attribute15 = Strings.isEmpty(dto.getZZBQ()) ? null : dto.getZZBQ();
        this.attribute16 = Strings.isEmpty(dto.getZZBQDW()) ? null : dto.getZZBQDW();
        this.attribute17 = Strings.isEmpty(dto.getZZSNSJBS()) ? null : dto.getZZSNSJBS();
        this.attribute18 = Strings.isEmpty(dto.getZCPSN()) ? null : dto.getZCPSN();
        this.erpCreationDate = Strings.isEmpty(dto.getERSDA()) ? null : dto.getERSDA();
        this.attribute19 = Strings.isEmpty(dto.getZQMAT()) ? null : dto.getZQMAT();
    }

}
