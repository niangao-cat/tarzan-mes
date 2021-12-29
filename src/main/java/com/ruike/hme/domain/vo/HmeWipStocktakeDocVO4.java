package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeWipStocktakeRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeWipStocktakeDocVO4
 *
 * @author: chaonan.hu@hand-china.com 2021/3/7 18:03:45
 **/
@Data
public class HmeWipStocktakeDocVO4 extends HmeWipStocktakeRange implements Serializable {
    private static final long serialVersionUID = -170995747969057949L;

    @ApiModelProperty("范围对象编码")
    private String rangeObjectCode;

    @ApiModelProperty("范围对象描述")
    private String rangeObjectName;
}
