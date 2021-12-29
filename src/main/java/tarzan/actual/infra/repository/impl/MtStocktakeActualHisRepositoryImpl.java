package tarzan.actual.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.entity.MtStocktakeActualHis;
import tarzan.actual.domain.repository.MtStocktakeActualHisRepository;
import tarzan.actual.infra.mapper.MtStocktakeActualHisMapper;

/**
 * 盘点实绩历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtStocktakeActualHisRepositoryImpl extends BaseRepositoryImpl<MtStocktakeActualHis>
                implements MtStocktakeActualHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtStocktakeActualHisMapper mtStocktakeActualHisMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveStocktakeActualHistory(Long tenantId, MtStocktakeActual mtStocktakeActual, String eventId) {
        MtStocktakeActualHis mtStocktakeActualHis = new MtStocktakeActualHis();
        BeanUtils.copyProperties(mtStocktakeActual, mtStocktakeActualHis);

        mtStocktakeActualHis.setTenantId(tenantId);
        mtStocktakeActualHis.setEventId(eventId);

        self().insertSelective(mtStocktakeActualHis);
    }

    @Override
    public List<MtStocktakeActualHis> stocktakeActualHisQuery(Long tenantId, MtStocktakeActualHis queryActualHis) {
        if (StringUtils.isEmpty(queryActualHis.getStocktakeActualHisId())
                        && StringUtils.isEmpty(queryActualHis.getStocktakeActualId())) {
            throw new MtException("MT_STOCKTAKE_0007",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_STOCKTAKE_0007", "STOCKTAKE",
                                            "stocktakeActualId", "stocktakeActualHisId",
                                            "【API:stocktakeActualHisQuery】"));
        }

        MtStocktakeActualHis actualHis = new MtStocktakeActualHis();
        actualHis.setTenantId(tenantId);
        actualHis.setStocktakeActualHisId(queryActualHis.getStocktakeActualHisId());
        actualHis.setStocktakeActualId(queryActualHis.getStocktakeActualId());
        actualHis.setEventId(queryActualHis.getEventId());

        return mtStocktakeActualHisMapper.select(actualHis);
    }

}
