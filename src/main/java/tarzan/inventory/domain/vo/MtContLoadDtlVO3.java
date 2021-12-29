package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContLoadDtlVO3 implements Serializable {
    private static final long serialVersionUID = -2973123022680945864L;

    private String loadContainerId; // 执行作业ID
    private Long locationRow; // 容器行
    private Long locationColumn; // 容器列
    private Long loadSequence; // 装载次序
    private String upperContainerId; // 容器

    public String getLoadContainerId() {
        return loadContainerId;
    }

    public void setLoadContainerId(String loadContainerId) {
        this.loadContainerId = loadContainerId;
    }

    public Long getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(Long locationRow) {
        this.locationRow = locationRow;
    }

    public Long getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(Long locationColumn) {
        this.locationColumn = locationColumn;
    }

    public Long getLoadSequence() {
        return loadSequence;
    }

    public void setLoadSequence(Long loadSequence) {
        this.loadSequence = loadSequence;
    }

    public String getUpperContainerId() {
        return upperContainerId;
    }

    public void setUpperContainerId(String upperContainerId) {
        this.upperContainerId = upperContainerId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((loadContainerId == null) ? 0 : loadContainerId.hashCode());
        result = prime * result + ((locationRow == null) ? 0 : locationRow.hashCode());
        result = prime * result + ((locationColumn == null) ? 0 : locationColumn.hashCode());
        result = prime * result + ((loadSequence == null) ? 0 : loadSequence.hashCode());
        result = prime * result + ((upperContainerId == null) ? 0 : upperContainerId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MtContLoadDtlVO3 other = (MtContLoadDtlVO3) obj;
        if (loadContainerId == null) {
            if (other.loadContainerId != null) {
                return false;
            }
        } else if (!loadContainerId.equals(other.loadContainerId)) {
            return false;
        }
        if (locationRow == null) {
            if (other.locationRow != null) {
                return false;
            }
        } else if (!locationRow.equals(other.locationRow)) {
            return false;
        }
        if (locationColumn == null) {
            if (other.locationColumn != null) {
                return false;
            }
        } else if (!locationColumn.equals(other.locationColumn)) {
            return false;
        }
        if (loadSequence == null) {
            if (other.loadSequence != null) {
                return false;
            }
        } else if (!loadSequence.equals(other.loadSequence)) {
            return false;
        }
        if (upperContainerId == null) {
            if (other.upperContainerId != null) {
                return false;
            }
        } else if (!upperContainerId.equals(other.upperContainerId)) {
            return false;
        }
        return true;
    }
}
