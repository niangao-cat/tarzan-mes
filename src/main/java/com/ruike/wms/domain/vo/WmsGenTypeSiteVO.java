package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname GenTypeSite
 * @Description TODO
 * @Date 2019/12/13 15:27
 * @Author admin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WmsGenTypeSiteVO {

    @ApiModelProperty("类型组")
    private String typeGroup;
    @ApiModelProperty("类型编码")
    private String typeCode;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("站点名称")
    private String siteName;
}
