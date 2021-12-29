package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 偏振度&发散角良率维护 头表查询返回字段
 *
 * @author wengang.qiang@hand-china.com 2021/09/14 10:47
 */
@Data
public class HmeTagPassRateHeaderVO implements Serializable {

    private static final long serialVersionUID = 2505220810504804053L;

    @ApiModelProperty("主键")
    private String headerId;

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

    @ApiModelProperty(value = "备注")
    private String remark;

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
}
