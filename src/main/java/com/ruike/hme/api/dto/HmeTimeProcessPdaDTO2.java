package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO2;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeTimeProcessPdaDTO2
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 15:06:24
 **/
@Data
public class HmeTimeProcessPdaDTO2 implements Serializable {
    private static final long serialVersionUID = 3056555319612234373L;

    @ApiModelProperty(value = "扫描条码", required = true)
    private String scanBarcode;

    @ApiModelProperty(value = "工艺路线ID列表", required = true)
    private List<String> operationIdList;

    @ApiModelProperty(value = "设备编码", required = true)
    private String equipmentCode;

    @ApiModelProperty(value = "条码信息")
    private HmeTimeProcessPdaVO2 hmeTimeProcessPdaVO2;

}
