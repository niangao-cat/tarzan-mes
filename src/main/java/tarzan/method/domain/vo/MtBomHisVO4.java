package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtBomHisVO4 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8922841381354195403L;
    private String bomHisId;
    private List<String> bomComponentHisId = null;
    private List<String> bomSubstituteGroupHisId = null;
    private List<String> bomSubstituteHisId = null;
    private List<String> bomReferencePointHisId = null;

    public String getBomHisId() {
        return bomHisId;
    }

    public void setBomHisId(String bomHisId) {
        this.bomHisId = bomHisId;
    }

    public List<String> getBomComponentHisId() {
        return bomComponentHisId;
    }

    public void setBomComponentHisId(List<String> bomComponentHisId) {
        this.bomComponentHisId = bomComponentHisId;
    }

    public List<String> getBomSubstituteGroupHisId() {
        return bomSubstituteGroupHisId;
    }

    public void setBomSubstituteGroupHisId(List<String> bomSubstituteGroupHisId) {
        this.bomSubstituteGroupHisId = bomSubstituteGroupHisId;
    }

    public List<String> getBomSubstituteHisId() {
        return bomSubstituteHisId;
    }

    public void setBomSubstituteHisId(List<String> bomSubstituteHisId) {
        this.bomSubstituteHisId = bomSubstituteHisId;
    }

    public List<String> getBomReferencePointHisId() {
        return bomReferencePointHisId;
    }

    public void setBomReferencePointHisId(List<String> bomReferencePointHisId) {
        this.bomReferencePointHisId = bomReferencePointHisId;
    }

}
