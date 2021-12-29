package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeWoJobSnReturnDTO5;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeNcDisposePlatformVO8
 *
 * @author: chaonan.hu@hand-china.com 2021-03-18 11:21:15
 **/
@Data
public class HmeNcDisposePlatformVO8 implements Serializable {
    private static final long serialVersionUID = 2095592054791983577L;

    private String jobRecordId;

    private String tagCode;

    private BigDecimal result;
}
