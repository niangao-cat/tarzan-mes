package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * distributionGroupLimitTagGroupQuery-根据收集组及收集参数获取应分发组清单 传入参数类
 * 
 * @author benjamin
 * @date 2019-07-02 17:43
 */
public class MtDataRecordVO3 implements Serializable {
    private static final long serialVersionUID = -4149949759879002410L;

    private String tagGroupId;

    private String workcellId;

    private String eoId;

    private List<TagData> tagDataList;

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

    public List<TagData> getTagDataList() {
        return tagDataList;
    }

    public void setTagDataList(List<TagData> tagDataList) {
        this.tagDataList = tagDataList;
    }

    /**
     * TAG 内部类
     * 
     * @author benjamin
     * @date 2019-07-02 17:51
     */
    public static class TagData implements Serializable {
        private static final long serialVersionUID = 4997760737604131679L;

        private String tagId;

        private String tagValue;

        private Date recordDate;

        public String getTagId() {
            return tagId;
        }

        public void setTagId(String tagId) {
            this.tagId = tagId;
        }

        public String getTagValue() {
            return tagValue;
        }

        public void setTagValue(String tagValue) {
            this.tagValue = tagValue;
        }

        public Date getRecordDate() {
            if (recordDate == null) {
                return null;
            } else {
                return (Date) recordDate.clone();
            }
        }

        public void setRecordDate(Date recordDate) {
            if (recordDate == null) {
                this.recordDate = null;
            } else {
                this.recordDate = (Date) recordDate.clone();
            }
        }
    }
}
