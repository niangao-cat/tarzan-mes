package com.ruike.hme.api.dto;

import java.io.Serializable;

import com.ruike.hme.domain.vo.HmeEoJobContainerVO2;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * HmeEoJobSnDTO
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobSnDTO implements Serializable {

    private static final long serialVersionUID = -3121029174651702873L;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工位编码")
    private String workcellCode;
    @ApiModelProperty("工艺ID")
    private String operationId;
    @ApiModelProperty("作业平台类型")
    private String jobType;
    @ApiModelProperty("条码")
    private String barcode;
    @ApiModelProperty("物料名称")
    private String materialName;
}
