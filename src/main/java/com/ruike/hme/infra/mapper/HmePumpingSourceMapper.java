package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmePumpingSourceDTO;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.HmePumpingSourceVO;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;

/**
 * 泵浦源
 *
 * @author wengang.qiang@hand-china.com 2021/09/01 11:11
 */
public interface HmePumpingSourceMapper {
    /**
     * 查询泵浦源组合SN，泵浦源SN，位置
     *
     * @param tenant
     * @param hmePumpingSourceDTO
     * @return
     */
    List<HmePumpingSourceVO> queryHmePumping(@Param("tenantId") Long tenant, @Param("dto") HmePumpingSourceDTO hmePumpingSourceDTO);

    /**
     * 查询工位id
     *
     * @param tenant
     * @param operationName
     * @return
     */
    List<String> queryWorkcellIdList(@Param("tenantId") Long tenant, @Param("operationName") String operationName);

    /**
     * 根据eo_id和工位id去查询jobId
     *
     * @param tenant
     * @param eoIds
     * @param workcellId
     * @return
     */
    List<HmeEoJobSn> queryJobId(@Param("tenantId") Long tenant, @Param("eoIds") List<String> eoIds, @Param("workcellId") List<String> workcellId);

    /**
     * 根据tag_id和最新的job_id去record表查找
     *
     * @param tenant
     * @param tagIdList     tag_id 集合
     * @param newHmeEoJobSn 最新的job_id集合
     * @return
     */
    List<HmeEoJobDataRecord> queryDataRecord(@Param("tenantId") Long tenant, @Param("tagList") List<String> tagIdList, @Param("newHmeEoJobSn") List<String> newHmeEoJobSn);

    /**
     * 查找materialId
     *
     * @param tenant              租户id
     * @param hmePumpingSourceDTO sn
     * @return
     */
    MtMaterialLot queryMaterialId(@Param("tenantId") Long tenant, @Param("dto") HmePumpingSourceDTO hmePumpingSourceDTO);

}