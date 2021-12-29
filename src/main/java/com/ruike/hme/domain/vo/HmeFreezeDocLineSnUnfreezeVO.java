package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;

/**
 * <p>
 * 条码冻结单 条码解冻判定结果
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/25 10:08
 */
@Data
public class HmeFreezeDocLineSnUnfreezeVO {
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "在制标识")
    private String mfFlag;

    public static MtMaterialLotVO20 toMaterialLot(HmeFreezeDocLineSnUnfreezeVO obj) {
        MtMaterialLotVO20 sn = new MtMaterialLotVO20();
        sn.setMaterialLotId(obj.getMaterialLotId());
        sn.setFreezeFlag(NO);
        return sn;
    }
}
