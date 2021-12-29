package tarzan.method.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtBomSiteAssignDTO2 implements Serializable {
    private static final long serialVersionUID = -2256232642867546443L;

    @ApiModelProperty("装配清单Id列表")
    private List<String> bomIds;
    @ApiModelProperty("是否有效标识")
    private String enableFlag;

    public List<String> getBomIds() {
        return bomIds;
    }

    public void setBomIds(List<String> bomIds) {
        this.bomIds = bomIds;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
