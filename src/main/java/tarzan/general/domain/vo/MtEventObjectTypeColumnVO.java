package tarzan.general.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 对象类型-对象列组合VO
 * @author benjamin
 */
public class MtEventObjectTypeColumnVO implements Serializable {
    private static final long serialVersionUID = -7387906754510859430L;

    private String objectTypeCode;

    private String description;

    private String tableName;

    private String whereClause;

    private List<MtEventObjectColumnVO> eventObjectColumnList;

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public List<MtEventObjectColumnVO> getEventObjectColumnList() {
        return eventObjectColumnList;
    }

    public void setEventObjectColumnList(List<MtEventObjectColumnVO> eventObjectColumnList) {
        this.eventObjectColumnList = eventObjectColumnList;
    }
}
