package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeWoJobSnReturnDTO4
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/13 14:08
 * @Version 1.0
 **/
@Data
public class HmeWoJobSnReturnDTO4 implements Serializable {
    private static final long serialVersionUID = 6297958668183290697L;

    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "cos类型")
    private String cosType;
    @ApiModelProperty(value = "出站时间")
    private String siteOutDate;
    @ApiModelProperty(value = "已入数量")
    private String primaryUonQty;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "来料批次")
    private String jobBatch;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "来料批次")
    private Double snQty;
    @ApiModelProperty(value = "备注")
    private String chipNum;



    @ApiModelProperty(value = "eojobId")
    private String eoJobId;

}
