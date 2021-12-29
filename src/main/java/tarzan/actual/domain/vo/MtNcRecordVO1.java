package tarzan.actual.domain.vo;

import java.io.Serializable;

public class MtNcRecordVO1 implements Serializable {


    private static final long serialVersionUID = 1571070346409235240L;

    private String eoId;// 执行作业

    private String ncCodeId;// 不良代码

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }
}
