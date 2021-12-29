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
import tarzan.inventory.domain.entity.MtInvJournal;
import tarzan.inventory.domain.repository.MtInvJournalRepository;
import tarzan.inventory.domain.vo.MtInvJournalVO;
import tarzan.inventory.infra.mapper.MtInvJournalMapper;

/**
 * 库存日记账 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@Component
public class MtInvJournalRepositoryImpl extends BaseRepositoryImpl<MtInvJournal> implements MtInvJournalRepository {

    @Autowired
    private MtInvJournalMapper mtInvJournalMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Override
    public List<String> propertyLimitInvJournalQuery(Long tenantId, MtInvJournalVO dto) {
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_INVENTORY_0002", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0002", "INVENTORY", "【API：propertyLimitInvJournalQuery】"));
        }

        return mtInvJournalMapper.propertyLimitInvJournalQuery(tenantId, dto);
    }

    @Override
    public MtInvJournal invJournalGet(Long tenantId, String journalId) {
        if (StringUtils.isEmpty(journalId)) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "journalId", "【API：invJournalGet】"));
        }

        MtInvJournal mtInvJournal = new MtInvJournal();
        mtInvJournal.setTenantId(tenantId);
        mtInvJournal.setJournalId(journalId);

        return mtInvJournalMapper.selectOne(mtInvJournal);
    }

    @Override
    public List<MtInvJournal> invJournalBatchGet(Long tenantId, List<String> journalIds) {
        if (CollectionUtils.isEmpty(journalIds)) {
            throw new MtException("MT_INVENTORY_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0001", "INVENTORY", "onhandHoldJournalIds", "【API：invJournalBatchGet】"));
        }
        return mtInvJournalMapper.invJournalBatchGet(tenantId, journalIds);
    }
}
