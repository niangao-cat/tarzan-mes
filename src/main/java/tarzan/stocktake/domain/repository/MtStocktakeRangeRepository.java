package tarzan.stocktake.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.stocktake.domain.entity.MtStocktakeRange;

/**
 * 盘点范围表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
public interface MtStocktakeRangeRepository
                extends BaseRepository<MtStocktakeRange>, AopProxy<MtStocktakeRangeRepository> {


    /**
     * stocktakeRangeQuery-盘点范围查询
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/5/16
     */
    List<MtStocktakeRange> stocktakeRangeQuery(Long tenantId, MtStocktakeRange dto);
}
