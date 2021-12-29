package tarzan.iface.domain.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物料接口表
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@ApiModel("物料接口表")

@ModifyAudit

@Table(name = "mt_inv_item_iface")
@CustomPrimary
public class MtInvItemIface extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_IFACE_ID = "ifaceId";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_ITEM_ID = "itemId";
    public static final String FIELD_ITEM_CODE = "itemCode";
    public static final String FIELD_OLD_ITEM_CODE = "oldItemCode";
    public static final String FIELD_PRIMARY_UOM = "primaryUom";
    public static final String FIELD_DESCRIPTIONS = "descriptions";
    public static final String FIELD_DESCRIPTION_MIR = "descriptionMir";
    public static final String FIELD_ENABLE_FLAG = "enableFlag";
    public static final String FIELD_ITEM_TYPE = "itemType";
    public static final String FIELD_PLANNING_MAKE_BUY_CODE = "planningMakeBuyCode";
    public static final String FIELD_LOT_CONTROL_CODE = "lotControlCode";
    public static final String FIELD_PRERPOCESSING_LEAD_TIME = "prerpocessingLeadTime";
    public static final String FIELD_PURCHASE_LEADTIME = "purchaseLeadtime";
    public static final String FIELD_WIP_LEAD_TIME = "wipLeadTime";
    public static final String FIELD_INSTOCK_LEAD_TIME = "instockLeadTime";
    public static final String FIELD_MIN_PACK_QTY = "minPackQty";
    public static final String FIELD_MINIMUM_ORDER_QUANTITY = "minimumOrderQuantity";
    public static final String FIELD_BUYER_CODE = "buyerCode";
    public static final String FIELD_QC_FLAG = "qcFlag";
    public static final String FIELD_RECEIVING_ROUTING_ID = "receivingRoutingId";
    public static final String FIELD_WIP_SUPPLY_TYPE = "wipSupplyType";
    public static final String FIELD_VMI_FLAG = "vmiFlag";
    public static final String FIELD_ITEM_GROUP = "itemGroup";
    public static final String FIELD_PRODUCT_GROUP = "productGroup";
    public static final String FIELD_ERP_CREATION_DATE = "erpCreationDate";
    public static final String FIELD_ERP_CREATED_BY = "erpCreatedBy";
    public static final String FIELD_ERP_LAST_UPDATED_BY = "erpLastUpdatedBy";
    public static final String FIELD_ERP_LAST_UPDATE_DATE = "erpLastUpdateDate";
    public static final String FIELD_MATERIAL_DESIGN_CODE = "materialDesignCode";
    public static final String FIELD_MATERIAL_IDENTIFY_CODE = "materialIdentifyCode";
    public static final String FIELD_LENGTH = "length";
    public static final String FIELD_WIDTH = "width";
    public static final String FIELD_HEIGHT = "height";
    public static final String FIELD_SIZE_UOM_CODE = "sizeUomCode";
    public static final String FIELD_MODEL = "model";
    public static final String FIELD_VOLUME = "volume";
    public static final String FIELD_VOLUME_UOM_CODE = "volumeUomCode";
    public static final String FIELD_WEIGHT = "weight";
    public static final String FIELD_WEIGHT_UOM_CODE = "weightUomCode";
    public static final String FIELD_SHELF_LIFE = "shelfLife";
    public static final String FIELD_SHELF_LIFE_UOM_CODE = "shelfLifeUomCode";
    public static final String FIELD_SECONDARY_UOM_CODE = "secondaryUomCode";
    public static final String FIELD_CONVERSION_RATE = "conversionRate";
    public static final String FIELD_STOCK_LOCATOR_CODE = "stockLocatorCode";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 5133244348756048595L;

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
    @ApiModelProperty(value = "工厂CODE", required = true)
    @NotBlank
    @Where
    private String plantCode;
    @ApiModelProperty(value = "物料Id")
    @Where
    private Double itemId;
    @ApiModelProperty(value = "物料编码", required = true)
    @NotBlank
    @Where
    private String itemCode;
    @ApiModelProperty(value = "旧物料号")
    @Where
    private String oldItemCode;
    @ApiModelProperty(value = "主单位", required = true)
    @NotBlank
    @Where
    private String primaryUom;
    @ApiModelProperty(value = "物料描述", required = true)
    @NotBlank
    @Where
    private String descriptions;
    @ApiModelProperty(value = "长描述")
    @Where
    private String descriptionMir;
    @ApiModelProperty(value = "有效标识", required = true)
    @NotBlank
    @Where
    private String enableFlag;
    @ApiModelProperty(value = "物料类型")
    @Where
    private String itemType;
    @ApiModelProperty(value = "制造/采购标志")
    @Where
    private String planningMakeBuyCode;
    @ApiModelProperty(value = "批次管理标志")
    @Where
    private String lotControlCode;
    @ApiModelProperty(value = "前处理提前期")
    @Where
    private Double prerpocessingLeadTime;
    @ApiModelProperty(value = "采购提前期")
    @Where
    private Double purchaseLeadtime;
    @ApiModelProperty(value = "制造提前期")
    @Where
    private Double wipLeadTime;
    @ApiModelProperty(value = "后处理提前期")
    @Where
    private Double instockLeadTime;
    @ApiModelProperty(value = "最小包装")
    @Where
    private Double minPackQty;
    @ApiModelProperty(value = "最小起订量")
    @Where
    private Double minimumOrderQuantity;
    @ApiModelProperty(value = "采购员CODE")
    @Where
    private String buyerCode;
    @ApiModelProperty(value = "检验标识")
    @Where
    private String qcFlag;
    @ApiModelProperty(value = "接收方式")
    @Where
    private String receivingRoutingId;
    @ApiModelProperty(value = "wip发料类型")
    @Where
    private String wipSupplyType;
    @ApiModelProperty(value = "寄售vmi标识")
    @Where
    private String vmiFlag;
    @ApiModelProperty(value = "物料组")
    @Where
    private String itemGroup;
    @ApiModelProperty(value = "产品组")
    @Where
    private String productGroup;
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

    @ApiModelProperty(value = "物料图号")
    @Where
    private String materialDesignCode;
    @ApiModelProperty(value = "物料简称")
    @Where
    private String materialIdentifyCode;
    @ApiModelProperty(value = "长")
    @Where
    private Double length;
    @ApiModelProperty(value = "宽")
    @Where
    private Double width;
    @ApiModelProperty(value = "高")
    @Where
    private Double height;
    @ApiModelProperty(value = "尺寸单位编码")
    @Where
    private String sizeUomCode;
    @ApiModelProperty(value = "材质、型号")
    @Where
    private String model;
    @ApiModelProperty(value = "体积")
    @Where
    private Double volume;
    @ApiModelProperty(value = "体积单位编码")
    @Where
    private String volumeUomCode;
    @ApiModelProperty(value = "重量")
    @Where
    private Double weight;
    @ApiModelProperty(value = "重量单位编码")
    @Where
    private String weightUomCode;
    @ApiModelProperty(value = "保质期")
    @Where
    private Double shelfLife;
    @ApiModelProperty(value = "保质期单位编码")
    @Where
    private String shelfLifeUomCode;
    @ApiModelProperty(value = "辅助单位编码")
    @Where
    private String secondaryUomCode;
    @ApiModelProperty(value = "主辅单位转换比例：基本计量单位/辅助单位")
    @Where
    private Double conversionRate;

    @ApiModelProperty(value = "默认存储位置")
    @Where
    private String stockLocatorCode;

    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    @Where
    private Double batchId;
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
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute1;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute2;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute3;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute4;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute5;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute6;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute7;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute8;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute9;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute10;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute11;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute12;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute13;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute14;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute15;

    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute16;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute17;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute18;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute19;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute20;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute21;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute22;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute23;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute24;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute25;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute26;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute27;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute28;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute29;
    @ApiModelProperty(value = "备用字段")
    @Where
    private String attribute30;
    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 主键
     */
    public String getIfaceId() {
        return ifaceId;
    }

    public void setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
    }

    /**
     * @return 工厂CODE
     */
    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    /**
     * @return 物料Id
     */
    public Double getItemId() {
        return itemId;
    }

    public void setItemId(Double itemId) {
        this.itemId = itemId;
    }

    /**
     * @return 物料编码
     */
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return 旧物料号
     */
    public String getOldItemCode() {
        return oldItemCode;
    }

    public void setOldItemCode(String oldItemCode) {
        this.oldItemCode = oldItemCode;
    }

    /**
     * @return 主单位
     */
    public String getPrimaryUom() {
        return primaryUom;
    }

    public void setPrimaryUom(String primaryUom) {
        this.primaryUom = primaryUom;
    }

    /**
     * @return 物料描述
     */
    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * @return 长描述
     */
    public String getDescriptionMir() {
        return descriptionMir;
    }

    public void setDescriptionMir(String descriptionMir) {
        this.descriptionMir = descriptionMir;
    }

    /**
     * @return 有效标识
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    /**
     * @return 物料类型
     */
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * @return 制造/采购标志
     */
    public String getPlanningMakeBuyCode() {
        return planningMakeBuyCode;
    }

    public void setPlanningMakeBuyCode(String planningMakeBuyCode) {
        this.planningMakeBuyCode = planningMakeBuyCode;
    }

    /**
     * @return 批次管理标志
     */
    public String getLotControlCode() {
        return lotControlCode;
    }

    public void setLotControlCode(String lotControlCode) {
        this.lotControlCode = lotControlCode;
    }

    /**
     * @return 前处理提前期
     */
    public Double getPrerpocessingLeadTime() {
        return prerpocessingLeadTime;
    }

    public void setPrerpocessingLeadTime(Double prerpocessingLeadTime) {
        this.prerpocessingLeadTime = prerpocessingLeadTime;
    }

    /**
     * @return 采购提前期
     */
    public Double getPurchaseLeadtime() {
        return purchaseLeadtime;
    }

    public void setPurchaseLeadtime(Double purchaseLeadtime) {
        this.purchaseLeadtime = purchaseLeadtime;
    }

    /**
     * @return 制造提前期
     */
    public Double getWipLeadTime() {
        return wipLeadTime;
    }

    public void setWipLeadTime(Double wipLeadTime) {
        this.wipLeadTime = wipLeadTime;
    }

    /**
     * @return 后处理提前期
     */
    public Double getInstockLeadTime() {
        return instockLeadTime;
    }

    public void setInstockLeadTime(Double instockLeadTime) {
        this.instockLeadTime = instockLeadTime;
    }

    /**
     * @return 最小包装
     */
    public Double getMinPackQty() {
        return minPackQty;
    }

    public void setMinPackQty(Double minPackQty) {
        this.minPackQty = minPackQty;
    }

    /**
     * @return 最小起订量
     */
    public Double getMinimumOrderQuantity() {
        return minimumOrderQuantity;
    }

    public void setMinimumOrderQuantity(Double minimumOrderQuantity) {
        this.minimumOrderQuantity = minimumOrderQuantity;
    }

    /**
     * @return 采购员CODE
     */
    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode;
    }

    /**
     * @return 检验标识
     */
    public String getQcFlag() {
        return qcFlag;
    }

    public void setQcFlag(String qcFlag) {
        this.qcFlag = qcFlag;
    }

    /**
     * @return 接收方式
     */
    public String getReceivingRoutingId() {
        return receivingRoutingId;
    }

    public void setReceivingRoutingId(String receivingRoutingId) {
        this.receivingRoutingId = receivingRoutingId;
    }

    /**
     * @return wip发料类型
     */
    public String getWipSupplyType() {
        return wipSupplyType;
    }

    public void setWipSupplyType(String wipSupplyType) {
        this.wipSupplyType = wipSupplyType;
    }

    /**
     * @return 寄售vmi标识
     */
    public String getVmiFlag() {
        return vmiFlag;
    }

    public void setVmiFlag(String vmiFlag) {
        this.vmiFlag = vmiFlag;
    }

    /**
     * @return 物料组
     */
    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    /**
     * @return 产品组
     */
    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    /**
     * @return ERP创建日期
     */
    public Date getErpCreationDate() {
        if (erpCreationDate != null) {
            return (Date) erpCreationDate.clone();
        } else {
            return null;
        }
    }

    public void setErpCreationDate(Date erpCreationDate) {
        if (erpCreationDate == null) {
            this.erpCreationDate = null;
        } else {
            this.erpCreationDate = (Date) erpCreationDate.clone();
        }
    }

    /**
     * @return ERP创建人
     */
    public Long getErpCreatedBy() {
        return erpCreatedBy;
    }

    public void setErpCreatedBy(Long erpCreatedBy) {
        this.erpCreatedBy = erpCreatedBy;
    }

    /**
     * @return ERP最后更新人
     */
    public Long getErpLastUpdatedBy() {
        return erpLastUpdatedBy;
    }

    public void setErpLastUpdatedBy(Long erpLastUpdatedBy) {
        this.erpLastUpdatedBy = erpLastUpdatedBy;
    }

    /**
     * @return ERP最后更新日期
     */
    public Date getErpLastUpdateDate() {
        if (erpLastUpdateDate != null) {
            return (Date) erpLastUpdateDate.clone();
        } else {
            return null;
        }
    }

    public void setErpLastUpdateDate(Date erpLastUpdateDate) {
        if (erpLastUpdateDate == null) {
            this.erpLastUpdateDate = null;
        } else {
            this.erpLastUpdateDate = (Date) erpLastUpdateDate.clone();
        }
    }

    public String getMaterialDesignCode() {
        return materialDesignCode;
    }

    public void setMaterialDesignCode(String materialDesignCode) {
        this.materialDesignCode = materialDesignCode;
    }

    public String getMaterialIdentifyCode() {
        return materialIdentifyCode;
    }

    public void setMaterialIdentifyCode(String materialIdentifyCode) {
        this.materialIdentifyCode = materialIdentifyCode;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getSizeUomCode() {
        return sizeUomCode;
    }

    public void setSizeUomCode(String sizeUomCode) {
        this.sizeUomCode = sizeUomCode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getVolumeUomCode() {
        return volumeUomCode;
    }

    public void setVolumeUomCode(String volumeUomCode) {
        this.volumeUomCode = volumeUomCode;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getWeightUomCode() {
        return weightUomCode;
    }

    public void setWeightUomCode(String weightUomCode) {
        this.weightUomCode = weightUomCode;
    }

    public Double getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Double shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getShelfLifeUomCode() {
        return shelfLifeUomCode;
    }

    public void setShelfLifeUomCode(String shelfLifeUomCode) {
        this.shelfLifeUomCode = shelfLifeUomCode;
    }

    public String getSecondaryUomCode() {
        return secondaryUomCode;
    }

    public void setSecondaryUomCode(String secondaryUomCode) {
        this.secondaryUomCode = secondaryUomCode;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    /**
     * @return 数据批次ID
     */
    public Double getBatchId() {
        return batchId;
    }

    public void setBatchId(Double batchId) {
        this.batchId = batchId;
    }

    /**
     * @return 数据处理状态，初始为N，失败为E，成功S，处理中P
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return 数据处理消息返回
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public String getStockLocatorCode() {
        return stockLocatorCode;
    }

    public void setStockLocatorCode(String stockLocatorCode) {
        this.stockLocatorCode = stockLocatorCode;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    public String getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    public String getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    public String getAttribute13() {
        return attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    public String getAttribute14() {
        return attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }

    public String getAttribute16() {
        return attribute16;
    }

    public void setAttribute16(String attribute16) {
        this.attribute16 = attribute16;
    }

    public String getAttribute17() {
        return attribute17;
    }

    public void setAttribute17(String attribute17) {
        this.attribute17 = attribute17;
    }

    public String getAttribute18() {
        return attribute18;
    }

    public void setAttribute18(String attribute18) {
        this.attribute18 = attribute18;
    }

    public String getAttribute19() {
        return attribute19;
    }

    public void setAttribute19(String attribute19) {
        this.attribute19 = attribute19;
    }

    public String getAttribute20() {
        return attribute20;
    }

    public void setAttribute20(String attribute20) {
        this.attribute20 = attribute20;
    }

    public String getAttribute21() {
        return attribute21;
    }

    public void setAttribute21(String attribute21) {
        this.attribute21 = attribute21;
    }

    public String getAttribute22() {
        return attribute22;
    }

    public void setAttribute22(String attribute22) {
        this.attribute22 = attribute22;
    }

    public String getAttribute23() {
        return attribute23;
    }

    public void setAttribute23(String attribute23) {
        this.attribute23 = attribute23;
    }

    public String getAttribute24() {
        return attribute24;
    }

    public void setAttribute24(String attribute24) {
        this.attribute24 = attribute24;
    }

    public String getAttribute25() {
        return attribute25;
    }

    public void setAttribute25(String attribute25) {
        this.attribute25 = attribute25;
    }

    public String getAttribute26() {
        return attribute26;
    }

    public void setAttribute26(String attribute26) {
        this.attribute26 = attribute26;
    }

    public String getAttribute27() {
        return attribute27;
    }

    public void setAttribute27(String attribute27) {
        this.attribute27 = attribute27;
    }

    public String getAttribute28() {
        return attribute28;
    }

    public void setAttribute28(String attribute28) {
        this.attribute28 = attribute28;
    }

    public String getAttribute29() {
        return attribute29;
    }

    public void setAttribute29(String attribute29) {
        this.attribute29 = attribute29;
    }

    public String getAttribute30() {
        return attribute30;
    }

    public void setAttribute30(String attribute30) {
        this.attribute30 = attribute30;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtInvItemIface that = (MtInvItemIface) o;
        return Objects.equals(getTenantId(), that.getTenantId()) && Objects.equals(getIfaceId(), that.getIfaceId())
                        && Objects.equals(getPlantCode(), that.getPlantCode())
                        && Objects.equals(getItemId(), that.getItemId())
                        && Objects.equals(getItemCode(), that.getItemCode())
                        && Objects.equals(getOldItemCode(), that.getOldItemCode())
                        && Objects.equals(getPrimaryUom(), that.getPrimaryUom())
                        && Objects.equals(getDescriptions(), that.getDescriptions())
                        && Objects.equals(getDescriptionMir(), that.getDescriptionMir())
                        && Objects.equals(getEnableFlag(), that.getEnableFlag())
                        && Objects.equals(getItemType(), that.getItemType())
                        && Objects.equals(getPlanningMakeBuyCode(), that.getPlanningMakeBuyCode())
                        && Objects.equals(getLotControlCode(), that.getLotControlCode())
                        && Objects.equals(getPrerpocessingLeadTime(), that.getPrerpocessingLeadTime())
                        && Objects.equals(getPurchaseLeadtime(), that.getPurchaseLeadtime())
                        && Objects.equals(getWipLeadTime(), that.getWipLeadTime())
                        && Objects.equals(getInstockLeadTime(), that.getInstockLeadTime())
                        && Objects.equals(getMinPackQty(), that.getMinPackQty())
                        && Objects.equals(getMinimumOrderQuantity(), that.getMinimumOrderQuantity())
                        && Objects.equals(getBuyerCode(), that.getBuyerCode())
                        && Objects.equals(getQcFlag(), that.getQcFlag())
                        && Objects.equals(getReceivingRoutingId(), that.getReceivingRoutingId())
                        && Objects.equals(getWipSupplyType(), that.getWipSupplyType())
                        && Objects.equals(getVmiFlag(), that.getVmiFlag())
                        && Objects.equals(getItemGroup(), that.getItemGroup())
                        && Objects.equals(getProductGroup(), that.getProductGroup())
                        && Objects.equals(getErpCreationDate(), that.getErpCreationDate())
                        && Objects.equals(getErpCreatedBy(), that.getErpCreatedBy())
                        && Objects.equals(getErpLastUpdatedBy(), that.getErpLastUpdatedBy())
                        && Objects.equals(getErpLastUpdateDate(), that.getErpLastUpdateDate())
                        && Objects.equals(getMaterialDesignCode(), that.getMaterialDesignCode())
                        && Objects.equals(getMaterialIdentifyCode(), that.getMaterialIdentifyCode())
                        && Objects.equals(getLength(), that.getLength()) && Objects.equals(getWidth(), that.getWidth())
                        && Objects.equals(getHeight(), that.getHeight())
                        && Objects.equals(getSizeUomCode(), that.getSizeUomCode())
                        && Objects.equals(getModel(), that.getModel()) && Objects.equals(getVolume(), that.getVolume())
                        && Objects.equals(getVolumeUomCode(), that.getVolumeUomCode())
                        && Objects.equals(getWeight(), that.getWeight())
                        && Objects.equals(getWeightUomCode(), that.getWeightUomCode())
                        && Objects.equals(getShelfLife(), that.getShelfLife())
                        && Objects.equals(getShelfLifeUomCode(), that.getShelfLifeUomCode())
                        && Objects.equals(getSecondaryUomCode(), that.getSecondaryUomCode())
                        && Objects.equals(getConversionRate(), that.getConversionRate())
                        && Objects.equals(getStockLocatorCode(), that.getStockLocatorCode())
                        && Objects.equals(getBatchId(), that.getBatchId())
                        && Objects.equals(getStatus(), that.getStatus())
                        && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getCid(), that.getCid())
                        && Objects.equals(getAttribute1(), that.getAttribute1())
                        && Objects.equals(getAttribute2(), that.getAttribute2())
                        && Objects.equals(getAttribute3(), that.getAttribute3())
                        && Objects.equals(getAttribute4(), that.getAttribute4())
                        && Objects.equals(getAttribute5(), that.getAttribute5())
                        && Objects.equals(getAttribute6(), that.getAttribute6())
                        && Objects.equals(getAttribute7(), that.getAttribute7())
                        && Objects.equals(getAttribute8(), that.getAttribute8())
                        && Objects.equals(getAttribute9(), that.getAttribute9())
                        && Objects.equals(getAttribute10(), that.getAttribute10())
                        && Objects.equals(getAttribute11(), that.getAttribute11())
                        && Objects.equals(getAttribute12(), that.getAttribute12())
                        && Objects.equals(getAttribute13(), that.getAttribute13())
                        && Objects.equals(getAttribute14(), that.getAttribute14())
                        && Objects.equals(getAttribute15(), that.getAttribute15())
                        && Objects.equals(getAttribute16(), that.getAttribute16())
                        && Objects.equals(getAttribute17(), that.getAttribute17())
                        && Objects.equals(getAttribute18(), that.getAttribute18())
                        && Objects.equals(getAttribute19(), that.getAttribute19())
                        && Objects.equals(getAttribute20(), that.getAttribute20())
                        && Objects.equals(getAttribute21(), that.getAttribute21())
                        && Objects.equals(getAttribute22(), that.getAttribute22())
                        && Objects.equals(getAttribute23(), that.getAttribute23())
                        && Objects.equals(getAttribute24(), that.getAttribute24())
                        && Objects.equals(getAttribute25(), that.getAttribute25())
                        && Objects.equals(getAttribute26(), that.getAttribute26())
                        && Objects.equals(getAttribute27(), that.getAttribute27())
                        && Objects.equals(getAttribute28(), that.getAttribute28())
                        && Objects.equals(getAttribute29(), that.getAttribute29())
                        && Objects.equals(getAttribute30(), that.getAttribute30());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTenantId(), getIfaceId(), getPlantCode(), getItemId(), getItemCode(), getOldItemCode(),
                        getPrimaryUom(), getDescriptions(), getDescriptionMir(), getEnableFlag(), getItemType(),
                        getPlanningMakeBuyCode(), getLotControlCode(), getPrerpocessingLeadTime(),
                        getPurchaseLeadtime(), getWipLeadTime(), getInstockLeadTime(), getMinPackQty(),
                        getMinimumOrderQuantity(), getBuyerCode(), getQcFlag(), getReceivingRoutingId(),
                        getWipSupplyType(), getVmiFlag(), getItemGroup(), getProductGroup(), getErpCreationDate(),
                        getErpCreatedBy(), getErpLastUpdatedBy(), getErpLastUpdateDate(), getMaterialDesignCode(),
                        getMaterialIdentifyCode(), getLength(), getWidth(), getHeight(), getSizeUomCode(), getModel(),
                        getVolume(), getVolumeUomCode(), getWeight(), getWeightUomCode(), getShelfLife(),
                        getShelfLifeUomCode(), getSecondaryUomCode(), getConversionRate(), getStockLocatorCode(),
                        getBatchId(), getStatus(), getMessage(), getCid(), getAttribute1(), getAttribute2(),
                        getAttribute3(), getAttribute4(), getAttribute5(), getAttribute6(), getAttribute7(),
                        getAttribute8(), getAttribute9(), getAttribute10(), getAttribute11(), getAttribute12(),
                        getAttribute13(), getAttribute14(), getAttribute15(), getAttribute16(), getAttribute17(),
                        getAttribute18(), getAttribute19(), getAttribute20(), getAttribute21(), getAttribute22(),
                        getAttribute23(), getAttribute24(), getAttribute25(), getAttribute26(), getAttribute27(),
                        getAttribute28(), getAttribute29(), getAttribute30());
    }
}
