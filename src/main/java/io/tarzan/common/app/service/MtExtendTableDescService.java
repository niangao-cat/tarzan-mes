package io.tarzan.common.app.service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseService;
import io.tarzan.common.api.dto.MtExtendAttrDTO4;
import io.tarzan.common.domain.entity.MtExtendTableDesc;
import io.tarzan.common.domain.vo.MtExtendTableDescVO;

/**
 * 扩展说明表应用服务
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtExtendTableDescService extends BaseService<MtExtendTableDesc> {

    Page<MtExtendTableDescVO> extendListGet(Long tenantId, PageRequest pageRequest, MtExtendAttrDTO4 dto);

    MtExtendTableDescVO extendSave(Long tenantId, MtExtendTableDescVO dto);

    MtExtendTableDescVO extendOneGet(Long tenantId, String extendTableDescId);
}
