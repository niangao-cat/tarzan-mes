package tarzan.inventory.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.inventory.domain.entity.MtInvOnhandHoldJournal;
import tarzan.inventory.domain.repository.MtInvOnhandHoldJournalRepository;
import tarzan.inventory.domain.vo.MtInvOnhandHoldJournalVO2;
import tarzan.inventory.infra.mapper.MtInvOnhandHoldJournalMapper;

/**
 * 库存保留日记账 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@Component
public class MtInvOnhandHoldJournalRepositoryImpl extends BaseRepositoryImpl<MtInvOnhandHoldJournal>
                implements MtInvOnhandHoldJournalRepository {

    @Autowired
    private MtInvOnhandHoldJournalMapper mtInvOnhandHoldJournalMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    /**
     * onhandReserveJournalGet-获取库存保留日记账
     *
     * @param tenantId
     * @param onhandHoldJournalId
     * @return
     */
    @Override
    public MtInvOnhandHoldJournal onhandReserveJournalGet(Long tenantId, String onhandHoldJournalId) {
        if (StringUtils.isEmpty(onhandHoldJournalId)) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "onhandHoldJournalId", "【API：onhandReserveJournalGet】"));
        }

        MtInvOnhandHoldJournal invOnhandHoldJournal = new MtInvOnhandHoldJournal();
        invOnhandHoldJournal.setTenantId(tenantId);
        invOnhandHoldJournal.setOnhandHoldId(onhandHoldJournalId);

        return mtInvOnhandHoldJournalMapper.selectOne(invOnhandHoldJournal);
    }

    /**
     * onhandReserveJournalBatchGet-批量获取库存保留日记账
     *
     * @param tenantId
     * @param onhandHoldJournalIds
     * @return
     */
    @Override
    public List<MtInvOnhandHoldJournal> onhandReserveJournalBatchGet(Long tenantId, List<String> onhandHoldJournalIds) {
        if (CollectionUtils.isEmpty(onhandHoldJournalIds)) {
            throw new MtException("MT_INVENTORY_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_INVENTORY_0001",
                                            "INVENTORY", "onhandHoldJournalIds", "【API：onhandReserveJournalBatchGet】"));
        }
        return mtInvOnhandHoldJournalMapper.onhandHoldJournalBatchGet(tenantId, onhandHoldJournalIds);
    }

    /**
     * propertyLimitOnhandReserveJournalQuery-根据属性获取库存保留日记账ID
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public List<String> propertyLimitOnhandReserveJournalQuery(Long tenantId, MtInvOnhandHoldJournalVO2 dto) {
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_INVENTORY_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0002", "INVENTORY", "【API：propertyLimitOnhandReserveJournalQuery】"));
        }

        return mtInvOnhandHoldJournalMapper.propertyLimitOnhandHoldJournalQuery(tenantId, dto);
    }



}
