package tarzan.stocktake.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.stocktake.domain.entity.MtStocktakeRange;
import tarzan.stocktake.domain.repository.MtStocktakeRangeRepository;
import tarzan.stocktake.infra.mapper.MtStocktakeRangeMapper;

/**
 * 盘点范围表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
@Component
public class MtStocktakeRangeRepositoryImpl extends BaseRepositoryImpl<MtStocktakeRange>
                implements MtStocktakeRangeRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtStocktakeRangeMapper mtStocktakeRangeMapper;

    @Override
    public List<MtStocktakeRange> stocktakeRangeQuery(Long tenantId, MtStocktakeRange dto) {
        if (StringUtils.isEmpty(dto.getStocktakeId()) && StringUtils.isEmpty(dto.getStocktakeRangeId())) {
            throw new MtException("MT_STOCKTAKE_0007",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0007", "STOCKTAKE",
                                            "stocktakeId ", "stocktakeRangeId", "【API:stocktakeRangeQuery】"));
        }
        dto.setTenantId(tenantId);
        return mtStocktakeRangeMapper.select(dto);
    }
}
