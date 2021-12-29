package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeEquipmentStocktakePdaVO
 * 扫描单据返回对象VO
 * @author: chaonan.hu@hand-china.com 2021/04/01 15:57:16
 **/
@Data
public class HmeEquipmentStocktakePdaVO implements Serializable {
    private static final long serialVersionUID = -5508166255931595920L;

    @ApiModelProperty(value = "单据ID")
    private String stocktakeId;

    @ApiModelProperty(value = "单据号")
    private String stocktakeNum;

    @ApiModelProperty(value = "盘点类型")
    private String stocktakeType;

    @ApiModelProperty(value = "盘点类型含义")
    private String stocktakeTypeMeaning;

    @ApiModelProperty(value = "盘点范围")
    private String stocktakeRange;

    @ApiModelProperty(value = "保管部门ID")
    private String businessId;

    @ApiModelProperty(value = "保管部门")
    private String businessName;

    @ApiModelProperty(value = "创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
}
