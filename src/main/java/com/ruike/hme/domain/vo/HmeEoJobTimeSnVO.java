package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeEoJobTimeSnVO
 *
 * @author liyuan.lv@hand-china.com 2020/06/19 10:59
 */
@Data
public class HmeEoJobTimeSnVO implements Serializable {

    private static final long serialVersionUID = 4819528358932623623L;

    @ApiModelProperty("进炉/出炉扫描, IN代表进炉扫描，OUT代表出炉扫描")
    private String InOutType;
    @ApiModelProperty("条码")
    private String snNum;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工艺ID")
    private String operationId;
}
