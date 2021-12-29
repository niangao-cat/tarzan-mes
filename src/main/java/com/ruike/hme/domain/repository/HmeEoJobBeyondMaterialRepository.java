package com.ruike.hme.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEoJobBeyondMaterial;
import com.ruike.hme.domain.vo.HmeEoJobBeyondMaterialVO;

/**
 * 工序作业平台-计划外物料资源库
 *
 * @author liyuan.lv@hand-china.com 2020-07-15 15:27:06
 */
public interface HmeEoJobBeyondMaterialRepository extends BaseRepository<HmeEoJobBeyondMaterial>, AopProxy<HmeEoJobBeyondMaterialRepository> {
    /**
     * 计划外物料查询
     *
     * @param tenantId 租户ID
     * @param dto 计划外物料参数
     * @author liyuan.lv@hand-china.com 20.7.15 08:00:59
     * @return com.ruike.hme.domain.entity.HmeEoJobBeyondMaterial
     */

    List<HmeEoJobBeyondMaterial> list(Long tenantId, HmeEoJobBeyondMaterialVO dto);

    /**
     * 计划外物料批料保存
     *
     * @param tenantId 租户ID
     * @param dtoList 计划外物料列表
     * @author liyuan.lv@hand-china.com 20.7.16 11:29:47
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobBeyondMaterial>
     */
    List<HmeEoJobBeyondMaterial> batchSave(Long tenantId, List<HmeEoJobBeyondMaterial> dtoList);

    /**
     * 计划外物料批料删除
     *
     * @param dtoList 计划外物料列表
     * @author liyuan.lv@hand-china.com 20.7.20 03:14:30
     */
    void batchRemove(List<HmeEoJobBeyondMaterial> dtoList);
}
