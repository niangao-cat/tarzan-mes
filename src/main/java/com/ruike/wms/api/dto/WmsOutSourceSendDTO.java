package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 外协发货提交参数
 * @author: han.zhang
 * @create: 2020/06/22 11:47
 */
@Getter
@Setter
@ToString
public class WmsOutSourceSendDTO implements Serializable {
    private static final long serialVersionUID = -6706615374338435231L;

    @ApiModelProperty(value = "单据头id")
    private String instructionDocId;
}