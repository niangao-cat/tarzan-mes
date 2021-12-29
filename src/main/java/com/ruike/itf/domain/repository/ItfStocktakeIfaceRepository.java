package com.ruike.itf.domain.repository;

import com.ruike.itf.domain.vo.ItfStocktakeVO;
import com.ruike.itf.domain.vo.ItfStocktakeVO3;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/6 18:27
 */
public interface ItfStocktakeIfaceRepository {

    /**
     * 盘点单查询
     *
     * @author sanfeng.zhang@hand-china.com 2021/7/6 18:32
     * @return java.util.List<com.ruike.itf.domain.vo.ItfStocktakeVO>
     */
    List<ItfStocktakeVO> queryStocktakeList(Long tenantId);

    /**
     * 盘点单-更新
     *
     * @param stocktakeVOList
     * @author sanfeng.zhang@hand-china.com 2021/7/6 19:00
     * @return java.util.List<com.ruike.itf.domain.vo.ItfStocktakeVO3>
     */
    List<ItfStocktakeVO3> updateStocktakeList(List<ItfStocktakeVO> stocktakeVOList);
}
