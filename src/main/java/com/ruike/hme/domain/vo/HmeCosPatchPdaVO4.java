package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCosPatchPdaVO4
 *
 * @author: chaonan.hu@hand-china.com 2020/8/29 11:38:47
 **/
@Data
public class HmeCosPatchPdaVO4 implements Serializable {
    private static final long serialVersionUID = -1943557782793130472L;

    private String materialId;

    private String materialCode;

    private String materialName;

    private BigDecimal qty;

    private String uomCode;

    @ApiModelProperty(value = "1代表解绑，2代表成功")
    private Long flag;

    private String materialLotId;

    private String materialLotCode;
}
