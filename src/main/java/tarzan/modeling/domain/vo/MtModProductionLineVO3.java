package tarzan.modeling.domain.vo;

import java.io.Serializable;

/**
 * Created by slj on 2018-12-02.
 */
public class MtModProductionLineVO3 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7873129373382702738L;
    private String prodLineId;
    private String prodLineCode;
    private String prodLineName;
    private String description;
    private String prodLineType;
    private String prodLineTypeDesc;
    private String enableFlag;

    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    public String getProdLineCode() {
        return prodLineCode;
    }

    public void setProdLineCode(String prodLineCode) {
        this.prodLineCode = prodLineCode;
    }

    public String getProdLineName() {
        return prodLineName;
    }

    public void setProdLineName(String prodLineName) {
        this.prodLineName = prodLineName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProdLineType() {
        return prodLineType;
    }

    public void setProdLineType(String prodLineType) {
        this.prodLineType = prodLineType;
    }

    public String getProdLineTypeDesc() {
        return prodLineTypeDesc;
    }

    public void setProdLineTypeDesc(String prodLineTypeDesc) {
        this.prodLineTypeDesc = prodLineTypeDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
