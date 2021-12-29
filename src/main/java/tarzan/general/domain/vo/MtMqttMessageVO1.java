package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Leeloing
 * @date 2019/7/2 15:03
 */
public class MtMqttMessageVO1 implements Serializable {
    private static final long serialVersionUID = -3570235967359064578L;

    @JsonProperty("TagGroupCode")
    private String tagGroupCode;

    @JsonProperty("PlcName")
    private String plcName;

    @JsonProperty("WkcCode")
    private String wkcCode;

    @JsonProperty("Tags")
    private List<MtMqttMessageVO2> tags;

    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public String getWkcCode() {
        return wkcCode;
    }

    public void setWkcCode(String wkcCode) {
        this.wkcCode = wkcCode;
    }

    public List<MtMqttMessageVO2> getTags() {
        return tags;
    }

    public void setTags(List<MtMqttMessageVO2> tags) {
        this.tags = tags;
    }
}
