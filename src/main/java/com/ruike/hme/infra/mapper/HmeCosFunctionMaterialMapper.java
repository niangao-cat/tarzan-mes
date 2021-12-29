package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterial;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterialTime;
import com.ruike.hme.domain.vo.HmeCosFunctionMaterialVO;
import com.ruike.hme.domain.vo.HmeCosFunctionMaterialVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS投料性能表Mapper
 *
 * @author penglin.sui@hand-china.com 2021-06-22 20:50:13
 */
public interface HmeCosFunctionMaterialMapper extends BaseMapper<HmeCosFunctionMaterial> {

    /**
     * 查询数据收集组
     *
     * @param tenantId 租户id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosFunctionMaterialVO>
     */
    List<HmeCosFunctionMaterialVO> selectTags(@Param("tenantId") Long tenantId);

    /**
     * 查询挑选明细
     *
     * @param tenantId 租户id
     * @param materialIdList 物料ID集合
     * @param hmeCosFunctionMaterialTime 时间
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosFunctionMaterialVO2>
     */
    List<HmeCosFunctionMaterialVO2> selectSelection(@Param("tenantId") Long tenantId ,
                                                    @Param("materialIdList") List<String> materialIdList,
                                                    @Param("hmeCosFunctionMaterialTime") HmeCosFunctionMaterialTime hmeCosFunctionMaterialTime);

    /**
     * 查询挑选明细
     *
     * @param tenantId 租户id
     * @param siteId 站点id
     * @param cofFunctionQueryParasList
     * @param hmeCosFunctionMaterialTime
     * @return java.util.List<com.ruike.hme.domain.entity.HmeCosFunctionMaterial>
     */
    List<HmeCosFunctionMaterial> selectCosFunction(@Param("tenantId") Long tenantId,
                                                   @Param("siteId") String siteId,
                                                   @Param("cofFunctionQueryParasList") List<HmeCosFunctionMaterialVO2> cofFunctionQueryParasList,
                                                   @Param("hmeCosFunctionMaterialTime") HmeCosFunctionMaterialTime hmeCosFunctionMaterialTime);

    /**
     * 批量删除
     * @param cosFunctionMaterialIdList 主键ID集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/23 20:08
     */
    void batchDeleteByPrimary(@Param("cosFunctionMaterialIdList")List<String> cosFunctionMaterialIdList);

    /**
     * 批量新增
     * @param domains
     * @return
     * @author penglin.sui@hand-china.com 2021/6/23 20:24
     */
    void myBatchInsert(@Param("domains")List<HmeCosFunctionMaterial> domains);
}
