package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeCosDegreeTestActualVO
 *
 * @author: chaonan.hu@hand-china.com 2021-11-12 15:02
 **/
@Data
public class HmeCosDegreeTestActualVO3 implements Serializable {
    private static final long serialVersionUID = -4104831211684356923L;

    @ApiModelProperty("主键")
    private String degreeTestId;

    @ApiModelProperty("COS类型")
    @LovValue(value = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;

    @ApiModelProperty("COS类型含义")
    private String cosTypeMeaning;

    @ApiModelProperty("WAFER")
    private String wafer;

    @ApiModelProperty("测试对象")
    @LovValue(value = "HME.COS_TEST_OBJECT", meaningField = "testObjectMeaning")
    private String testObject;

    @ApiModelProperty("测试对象含义")
    private String testObjectMeaning;

    @ApiModelProperty("测试状态")
    @LovValue(value = "HME.COS_DEGREE_TEST_STATUS", meaningField = "testStatusMeaning")
    private String testStatus;

    @ApiModelProperty("测试状态含义")
    private String testStatusMeaning;

    @ApiModelProperty("测试数量")
    private BigDecimal testQty;

    @ApiModelProperty("目标数量")
    private BigDecimal targetQty;

    @ApiModelProperty("测试良率")
    private BigDecimal testRate;

    @ApiModelProperty("放行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date releaseDate;

    @ApiModelProperty("放行人")
    private Long releaseBy;

    @ApiModelProperty("放行人姓名")
    private String releaseByName;
}
