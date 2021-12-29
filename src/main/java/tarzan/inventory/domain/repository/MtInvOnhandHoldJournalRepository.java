package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.inventory.domain.entity.MtInvOnhandHoldJournal;
import tarzan.inventory.domain.vo.MtInvOnhandHoldJournalVO2;

/**
 * 库存保留日记账资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvOnhandHoldJournalRepository
                extends BaseRepository<MtInvOnhandHoldJournal>, AopProxy<MtInvOnhandHoldJournalRepository> {

    /**
     * onhandReserveJournalGet-获取库存保留日记账
     *
     * @param tenantId
     * @param onhandHoldJournalId
     * @return
     */
    MtInvOnhandHoldJournal onhandReserveJournalGet(Long tenantId, String onhandHoldJournalId);

    /**
     * onhandReserveJournalBatchGet-批量获取库存保留日记账
     *
     * @param tenantId
     * @param onhandHoldJournalIds
     * @return
     */
    List<MtInvOnhandHoldJournal> onhandReserveJournalBatchGet(Long tenantId, List<String> onhandHoldJournalIds);

    /**
     * propertyLimitOnhandReserveJournalQuery-根据属性获取库存保留日记账ID
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitOnhandReserveJournalQuery(Long tenantId, MtInvOnhandHoldJournalVO2 dto);



}
