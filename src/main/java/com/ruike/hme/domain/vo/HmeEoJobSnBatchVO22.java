package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import lombok.Data;

@Data
public class HmeEoJobSnBatchVO22 implements Serializable {
    private static final long serialVersionUID = -6710141990815462653L;
    BigDecimal virtualSnQtySum;
    List<HmeEoJobSnLotMaterial> virtualComponentMaterialList;
}
