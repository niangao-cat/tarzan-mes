package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/6 14:06
 */
@Data
public class HmeServiceSplitRecordVO3 implements Serializable {

    private static final long serialVersionUID = 4175492535001516891L;

    @ApiModelProperty(value = "接收SN")
    private String snNum;

    @ApiModelProperty(value = "工厂编码")
    private String plantCode;

    @ApiModelProperty(value = "接收时间")
    private Date receiveDate;

    @ApiModelProperty(value = "返回类型")
    private String backType;

    @ApiModelProperty(value = "区域编码")
    private String areaCode;

}
