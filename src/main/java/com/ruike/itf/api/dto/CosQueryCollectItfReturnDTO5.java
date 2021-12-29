package com.ruike.itf.api.dto;

import io.choerodon.mybatis.annotation.Cid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * CosQueryCollectItfReturnDTO5
 *
 * @author: chaonan.hu@hand-china.com 2021-10-11 11:20:21
 **/
@Data
public class CosQueryCollectItfReturnDTO5 implements Serializable {
    private static final long serialVersionUID = 4909305303057224955L;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;
    @ApiModelProperty("偏振度和发散角测试结果表主键")
    private String degreeTestId;
    @ApiModelProperty("偏振度和发散角测试结果历史表主键")
    private String degreeTestHisId;
    @ApiModelProperty(value = "cos类型")
    private String cosType;
    @ApiModelProperty(value = "wafer")
    private String wafer;
    @ApiModelProperty(value = "测试对象")
    private String testObject;
    @ApiModelProperty(value = "测试数量")
    private Long testQty;
    @ApiModelProperty(value = "目标数量")
    private Long targetQty;
    @ApiModelProperty(value = "测试良率")
    private BigDecimal testRate;
    @ApiModelProperty(value = "优先级")
    private Long priority;
    @ApiModelProperty(value = "测试状态")
    private String testStatus;
    @ApiModelProperty(value = "CID")
    private Long cid;
    @ApiModelProperty(value = "CID")
    private Long hisCid;

    private Date creationDate;

    private Long createdBy;

    private Date lastUpdateDate;

    private Long lastUpdatedBy;

    private Long objectVersionNumber;
}
