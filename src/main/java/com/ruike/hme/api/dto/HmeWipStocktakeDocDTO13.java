package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO13
 *
 * @author: chaonan.hu@hand-china.com 2021-03-09 19:53:32
 **/
@Data
public class HmeWipStocktakeDocDTO13 implements Serializable {

    @ApiModelProperty(value = "产线ID集合", required = true)
    private List<String> prodLineIdList;

    @ApiModelProperty(value = "工序编码")
    private String workcellCode;

    @ApiModelProperty(value = "工序名称")
    private String workcellName;

}
