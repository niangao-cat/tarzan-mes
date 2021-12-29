package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 在制品盘点执行 单据展示
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 10:03
 */
@Data
public class HmeWipStocktakeExecuteDocRepresentationDTO implements Serializable {
    private static final long serialVersionUID = -8636714379653207849L;

    @ApiModelProperty("盘点单号")
    private String stocktakeNum;
    @ApiModelProperty("盘点单Id")
    private String stocktakeId;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("站点编码")
    private String siteCode;
    @ApiModelProperty("部门ID")
    private String areaId;
    @ApiModelProperty("部门编码")
    private String areaCode;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否明盘")
    private String openFlag;
    @ApiModelProperty("状态")
    @LovValue(lovCode = "WMS.STOCKTAKE_STATUS", meaningField = "stocktakeStatusMeaning")
    private String stocktakeStatus;
    @ApiModelProperty("状态含义")
    private String stocktakeStatusMeaning;
    @ApiModelProperty("创建日期")
    private Date creationDate;
}
