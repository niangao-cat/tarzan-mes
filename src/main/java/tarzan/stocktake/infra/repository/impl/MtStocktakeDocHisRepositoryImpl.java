package tarzan.stocktake.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.stocktake.domain.entity.MtStocktakeDocHis;
import tarzan.stocktake.domain.repository.MtStocktakeDocHisRepository;
import tarzan.stocktake.infra.mapper.MtStocktakeDocHisMapper;

/**
 * 盘点单据历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
@Component
public class MtStocktakeDocHisRepositoryImpl extends BaseRepositoryImpl<MtStocktakeDocHis>
                implements MtStocktakeDocHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtStocktakeDocHisMapper mtStocktakeDocHisMapper;

    @Override
    public List<MtStocktakeDocHis> stocktakeDocHisQuery(Long tenantId, MtStocktakeDocHis queryDocHis) {
        if (StringUtils.isEmpty(queryDocHis.getStocktakeHisId()) && StringUtils.isEmpty(queryDocHis.getStocktakeId())) {
            throw new MtException("MT_STOCKTAKE_0007",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0007", "STOCKTAKE",
                                            "stocktakeId", "stocktakeHisId", "【API:stocktakeDocHisQuery】"));
        }

        MtStocktakeDocHis docHis = new MtStocktakeDocHis();
        docHis.setTenantId(tenantId);
        docHis.setStocktakeHisId(queryDocHis.getStocktakeHisId());
        docHis.setStocktakeId(queryDocHis.getStocktakeId());
        docHis.setEventId(queryDocHis.getEventId());

        return mtStocktakeDocHisMapper.select(docHis);
    }
}
