package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO
 *
 * @author: chaonan.hu@hand-china.com 2021-03-03 15:23:56
 **/
@Data
public class HmeWipStocktakeDocDTO implements Serializable {
    private static final long serialVersionUID = -7826726645844862104L;

    @ApiModelProperty(value = "盘点单号")
    private String stocktakeNum;

    @ApiModelProperty(value = "单据状态")
    private String stocktakeStatus;

    @ApiModelProperty(value = "是否明盘")
    private String openFlag;

    @ApiModelProperty(value = "工厂")
    private String siteId;

    @ApiModelProperty(value = "部门")
    private String areaId;

    @ApiModelProperty(value = "物料ID")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "产线Id")
    private String prodLineId;

    @ApiModelProperty(value = "工序Id")
    private String workcellId;

    @ApiModelProperty(value = "创建人")
    private String userId;

    @ApiModelProperty(value = "创建时间从")
    private Date createdDateFrom;

    @ApiModelProperty(value = "创建时间至")
    private Date createdDateTo;

    @ApiModelProperty(value = "工厂ID集合，后端自用")
    private List<String> siteIdList;

    @ApiModelProperty(value = "部门ID集合，后端自用")
    private List<String> areaIdList;

}
