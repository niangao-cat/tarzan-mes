package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-14 09:16
 */
public class MtNumrangeVO8 implements Serializable {
    private static final long serialVersionUID = -1803952372785597337L;
    @ApiModelProperty(value = "对象编码列表")
    private List<String> numberList;
    @ApiModelProperty(value = "返回预警消息")
    private String warningMessage;

    public List<String> getNumberList() {
        return numberList;
    }

    public void setNumberList(List<String> numberList) {
        this.numberList = numberList;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }
}
