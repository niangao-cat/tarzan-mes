package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * description
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/05 12:30
 */
@Data
public class HmeSignInOutRecordVO2 implements Serializable {

    private static final long serialVersionUID = 5361135892876769963L;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    @ApiModelProperty(value = "日期")
    private String dateString;

    @ApiModelProperty(value = "上岗时间")
    private BigDecimal onTime;

    @ApiModelProperty(value = "下岗时间")
    private BigDecimal downTime;

    @ApiModelProperty(value = "暂停时间")
    private BigDecimal stopTime;

    @ApiModelProperty(value = "暂停原因")
    private String reasonMeaning;

    @ApiModelProperty(value = "班次")
    private String shiftCode;


}
