package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

/**
 * @author lijinghua
 * @Classname ZContainerInfoResultDTO
 * @Description TODO
 * @Date 2019/9/25 14:05
 * @Created by lijinghua
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsContainerInfoResultDTO implements Serializable {

    private static final long serialVersionUID = 7127811365905986239L;

    @ApiModelProperty(value = "物流器具编码")
    private String containercode;

    @ApiModelProperty(value = "物流器具类型")
    private String containerType;

    @ApiModelProperty(value = "物流器具名称")
    private String containerName;

    @ApiModelProperty(value = "有效性")
    private Boolean enableFlag;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "工厂描述")
    private String siteName;

    @ApiModelProperty(value = "状态")
    @LovValue(value = "Z.CONTAINER.STATUS", meaningField = "containerStatusMeaning")
    private String status;

    @ApiModelProperty(value = "容器状态meaning")
    private String containerStatusMeaning;

    @ApiModelProperty(value = "对象列表")
    private List<WmsContainerDetailResultDTO> details;

}
