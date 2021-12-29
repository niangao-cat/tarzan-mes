package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO8
 *
 * @author: chaonan.hu@hand-china.com 2021-03-05 18:13:26
 **/
@Data
public class HmeWipStocktakeDocDTO8 implements Serializable {
    private static final long serialVersionUID = 4413773838023087024L;

    private String stocktakeNum;

    private String workcellCode;
}
