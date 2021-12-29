package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-11-13 09:55
 */
public class MtExtendVO11 implements Serializable {
    private static final long serialVersionUID = -7622393013927080745L;
    @ApiModelProperty("主键ID")
    private String keyId;
    @ApiModelProperty("最新历史ID")
    private String latestHisId;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getLatestHisId() {
        return latestHisId;
    }

    public void setLatestHisId(String latestHisId) {
        this.latestHisId = latestHisId;
    }
}
