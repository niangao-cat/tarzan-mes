package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruike.wms.domain.vo.WmsStocktakeRangeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeWipStocktakeDocVO
 *
 * @author: chaonan.hu@hand-china.com 2021/3/3 14:44:23
 **/
@Data
public class HmeWipStocktakeDocVO implements Serializable {
    private static final long serialVersionUID = 664316806203218113L;

    @ApiModelProperty(value = "盘点单ID")
    private String stocktakeId;

    @ApiModelProperty(value = "盘点单号")
    private String stocktakeNum;

    @ApiModelProperty(value = "单据状态")
    @LovValue(value = "WMS.STOCKTAKE_STATUS", meaningField = "stocktakeStatusMeaning")
    private String stocktakeStatus;

    @ApiModelProperty(value = "单据状态含义")
    private String stocktakeStatusMeaning;

    @ApiModelProperty(value = "是否明盘")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "openFlagMeaning")
    private String openFlag;

    @ApiModelProperty(value = "是否明盘含义")
    private String openFlagMeaning;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "工厂编码")
    private String siteCode;

    @ApiModelProperty(value = "工厂名称")
    private String siteName;

    @ApiModelProperty(value = "部门ID")
    private String areaId;

    @ApiModelProperty(value = "部门编码")
    private String areaCode;

    @ApiModelProperty(value = "部门名称")
    private String areaName;

    @ApiModelProperty(value = "产线范围")
    List<String> prodLineRangeList;

    @ApiModelProperty(value = "创建人ID")
    private String createdBy;

    @ApiModelProperty(value = "创建人名称")
    private String createdByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty(value = "最后更新人ID")
    private String lastUpdatedBy;

    @ApiModelProperty(value = "最后更新人名称")
    private String lastUpdatedByName;

    @ApiModelProperty(value = "最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "备注")
    private String remark;
}
