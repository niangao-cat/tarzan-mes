package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 偏振度和发散角良率维护头历史表查询
 *
 * @author wengang.qiang@hand-china.com 2021/09/14 11:30
 */
@Data
public class HmeTagPassRateHeaderAndLineHisVO implements Serializable {

    private static final long serialVersionUID = -1185049229452107798L;

    @ApiModelProperty(value = "cos类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;

    @ApiModelProperty(value = "测试对象")
    @LovValue(lovCode = "HME.COS_TEST_OBJECT", meaningField = "testObjectMeaning")
    private String testObject;

    @ApiModelProperty(value = "测试类型")
    @LovValue(lovCode = "HME.TEST_TYPE", meaningField = "testTypeMeaning")
    private String testType;

    @ApiModelProperty(value = "测试数量")
    private Long testQty;

    @ApiModelProperty(value = "良率")
    private BigDecimal passRate;

    @ApiModelProperty(value = "有效性")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "cos类型描述")
    private String cosTypeMeaning;

    @ApiModelProperty(value = "测试对象描述")
    private String testObjectMeaning;

    @ApiModelProperty(value = "测试类型描述")
    private String testTypeMeaning;

    @ApiModelProperty(value = "是否有效性描述")
    private String enableFlagMeaning;

    @ApiModelProperty(value = "优先级")
    private Long priority;

    @ApiModelProperty(value = "测试总量")
    private Long testSumQty;

    @ApiModelProperty(value = "加测目标良率")
    private BigDecimal addPassRate;

    @ApiModelProperty(value = "更新人")
    private String lastUpdatedByName;

    @ApiModelProperty(value = "更新人id")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "更新时间")
    private Date lastUpdateDate;

}
