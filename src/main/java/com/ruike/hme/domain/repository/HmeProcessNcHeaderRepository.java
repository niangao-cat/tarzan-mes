package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeProcessNcHeaderDTO;
import com.ruike.hme.domain.vo.HmeProcessNcHeaderVO;
import com.ruike.hme.domain.vo.HmeProcessNcVO;
import com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeProcessNcHeader;

import java.util.*;

/**
 * 工序不良头表资源库
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
public interface HmeProcessNcHeaderRepository extends BaseRepository<HmeProcessNcHeader> {

    /**
     * 获取工序不良头表信息
     *
     * @param tenantId
     * @param hmeProcessNcHeaderDTO
     * @author li.zhang13@hand-china.com
     * @date 2021-01-21 09:36:44
     */
    Page<HmeProcessNcHeaderVO> selectProcessHeader(Long tenantId, PageRequest pageRequest, HmeProcessNcHeaderDTO hmeProcessNcHeaderDTO);

    /**
     * 工序不良判定标准维护导出
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/7 11:23:48
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessNcVO>
     */
    List<HmeProcessNcVO> processNcExport(Long tenantId, HmeProcessNcHeaderDTO dto);

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
    HmeProcessNcHeaderVO2 queryProcessNcInfoForNcRecordValidate(Long tenantId, String operationId, String materialId, String productCode, String cosModel, String chipCombination);

    /**
     *
     * @Description 批量查询工序不良头行明细信息 key:materialId+productCode+cosModel
     *
     * @author yuchao.wang
     * @date 2021/1/25 18:52
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialIdList 物料ID
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2>
     *
     */
    Map<String, HmeProcessNcHeaderVO2> batchQueryProcessNcInfoForNcRecordValidate(Long tenantId, String operationId, List<String> materialIdList);

    /**
     *
     * @Description 查询工序不良头行明细信息-老化不良
     *
     * @author yuchao.wang
     * @date 2021/2/4 11:28
     * @param tenantId 租户ID
     * @param materialId 产品ID
     * @param stationId 工序ID
     * @param cosModel 芯片类型
     * @param materialLotCode 出站SN
     * @param operationId 工艺ID
     * @return com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2
     *
     */
    HmeProcessNcHeaderVO2 queryProcessNcInfoForAgeingNcRecordValidate(Long tenantId, String materialId, String stationId, String cosModel, String materialLotCode, String operationId);

    /**
     *
     * @Description 批量查询工序不良头行明细信息-老化不良 key:productCode
     *
     * @author yuchao.wang
     * @date 2021/2/4 14:30
     * @param tenantId 租户ID
     * @param operationId 工艺ID
     * @param materialIdList 物料ID集合
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessNcHeaderVO2>
     *
     */
    List<HmeProcessNcHeaderVO2> batchQueryProcessNcInfoForAgeingNcRecordValidate(Long tenantId, String operationId, List<String> materialIdList);

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
    HmeProcessNcHeaderVO2 queryProcessNcInfoForReflectorNcRecordValidate(Long tenantId, String operationId, String materialId, String cosModel, String chipCombination);
}
