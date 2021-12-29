package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2019/9/18 11:19
 * @Author: {yiyang.xie@hand-china.com}
 */
public class MtInstructionActualVO1 implements Serializable {
    private static final long serialVersionUID = -1857415800219594365L;
    /**
     * 指令实绩ID
     */
    private String actualId;

    /**
     * 指令实绩历史ID
     */
    private String actualHisId;

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

    public String getActualHisId() {
        return actualHisId;
    }

    public void setActualHisId(String actualHisId) {
        this.actualHisId = actualHisId;
    }
}
