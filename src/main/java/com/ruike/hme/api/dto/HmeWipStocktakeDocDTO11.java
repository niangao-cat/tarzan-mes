package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO4;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO11
 *
 * @author: chaonan.hu@hand-china.com 2021-03-07 18:07:34
 **/
@Data
public class HmeWipStocktakeDocDTO11 implements Serializable {
    private static final long serialVersionUID = -5203485666430986566L;

    @ApiModelProperty(value = "盘点单ID", required = true)
    private String stocktakeId ;

    @ApiModelProperty(value = "盘点范围类型", required = true)
    private String rangeObjectType;

    @ApiModelProperty(value = "新增数据集合", required = true)
    private List<HmeWipStocktakeDocVO4> addList;

}
