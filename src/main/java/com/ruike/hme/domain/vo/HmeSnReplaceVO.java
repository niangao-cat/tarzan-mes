package com.ruike.hme.domain.vo;

import com.ruike.hme.api.dto.HmeSnReplaceDTO;
import lombok.Data;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.order.domain.vo.MtEoVO;
import tarzan.order.domain.vo.MtEoVO38;

import java.io.Serializable;
import java.util.List;

/**
 * HmeSnReplaceVO
 * @description:
 * @author: chaonan.hu@hand-china.com 2020/11/4 13:55
 **/
@Data
public class HmeSnReplaceVO implements Serializable {
    private static final long serialVersionUID = -1824367575358860629L;

    private List<MtMaterialLotVO20> mtMaterialLotVO20List;

    private List<MtEoVO> eoMessageList;

    private List<HmeSnReplaceDTO> hmeSnReplaceDTOList;

}
