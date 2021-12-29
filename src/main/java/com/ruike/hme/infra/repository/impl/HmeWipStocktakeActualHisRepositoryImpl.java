package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeWipStocktakeActualHis;
import com.ruike.hme.domain.repository.HmeWipStocktakeActualHisRepository;
import com.ruike.hme.infra.mapper.HmeWipStocktakeActualHisMapper;
import org.apache.commons.lang.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

/**
 * 在制盘点实际历史 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@Component
public class HmeWipStocktakeActualHisRepositoryImpl extends BaseRepositoryImpl<HmeWipStocktakeActualHis> implements HmeWipStocktakeActualHisRepository {
    private final HmeWipStocktakeActualHisMapper mapper;

    public HmeWipStocktakeActualHisRepositoryImpl(HmeWipStocktakeActualHisMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public int save(HmeWipStocktakeActualHis record) {
        if (StringUtils.isBlank(record.getStocktakeActualHisId())) {
            return this.insert(record);
        } else {
            return mapper.updateByPrimaryKeySelective(record);
        }
    }
}
