package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 反冲料查询参数
 *
 * @author penglin.sui@hand-china.com 2020/10/15 21:33
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeBackFlushDTO implements Serializable {
    private static final long serialVersionUID = 6136121541675890151L;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("EO")
    private String eoId;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("工艺步骤ID")
    private String eoStepId;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("库位ID")
    private String locatorId;
    @ApiModelProperty("组件ID")
    private List<String> bomComponentIdList;
}
