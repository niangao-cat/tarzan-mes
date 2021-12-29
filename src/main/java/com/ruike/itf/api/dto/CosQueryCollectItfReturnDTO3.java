package com.ruike.itf.api.dto;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * CosQueryCollectItfReturnDTO3
 *
 * @author: chaonan.hu@hand-china.com 2021-10-11 10:25:12
 **/
@Data
public class CosQueryCollectItfReturnDTO3 implements Serializable {
    private static final long serialVersionUID = 6270528954226304943L;

    private List<HmeMaterialLotLoad> materialLotLoadList;

    @ApiModelProperty(value = "处理消息")
    private String processMessage;

    @ApiModelProperty(value = "处理状态")
    private String processStatus;
}
