package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.*;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoRouterActualVO32
 * @description
 * @date 2019年11月11日 17:53
 */
public class MtEoRouterActualVO32 implements Serializable {
    private static final long serialVersionUID = -1179371521976758701L;
    @ApiModelProperty("当前工艺路线")
    private String currentRouterId;
    @ApiModelProperty("EO工艺路线完成标识")
    private String eoRouterCompletedFlag;
    @ApiModelProperty("主工艺路线标识")
    private String primaryRouterFlag;

    @ApiModelProperty("eoIdList")
    private List<String> eoIdList;

    public String getCurrentRouterId() {
        return currentRouterId;
    }

    public void setCurrentRouterId(String currentRouterId) {
        this.currentRouterId = currentRouterId;
    }

    public String getEoRouterCompletedFlag() {
        return eoRouterCompletedFlag;
    }

    public void setEoRouterCompletedFlag(String eoRouterCompletedFlag) {
        this.eoRouterCompletedFlag = eoRouterCompletedFlag;
    }

    public String getPrimaryRouterFlag() {
        return primaryRouterFlag;
    }

    public void setPrimaryRouterFlag(String primaryRouterFlag) {
        this.primaryRouterFlag = primaryRouterFlag;
    }

    public List<String> getEoIdList() {
        return eoIdList;
    }

    public void setEoIdList(List<String> eoIdList) {
        this.eoIdList = eoIdList;
    }
}
