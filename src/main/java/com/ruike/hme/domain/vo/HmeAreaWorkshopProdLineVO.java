package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName HmeArea
 * @Description 区域-车间-产线信息
 * @Author lkj
 * @Date 2021/2/25
 */
@Data
@ApiModel("区域-车间-产线信息")
public class HmeAreaWorkshopProdLineVO {

    @ApiModelProperty("区域")
    private List<HmeAreaInfo> areaInfo;

    @ApiModelProperty("车间")
    private List<HmeAreaInfo> workshopInfo;

    @ApiModelProperty("产线")
    private List<HmeProdLineInfo> prodLineInfo;

}
