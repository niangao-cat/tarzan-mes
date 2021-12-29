package com.ruike.wms.infra.mapper;

import java.util.List;

import com.ruike.wms.domain.entity.WmsDistributionDemandDetail;
import com.ruike.wms.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;

import org.apache.ibatis.annotations.Param;

/**
 * 配送需求平台Mapper
 *
 * @author penglin.sui@hand-china.com 2020/07/22 11:28
 */
public interface WmsDistributionDemandDetailMapper extends BaseMapper<WmsDistributionDemandDetail> {

    /**
     * 通过需求ID查询替代料
     *
     * @param tenantId 租户
     * @param idList   id列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributeSubstituteVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 01:57:42
     */
    List<WmsDistributeSubstitutionVO> selectSubstituteByDemandId(@Param("tenantId") Long tenantId, @Param("idList") List<String> idList);

    /**
     * 通过配送需求ID查询配送明细
     *
     * @param tenantId     租户
     * @param distDemandId 配送需求ID
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 04:10:42
     */
    List<WmsDistributionDemandDetail> selectListByDemandId(@Param("tenantId") Long tenantId, @Param("distDemandId") String distDemandId);

    /**
     * 通过配送需求ID查询配送明细
     *
     * @param tenantId 租户
     * @param idList   明细id列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 04:10:42
     */
    List<WmsDistributionDemandDetail> selectListByDetailIdList(@Param("tenantId") Long tenantId, @Param("idList") List<String> idList);

    /**
     * 通过配送需求ID查询配送带损耗率明细
     *
     * @param tenantId 租户
     * @param idList   明细id列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 04:10:42
     */
    List<WmsDistributionDemandDetail> selectAttritionListByDetailIdList(@Param("tenantId") Long tenantId, @Param("idList") List<String> idList);

    /**
     * 通过配送需求ID列表查询生成配送单明细信息
     *
     * @param tenantId 租户
     * @param idList   需求id列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 04:10:42
     */
    List<WmsDistDemandCreateVO> selectCreateListByDemandIdList(@Param("tenantId") Long tenantId, @Param("idList") List<String> idList);

    /**
     * 启用线边库存计算逻辑时，通过配送需求ID列表查询生成配送单明细信息
     *
     * @param tenantId 租户
     * @param idList   需求id列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author yifan.xiong@hand-china.com 2021-3-19 14:29:50
     */
    List<WmsDistDemandCreateVO> selectCreateListByDemandIdListNew(@Param("tenantId") Long tenantId, @Param("idList") List<String> idList);

    /**
     * 启用线边库存计算逻辑时，通过配送需求ID列表查询生成配送单明细信息
     *
     * @param tenantId 租户
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author yifan.xiong@hand-china.com 2021-3-25 10:57:18
     */
    List<WmsDistDemandCreateVO> selectAllCreateList(@Param("tenantId") Long tenantId);

    List<String> getSubstitute(@Param("tenantId") Long tenantId, @Param("distDemandId") String distDemandId,@Param("workOrderId") String workOrderId);

    List<WmsDistributionListQueryVO3> selectByInstructionId(@Param("tenantId")Long tenantId, @Param("instructionDocId")String instructionDocId, @Param("instructionIds")List<String> instructionIds);
}
