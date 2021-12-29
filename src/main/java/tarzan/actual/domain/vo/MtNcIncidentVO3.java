package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/9/17 17:13
 * @Description:
 */
public class MtNcIncidentVO3 implements Serializable {
    private static final long serialVersionUID = -4165489680419607540L;

    /**
     * 不良事故
     */
    private String ncIncidentId;
    /**
     * 不良事故历史ID
     */
    private String ncIncidentHisId;

    public String getNcIncidentId() {
        return ncIncidentId;
    }

    public void setNcIncidentId(String ncIncidentId) {
        this.ncIncidentId = ncIncidentId;
    }

    public String getNcIncidentHisId() {
        return ncIncidentHisId;
    }

    public void setNcIncidentHisId(String ncIncidentHisId) {
        this.ncIncidentHisId = ncIncidentHisId;
    }
}
