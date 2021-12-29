package tarzan.inventory.domain.vo;


import java.io.Serializable;
import java.util.List;

import io.tarzan.common.domain.vo.MtExtendVO5;

/**
 * @Author: chuang.yang
 * @Date: 2019/4/17 14:23
 * @Description:
 */
public class MtContainerAttrVO3 implements Serializable {
    private static final long serialVersionUID = -6043091500038105104L;

    private String containerId;
    private String containerHisId;
    private String eventId;
    private List<MtExtendVO5> attr;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerHisId() {
        return containerHisId;
    }

    public void setContainerHisId(String containerHisId) {
        this.containerHisId = containerHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtExtendVO5> getAttr() {
        return attr;
    }

    public void setAttr(List<MtExtendVO5> attr) {
        this.attr = attr;
    }
}
