package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeLoadJobDTO3
 *
 * @author: chaonan.hu@hand-china.com 2021/2/4 09:47:25
 **/
@Data
public class HmeLoadJobDTO3 implements Serializable {
    private static final long serialVersionUID = -6352675107034288009L;

    private String siteId;

    private String workcellId;

    private String operationId;

    private List<String> assetEncodingList;
}
