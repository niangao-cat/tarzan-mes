package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/13 18:32
 */
@Data
public class HmeEoJobSnVO7 extends HmeEoJobSnVO4 implements Serializable {

    private static final long serialVersionUID = -1435444503211835759L;

    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shiftDate;
    @ApiModelProperty(value = "班次")
    private String shiftCode;
}
