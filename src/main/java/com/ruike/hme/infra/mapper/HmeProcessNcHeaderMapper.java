package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeProcessNcHeaderDTO;
import com.ruike.hme.api.dto.HmeProcessNcImportDTO;
import com.ruike.hme.domain.entity.HmeProcessNcHeader;
import com.ruike.hme.domain.vo.HmeProcessNcHeaderVO;
import com.ruike.hme.domain.vo.HmeProcessNcVO;
import com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序不良头表Mapper
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
public interface HmeProcessNcHeaderMapper extends BaseMapper<HmeProcessNcHeader> {

    List<HmeProcessNcHeaderVO> selectProcessHeader(@Param("tenantId") Long tenantId, @Param("hmeProcessNcHeaderDTO") HmeProcessNcHeaderDTO hmeProcessNcHeaderDTO);

    /**
     * 查询工序不良头
     *
     * @param tenantId
     * @param importDTO
     * @return java.util.List<com.ruike.hme.domain.entity.HmeProcessNcHeader>
     * @author sanfeng.zhang@hand-china.com 2021/3/29 18:05
     */
    List<HmeProcessNcHeader> queryProcessNcHeader(@Param("tenantId") Long tenantId, @Param("importDTO") HmeProcessNcImportDTO importDTO);

    /**
     * 批量更新工序不良头
     *
     * @param tenantId
     * @param userId
     * @param headerList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/30 16:44
     */
    void batchHeaderUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("headerList") List<HmeProcessNcHeader> headerList);

    /**
     * 工序不良判定标准维护导出
     *
     * @param tenantId 租户ID
     * @param hmeProcessNcHeaderDTO 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/7 11:21:17
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessNcVO>
     */
    List<HmeProcessNcVO> processNcExport(@Param("tenantId") Long tenantId, @Param("hmeProcessNcHeaderDTO")HmeProcessNcHeaderDTO hmeProcessNcHeaderDTO);

    /**
     *
     * @Description 查询工序不良头行明细信息
     *
     * @author yuchao.wang
     * @date 2021/1/22 17:46
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialId 物料ID
     * @param productCode 产品编码
     * @param cosModel cos类型
     * @param chipCombination 芯片组合
     * @return com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2
     *
     */
    List<HmeProcessNcHeaderVO2> queryProcessNcInfoForNcRecordValidate(@Param("tenantId") Long tenantId,
                                                                      @Param("operationId") String operationId,
                                                                      @Param("materialId") String materialId,
                                                                      @Param("productCode") String productCode,
                                                                      @Param("cosModel") String cosModel,
                                                                      @Param("chipCombination") String chipCombination);

    /**
     *
     * @Description 批量查询工序不良头行明细信息
     *
     * @author yuchao.wang
     * @date 2021/1/25 18:56
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2>
     *
     */
    List<HmeProcessNcHeaderVO2> batchQueryProcessNcInfoForNcRecordValidate(@Param("tenantId") Long tenantId,
                                                                           @Param("operationId") String operationId,
                                                                           @Param("materialIdList") List<String> materialIdList);

    /**
     *
     * @Description 查询工序不良头行明细信息-老化不良
     *
     * @author yuchao.wang
     * @date 2021/2/4 11:30
     * @param tenantId 租户ID
     * @param materialId 产品ID
     * @param stationId 工序ID
     * @param cosModel 芯片类型
     * @param operationId 工艺ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2>
     *
     */
    List<HmeProcessNcHeaderVO2> queryProcessNcInfoForAgeingNcRecordValidate(@Param("tenantId") Long tenantId,
                                                                            @Param("materialId") String materialId,
                                                                            @Param("stationId") String stationId,
                                                                            @Param("cosModel") String cosModel,
                                                                            @Param("operationId") String operationId);

    /**
     *
     * @Description 批量查询工序不良头行明细信息-老化不良
     *
     * @author yuchao.wang
     * @date 2021/2/4 14:44
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialIdList 物料ID集合
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2>
     *
     */
    List<HmeProcessNcHeaderVO2> batchQueryProcessNcInfoForAgeingNcRecordValidate(@Param("tenantId") Long tenantId,
                                                                                 @Param("operationId") String operationId,
                                                                                 @Param("materialIdList") List<String> materialIdList);

    /**
     *
     * @Description 查询工序不良头行明细信息
     *
     * @author yuchao.wang
     * @date 2021/1/22 17:46
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialId 物料ID
     * @param cosModel cos类型
     * @param chipCombination 芯片组合
     * @return com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2
     *
     */
    List<HmeProcessNcHeaderVO2> queryProcessNcInfoForReflectorNcRecordValidate(@Param("tenantId") Long tenantId,
                                                                               @Param("operationId") String operationId,
                                                                               @Param("materialId") String materialId,
                                                                               @Param("cosModel") String cosModel,
                                                                               @Param("chipCombination") String chipCombination);
}
