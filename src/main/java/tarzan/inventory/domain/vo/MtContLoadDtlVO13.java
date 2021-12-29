package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Leeloing
 * @date 2019-10-22 17:36
 */
public class MtContLoadDtlVO13 implements Serializable {
    private static final long serialVersionUID = -696604171939792658L;
    private String loadObjectId;
    private String loadObjectType;

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    public MtContLoadDtlVO13() {}

    public MtContLoadDtlVO13(String loadObjectId, String loadObjectType) {
        this.loadObjectId = loadObjectId;
        this.loadObjectType = loadObjectType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtContLoadDtlVO13 that = (MtContLoadDtlVO13) o;
        return Objects.equals(loadObjectId, that.loadObjectId) && Objects.equals(loadObjectType, that.loadObjectType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loadObjectId, loadObjectType);
    }
}
