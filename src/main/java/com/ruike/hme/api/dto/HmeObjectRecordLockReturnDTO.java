package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据记录锁
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 17:10:57
 */
@Data
public class HmeObjectRecordLockReturnDTO implements Serializable {

    private static final long serialVersionUID = 7362696386936130111L;

    @ApiModelProperty(value = "锁ID")
    private String lockId;
    @ApiModelProperty(value = "功能名称")
    private String functionName;
    @ApiModelProperty(value = "来源设备(PC,PDA)")
    private String deviceCode;
    @ApiModelProperty(value = "记录类型(容器,单据,条码)")
    @LovValue(value = "HME.LOCK_OBJECT_TYPE", meaningField = "objectTypeMeaning")
    private String objectType;
    @ApiModelProperty(value = "记录类型(容器,单据,条码)说明")
    private String objectTypeMeaning;
    @ApiModelProperty(value = "锁定记录ID")
    private String objectRecordId;
    @ApiModelProperty(value = "锁定记录编码")
    private String objectRecordCode;
    @ApiModelProperty(value = "锁定时间")
    private Date creationDate;
    @ApiModelProperty(value = "锁定人")
    private Long createdBy;
    @ApiModelProperty(value = "锁定名称")
    private String createdByName;
    @ApiModelProperty(value = "失效时间")
    private Date expireDate;
}
