package com.ruike.itf.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.domain.AuditDomain;

import java.util.Date;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.mybatis.common.query.Where;

/**
 * 物料接口表
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@ApiModel("物料接口表")
@Data
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "itf_inv_item_iface")
@CustomPrimary
public class ItfInvItemIface extends AuditDomain {

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
    public static final String FIELD_SIZE_UOM_CODE = "sizeUomCode";
    public static final String FIELD_MODEL = "model";
    public static final String FIELD_LENGTH = "length";
    public static final String FIELD_WIDTH = "width";
    public static final String FIELD_HEIGHT = "height";
    public static final String FIELD_VOLUME = "volume";
    public static final String FIELD_WEIGHT = "weight";
    public static final String FIELD_SHELF_LIFE = "shelfLife";
    public static final String FIELD_CONVERSION_RATE = "conversionRate";
    public static final String FIELD_VOLUME_UOM_CODE = "volumeUomCode";
    public static final String FIELD_WEIGHT_UOM_CODE = "weightUomCode";
    public static final String FIELD_SHELF_LIFE_UOM_CODE = "shelfLifeUomCode";
    public static final String FIELD_SECONDARY_UOM_CODE = "secondaryUomCode";
    public static final String FIELD_STOCK_LOCATOR_CODE = "stockLocatorCode";
    public static final String FIELD_BATCH_ID = "batchId";
    public static final String FIELD_BATCH_DATE = "batchDate";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_MESSAGE = "message";
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
    public static final String FIELD_ATTRIBUTE16 = "attribute16";
    public static final String FIELD_ATTRIBUTE17 = "attribute17";
    public static final String FIELD_ATTRIBUTE18 = "attribute18";
    public static final String FIELD_ATTRIBUTE19 = "attribute19";
    public static final String FIELD_ATTRIBUTE20 = "attribute20";
    public static final String FIELD_ATTRIBUTE21 = "attribute21";
    public static final String FIELD_ATTRIBUTE22 = "attribute22";
    public static final String FIELD_ATTRIBUTE23 = "attribute23";
    public static final String FIELD_ATTRIBUTE24 = "attribute24";
    public static final String FIELD_ATTRIBUTE25 = "attribute25";
    public static final String FIELD_ATTRIBUTE26 = "attribute26";
    public static final String FIELD_ATTRIBUTE27 = "attribute27";
    public static final String FIELD_ATTRIBUTE28 = "attribute28";
    public static final String FIELD_ATTRIBUTE29 = "attribute29";
    public static final String FIELD_ATTRIBUTE30 = "attribute30";
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
    @ApiModelProperty("")
    @Id
    @Where
    private String ifaceId;
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
    private Date erpCreationDate;
    @ApiModelProperty(value = "ERP创建人")
    private Long erpCreatedBy;
    @ApiModelProperty(value = "ERP最后更新人")
    private Long erpLastUpdatedBy;
    @ApiModelProperty(value = "ERP最后更新日期")
    private Date erpLastUpdateDate;
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
    @ApiModelProperty(value = "数据批次ID", required = true)
    @NotNull
    private Double batchId;
    @ApiModelProperty(value = "数据批次日期")
    private String batchDate;
    @ApiModelProperty(value = "数据处理状态，初始为N，失败为E，成功S，处理中P", required = true)
    @NotBlank
    private String status;
    @ApiModelProperty(value = "数据处理消息返回")
    private String message;
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
    @ApiModelProperty(value = "CID", required = true)
    @NotNull
    @Cid
    private Long cid;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

}
