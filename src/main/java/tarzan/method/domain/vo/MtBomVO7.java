package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import tarzan.method.domain.entity.MtBom;

/**
 * Created by slj on 2019-04-27.
 */
public class MtBomVO7 extends MtBom implements Serializable {

    private static final long serialVersionUID = -4969824787659634814L;

    private List<String> siteId;

    public List<String> getSiteId() {
        return siteId;
    }

    public void setSiteId(List<String> siteId) {
        this.siteId = siteId;
    }

}
