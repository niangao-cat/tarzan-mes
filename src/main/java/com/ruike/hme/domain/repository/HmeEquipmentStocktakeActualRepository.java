package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeActualRepresentation;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeActual;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 设备盘点实际资源库
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
public interface HmeEquipmentStocktakeActualRepository extends BaseRepository<HmeEquipmentStocktakeActual> {

    /**
     * 保存
     *
     * @param entity 实体
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 09:40:13
     */
    void save(HmeEquipmentStocktakeActual entity);

    /**
     * 分页查询
     *
     * @param stocktakeId 单据ID
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeActualRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 03:19:19
     */
    Page<HmeEquipmentStocktakeActualRepresentation> page(String stocktakeId, PageRequest pageRequest);

    /**
     * 列表查询
     *
     * @param stocktakeId 单据ID
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeActualRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 03:19:19
     */
    List<HmeEquipmentStocktakeActualRepresentation> list(String stocktakeId);

    /**
     * 盘点单设备
     * @param tenantId
     * @param stocktakeId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/8/2
     */
    List<String> queryStocktakeEquipment(Long tenantId, String stocktakeId);
}
