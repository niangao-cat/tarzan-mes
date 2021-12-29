package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 售后返品拆机
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 17:10:57
 */
@Data
public class HmeServiceSplitMaterialLotVO implements Serializable {

    private static final long serialVersionUID = 7362696386936130111L;

    @ApiModelProperty("拆机ID")
    private String splitRecordId;
    @ApiModelProperty("售后接收信息id")
    private String serviceReceiveId;
    @ApiModelProperty("组件编码")
    private String materialLotCode;
    @ApiModelProperty("顶层售后返品拆机主键")
    private String topSplitRecordId;

    public HmeServiceSplitMaterialLotVO() {
    }

    public HmeServiceSplitMaterialLotVO(String splitRecordId, String serviceReceiveId, String materialLotCode, String topSplitRecordId) {
        this.splitRecordId = splitRecordId;
        this.serviceReceiveId = serviceReceiveId;
        this.materialLotCode = materialLotCode;
        this.topSplitRecordId = topSplitRecordId;
    }
}
