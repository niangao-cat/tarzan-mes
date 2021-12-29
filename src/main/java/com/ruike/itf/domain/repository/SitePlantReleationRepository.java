package com.ruike.itf.domain.repository;

import com.ruike.itf.api.dto.SitePlantReleationDTO;
import com.ruike.itf.domain.entity.SitePlantReleation;
import org.hzero.mybatis.base.BaseRepository;

/**
 * ERP工厂与站点映射关系资源库
 *
 * @author taowen.wang@hand-china.com 2021-07-06 14:14:34
 */
public interface SitePlantReleationRepository extends BaseRepository<SitePlantReleation> {

    SitePlantReleationDTO sitePlantReleationByPlantCode(Long tenantId, String plantCode);

}
