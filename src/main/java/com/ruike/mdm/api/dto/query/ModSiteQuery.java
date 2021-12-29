package com.ruike.mdm.api.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;

/**
 * <p>
 * 站点 查询
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/12 15:13
 */
@Data
public class ModSiteQuery implements Serializable {
    private static final long serialVersionUID = 3826409192714848612L;

    @ApiModelProperty(value = "租户", hidden = true)
    @JsonIgnore
    private Long tenantId;
    @ApiModelProperty("站点名称")
    private String siteName;
    @ApiModelProperty("站点编码")
    private String siteCode;
}
