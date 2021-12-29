package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeOperationInsHeadDTO;
import com.ruike.hme.api.dto.HmeOperationInsHeadDTO2;
import com.ruike.hme.domain.entity.HmeOperationInstruction;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeOperationInsHead;

import java.util.List;

/**
 * 作业指导头表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-11-09 19:09:39
 */
public interface HmeOperationInsHeadRepository extends BaseRepository<HmeOperationInsHead>, AopProxy<HmeOperationInsHeadRepository> {

    /**
     * 作业指导书查询
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/11/9 20:49
     * @return com.ruike.hme.domain.entity.HmeOperationInsHead
     */
    List<HmeOperationInsHeadDTO2> operationInsHeadQuery(Long tenantId, HmeOperationInsHeadDTO dto);

    /**
     * 作业指导书明细查询
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/11/9 21:29
     * @return com.ruike.hme.api.dto.HmeOperationInsHeadDTO2
     */
    HmeOperationInsHeadDTO2 detailListForUi(Long tenantId, HmeOperationInsHeadDTO dto);

    /**
     * 作业指导创建/更新
     *
     * @param tenantId
     * @param hmeOperationInsHead
     * @author jiangling.zheng@hand-china.com 2020/10/20 17:28
     * @return com.ruike.hme.domain.entity.HmeOperationInsHead
     */
    HmeOperationInsHead operationInsHeadUpdate(Long tenantId, HmeOperationInsHead hmeOperationInsHead);
}
