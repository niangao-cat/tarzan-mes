package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by slj on 2019-01-08.
 */
public class MtEoVO5 implements Serializable {

    private static final long serialVersionUID = 7826321629229313779L;
    private String bomId;
    private String routerId;
    private List<String> eoType;
    private List<String> status;
    private String productionLineId;
    private String materialId;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public List<String> getEoType() {
        return eoType;
    }

    public void setEoType(List<String> eoType) {
        this.eoType = eoType;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }
}
