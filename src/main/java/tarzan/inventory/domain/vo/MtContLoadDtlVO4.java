package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContLoadDtlVO4 implements Serializable {
    private static final long serialVersionUID = 1526736105428735026L;

    private String materialLotId; // 执行作业ID
    private Long locationRow; // 容器行
    private Long locationColumn; // 容器列
    private Long loadSequence; // 装载次序
    private String containerId; // 容器

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
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

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((materialLotId == null) ? 0 : materialLotId.hashCode());
        result = prime * result + ((locationRow == null) ? 0 : locationRow.hashCode());
        result = prime * result + ((locationColumn == null) ? 0 : locationColumn.hashCode());
        result = prime * result + ((loadSequence == null) ? 0 : loadSequence.hashCode());
        result = prime * result + ((containerId == null) ? 0 : containerId.hashCode());
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

        MtContLoadDtlVO4 other = (MtContLoadDtlVO4) obj;
        if (materialLotId == null) {
            if (other.materialLotId != null) {
                return false;
            }
        } else if (!materialLotId.equals(other.materialLotId)) {
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
        if (containerId == null) {
            if (other.containerId != null) {
                return false;
            }
        } else if (!containerId.equals(other.containerId)) {
            return false;
        }
        return true;
    }
}
