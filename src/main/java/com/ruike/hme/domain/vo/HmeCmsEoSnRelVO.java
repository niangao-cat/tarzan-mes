package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.io.Serializable;

@Data
@ExcelSheet(zh = "CMS-EO_SN关系表")
public class HmeCmsEoSnRelVO implements Serializable {
    private static final long serialVersionUID = -3398751682390521266L;
    @ApiModelProperty("主键")
    private String cmsEoSnRelId;

    @ApiModelProperty("光纤EO")
    @ExcelColumn(zh = "光纤EO", order = 0)
    private String identification;

    @ApiModelProperty("盖板SN")
    @ExcelColumn(zh = "盖板SN", order = 1)
    private String snNum;


}
