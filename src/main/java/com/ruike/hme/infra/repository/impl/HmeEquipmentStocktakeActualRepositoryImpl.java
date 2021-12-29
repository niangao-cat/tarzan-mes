package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeActualRepresentation;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeActual;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeActualRepository;
import com.ruike.hme.infra.mapper.HmeEquipmentStocktakeActualMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 设备盘点实际 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
@Component
public class HmeEquipmentStocktakeActualRepositoryImpl extends BaseRepositoryImpl<HmeEquipmentStocktakeActual> implements HmeEquipmentStocktakeActualRepository {
    private final HmeEquipmentStocktakeActualMapper mapper;

    public HmeEquipmentStocktakeActualRepositoryImpl(HmeEquipmentStocktakeActualMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(HmeEquipmentStocktakeActual entity) {
        if (StringUtils.isBlank(entity.getStocktakeActualId())) {
            this.insertSelective(entity);
        } else {
            mapper.updateByPrimaryKeySelective(entity);
        }
    }

    @Override
    @ProcessLovValue
    public Page<HmeEquipmentStocktakeActualRepresentation> page(String stocktakeId, PageRequest pageRequest) {
        Page<HmeEquipmentStocktakeActualRepresentation> page = PageHelper.doPage(pageRequest, () -> mapper.selectList(stocktakeId));
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            AtomicInteger seqGen = new AtomicInteger(pageRequest.getPage() * page.getSize());
            page.getContent().forEach(rec -> rec.setSequenceNum(seqGen.incrementAndGet()));
        }
        return page;
    }

    @Override
    public List<HmeEquipmentStocktakeActualRepresentation> list(String stocktakeId) {
        return mapper.selectList(stocktakeId);
    }

    @Override
    public List<String> queryStocktakeEquipment(Long tenantId, String stocktakeId) {
        return mapper.queryStocktakeEquipment(tenantId, stocktakeId);
    }
}
