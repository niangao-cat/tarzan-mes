package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 容器基本信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/16 16:21
 */
@Data
public class WmsContainerVO {
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("容器编码")
    private String containerCode;
    @ApiModelProperty("容器名称")
    private String containerName;
    @ApiModelProperty("站点")
    private String siteId;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("顶层容器ID")
    private String topContainerId;
    @ApiModelProperty("上层容器ID")
    private String currentContainerId;
}
