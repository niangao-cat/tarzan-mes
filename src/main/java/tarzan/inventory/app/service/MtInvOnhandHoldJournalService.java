package tarzan.inventory.app.service;

import java.util.List;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.inventory.api.dto.MtInvOnhandHoldJournalDTO;
import tarzan.inventory.api.dto.MtInvOnhandHoldJournalDTO2;

/**
 * 库存保留日记账应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvOnhandHoldJournalService {

    /**
     * 查询库存保留日记帐
     * 
     * @author benjamin
     * @date 2019-08-15 18:26
     * @param tenantId 租户Id
     * @param dto MtInvOnhandHoldJournalDTO
     * @param pageRequest PageRequest
     * @return list
     */
    List<MtInvOnhandHoldJournalDTO2> queryHoldInvJournalForUi(Long tenantId, MtInvOnhandHoldJournalDTO dto,
                    PageRequest pageRequest);
}
