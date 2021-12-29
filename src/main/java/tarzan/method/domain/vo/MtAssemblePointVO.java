package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtAssemblePointVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3738059786491088895L;
    private String assemblePointId;
    private String assemblePointCode;
    private Long sequence;

    public String getAssemblePointId() {
        return assemblePointId;
    }

    public void setAssemblePointId(String assemblePointId) {
        this.assemblePointId = assemblePointId;
    }

    public String getAssemblePointCode() {
        return assemblePointCode;
    }

    public void setAssemblePointCode(String assemblePointCode) {
        this.assemblePointCode = assemblePointCode;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

}
