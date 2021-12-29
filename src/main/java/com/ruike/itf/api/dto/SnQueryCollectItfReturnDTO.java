package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SnQueryCollectItfDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/24 9:34
 * @Version 1.0
 **/
@Data
public class SnQueryCollectItfReturnDTO implements Serializable {
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;

    @ApiModelProperty(value = "sn")
    private String sn;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "Fac物料编码")
    private String facMaterialCode;

    @ApiModelProperty(value = "COS数量")
    private Long  cosNum;

    @ApiModelProperty(value = "结果")
    private List<SnQueryCollectItfReturnDTO2> resultList;

    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;
}
