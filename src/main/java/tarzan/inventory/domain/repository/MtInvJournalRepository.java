package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.inventory.domain.entity.MtInvJournal;
import tarzan.inventory.domain.vo.MtInvJournalVO;

/**
 * 库存日记账资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvJournalRepository extends BaseRepository<MtInvJournal>, AopProxy<MtInvJournalRepository> {

    /**
     * propertyLimitInvJournalQuery-根据属性获取库存日记账ID
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitInvJournalQuery(Long tenantId, MtInvJournalVO dto);

    /**
     * invJournalGet-获取库存日记账
     *
     * @param tenantId
     * @param journalId
     * @return
     */
    MtInvJournal invJournalGet(Long tenantId, String journalId);

    /**
     * invJournalBatchGet-批量获取库存日记账
     *
     * @param tenantId
     * @param journalIds
     * @return
     */
    List<MtInvJournal> invJournalBatchGet(Long tenantId, List<String> journalIds);
}
