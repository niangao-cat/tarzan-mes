package tarzan.stocktake.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.stocktake.domain.entity.MtStocktakeDocHis;

/**
 * 盘点单据历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
public interface MtStocktakeDocHisRepository
                extends BaseRepository<MtStocktakeDocHis>, AopProxy<MtStocktakeDocHisRepository> {

    /**
     * stocktakeDocHisQuery-获取盘点单据历史
     *
     * @author benjamin
     * @date 2019-07-23 09:08
     * @param tenantId 租户Id
     * @param queryDocHis MtStocktakeDocHis
     * @return List
     */
    List<MtStocktakeDocHis> stocktakeDocHisQuery(Long tenantId, MtStocktakeDocHis queryDocHis);
}
