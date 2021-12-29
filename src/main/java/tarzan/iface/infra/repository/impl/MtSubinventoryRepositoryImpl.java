package tarzan.iface.infra.repository.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tarzan.iface.domain.entity.MtSubinventory;
import tarzan.iface.domain.repository.MtSubinventoryRepository;
import tarzan.iface.domain.vo.MtSubinventoryIfaceVO;
import tarzan.iface.infra.mapper.MtSubinventoryMapper;

/**
 * ERP库存表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:40:10
 */
@Component
public class MtSubinventoryRepositoryImpl extends BaseRepositoryImpl<MtSubinventory>
                implements MtSubinventoryRepository {

    
    @Autowired
    private MtSubinventoryMapper mtSubinventoryMapper;

    @Override
    public List<MtSubinventory> subInventoryBatchGet(Long tenantId,List<MtSubinventoryIfaceVO> mtSubInventoryList) {
        if(CollectionUtils.isEmpty(mtSubInventoryList)){
            return Collections.emptyList();
        }
        return mtSubinventoryMapper.subInventoryBatchGet(tenantId,mtSubInventoryList);
    }

}
