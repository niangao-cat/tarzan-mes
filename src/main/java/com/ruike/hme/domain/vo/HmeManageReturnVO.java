package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/17 16:47
 */
@Data
public class HmeManageReturnVO implements Serializable {

    private static final long serialVersionUID = -5259778412294442527L;

    @ApiModelProperty("员工号")
    private String cardId;
    @ApiModelProperty("0代表OK，1代表NG")
    private String errCode;
    @ApiModelProperty("检测时间")
    private Date dt;
}
