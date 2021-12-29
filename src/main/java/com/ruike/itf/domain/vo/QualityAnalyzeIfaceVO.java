package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

import static com.ruike.itf.infra.constant.ItfConstant.ProcessStatus.ERROR;
import static com.ruike.itf.infra.constant.ItfConstant.ProcessStatus.NEW;

/**
 * <p>
 * 质量文件解析接口
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/6 13:58
 */
@Data
public class QualityAnalyzeIfaceVO {

    public static final String TYPE_G = "G";
    public static final String TYPE_D = "D";

    @ApiModelProperty(value = "文件路径")
    private String filePath;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "处理日期")
    private Date processDate;
    @ApiModelProperty(value = "处理消息")
    private String processMessage;
    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;

    @ApiModelProperty(value = "文件行列表")
    private List<QualityAnalyzeLineIfaceVO> lineList;

    public QualityAnalyzeIfaceVO(String filePath, List<QualityAnalyzeLineIfaceVO> lineList) {
        this.filePath = filePath;
        this.lineList = lineList;
        this.type = lineList.get(0).getType();
        this.processDate = new Date();
        this.processStatus = NEW;
    }

    public void setErrorMessage(String processMessage) {
        if (StringUtils.isNotBlank(processMessage)) {
            this.processStatus = ERROR;
            this.processMessage = processMessage;
        }
    }
}
