package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmeWoJobSnDTO6
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/10/26 9:56
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnDTO6 extends HmeCosOperationRecord implements Serializable {
    private static final long serialVersionUID = -1874745262417906372L;

    @ApiModelProperty("容器类型")
    private String containerTypeCode;

    @ApiModelProperty("来源条码")
    private String materialLotId;

    @ApiModelProperty(value = "转移目标")
    private List<HmeWoJobSnDTO7> targetList;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

    @ApiModelProperty(value = "工段id")
    private String wkcLinetId;

    @ApiModelProperty(value = "班次id")
    private String wkcShiftId;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工序Id")
    private String operationId;

}
