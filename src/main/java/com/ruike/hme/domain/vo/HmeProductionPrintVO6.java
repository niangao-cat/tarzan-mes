package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeProductionPrintVO6
 *
 * @author chaonan.hu@hand-china.com 2021/10/18 10:14
 */
@Data
public class HmeProductionPrintVO6 implements Serializable {
    private static final long serialVersionUID = -517621026142362629L;

    @ApiModelProperty(value = "执行作业值")
    List<HmeProductionPrintVO3> resultList;

    @ApiModelProperty(value = "执行作业所对应的质量文件头表ID关系")
    List<HmeProductionPrintVO5> quantityAnalyzeDocList;

    private List<String> identificationList;
}
