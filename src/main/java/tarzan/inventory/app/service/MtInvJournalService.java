package tarzan.inventory.app.service;

import java.util.List;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.inventory.api.dto.MtInvJournalDTO;
import tarzan.inventory.api.dto.MtInvJournalDTO2;
import tarzan.inventory.api.dto.MtInvJournalDTO4;

/**
 * 库存日记账应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvJournalService {

    /**
     * 库存日记查询
     * 
     * @author benjamin
     * @date 2019-08-14 20:20
     * @param tenantId 租户Id
     * @param dto MtInvJournalDTO
     * @param pageRequest PageRequest
     * @return list
     */
    List<MtInvJournalDTO2> queryInvJournalForUi(Long tenantId, MtInvJournalDTO dto, PageRequest pageRequest);

    /**
     *
     * @Description 库存日记报表
     *
     * @author yuchao.wang
     * @date 2020/10/19 12:34
     * @param tenantId 租户Id
     * @param dto MtInvJournalDTO4
     * @param pageRequest PageRequest
     * @return java.util.List<tarzan.inventory.api.dto.MtInvJournalDTO2>
     *
     */
    List<MtInvJournalDTO2> queryInvJournalReport(Long tenantId, MtInvJournalDTO4 dto, PageRequest pageRequest);
}
