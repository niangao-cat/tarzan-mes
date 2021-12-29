package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.vo.MtContLoadDtlVO1;
import tarzan.order.domain.entity.MtEo;
import com.ruike.hme.domain.entity.HmeEoJobContainer;

/**
 * description
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobContainerVO2 extends HmeEoJobContainer implements Serializable {

    private static final long serialVersionUID = -3346821357897210486L;
    private Double maxLoadQty;
    private List<MtMaterialLot> materialLotList;
}
