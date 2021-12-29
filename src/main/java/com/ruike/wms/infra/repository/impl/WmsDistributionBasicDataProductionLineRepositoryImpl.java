package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO;
import com.ruike.wms.domain.entity.WmsDistributionBasicData;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.wms.domain.entity.WmsDistributionBasicDataProductionLine;
import com.ruike.wms.domain.repository.WmsDistributionBasicDataProductionLineRepository;
import org.springframework.stereotype.Component;

/**
 * 配送物料产线表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 21:09:25
 */
@Component
public class WmsDistributionBasicDataProductionLineRepositoryImpl extends BaseRepositoryImpl<WmsDistributionBasicDataProductionLine> implements WmsDistributionBasicDataProductionLineRepository {

}
