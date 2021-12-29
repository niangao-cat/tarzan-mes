package com.ruike.hme.domain.repository;

import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterial;

import java.util.List;

/**
 * COS投料性能表资源库
 *
 * @author penglin.sui@hand-china.com 2021-06-22 20:50:13
 */
public interface HmeCosFunctionMaterialRepository extends BaseRepository<HmeCosFunctionMaterial> {
    /**
     * 批量删除
     * @param cosFunctionMaterialIdList 主键ID集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/23 20:06
     */
    void batchDeleteByPrimary(@Param("cosFunctionMaterialIdList") List<String> cosFunctionMaterialIdList);

    /**
     * 批量新增
     * @param cosFunctionMaterialList
     * @return
     * @author penglin.sui@hand-china.com 2021/6/23 20:21
     */
    void myBatchInsert(@Param("cosFunctionMaterialList") List<HmeCosFunctionMaterial> cosFunctionMaterialList);
}
