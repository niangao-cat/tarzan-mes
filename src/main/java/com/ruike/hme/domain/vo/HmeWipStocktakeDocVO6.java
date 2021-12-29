package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeWipStocktakeDocVO6
 *
 * @author: chaonan.hu@hand-china.com 2021/3/8 17:31:45
 **/
@Data
public class HmeWipStocktakeDocVO6 implements Serializable {
    private static final long serialVersionUID = -8767123653233027343L;

    @ApiModelProperty("标识,为true时代表已完成盘点单,为false时代表前台需要弹窗提示")
    private boolean flag;
}
