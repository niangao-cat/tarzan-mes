package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * HmePumpPreSelectionVO13
 *
 * @author: chaonan.hu@hand-china.com 2021/09/03 16:38:51
 **/
@Data
public class HmePumpPreSelectionVO13 implements Serializable {
    private static final long serialVersionUID = 3550296051796116098L;

    @ApiModelProperty(value = "有效的筛选规则行")
    private List<HmePumpPreSelectionVO7> pumpFilterRuleLineList;

    @ApiModelProperty(value = "WPE类型计算公式的字母与SUM类型数据项的对应关系")
    private Map<String, String> letterTagIdMap;

    @ApiModelProperty(value = "WPE筛选规则行")
    private HmePumpPreSelectionVO7 wpeLine;
}
