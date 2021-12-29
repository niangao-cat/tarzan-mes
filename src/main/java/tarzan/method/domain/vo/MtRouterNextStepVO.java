package tarzan.method.domain.vo;

import java.io.Serializable;

import tarzan.method.domain.entity.MtRouterNextStep;

/**
 * @Author: chuang.yang
 * @Date: 2019/5/31 9:39
 * @Description:
 */
public class MtRouterNextStepVO extends MtRouterNextStep implements Serializable {
    private static final long serialVersionUID = -6376185991645406622L;

    private String keyStepFlag; // 关键步骤标识

    public String getKeyStepFlag() {
        return keyStepFlag;
    }

    public void setKeyStepFlag(String keyStepFlag) {
        this.keyStepFlag = keyStepFlag;
    }
}
