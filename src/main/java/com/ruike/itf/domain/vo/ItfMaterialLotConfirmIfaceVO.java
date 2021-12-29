package com.ruike.itf.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ItfMaterialLotConfirmIfaceVO
 *
 * @author: chaonan.hu@hand-china.com 2021/07/14 10:10
 **/
@Data
public class ItfMaterialLotConfirmIfaceVO implements Serializable {
    private static final long serialVersionUID = 2305751774761001968L;
    @JSONField(name = "MATERIAL_LOT_CODE")
    private String MATERIAL_LOT_CODE;

    @JSONField(name = "MATERIAL_CODE")
    private String MATERIAL_CODE;

    @JSONField(name = "MATERIAL_VERSION")
    private String MATERIAL_VERSION;

    @JSONField(name = "UOMCODE")
    private String UOMCODE;

    @JSONField(name = "WAREHOUSE_CODE")
    private String WAREHOUSE_CODE;

    @JSONField(name = "STATUS")
    private String STATUS;

    @JSONField(name = "QUALITY_STATUS")
    private String QUALITY_STATUS;

    @JSONField(name = "QTY")
    private String QTY;

    @JSONField(name = "SO_NUM")
    private String SO_NUM;

    @JSONField(name = "SO_LINE_NUM")
    private String SO_LINE_NUM;

    @JSONField(name = "LOT_CODE")
    private String LOT_CODE;

    @JSONField(name = "PRODUCTION_DATE")
    private String PRODUCTION_DATE;

    @JSONField(name = "CONTAINER_CODE")
    private String CONTAINER_CODE;
}
