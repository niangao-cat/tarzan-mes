package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeWipStocktakeActual;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeWipStocktakeActualVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/08/27 18:16:43
 **/
@Data
public class HmeWipStocktakeActualVO2 implements Serializable {
    private static final long serialVersionUID = -328807406420937104L;

    private List<String> errorMaterialLotIdList;

    private List<HmeWipStocktakeActual> hmeWipStocktakeActualList;
}
