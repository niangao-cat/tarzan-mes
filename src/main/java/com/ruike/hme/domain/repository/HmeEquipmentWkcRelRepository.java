package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeEquipmentWkcRelDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRel;

import java.util.List;

/**
 * 设备工位关系表资源库
 *
 * @author han.zhang03@hand-china.com 2020-06-09 11:32:08
 */
public interface HmeEquipmentWkcRelRepository extends BaseRepository<HmeEquipmentWkcRel> {

    /**
     * 查询设备工位关系数据
     *
     * @param pageRequest        分页参数
     * @param hmeEquipmentWkcRel 查询参数
     * @param tenantId           租户ID
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeEquipmentWkcRel>
     */
    Page<HmeEquipmentWkcRel> queryBaseData(Long tenantId, PageRequest pageRequest, HmeEquipmentWkcRelDTO hmeEquipmentWkcRel);

    /**
     * 新增or修改设备工位关系表
     *
     * @param tenantId            租户ID
     * @param hmeEquipmentWkcRels 更新数据
     * @return com.ruike.hme.domain.entity.HmeEquipmentWkcRel
     */
    List<HmeEquipmentWkcRel> update(Long tenantId, List<HmeEquipmentWkcRel> hmeEquipmentWkcRels);
}
