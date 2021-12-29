package tarzan.instruction.domain.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import tarzan.instruction.domain.entity.MtInstructionDoc;


/**
 * instructionDocCreate-指令单据创建 传入参数使用VO
 * 
 * @author benjamin
 * @date 2019-07-17 16:19
 */
public class MtInstructionDocVO2 extends MtInstructionDoc implements Serializable {
    private static final long serialVersionUID = 2888606261053493635L;

    private String eventRequestId;

    /**
     * 编码对象类型Code
     */
    private String numObjectTypeCode;
    /**
     * 编码传入参数列表
     */
    private Map<String, String> numCallObjectCodeList;
    /**
     * 编码参数列表
     */
    private List<String> numIncomingValueList;

    public String getNumObjectTypeCode() {
        return numObjectTypeCode;
    }

    public void setNumObjectTypeCode(String numObjectTypeCode) {
        this.numObjectTypeCode = numObjectTypeCode;
    }

    public Map<String, String> getNumCallObjectCodeList() {
        return numCallObjectCodeList;
    }

    public void setNumCallObjectCodeList(Map<String, String> numCallObjectCodeList) {
        this.numCallObjectCodeList = numCallObjectCodeList;
    }

    public List<String> getNumIncomingValueList() {
        return numIncomingValueList;
    }

    public void setNumIncomingValueList(List<String> numIncomingValueList) {
        this.numIncomingValueList = numIncomingValueList;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }
}
