package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * COS测试良率监控 历史表查询返回数据
 *
 * @author wengang.qiang@hand-china.com 2021/09/16 20:00
 */
@Data
public class HmeCosTestMonitorRecordVO implements Serializable {

    private static final long serialVersionUID = -7872201976588168418L;

    @ApiModelProperty(value = "监控单据号")
    private String monitorDocNum;

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "HME.COS_DOC_STATUS", meaningField = "docStatusMeaning")
    private String docStatus;

    @LovValue(lovCode = "HME.COS_CHECK_STATUS", meaningField = "checkStatusMeaning")
    @ApiModelProperty(value = "审核状态")
    private String checkStatus;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "WAFER")
    private String wafer;

    @ApiModelProperty(value = "cos良率")
    private BigDecimal testPassRate;

    @ApiModelProperty(value = "放行时间")
    private Date passDate;

    @ApiModelProperty(value = "放行人")
    private Long passBy;

    @ApiModelProperty(value = "放行人姓名")
    private String passByName;

    @ApiModelProperty(value = "单据状态描述")
    private String docStatusMeaning;

    @ApiModelProperty(value = "审核状态描述")
    private String checkStatusMeaning;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
}
