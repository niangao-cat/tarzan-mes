package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeWipStocktakeRange;
import com.ruike.hme.domain.repository.HmeWipStocktakeRangeRepository;
import com.ruike.hme.infra.mapper.HmeWipStocktakeRangeMapper;
import org.apache.commons.lang.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

/**
 * 在制盘点范围 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@Component
public class HmeWipStocktakeRangeRepositoryImpl extends BaseRepositoryImpl<HmeWipStocktakeRange> implements HmeWipStocktakeRangeRepository {
    private final HmeWipStocktakeRangeMapper mapper;

    public HmeWipStocktakeRangeRepositoryImpl(HmeWipStocktakeRangeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public int save(HmeWipStocktakeRange record) {
        if (StringUtils.isBlank(record.getStocktakeRangeId())) {
            return this.insert(record);
        } else {
            return mapper.updateByPrimaryKeySelective(record);
        }
    }
}
