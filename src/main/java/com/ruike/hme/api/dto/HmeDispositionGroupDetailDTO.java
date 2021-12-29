package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description 处置方法组头行数据DTO
 *
 * @author quan.luo@hand-china.com 2020/11/24 14:13
 */
@Data
public class HmeDispositionGroupDetailDTO implements Serializable {

    private static final long serialVersionUID = -1772470257842736411L;

    @ApiModelProperty(value = "处置组id")
    private String dispositionGroupId;

    @ApiModelProperty(value = "处置组编码", required = true)
    private String dispositionGroup;

    @ApiModelProperty(value = "处置组描述")
    private String description;

    @ApiModelProperty(value = "生产站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "行数据", required = true)
    private List<HmeDispositionFunctionDTO> hmeDispositionFunctionDtoList;
}
