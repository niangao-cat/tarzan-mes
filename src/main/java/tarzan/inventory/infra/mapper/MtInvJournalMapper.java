package tarzan.inventory.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.api.dto.MtInvJournalDTO;
import tarzan.inventory.api.dto.MtInvJournalDTO2;
import tarzan.inventory.api.dto.MtInvJournalDTO4;
import tarzan.inventory.domain.entity.MtInvJournal;
import tarzan.inventory.domain.vo.MtInvJournalVO;

/**
 * 库存日记账Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvJournalMapper extends BaseMapper<MtInvJournal> {

    List<String> propertyLimitInvJournalQuery(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtInvJournalVO dto);

    List<MtInvJournal> invJournalBatchGet(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "journalIds") List<String> journalIds);

    List<MtInvJournalDTO2> queryInvJournalForUi(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtInvJournalDTO dto,
                    @Param(value = "locatorIdList") List<String> locatorIdList);

    /**
     *
     * @Description 库存日记报表
     *
     * @author yuchao.wang
     * @date 2020/10/19 12:36
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<tarzan.inventory.api.dto.MtInvJournalDTO2>
     *
     */
    List<MtInvJournalDTO2> queryInvJournalReport(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "dto") MtInvJournalDTO4 dto);
}
