package com.ruike.wms.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WmsMaterialVO
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 16:55
 */
@Data
public class WmsMaterialVO implements Serializable {

    private static final long serialVersionUID = -2307102155968052702L;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "批次类型")
    private String lotType;
    @ApiModelProperty(value = "时效")
    private String availableTime;
}
