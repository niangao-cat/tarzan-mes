package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.entity.MtStocktakeActualHis;

/**
 * 盘点实绩历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtStocktakeActualHisRepository
                extends BaseRepository<MtStocktakeActualHis>, AopProxy<MtStocktakeActualHisRepository> {

    /**
     * 记录盘点指令实绩历史
     *
     * @author benjamin
     * @date 2019-07-04 17:24
     * @param tenantId 租户Id
     * @param mtStocktakeActual MtStocktakeActual
     * @param eventId 事件Id
     */
    void saveStocktakeActualHistory(Long tenantId, MtStocktakeActual mtStocktakeActual, String eventId);

    /**
     * stocktakeActualHisQuery-查询盘点实绩历史
     *
     * @author benjamin
     * @date 2019-07-23 09:16
     * @param tenantId 租户Id
     * @param queryActualHis MtStocktakeActualHis
     * @return List
     */
    List<MtStocktakeActualHis> stocktakeActualHisQuery(Long tenantId, MtStocktakeActualHis queryActualHis);
}
