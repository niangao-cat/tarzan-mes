package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeMaterialLotNcRecordVO2;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.*;

/**
 * @Classname HmeCosPoorInspectionScanBarcodeResponseDTO
 * @Description 来料目检返回数据
 * @Date 2020/8/24 15:41
 * @Author yuchao.wang
 */
@Data
public class HmeCosPoorInspectionScanBarcodeResponseDTO extends HmeCosGetChipScanBarcodeResponseDTO {
    private static final long serialVersionUID = 1862716423547409743L;

    @ApiModelProperty("不良信息")
    private List<HmeMaterialLotNcRecordVO2> materialLotNcList;
}