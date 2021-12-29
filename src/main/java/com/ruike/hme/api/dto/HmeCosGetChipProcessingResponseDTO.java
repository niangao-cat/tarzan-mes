package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeMaterialLotVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;
import java.util.*;

/**
 * @Classname HmeCosGetChipProcessingResponseDTO
 * @Description 查询进行中数据
 * @Date 2020/8/24 20:28
 * @Author yuchao.wang
 */
@Data
public class HmeCosGetChipProcessingResponseDTO extends HmeCosGetChipScanBarcodeResponseDTO {
    private static final long serialVersionUID = 7429406298926330788L;

    @ApiModelProperty("OK条码信息")
    List<HmeMaterialLotVO> siteOutOkList;

    @ApiModelProperty("不良条码信息")
    List<HmeMaterialLotVO> siteOutNgList;
}