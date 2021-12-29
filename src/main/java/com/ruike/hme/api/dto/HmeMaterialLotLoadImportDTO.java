package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description 物料批容器装载导入DTO
 *
 * @author quan.luo@hand-china.com 2020/11/23 19:37
 */
@Data
public class HmeMaterialLotLoadImportDTO implements Serializable {

    private static final long serialVersionUID = -5691895128047547949L;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "装载对象编码")
    private String loadObjectCode;
}
