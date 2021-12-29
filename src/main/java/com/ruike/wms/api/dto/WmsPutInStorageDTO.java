package com.ruike.wms.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * WmsPutInStorageDTO
 *
 * @author liyuan.lv@hand-china.com 2020/04/03 18:25
 */

@Data
public class WmsPutInStorageDTO implements Serializable {

    private static final long serialVersionUID = 7398014942584934750L;

    @ApiModelProperty("单据Num")
    private String instructionDocNum;
    @ApiModelProperty("是否启用")
    private String enableDocFlag;
    @ApiModelProperty("条码")
    private String barCode;
    @ApiModelProperty("货位code")
    private String locatorCode;
}
