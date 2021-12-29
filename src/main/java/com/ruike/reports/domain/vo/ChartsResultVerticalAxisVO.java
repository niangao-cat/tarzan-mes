package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 图表返回纵向数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/29 15:15
 */
@Data
public class ChartsResultVerticalAxisVO {
    @ApiModelProperty
    private String verticalAxis;

    @ApiModelProperty("值列表")
    private List<String> valueList;
}
