package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leeloing
 * @date 2019/7/2 15:24
 */
public class MtMqttMessageVO3 implements Serializable {
    private static final long serialVersionUID = -9207044557915409037L;
    private String tagGroupId;
    private String workcellId;
    private String eoId;
    private List<MtDataRecordVO3.TagData> tagData;
    private String tagGroupType;

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public List<MtDataRecordVO3.TagData> getTagData() {
        return tagData;
    }

    public void setTagData(List<MtDataRecordVO3.TagData> tagData) {
        this.tagData = tagData;
    }

    public String getTagGroupType() {
        return tagGroupType;
    }

    public void setTagGroupType(String tagGroupType) {
        this.tagGroupType = tagGroupType;
    }
}
