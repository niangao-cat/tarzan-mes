package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据记录锁
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 17:10:57
 */
@Data
public class HmeObjectRecordLockParamDTO implements Serializable {

    private static final long serialVersionUID = 7362696386936130111L;

    @ApiModelProperty(value = "记录类型(容器,单据,条码)")
    private String objectType;
    @ApiModelProperty(value = "锁定记录编码")
    private String objectRecordCode;
}
