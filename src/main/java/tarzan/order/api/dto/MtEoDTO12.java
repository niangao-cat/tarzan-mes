package tarzan.order.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/1/8 11:17 上午
 */
public class MtEoDTO12 implements Serializable {
    private static final long serialVersionUID = -5368517695304294821L;
    @ApiModelProperty("EOID")
    private String eoId;
    @ApiModelProperty("EO编码")
    private String       eoNum;
    @ApiModelProperty("EO关系")
    private List<String> eoRelationStatus;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public List<String> getEoRelationStatus() {
        return eoRelationStatus;
    }

    public void setEoRelationStatus(List<String> eoRelationStatus) {
        this.eoRelationStatus = eoRelationStatus;
    }
}
