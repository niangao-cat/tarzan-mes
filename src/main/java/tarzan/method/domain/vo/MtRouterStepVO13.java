package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/11/7 19:39
 * @Author: ${yiyang.xie}
 */
public class MtRouterStepVO13 implements Serializable {
    private static final long serialVersionUID = 7469196318817612534L;

    @ApiModelProperty("执行作业唯一标识")
    private String eoId;
    @ApiModelProperty("是否考虑未经过及未完工的工艺路线步骤")
    private String unStartFlag;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getUnStartFlag() {
        return unStartFlag;
    }

    public void setUnStartFlag(String unStartFlag) {
        this.unStartFlag = unStartFlag;
    }
}
