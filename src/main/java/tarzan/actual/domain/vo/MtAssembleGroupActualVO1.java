package tarzan.actual.domain.vo;

import java.io.Serializable;

/**
 * @author MrZ
 */
public class MtAssembleGroupActualVO1 implements Serializable {

    private static final long serialVersionUID = 8248135818858725143L;
    private String workcellId;
    private String assembleGroupId;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    @Override
    public String toString() {
        return "MtAssembleGroupActualVO1{" + "workcellId='" + workcellId + '\'' + ", assembleGroupId='"
                        + assembleGroupId + '\'' + '}';
    }
}
