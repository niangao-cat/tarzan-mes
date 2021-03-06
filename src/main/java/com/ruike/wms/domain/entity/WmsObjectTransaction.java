package com.ruike.wms.domain.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruike.itf.domain.entity.ItfObjectTransactionIface;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * WmsObjectTransaction
 *
 * @author liyuan.lv@hand-china.com 2020/04/09 11:51
 */
@ApiModel("δΊε‘ηζ")
@VersionAudit
@ModifyAudit
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "wms_object_transaction")
public class WmsObjectTransaction extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TRANSACTION_ID = "transactionId";
    public static final String FIELD_TRANSACTION_TYPE_CODE = "transactionTypeCode";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_BARCODE = "barcode";
    public static final String FIELD_PLANT_CODE = "plantCode";
    public static final String FIELD_MATERIAL_CODE = "materialCode";
    public static final String FIELD_TRANSACTION_QTY = "transactionQty";
    public static final String FIELD_LOT_NUMBER = "lotNumber";
    public static final String FIELD_DELIVERY_BATCH = "deliveryBatch";
    public static final String FIELD_TRANSACTION_UOM = "transactionUom";
    public static final String FIELD_TRANSACTION_TIME = "transactionTime";
    public static final String FIELD_TRANSACTION_REASON_CODE = "transactionReasonCode";
    public static final String FIELD_ACCOUNT_DATE = "accountDate";
    public static final String FIELD_WAREHOUSE_CODE = "warehouseCode";
    public static final String FIELD_LOCATOR_CODE = "locatorCode";
    public static final String FIELD_TRANSFER_PLANT_CODE = "transferPlantCode";
    public static final String FIELD_TRANSFER_WAREHOUSE_CODE = "transferWarehouseCode";
    public static final String FIELD_TRANSFER_LOCATOR_CODE = "transferLocatorCode";
    public static final String FIELD_COSTCENTER_CODE = "costcenterCode";
    public static final String FIELD_SUPPLIER_CODE = "supplierCode";
    public static final String FIELD_SUPPLIER_SITE_CODE = "supplierSiteCode";
    public static final String FIELD_CUSTOMER_CODE = "customerCode";
    public static final String FIELD_CUSTOMER_SITE_CODE = "customerSiteCode";
    public static final String FIELD_SOURCE_DOC_TYPE = "sourceDocType";
    public static final String FIELD_SOURCE_DOC_NUM = "sourceDocNum";
    public static final String FIELD_SOURCE_DOC_LINE_NUM = "sourceDocLineNum";
    public static final String FIELD_WORK_ORDER_NUM = "workOrderNum";
    public static final String FIELD_OPERATION_SEQUENCE = "operationSequence";
    public static final String FIELD_PROD_LINE_CODE = "prodLineCode";
    public static final String FIELD_MERGE_FLAG = "mergeFlag";
    public static final String FIELD_MERGE_ID = "mergeId";
    public static final String FIELD_SPEC_STOCK_FLAG = "specStockFlag";
    public static final String FIELD_CONTAINER_ID = "containerId";
    public static final String FIELD_TRANSFER_LOT_NUMBER = "transferLotNumber";
    public static final String FIELD_CONTAINER_CODE = "containerCode";
    public static final String FIELD_TRANSFER_SO_LINE_NUM = "transferSoLineNum";
    public static final String FIELD_TRANSFER_SO_NUM = "transferSoNum";
    public static final String FIELD_SO_LINE_NUM = "soLineNum";
    public static final String FIELD_SO_NUM = "soNum";
    public static final String FIELD_PO_LINE_NUM = "poLineNum";
    public static final String FIELD_PO_NUM = "poNum";
    public static final String FIELD_MOVE_REASON = "moveReason";
    public static final String FIELD_MOVE_TYPE = "moveType";
    public static final String FIELD_BOM_RESERVE_LINE_NUM = "bomReserveLineNum";
    public static final String FIELD_BOM_RESERVE_NUM = "bomReserveNum";
    public static final String FIELD_CID = "cid";
    public static final String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
    public static final String FIELD_CREATED_BY = "createdBy";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
    public static final String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
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
    public static final String FIELD_ATTRIBUTE31 = "attribute31";
    public static final String FIELD_ATTRIBUTE32 = "attribute32";
    public static final String FIELD_ATTRIBUTE33 = "attribute33";
    public static final String FIELD_ATTRIBUTE34 = "attribute34";
    public static final String FIELD_ATTRIBUTE35 = "attribute35";
    public static final String FIELD_ATTRIBUTE36 = "attribute36";
    public static final String FIELD_ATTRIBUTE37 = "attribute37";
    public static final String FIELD_ATTRIBUTE38 = "attribute38";
    public static final String FIELD_ATTRIBUTE39 = "attribute39";
    public static final String FIELD_ATTRIBUTE40 = "attribute40";
    public static final String FIELD_SOURCE_DOC_ID = "sourceDocId";
    public static final String FIELD_SOURCE_DOC_LINE_ID = "sourceDocLineId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_MATERIAL_ID = "materialId";
    public static final String FIELD_LOCATOR_ID = "locatorId";
    public static final String FIELD_TRANSFER_LOCATOR_ID = "transferLocatorId";
    public static final String FIELD_PLANT_ID = "plantId";
    public static final String FIELD_REMARK = "remark";
    public static final String FIELD_SALE_DOC_ID = "saleDocId";
    public static final String FIELD_SALE_DOC_LINE_ID = "saleDocLineId";
    public static final String FIELD_TRANSFER_SALE_DOC_ID = "transferSaleDocId";
    public static final String FIELD_TRANSFER_SALE_DOC_LINE_ID = "transferSaleDocLineId";
    public static final String FIELD_INSIDE_ORDER = "insideOrder";
    public static final String FIELD_MAKE_ORDER_NUM = "makeOrderNum";
    public static final String FIELD_OWNER_TYPE = "ownerType";
    public static final String FIELD_WAREHOUSE_ID = "warehouseId";
    public static final String FIELD_TRANSFER_WAREHOUSE_ID = "transferWarehouseId";
    public static final String FIELD_TRANSFER_PLANT_ID = "transferPlantId";
    public static final String FIELD_SN_NUM = "snNum";
    public static final String FIELD_GMCODE = "gmcode";

    //
    // δΈε‘ζΉζ³(ζpublic protected privateι‘ΊεΊζε)
    // ------------------------------------------------------------------------------

    /**
     * ηζζ±ζ»ζ°ζ?
     *
     * @param detail ζη»ζ°ζ?
     * @return com.ruike.itf.domain.entity.ItfObjectTransactionIface
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/11 09:57:02
     */
    public static ItfObjectTransactionIface createSummary(WmsObjectTransaction detail) {
        ItfObjectTransactionIface iface = new ItfObjectTransactionIface();
        BeanUtils.copyProperties(detail, iface, FIELD_TRANSACTION_ID, FIELD_EVENT_ID, FIELD_LOCATOR_CODE, FIELD_TRANSFER_LOCATOR_CODE,
                FIELD_TRANSACTION_TIME, FIELD_TRANSACTION_QTY, FIELD_MERGE_ID, FIELD_CONTAINER_ID, FIELD_SOURCE_DOC_ID, FIELD_SOURCE_DOC_LINE_ID,
                FIELD_MATERIAL_LOT_ID, FIELD_MATERIAL_ID, FIELD_LOCATOR_ID, FIELD_TRANSFER_LOCATOR_ID, FIELD_PLANT_ID,
                FIELD_SALE_DOC_ID, FIELD_SALE_DOC_LINE_ID, FIELD_TRANSFER_SALE_DOC_ID, FIELD_TRANSFER_SALE_DOC_LINE_ID,
                FIELD_WAREHOUSE_ID, FIELD_TRANSFER_WAREHOUSE_ID, FIELD_TRANSFER_PLANT_ID, FIELD_CREATED_BY,
                FIELD_CREATION_DATE, FIELD_LAST_UPDATE_DATE, FIELD_LAST_UPDATED_BY, FIELD_OBJECT_VERSION_NUMBER, FIELD_CID);

        //ε¦ζδΊε‘θ‘¨ε·₯εδΈΊη©Ί,ει¨θ?’εδΈδΈΊη©Ί,εζει¨θ?’εε·ε½εε·₯εε· modify by yuchao.wang for kang.wang at 2020.9.15
        if (StringUtils.isBlank(iface.getWorkOrderNum()) && StringUtils.isNotBlank(detail.getInsideOrder())){
            iface.setWorkOrderNum(detail.getInsideOrder());
        }
        return iface;
    }
    //
    // ζ°ζ?εΊε­ζ?΅
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "η§ζ·ID", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty("δΊε‘ID")
    @Id
    @GeneratedValue
    private String transactionId;
    @ApiModelProperty(value = "δΊε‘η±»εηΌη ", required = true)
    @NotBlank
    private String transactionTypeCode;
    @ApiModelProperty(value = "δΊδ»ΆID", required = true)
    @NotBlank
    private String eventId;
    @ApiModelProperty(value = "δΊε‘ζ‘η ")
    private String barcode;
    @ApiModelProperty(value = "ε·₯εηΌη ", required = true)
    @NotBlank
    private String plantCode;
    @ApiModelProperty(value = "η©ζηΌη ", required = true)
    @NotBlank
    private String materialCode;
    @ApiModelProperty(value = "δΊε‘ζ°ι", required = true)
    @NotNull
    private BigDecimal transactionQty;
    @ApiModelProperty(value = "ζΉζ¬‘ε·")
    private String lotNumber;
    @ApiModelProperty(value = "ζ₯ζΆζΉ")
    private String deliveryBatch;
    @ApiModelProperty(value = "δΊε‘εδ½")
    private String transactionUom;
    @ApiModelProperty(value = "δΊε‘ζΆι΄", required = true)
    @NotNull
    private Date transactionTime;
    @ApiModelProperty(value = "δΊε‘εε ")
    private String transactionReasonCode;
    @ApiModelProperty(value = "θ?°θ΄¦ζ₯ζ")
    private Date accountDate;
    @ApiModelProperty(value = "δ»εΊηΌη ")
    private String warehouseCode;
    @ApiModelProperty(value = "θ΄§δ½ηΌη ")
    private String locatorCode;
    @ApiModelProperty(value = "η?ζ ε·₯εηΌη ")
    private String transferPlantCode;
    @ApiModelProperty(value = "η?ζ δ»εΊηΌη ")
    private String transferWarehouseCode;
    @ApiModelProperty(value = "η?ζ θ΄§δ½ηΌη ")
    private String transferLocatorCode;
    @ApiModelProperty(value = "ζζ¬δΈ­εΏηΌη ")
    private String costcenterCode;
    @ApiModelProperty(value = "δΎεΊεηΌη ")
    private String supplierCode;
    @ApiModelProperty(value = "δΎεΊεε°ηΉηΌη ")
    private String supplierSiteCode;
    @ApiModelProperty(value = "ε?’ζ·ηΌη ")
    private String customerCode;
    @ApiModelProperty(value = "ε?’ζ·ε°ηΉηΌη ")
    private String customerSiteCode;
    @ApiModelProperty(value = "ζ₯ζΊεζ?η±»ε")
    private String sourceDocType;
    @ApiModelProperty(value = "ζ₯ζΊεζ?ε·")
    private String sourceDocNum;
    @ApiModelProperty(value = "ζ₯ζΊεζ?θ‘ε·")
    private String sourceDocLineNum;
    @ApiModelProperty(value = "ε·₯εε·")
    private String workOrderNum;
    @ApiModelProperty(value = "ε·₯εΊε·")
    private String operationSequence;
    @ApiModelProperty(value = "ηδΊ§ηΊΏηΌη ")
    private String prodLineCode;
    @ApiModelProperty(value = "ζ―ε¦εεΉΆ", required = true)
    @NotBlank
    private String mergeFlag;
    @ApiModelProperty(value = "ζ₯ε£ζ±ζ»ηζ°ζ?ID")
    private String mergeId;
    @ApiModelProperty(value = "ε?Ήε¨ID")
    private String specStockFlag;
    @ApiModelProperty(value = "ε?Ήε¨ID")
    private String containerId;
    @ApiModelProperty(value = "η?ζ ζΉζ¬‘")
    private String transferLotNumber;
    @ApiModelProperty(value = "ε?Ήε¨ηΌη ")
    private String containerCode;
    @ApiModelProperty(value = "η?ζ ιε?θ?’εθ‘ε·")
    private String transferSoLineNum;
    @ApiModelProperty(value = "η?ζ ιε?θ?’ε")
    private String transferSoNum;
    @ApiModelProperty(value = "ιε?θ?’εθ‘ε·")
    private String soLineNum;
    @ApiModelProperty(value = "ιε?θ?’ε")
    private String soNum;
    @ApiModelProperty(value = "ιθ΄­θ?’εθ‘ε·")
    private String poLineNum;
    @ApiModelProperty(value = "ιθ΄­θ?’εε·")
    private String poNum;
    @ApiModelProperty(value = "η§»ε¨εε ")
    private String moveReason;
    @ApiModelProperty(value = "η§»ε¨η±»ε")
    private String moveType;
    @ApiModelProperty(value = "ι’η/ιζ±ηι‘Ήη?ηΌε·")
    private String bomReserveLineNum;
    @ApiModelProperty(value = "ι’η/ιζ±ηηΌε·")
    private String bomReserveNum;
    @ApiModelProperty(value = "", required = true)
    @NotNull
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
    @ApiModelProperty(value = "")
    private String attribute31;
    @ApiModelProperty(value = "")
    private String attribute32;
    @ApiModelProperty(value = "")
    private String attribute33;
    @ApiModelProperty(value = "")
    private String attribute34;
    @ApiModelProperty(value = "")
    private String attribute35;
    @ApiModelProperty(value = "")
    private String attribute36;
    @ApiModelProperty(value = "")
    private String attribute37;
    @ApiModelProperty(value = "")
    private String attribute38;
    @ApiModelProperty(value = "")
    private String attribute39;
    @ApiModelProperty(value = "")
    private String attribute40;
    @ApiModelProperty(value = "")
    private String sourceDocId;
    @ApiModelProperty(value = "")
    private String sourceDocLineId;
    @ApiModelProperty(value = "")
    private String materialLotId;
    @ApiModelProperty(value = "")
    private String materialId;
    @ApiModelProperty(value = "")
    private String locatorId;
    @ApiModelProperty(value = "")
    private String transferLocatorId;
    @ApiModelProperty(value = "")
    private String plantId;
    @ApiModelProperty(value = "")
    private String remark;
    @ApiModelProperty(value = "ιε?θ?’εid")
    private String saleDocId;
    @ApiModelProperty(value = "ιε?θ?’εθ‘id")
    private String saleDocLineId;
    @ApiModelProperty(value = "η?ζ ιε?θ?’εid")
    private String transferSaleDocId;
    @ApiModelProperty(value = "η?ζ ιε?θ?’εθ‘id")
    private String transferSaleDocLineId;
    @ApiModelProperty(value = "ει¨θ?’εε·")
    private String insideOrder;
    @ApiModelProperty(value = "εΆι θ?’εε·")
    private String makeOrderNum;
    @ApiModelProperty(value = "ζζθη±»ε")
    private String ownerType;
    @ApiModelProperty(value = "δ»εΊId")
    private String warehouseId;
    @ApiModelProperty(value = "η?ζ δ»εΊId")
    private String transferWarehouseId;
    @ApiModelProperty(value = "η?ζ ε·₯εId")
    private String transferPlantId;
    @ApiModelProperty(value = "SN")
    private String snNum;
    @ApiModelProperty(value = "SAPη§»ε¨δΊη©ειδ»£η ")
    private String gmcode;

    //
    // ιζ°ζ?εΊε­ζ?΅

}
