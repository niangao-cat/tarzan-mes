package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWkcEquSwitchDTO5
 * @author chaonan.hu@hand-china.com 2020-06-28 17:03:21
 **/
@Data
public class HmeWkcEquSwitchDTO5 implements Serializable {
    private static final long serialVersionUID = -6280918307745019962L;

    @ApiModelProperty(value = "作业ID")
    private String jobId;

    @ApiModelProperty(value = "工作单元Id")
    private String workcellId;

    @ApiModelProperty(value = "设备及其状态集合")
    private List<HmeWkcEquSwitchDTO6> hmeWkcEquSwitchDTO6List;

}
