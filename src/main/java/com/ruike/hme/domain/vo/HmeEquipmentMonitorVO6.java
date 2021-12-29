package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeEquipmentMonitorVO6
 *
 * @author chaonan.hu@hand-china.com 2020/07/17 10:11:35
 */
@Data
public class HmeEquipmentMonitorVO6 implements Serializable {
    private static final long serialVersionUID = -4815783667378223155L;

    @ApiModelProperty(value = "设备状态一览数据")
    private List<HmeEquipmentMonitorVO4> equipmentStatusList;

    @ApiModelProperty(value = "总体概况数据")
    private HmeEquipmentMonitorVO5 generalOverview;

    @ApiModelProperty(value = "当月异常停机TOP10")
    private List<HmeEquipmentMonitorVO8> abnormalEquipmentList;

    @ApiModelProperty(value = "停机设备详情")
    private HmeEquipmentMonitorVO9 downEquipmentDetail;

    @ApiModelProperty(value = "30天内异常历史")
    private List<HmeEquipmentMonitorVO10> exceptionHistoryList;

    @ApiModelProperty(value = "同异常类型最近三次")
    private List<HmeEquipmentMonitorVO11> sameExceptionTypeList;
}
