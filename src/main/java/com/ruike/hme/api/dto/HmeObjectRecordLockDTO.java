package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;

/**
 * 数据记录锁
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 17:10:57
 */
@Data
public class HmeObjectRecordLockDTO implements Serializable {

    private static final long serialVersionUID = 7362696386936130111L;

    @ApiModelProperty(value = "功能名称")
    private String functionName;
    @ApiModelProperty(value = "来源设备(PC,PDA)",required = true)
    private String deviceCode;
    @ApiModelProperty(value = "记录类型(容器,单据,条码)",required = true)
    private String objectType;
    @ApiModelProperty(value = "锁定记录ID",required = true)
    private String objectRecordId;
    @ApiModelProperty(value = "锁定记录编码",required = true)
    private String objectRecordCode;
}
