package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-10-16 20:10
 */
public class MtBomComponentVO16 implements Serializable {

    private static final long serialVersionUID = 896882294614800396L;
    @ApiModelProperty("BOM主键Id")
    private String bomId;
    @ApiModelProperty("BOM组件Id")
    private List<String> bomComponentId;

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
}
