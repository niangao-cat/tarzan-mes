package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmeWojobSnDTO4
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/13 16:34
 * @Version 1.0
 **/
@Data
public class HmeWojobSnDTO4 implements Serializable {
    private static final long serialVersionUID = -4397342875457078798L;
    @ApiModelProperty(value = "eoJobId")
    private String eoJobSnId;
    @ApiModelProperty(value = "wojobid")
    private String woJobSnId;
    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;
    @ApiModelProperty(value = "来料信息记录i")
    private String operationRecordId;
    @ApiModelProperty(value = "加工数量")
    private String processedNum;
    @ApiModelProperty(value = "工艺id")
    private String  operationId;
    @ApiModelProperty(value = "wkcID")
    private String  workcellId;
    @ApiModelProperty(value = "设备及其状态集合")
    private List<HmeWkcEquSwitchDTO6> equipmentList;
}
