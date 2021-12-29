package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeMaterialLotNcRecordVO
 * @Description 来料不良明细表视图
 * @Date 2020/8/21 9:37
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotNcRecordVO implements Serializable {
    private static final long serialVersionUID = -7830260380014041965L;

    @ApiModelProperty(value = "id")
    private String ncRecordId;

    @ApiModelProperty(value = "不良位置ID")
    private String ncLoadId;

    @ApiModelProperty(value = "不良编码")
    private String ncCode;

    @ApiModelProperty(value = "不良描述")
    private String ncDesc;
}