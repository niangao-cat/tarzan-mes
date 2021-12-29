package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by slj on 2019-01-08.
 */
public class MtEoVO4 implements Serializable {

    private static final long serialVersionUID = 8019403394039233039L;
    private String eoId;
    private List<String> demandStatus;

    public List<String> getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(List<String> demandStatus) {
        this.demandStatus = demandStatus;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

}
