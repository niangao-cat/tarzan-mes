package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HmePumpPreSelectionVO8
 *
 * @author: chaonan.hu@hand-china.com 2021/09/01 17:05:12
 **/
@Data
public class HmePumpPreSelectionVO8 implements Serializable {
    private static final long serialVersionUID = 7314217694489297433L;

    @ApiModelProperty(value = "新筛选池中的条码")
    private List<String> materialLotIdList;

    @ApiModelProperty(value = "条码与其对应的数据采集项数据Map")
    private Map<String, List<HmePumpPreSelectionVO9>> materialLotJobDataRecordMap;

    @ApiModelProperty(value = "对新筛选池中的条码进行的分组")
    private List<List<String>> materialLotGroupList;

    @ApiModelProperty(value = "条码及其数据采集项及其结果数据集合")
    private List<HmePumpPreSelectionVO11> materialLotTagResultList;
}
