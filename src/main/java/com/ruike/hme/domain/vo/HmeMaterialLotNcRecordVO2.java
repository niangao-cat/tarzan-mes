package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeMaterialLotNcRecordVO
 * @Description 来料不良明细视图
 * @Date 2020/8/21 9:37
 * @Author yuchao.wang
 */
@Data
public class HmeMaterialLotNcRecordVO2 implements Serializable {
    private static final long serialVersionUID = 8137431226098889147L;

    public HmeMaterialLotNcRecordVO2(){}

    public HmeMaterialLotNcRecordVO2(String ncLoadId, String ncRecordId, String position, String ncCode, String ncDesc, String workcellId, String operationId){
        this.ncLoadId = ncLoadId;
        this.ncRecordId = ncRecordId;
        this.position = position;
        this.ncCode = ncCode;
        this.ncDesc = ncDesc;
        this.workcellId = workcellId;
        this.operationId = operationId;
    }

    @ApiModelProperty(value = "ncLoadId")
    private String ncLoadId;

    @ApiModelProperty(value = "ncRecordId")
    private String ncRecordId;

    @ApiModelProperty(value = "不良编码")
    private String position;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良描述")
    private String ncDesc;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "工位")
    private String workcellId;
}