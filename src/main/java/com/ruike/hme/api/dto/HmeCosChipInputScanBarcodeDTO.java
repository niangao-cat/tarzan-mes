package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeCosChipInputScanBarcodeDTO
 * @Description COS录入-条码查询输入参数
 * @Date 2020/8/18 10:30
 * @Author yuchao.wang
 */
@Data
public class HmeCosChipInputScanBarcodeDTO implements Serializable {
    private static final long serialVersionUID = -4388961171454469107L;

    @ApiModelProperty("物料批条码")
    private String barcode;

    @ApiModelProperty("工艺路线ID")
    private String operationId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("设备ID")
    private String equipmentId;

    @ApiModelProperty("班组ID")
    private String wkcShiftId;
}