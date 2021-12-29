package com.ruike.hme.domain.vo;

import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import lombok.Data;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.hme.infra.constant.HmeConstants.Event.MATERIAL_FREE;
import static com.ruike.hme.infra.constant.HmeConstants.Event.MATERIAL_FREEZE;

/**
 * <p>
 * 条码冻结单 事务数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 14:22
 */
@Data
public class HmeFreezeDocTrxVO {
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("事件")
    private String eventId;
    @ApiModelProperty("物料id")
    private String materialId;
    @ApiModelProperty("事务数量")
    private BigDecimal transactionQty;
    @ApiModelProperty("批次号")
    private String lotNumber;
    @ApiModelProperty("事务单位")
    private String transactionUom;
    @ApiModelProperty("事务时间")
    private String transactionTime;
    @ApiModelProperty("事务原因")
    private String transactionReasonCode;
    @ApiModelProperty("工厂id")
    private String plantId;
    @ApiModelProperty("仓库Id")
    private String warehouseId;
    @ApiModelProperty("仓库编码")
    private String warehouseCode;
    @ApiModelProperty("扣减货位Id")
    private String reduceLocatorId;
    @ApiModelProperty("增加货位Id")
    private String increaseLocatorId;
    @ApiModelProperty("货位Id")
    private String locatorId;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("目标仓库编码")
    private String transferWarehouseCode;
    @ApiModelProperty("目标货位编码")
    private String transferLocatorCode;
    @ApiModelProperty("供应商编码")
    private String supplierCode;


    public static WmsObjectTransactionRequestVO freezeTransactionBuilder(HmeFreezeDocTrxVO obj) {
        WmsObjectTransactionRequestVO transaction = new WmsObjectTransactionRequestVO();
        BeanCopierUtil.copy(obj, transaction);
        transaction.setTransactionTime(new Date());
        transaction.setTransactionTypeCode(MATERIAL_FREEZE);
        // 20210720 add by sanfeng.zhang for peng.zhao 增加移动类型 写死
        transaction.setMoveType("344");
        transaction.setMergeFlag(NO);
        return transaction;
    }

    public static WmsObjectTransactionRequestVO unFreezeTransactionBuilder(HmeFreezeDocTrxVO obj) {
        WmsObjectTransactionRequestVO transaction = new WmsObjectTransactionRequestVO();
        BeanCopierUtil.copy(obj, transaction);
        transaction.setTransactionTime(new Date());
        transaction.setTransactionTypeCode(MATERIAL_FREE);
        // 20210720 add by sanfeng.zhang for peng.zhao 增加移动类型 写死
        transaction.setMoveType("343");
        transaction.setMergeFlag(NO);
        return transaction;
    }

    public static MtMaterialLotVO20 toFreezeMaterialLot(HmeFreezeDocTrxVO dto) {
        MtMaterialLotVO20 sn = new MtMaterialLotVO20();
        sn.setMaterialLotId(dto.getMaterialLotId());
        sn.setFreezeFlag(YES);
        sn.setLocatorId(dto.getIncreaseLocatorId());
        return sn;
    }

    public static MtMaterialLotVO20 toUnfreezeMaterialLot(HmeFreezeDocTrxVO dto) {
        MtMaterialLotVO20 sn = new MtMaterialLotVO20();
        sn.setMaterialLotId(dto.getMaterialLotId());
        sn.setFreezeFlag(NO);
        sn.setLocatorId(dto.getIncreaseLocatorId());
        return sn;
    }


    public static MtCommonExtendVO6 toFreezeMaterialLotAttr(HmeFreezeDocTrxVO obj) {
        MtCommonExtendVO6 attr = new MtCommonExtendVO6();
        attr.setKeyId(obj.getMaterialLotId());
        List<MtCommonExtendVO5> attrs = new ArrayList<>();
        MtCommonExtendVO5 freezeLocator = new MtCommonExtendVO5();
        freezeLocator.setAttrName("FREEZE_LOCATOR");
        freezeLocator.setAttrValue(obj.getReduceLocatorId());
        attrs.add(freezeLocator);
        attr.setAttrs(attrs);
        return attr;
    }

    public static MtCommonExtendVO6 toUnfreezeMaterialLotAttr(HmeFreezeDocTrxVO obj) {
        MtCommonExtendVO6 attr = new MtCommonExtendVO6();
        attr.setKeyId(obj.getMaterialLotId());
        List<MtCommonExtendVO5> attrs = new ArrayList<>();
        MtCommonExtendVO5 freezeLocator = new MtCommonExtendVO5();
        freezeLocator.setAttrName("FREEZE_LOCATOR");
        freezeLocator.setAttrValue("");
        attrs.add(freezeLocator);
        attr.setAttrs(attrs);
        return attr;
    }
}
