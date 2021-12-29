package com.ruike.hme.domain.vo;

import java.io.Serializable;

import tarzan.actual.domain.vo.MtEoRouterActualVO1;

public class HmeMtEoRouterActualVO5 extends MtEoRouterActualVO1 implements Serializable {
    private static final long serialVersionUID = 2757317767278452744L;
    private String subRouterFlag; // 是否为分支工艺路线标识
    private String sourceEoStepActualId;
    private String eoId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getSubRouterFlag() {
        return subRouterFlag;
    }

    public void setSubRouterFlag(String subRouterFlag) {
        this.subRouterFlag = subRouterFlag;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }
}
