package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfMaterialSubstituteRelIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料全局替代关系表Mapper
 *
 * @author yapeng.yao@hand-china.com 2020-08-18 14:40:53
 */
public interface ItfMaterialSubstituteRelIfaceMapper extends BaseMapper<ItfMaterialSubstituteRelIface> {

    /**
     * 批量物料全局替代关系导入  全量表
     * @author yapeng.yao@hand-china.com 2020-08-18 14:40:53
     * @param tableName
     * @param itfMaterialSubstituteRelIfaceList
     * @return
     */
    void batchInsertMaterialSubstituteRelIface(@Param(value = "tableName") String tableName,
                              @Param(value = "itfMaterialSubstituteRelIfaceList") List<ItfMaterialSubstituteRelIface> itfMaterialSubstituteRelIfaceList);

    /**
     * 批量物料全局替代关系导入  业务表
     *
     * @param itfMaterialSubstituteRelIfaceList
     * @return
     * @author yapeng.yao@hand-china.com 2020-08-18 14:40:53
     */
    void batchInsertMaterialSubstituteRel(@Param(value = "itfMaterialSubstituteRelIfaceList") List<ItfMaterialSubstituteRelIface> itfMaterialSubstituteRelIfaceList);

    /**
     * 清空表
     *
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/24 05:21:55
     */
    void truncateAll();

}
