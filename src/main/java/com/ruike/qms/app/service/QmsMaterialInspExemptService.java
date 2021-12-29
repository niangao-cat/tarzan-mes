package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO2;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO3;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO4;
import com.ruike.qms.domain.entity.QmsMaterialInspExempt;
import org.hzero.core.base.AopProxy;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseService;

/**
 * 物料免检表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:06:18
 */
public interface QmsMaterialInspExemptService extends BaseService<QmsMaterialInspExempt>, AopProxy<QmsMaterialInspExemptService> {

    /**
     * 获取列表
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param dto QmsMaterialInspExemptDTO2
     * @param pageRequest pageRequest
     * @return Page<QmsMaterialInspExemptDTO>
     */
    Page<QmsMaterialInspExemptDTO> listMaterialInspExemptForUi(Long tenantId, QmsMaterialInspExemptDTO2 dto, PageRequest pageRequest);

    /**
     * 保存
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param dto QmsMaterialInspExemptDTO4
     * @return Page<QmsMaterialInspExemptDTO>
     */
    String saveMaterialInspExemptForUi(Long tenantId, QmsMaterialInspExemptDTO4 dto);

    /**
     * 保存
     * @author jiangling.zheng@hand-china.com
     * @param tenantId 租户ID
     * @param list QmsMaterialInspExemptDTO3
     * @return Page<QmsMaterialInspExemptDTO>
     */
    void removeMaterialInspExemptForUi(Long tenantId, List<QmsMaterialInspExemptDTO3> list);

}
