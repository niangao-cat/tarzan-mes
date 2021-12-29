package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO4;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO12
 *
 * @author: chaonan.hu@hand-china.com 2021-03-08 17:11:42
 **/
@Data
public class HmeWipStocktakeDocDTO12 implements Serializable {

    @ApiModelProperty(value = "盘点单集合", required = true)
    private List<HmeWipStocktakeDocVO> hmeWipStocktakeDocList ;

}
