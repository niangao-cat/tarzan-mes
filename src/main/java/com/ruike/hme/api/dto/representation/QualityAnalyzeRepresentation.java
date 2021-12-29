package com.ruike.hme.api.dto.representation;

import com.ruike.hme.domain.vo.QualityAnalyzeRepresentationLineVO;
import io.choerodon.core.domain.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 质量文件分析 展现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/8 09:58
 */
@Data
public class QualityAnalyzeRepresentation implements Serializable {
    private static final long serialVersionUID = 3630646914472685282L;

    @ApiModelProperty(value = "动态标题")
    private List<String> dynamicTitleList;

    @ApiModelProperty(value = "数据行")
    private Page<QualityAnalyzeRepresentationLineVO> lineList;

    public QualityAnalyzeRepresentation(List<String> dynamicTitleList, Page<QualityAnalyzeRepresentationLineVO> lineList) {
        this.dynamicTitleList = dynamicTitleList;
        this.lineList = lineList;
    }
}
