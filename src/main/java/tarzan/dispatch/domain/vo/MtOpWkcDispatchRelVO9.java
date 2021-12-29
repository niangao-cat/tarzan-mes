package tarzan.dispatch.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author : MrZ
 * @date : 2020-02-10 11:46
 **/
public class MtOpWkcDispatchRelVO9 implements Serializable {
    private static final long serialVersionUID = 5144802274307219589L;
    private List<String> workcellIdList; // 工作单元
    private String productionLineId; // 生产线
    private String siteId; // 站点

    public List<String> getWorkcellIdList() {
        return workcellIdList;
    }

    public void setWorkcellIdList(List<String> workcellIdList) {
        this.workcellIdList = workcellIdList;
    }

    public String getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(String productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

}
