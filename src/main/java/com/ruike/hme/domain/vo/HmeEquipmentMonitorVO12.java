package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeEquipmentMonitorVO12
 *
 * @author chaonan.hu@hand-china.com 2020/07/22 16:18:53
 */
@Data
public class HmeEquipmentMonitorVO12 implements Serializable {
    private static final long serialVersionUID = -2326964546435009954L;

    @ApiModelProperty(value = "停机设备详情")
    private HmeEquipmentMonitorVO9 downEquipmentDetail;

    @ApiModelProperty(value = "30天内异常历史")
    private List<HmeEquipmentMonitorVO10> exceptionHistoryList;

    @ApiModelProperty(value = "同异常类型最近三次")
    private List<HmeEquipmentMonitorVO11> sameExceptionTypeList;
}
