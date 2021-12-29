package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2019/7/22 15:35
 */
public class MtExtendVO7 implements Serializable {

    private static final long serialVersionUID = 8374092666147989842L;
    private List<String> sqlList;
    private List<String> kidList;

    public List<String> getSqlList() {
        return sqlList;
    }

    public void setSqlList(List<String> sqlList) {
        this.sqlList = sqlList;
    }

    public List<String> getKidList() {
        return kidList;
    }

    public void setKidList(List<String> kidList) {
        this.kidList = kidList;
    }
}
