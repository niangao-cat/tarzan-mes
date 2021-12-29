package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeCosGetChipScanBarcodeDTO
 * @Description COS取片平台-待取片容器进站条码扫描输入参数
 * @Date 2020/8/18 10:30
 * @Author yuchao.wang
 */
@Data
public class HmeCosGetChipScanBarcodeDTO implements Serializable {

    private static final long serialVersionUID = 5276961271565422661L;

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

    @ApiModelProperty("jobID")
    private String jobId;

    @ApiModelProperty("投入条码列表第一条数据，有则传")
    private HmeCosGetChipMaterialLotListResponseDTO materialLotInfo;
}