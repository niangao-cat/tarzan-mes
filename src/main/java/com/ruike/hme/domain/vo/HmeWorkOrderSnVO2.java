package com.ruike.hme.domain.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * HmeWorkOrderSnVO2
 *
 * @author liyuan.lv@hand-china.com 2020/06/10 9:45
 */
@Data
public class HmeWorkOrderSnVO2 implements Serializable {

    private static final long serialVersionUID = 1189974459268318576L;

    private String eoId;
    private String materialId;
    private String uomId;
}
