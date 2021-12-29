package io.tarzan.common.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtNumrangeHisDTO;
import io.tarzan.common.domain.vo.MtNumrangeHisVO;

/**
 * 号码段定义历史表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeHisService {

    /**
     * numrangeHisQueryForUi-获取号码段修改历史信息
     *
     * update remarks
     * <ul>
     * <li>2019-9-25 benjamin 添加号段组号传入参数</li>
     * </ul>
     * 
     * @param tenantId 租户id
     * @param dto MtNumrangeHisDTO
     * @param pageRequest 分页参数
     * @return page
     */
    Page<MtNumrangeHisVO> numrangeHisQueryForUi(Long tenantId, MtNumrangeHisDTO dto, PageRequest pageRequest);

}
