package com.ruike.hme.app.service;

import java.util.List;

import com.ruike.hme.domain.vo.HmeEoJobBeyondMaterialVO;
import com.ruike.hme.domain.entity.HmeEoJobBeyondMaterial;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 工序作业平台-计划外物料应用服务
 *
 * @author liyuan.lv@hand-china.com 2020-07-15 15:27:06
 */
public interface HmeEoJobBeyondMaterialService {
    /**
     * 界面查询功能
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @return io.choerodon.core.domain.Page<HmeWorkOrderVO>
     * @author liyuan.lv@hand-china.com 20.7.15 07:35:27
     */
    List<HmeEoJobBeyondMaterial> listForUi(Long tenantId, HmeEoJobBeyondMaterialVO dto);
}
