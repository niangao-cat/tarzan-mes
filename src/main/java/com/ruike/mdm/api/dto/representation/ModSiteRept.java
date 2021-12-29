package com.ruike.mdm.api.dto.representation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 站点 展示
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/12 15:08
 */
@Data
public class ModSiteRept implements Serializable {
    private static final long serialVersionUID = 2588318240547841782L;

    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("站点名称")
    private String siteName;
    @ApiModelProperty("站点编码")
    private String siteCode;

}
