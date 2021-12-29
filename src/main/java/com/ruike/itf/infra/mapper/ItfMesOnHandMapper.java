package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.ItfOnHandDTO;
import com.ruike.itf.domain.vo.MesOnHandVO;

import java.util.List;

/**
 * 查询mes现有量
 *
 * @author kejin.liu01@hand-china.com 2020/11/17 14:49
 */
public interface ItfMesOnHandMapper {

    /**
     * 查询mes所有的现有量，并且汇总
     *
     * @param dto
     * @return
     */
    List<MesOnHandVO> selectOnHand(ItfOnHandDTO dto);
}
