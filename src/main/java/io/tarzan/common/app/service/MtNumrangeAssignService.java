package io.tarzan.common.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtNumrangeAssignDTO2;
import io.tarzan.common.domain.vo.MtGenTypeVO6;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO3;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO4;

/**
 * 号码段分配表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
public interface MtNumrangeAssignService {
    
    Page<MtNumrangeAssignVO> listNumrangeAssignForUi(Long tenantId, MtNumrangeAssignVO3 condition, PageRequest pageRequest);

    MtNumrangeAssignVO detailNumrangeAssignForUi(Long tenantId, String numrangeAssignId);

    String saveNumrangeAssignForUi(Long tenantId, MtNumrangeAssignDTO2 dto);

    void batchRemoveNumrangeAssignForUi(Long tenantId, List<MtNumrangeAssignDTO2> list);

    Page<MtGenTypeVO6> queryObjectTypeLovForUi(Long tenantId, MtNumrangeAssignVO4 condition, PageRequest pageRequest);
}
