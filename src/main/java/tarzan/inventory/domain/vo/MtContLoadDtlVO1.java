package tarzan.inventory.domain.vo;

import java.io.Serializable;

public class MtContLoadDtlVO1 implements Serializable {
    private static final long serialVersionUID = -334652868015379290L;

    private String eoId; // 执行作业ID
    private Long locationRow; // 容器行
    private Long locationColumn; // 容器列
    private Long loadSequence; // 装载次序
    private String containerId; // 容器
    private Double loadQty; // 装载数量
    private String loadEoStepActualId; // 装载步骤


    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
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

    public Double getLoadQty() {
        return loadQty;
    }

    public void setLoadQty(Double loadQty) {
        this.loadQty = loadQty;
    }

    public String getLoadEoStepActualId() {
        return loadEoStepActualId;
    }

    public void setLoadEoStepActualId(String loadEoStepActualId) {
        this.loadEoStepActualId = loadEoStepActualId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eoId == null) ? 0 : eoId.hashCode());
        result = prime * result + ((locationRow == null) ? 0 : locationRow.hashCode());
        result = prime * result + ((locationColumn == null) ? 0 : locationColumn.hashCode());
        result = prime * result + ((loadSequence == null) ? 0 : loadSequence.hashCode());
        result = prime * result + ((containerId == null) ? 0 : containerId.hashCode());
        result = prime * result + ((loadQty == null) ? 0 : loadQty.hashCode());
        result = prime * result + ((loadEoStepActualId == null) ? 0 : loadEoStepActualId.hashCode());
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
        MtContLoadDtlVO1 other = (MtContLoadDtlVO1) obj;
        if (eoId == null) {
            if (other.eoId != null) {
                return false;
            }
        } else if (!eoId.equals(other.eoId)) {
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
        if (loadQty == null) {
            if (other.loadQty != null) {
                return false;
            }
        } else if (!loadQty.equals(other.loadQty)) {
            return false;
        }
        if (loadEoStepActualId == null) {
            if (other.loadEoStepActualId != null) {
                return false;
            }
        } else if (!loadEoStepActualId.equals(other.loadEoStepActualId)) {
            return false;
        }
        return true;
    }
}
