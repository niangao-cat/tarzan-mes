package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * HmeCosYieldComputeVO6
 *
 * @author: chaonan.hu@hand-china.com 2021/09/18 14:11
 **/
@Data
public class HmeCosYieldComputeVO6 implements Serializable {
    private static final long serialVersionUID = -5268575139962499308L;

    @ApiModelProperty(value = "表hme_cos_test_monitor_header插入的数据")
    private List<HmeCosYieldComputeVO5> monitorHeaderInsertDataList;

    @ApiModelProperty(value = "表hme_cos_test_monitor_header更新的数据")
    private List<HmeCosYieldComputeVO5> monitorHeaderUpdateDataList;

    @ApiModelProperty(value = "表hme_cos_test_monitor_record插入的数据")
    private List<HmeCosYieldComputeVO5> monitorRecordInsertDataList;
}
