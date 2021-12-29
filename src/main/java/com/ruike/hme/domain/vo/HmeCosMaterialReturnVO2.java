package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/12 13:54
 */
@Data
public class HmeCosMaterialReturnVO2 implements Serializable {

    private static final long serialVersionUID = 9140040351886273629L;

    @ApiModelProperty(value = "目标条码编码")
    private String targetMaterialLotCode;

    @ApiModelProperty(value = "目标条码Id")
    private String targetMaterialLotId;

    @ApiModelProperty(value = "热沉退料列表")
    private List<HmeCosReturnScanLineVO> hotSinkList;

    @ApiModelProperty(value = "打线退料列表")
    private List<HmeCosReturnScanLineVO> wireBondList;

    @ApiModelProperty(value = "芯片退料列表")
    private List<HmeCosReturnScanLineVO> cosReturnList;

}
