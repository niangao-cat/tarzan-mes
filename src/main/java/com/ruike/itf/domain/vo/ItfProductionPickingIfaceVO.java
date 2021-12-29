package com.ruike.itf.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/08/11 14:00
 */
@Data
public class ItfProductionPickingIfaceVO implements Serializable {

    private static final long serialVersionUID = -3692640452993723383L;

    @JSONField(name = "ZDONU")
    private String ZDONU;

    @JSONField(name = "ZPSELP")
    private String ZPSELP;

    @JSONField(name = "ZQTY")
    private String ZQTY;
}
