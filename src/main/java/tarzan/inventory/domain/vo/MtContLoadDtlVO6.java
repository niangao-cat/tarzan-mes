package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtContLoadDtlVO6 implements Serializable {
    private static final long serialVersionUID = -6741539933162382673L;

    @ApiModelProperty("对象类型")
    private String loadObjectType;
    @ApiModelProperty("对象值")
    private String loadObjectId;
    @ApiModelProperty("容器行")
    private Long locationRow;
    @ApiModelProperty("容器列")
    private Long locationColumn;
    @ApiModelProperty("装载顺序")
    private Long loadSequence;
    @ApiModelProperty("装载对象所在容器")
    private String containerId;
    @ApiModelProperty("装载数量")
    private Double loadQty;
    @ApiModelProperty("装载步骤")
    private String loadEoStepActualId;

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
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
        result = prime * result + ((loadObjectType == null) ? 0 : loadObjectType.hashCode());
        result = prime * result + ((loadObjectId == null) ? 0 : loadObjectId.hashCode());
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

        MtContLoadDtlVO6 other = (MtContLoadDtlVO6) obj;

        if (loadObjectType == null) {
            if (other.loadObjectType != null) {
                return false;
            }
        } else if (!loadObjectType.equals(other.loadObjectType)) {
            return false;
        }

        if (loadObjectId == null) {
            if (other.loadObjectId != null) {
                return false;
            }
        } else if (!loadObjectId.equals(other.loadObjectId)) {
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
