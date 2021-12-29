package com.ruike.qms.api.dto;

import com.ruike.qms.domain.entity.QmsIqcLine;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @Description IQC界面  行信息查询
 * @Author tong.li
 * @Date 2020/5/12 17:18
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformIqcReturnLineDTO extends QmsIqcLine implements Serializable {
    private static final long serialVersionUID = 7009940056237034820L;

    @ApiModelProperty(value = "检验项类别含义")
    private String inspectionTypeMeaning;

    @ApiModelProperty(value = "检验水平含义")
    private String inspectionLevelsMeaning;

    @ApiModelProperty(value = "缺陷等级含义")
    private String defectLevelsMeaning;

    @ApiModelProperty(value = "检验工具含义")
    private String inspectionToolMeaning;

    @ApiModelProperty(value = "结论含义")
    private String inspectionResultMeaning;

    @ApiModelProperty(value = "ac与re值拼接")
    private String acSplitRe;

    @ApiModelProperty(value = "抽样方案类型")
    private String sampleTypeCode;

    @ApiModelProperty(value = "规格单位编码")
    private String  uomCode;

    @ApiModelProperty(value = "规格范围")
    private String standardRange;

    @ApiModelProperty(value = "规格类型")
    private String standardType;

    @ApiModelProperty(value = "明细")
    private List<QmsIqcCheckPlatformIqcReturnDetailDTO> detailList;




}
