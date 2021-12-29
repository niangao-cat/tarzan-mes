package tarzan.material.domain.vo;

import java.io.Serializable;

import tarzan.material.domain.entity.MtUom;

/**
 * Created by slj on 2018-11-16.
 */
public class MtUomVO extends MtUom implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6690439443771700414L;

    private String uomTypeDesc;
    private String processModeDesc;

    public String getUomTypeDesc() {
        return uomTypeDesc;
    }

    public void setUomTypeDesc(String uomTypeDesc) {
        this.uomTypeDesc = uomTypeDesc;
    }

    public String getProcessModeDesc() {
        return processModeDesc;
    }

    public void setProcessModeDesc(String processModeDesc) {
        this.processModeDesc = processModeDesc;
    }

}
