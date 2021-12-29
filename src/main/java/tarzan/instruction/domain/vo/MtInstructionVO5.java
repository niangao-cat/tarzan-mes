package tarzan.instruction.domain.vo;

import java.io.Serializable;

/**
 * instructionExcuteBack-指令执行撤销 返回使用VO
 * 
 * @author benjamin
 * @date 2019-07-04 10:53
 */
public class MtInstructionVO5 implements Serializable {
    private static final long serialVersionUID = -7919535742152904178L;

    /**
     * 物料批Id
     */
    private String materialLotId;
    /**
     * 器具Id
     */
    private String containerId;

    private String actualId;

    private String eventId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getActualId() {
        return actualId;
    }

    public void setActualId(String actualId) {
        this.actualId = actualId;
    }

}
