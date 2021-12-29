package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class MtBomComponentVO15 implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1411362384135548981L;

    @ApiModelProperty("BOM主键Id")
    private String bomId;
    @ApiModelProperty("BOM组件Id")
    private List<String> bomComponentId;
    @ApiModelProperty("BOM组件历史Id")
    private List<String> bomComponentHisId;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public List<String> getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(List<String> bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public List<String> getBomComponentHisId() {
        return bomComponentHisId;
    }

    public void setBomComponentHisId(List<String> bomComponentHisId) {
        this.bomComponentHisId = bomComponentHisId;
    }
}
