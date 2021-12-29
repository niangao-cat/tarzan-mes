package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeWipStocktakeActualVO
 *
 * @author: chaonan.hu@hand-china.com 2021/08/27 15:13:11
 **/
@Data
public class HmeWipStocktakeActualVO implements Serializable {
    private static final long serialVersionUID = -3114230621193975151L;

    private String materialLotId;

    private String topEoId;

    private String eoId;

    private Date lastUpdateDate;
}
