package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeWipStocktakeActual;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeExecuteVO
 *
 * @author: chaonan.hu@hand-china.com 2021/8/27 18:45:21
 **/
@Data
public class HmeWipStocktakeExecuteVO implements Serializable {
    private static final long serialVersionUID = -6404038846672531409L;

    @ApiModelProperty("弹窗提示,为Y时前端弹窗,为N时不弹窗")
    private String flag;

    @ApiModelProperty("弹窗时的条码编码")
    private String materialLotCode;

    @ApiModelProperty("盘点实绩集合")
    private List<HmeWipStocktakeActual> hmeWipStocktakeActualList;
}
