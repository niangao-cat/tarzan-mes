package tarzan.modeling.api.dto;

import java.io.Serializable;
/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModLocatorDTO4 implements Serializable {
    private static final long serialVersionUID = -8641715204860666625L;
    private String sourceLocatorId;
    private String targetLocatorId;

    public String getTargetLocatorId() {
        return targetLocatorId;
    }

    public void setTargetLocatorId(String targetLocatorId) {
        this.targetLocatorId = targetLocatorId;
    }


    public String getSourceLocatorId() {
        return sourceLocatorId;
    }

    public void setSourceLocatorId(String sourceLocatorId) {
        this.sourceLocatorId = sourceLocatorId;
    }
}
