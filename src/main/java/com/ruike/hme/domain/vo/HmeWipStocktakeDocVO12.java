package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeWipStocktakeDocVO12
 *
 * @author: chaonan.hu@hand-china.com 2021/9/23 10:08:12
 **/
@Data
public class HmeWipStocktakeDocVO12 implements Serializable {
    private static final long serialVersionUID = 8290615959337715747L;

    private String eoId;

    private String materialLotId;
}
