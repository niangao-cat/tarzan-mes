package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 确认修改VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/25 17:25
 */
@Data
public class QmsDocMaterialLotVO2 implements Serializable {

    private static final long serialVersionUID = 427798036882875933L;

    @ApiModelProperty("物料批id")
    private String materialLotId;

    @ApiModelProperty("物料批编码")
    private String materialLotCode;

    @ApiModelProperty("送货单id")
    private String instructionDocId;

    @ApiModelProperty("质检单头id")
    private String iqcHeaderId;

    @ApiModelProperty("送货单行id")
    private String instructionLineDocId;

    @ApiModelProperty("数量")
    private Long primaryUomQty;

    @ApiModelProperty("物料批列表")
    private List<String> materialLotCodeList;

    @ApiModelProperty("物料批信息列表")
    private List<QmsDocMaterialLotVO> materialLotVOList;

}
