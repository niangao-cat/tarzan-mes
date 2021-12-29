package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName HmeMonthlyPlanDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 11:41
 * @Version 1.0
 **/
@Data
public class HmeMonthlyPlanDTO implements Serializable{

    private static final long serialVersionUID = 5997976012435753843L;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("物料编码")
    private List<String> materialCodeList;

    @ApiModelProperty("查询月份")
    private String queryMonth;

    @ApiModelProperty("查询月份起")
    private String queryMonthFrom;

    @ApiModelProperty("查询月份至")
    private String queryMonthTo;

    @ApiModelProperty("制造部")
    private String areaId;

    @ApiModelProperty("年月")
    @JsonIgnore
    private String yearMonth;

    @ApiModelProperty("站点")
    @JsonIgnore
    private String siteId;

    @ApiModelProperty("产线ID列表")
    @JsonIgnore
    private List<String> prodLineIdList;
}
