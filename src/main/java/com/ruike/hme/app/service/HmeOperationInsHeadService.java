package com.ruike.hme.app.service;

import java.util.List;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeOperationInsHead;
import com.ruike.hme.domain.entity.HmeOperationInstruction;

import com.ruike.hme.domain.vo.HmeOperationInsHeadVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 作业指导头表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-11-09 19:09:39
 */
public interface HmeOperationInsHeadService {

    /**
     * 作业指导书查询
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @author jiangling.zheng@hand-china.com 2020/11/9 21:01
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeOperationInsHeadDTO2>
     */
    Page<HmeOperationInsHeadDTO2> listForUi(Long tenantId, HmeOperationInsHeadDTO dto, PageRequest pageRequest);

    /**
     * 作业指导书明细查询
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/11/9 21:30
     * @return com.ruike.hme.api.dto.HmeOperationInsHeadDTO2
     */
    HmeOperationInsHeadDTO2 detailListForUi(Long tenantId, HmeOperationInsHeadDTO dto);

    /**
     * 新增/更新
     *
     * @param tenantId
     * @param hmeOperationInsHead
     * @author jiangling.zheng@hand-china.com 2020/11/11 20:14
     * @return com.ruike.hme.domain.entity.HmeOperationInsHead
     */
    HmeOperationInsHead saveForUi(Long tenantId, HmeOperationInsHead hmeOperationInsHead);

    /**
     * 作业指导书明细查询
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.vo.HmeOperationInsHeadVO
     * @author penglin.sui@hand-china.com 2021/01/19 17:29
     */
    Page<HmeOperationInsHeadVO> eSopQuery(Long tenantId, HmeOperationInsHeadDTO3 dto, PageRequest pageRequest);

    /**
     * 单件工序作业平台-工位未出站的eo信息查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/23 10:23:45
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeEoJobSnDTO5>
     */
    Page<HmeEoJobSnDTO5> noSiteOutEoQuery(Long tenantId, HmeEoJobSnDTO4 dto, PageRequest pageRequest);
}
