package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WmsMaterialReturnDetailDTO
 * @Deacription 明细查询传入
 * @Author ywz
 * @Date 2020/4/23 9:15
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsMaterialReturnDetailDTO implements Serializable {
    private static final long serialVersionUID = 1459910705215783679L;

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;

    @ApiModelProperty(value = "单据行ID")
    private String instructionId;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料批id")
    private List<String> materialLotIdList;


}
