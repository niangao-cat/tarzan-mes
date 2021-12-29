package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.List;

public class MtEventObjectTypeRelVO6 implements Serializable {
    private static final long serialVersionUID = 6052085057083749296L;

    private String objectTypeId;
    private String objectTypeCode;
    private String objectDescription;
    private List<MtEventObjectColumnValueVO> columnValueList;

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public String getObjectDescription() {
        return objectDescription;
    }

    public void setObjectDescription(String objectDescription) {
        this.objectDescription = objectDescription;
    }

    public List<MtEventObjectColumnValueVO> getColumnValueList() {
        return columnValueList;
    }

    public void setColumnValueList(List<MtEventObjectColumnValueVO> columnValueList) {
        this.columnValueList = columnValueList;
    }
}
