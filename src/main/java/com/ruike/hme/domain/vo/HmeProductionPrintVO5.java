package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * HmeProductionPrintVO5
 *
 * @author chaonan.hu@hand-china.com 2021/10/15 15:43
 */
@Data
public class HmeProductionPrintVO5 implements Serializable {
    private static final long serialVersionUID = 4131657218114451119L;

    @ApiModelProperty(value = "EOID")
    private String eoId;

    @ApiModelProperty(value = "质量文件头表ID")
    private String qaDocId;

    @ApiModelProperty(value = "质量文件行表结果")
    private String result;
}
