package tarzan.inventory.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.api.dto.MtInvOnhandHoldJournalDTO;
import tarzan.inventory.api.dto.MtInvOnhandHoldJournalDTO2;
import tarzan.inventory.domain.entity.MtInvOnhandHoldJournal;
import tarzan.inventory.domain.vo.MtInvOnhandHoldJournalVO2;

/**
 * 库存保留日记账Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvOnhandHoldJournalMapper extends BaseMapper<MtInvOnhandHoldJournal> {

    /**
     * onhandReserveJournalBatchGet-批量获取库存保留日记账
     *
     * @param onhandHoldJournalIds
     * @return
     */
    List<MtInvOnhandHoldJournal> onhandHoldJournalBatchGet(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "onhandHoldJournalIds") List<String> onhandHoldJournalIds);

    /**
     * propertyLimitOnhandReserveJournalQuery-根据属性获取库存保留日记账ID
     *
     * @param dto
     * @return
     */
    List<String> propertyLimitOnhandHoldJournalQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtInvOnhandHoldJournalVO2 dto);


    List<MtInvOnhandHoldJournalDTO2> queryInvOnhandHoldJournalForUi(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtInvOnhandHoldJournalDTO dto,
                    @Param(value = "locatorIdList") List<String> locatorIdList);
}
