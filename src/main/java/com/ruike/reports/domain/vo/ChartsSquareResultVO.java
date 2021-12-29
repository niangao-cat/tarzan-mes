package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 图表返回二维数据通用结果
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/29 15:06
 */
@Data
public class ChartsSquareResultVO {
    @ApiModelProperty("横轴列表")
    private List<String> horizontalAxisList;

    @ApiModelProperty("纵轴列表")
    private List<ChartsResultVerticalAxisVO> verticalAxisList;
}
