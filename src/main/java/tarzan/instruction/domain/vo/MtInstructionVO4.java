package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * instructionExecute-指令执行时返回参数使用VO类
 * 
 * @author benjamin
 * @date 2019-06-20 16:15
 */
public class MtInstructionVO4 implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = 1462173847435761713L;

    private Map<String, List<String>> actualList;

    /**
     * 事件Id
     */
    private String eventId;

    public Map<String, List<String>> getActualList() {
        return actualList;
    }

    public void setActualList(Map<String, List<String>> actualList) {
        this.actualList = actualList;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }



}
