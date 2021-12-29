package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Classname CodeIdentifyDTO
 * @Description 条码识别DTO
 * @Date 2019/10/4 16:30
 * @Author zhihao.sang
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsCodeIdentifyDTO {

    @ApiModelProperty("扫描的条码code")
    private String code;
    @ApiModelProperty(value = "条码类型")
    private String codeType;
    @ApiModelProperty(value = "条码ID")
    private String codeId;
    @ApiModelProperty(value = "容器类型")
    private String containerType;
    @ApiModelProperty(value = "装载容器ID")
    private String loadingContainerId;
    @ApiModelProperty(value = "容器类型CODE")
    private String containerTypeCode;
}
