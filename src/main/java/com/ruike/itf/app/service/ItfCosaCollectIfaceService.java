package com.ruike.itf.app.service;

import com.ruike.itf.api.dto.CosaCollectItfDTO;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.domain.entity.ItfCosaCollectIface;
import io.choerodon.mybatis.service.BaseService;
import io.choerodon.mybatis.service.BaseServiceImpl;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.util.List;

/**
 * 芯片转移接口表应用服务
 *
 * @author wenzhang.yu@hand-china.com 2021-01-21 14:53:19
 */
public interface ItfCosaCollectIfaceService extends BaseService<ItfCosaCollectIface>, AopProxy<ItfCosaCollectIfaceService> {

    List<DataCollectReturnDTO> invoke(Long tenantId, List<CosaCollectItfDTO> collectList);

    void loadNew(Long tenantId, List<ItfCosaCollectIface> dtos);

    List<DataCollectReturnDTO> invokeb(Long tenantId, List<CosaCollectItfDTO> collectList);
}
