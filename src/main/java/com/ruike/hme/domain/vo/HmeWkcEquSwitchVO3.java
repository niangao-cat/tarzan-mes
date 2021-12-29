package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeWkcEquSwitchVO3
 * @author: chaonan.hu@hand-china.com 2020-06-23 19:06:24
 **/
@Data
public class HmeWkcEquSwitchVO3 implements Serializable {

    private String workCellId;

    private Long completeQty;

    private Long totalQty;
}
