package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeProductionPrintVO9
 *
 * @author chaonan.hu@hand-china.com 2021/10/19 21:16
 */
@Data
public class HmeProductionPrintVO9 implements Serializable {
    private static final long serialVersionUID = 8522301490176050201L;

    @ApiModelProperty(value = "PDF界面所需数据")
    private List<HmeProductionPrintVO8> pdfDataList;

    @ApiModelProperty(value = "打印界面表格数据")
    private List<HmeProductionPrintVO3> eoInternalCodeList;
}
