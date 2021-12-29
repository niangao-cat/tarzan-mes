package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleGroupActualVO2 implements Serializable {

    private static final long serialVersionUID = -6228710394739707045L;
    private String assembleGroupId;
    private String assembleGroupActualId;

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getAssembleGroupActualId() {
        return assembleGroupActualId;
    }

    public void setAssembleGroupActualId(String assembleGroupActualId) {
        this.assembleGroupActualId = assembleGroupActualId;
    }

    @Override
    public String toString() {
        return "MtAssembleGroupActualVO2{" + "assembleGroupId='" + assembleGroupId + '\'' + ", assembleGroupActualId='"
                        + assembleGroupActualId + '\'' + '}';
    }
}
