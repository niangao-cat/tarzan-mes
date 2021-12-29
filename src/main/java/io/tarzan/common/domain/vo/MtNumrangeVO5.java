package io.tarzan.common.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/6/26 17:50
 */
public class MtNumrangeVO5 implements Serializable {

    private static final long serialVersionUID = -6306051107698058403L;
    private String number;
    private String warningMessage;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }
}
