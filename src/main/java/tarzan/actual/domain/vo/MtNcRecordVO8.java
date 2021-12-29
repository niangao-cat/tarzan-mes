package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2019/9/17 18:02
 * @Description:
 */
public class MtNcRecordVO8 implements Serializable {
    private static final long serialVersionUID = -1896332954714185493L;

    /**
     * 不良记录
     */
    private String ncRecordId;
    /**
     * 不良记录历史ID
     */
    private String ncRecordHisId;

    public String getNcRecordId() {
        return ncRecordId;
    }

    public void setNcRecordId(String ncRecordId) {
        this.ncRecordId = ncRecordId;
    }

    public String getNcRecordHisId() {
        return ncRecordHisId;
    }

    public void setNcRecordHisId(String ncRecordHisId) {
        this.ncRecordHisId = ncRecordHisId;
    }
}
