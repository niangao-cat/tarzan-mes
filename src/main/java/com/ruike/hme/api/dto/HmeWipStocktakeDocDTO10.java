package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO4;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO10
 *
 * @author: chaonan.hu@hand-china.com 2021-03-07 18:07:34
 **/
@Data
public class HmeWipStocktakeDocDTO10 implements Serializable {
    private static final long serialVersionUID = -1281650215122176712L;

    @ApiModelProperty(value = "盘点单ID", required = true)
    private String stocktakeId ;

    @ApiModelProperty(value = "盘点范围类型", required = true)
    private String rangeObjectType;

    @ApiModelProperty(value = "删除数据集合", required = true)
    private List<HmeWipStocktakeDocVO4> deleteList;

    @ApiModelProperty(value = "确认标识，只有用户点击确认时，传Y，其他情况传N", required = true)
    private String confirmStr;

}
