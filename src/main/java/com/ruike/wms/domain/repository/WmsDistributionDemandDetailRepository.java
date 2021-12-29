package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.vo.WmsDistDemandCreateVO;
import com.ruike.wms.domain.vo.WmsDistributionListQueryVO3;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.wms.domain.entity.WmsDistributionDemandDetail;

import java.util.List;

/**
 * 配送需求明细表资源库
 *
 * @author penglin.sui@hand-china.com 2020-08-04 11:08:57
 */
public interface WmsDistributionDemandDetailRepository extends BaseRepository<WmsDistributionDemandDetail> {
    /**
     * 通过配送需求ID查询配送明细
     *
     * @param tenantId     租户
     * @param distDemandId 配送需求ID
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 04:10:42
     */
    List<WmsDistributionDemandDetail> selectListByDemandId(Long tenantId, String distDemandId);

    /**
     * 通过配送需求ID列表查询生成配送单明细信息
     *
     * @param tenantId 租户
     * @param idList   id列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 04:10:42
     */
    List<WmsDistDemandCreateVO> selectCreateListByDemandIdList(Long tenantId, List<String> idList);

    /**
     * 启用线边库存计算逻辑时，通过配送需求ID列表查询生成配送单明细信息
     *
     * @param tenantId 租户
     * @param idList   id列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author yifan.xiong@hand-china.com 2021-3-19 14:31:24
     */
    List<WmsDistDemandCreateVO> selectCreateListByDemandIdListNew(Long tenantId, List<String> idList);

    /**
     * 通过配送需求ID查询配送明细
     *
     * @param tenantId 租户
     * @param idList   明细id列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 04:10:42
     */
    List<WmsDistributionDemandDetail> selectListByDetailIdList(Long tenantId, List<String> idList);

    /**
     * 通过配送需求ID查询配送带损耗率明细
     *
     * @param tenantId 租户
     * @param idList   明细id列表
     * @return java.util.List<com.ruike.wms.domain.entity.WmsDistributionDemandDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/31 04:10:42
     */
    List<WmsDistributionDemandDetail> selectAttritionListByDetailIdList(Long tenantId, List<String> idList);

    List<String> getSubstitute(Long tenantId, String distDemandId,String workOrderId);

    List<WmsDistributionListQueryVO3> selectByInstructionId(Long tenantId, String instructionDocId, List<String> instructionIds);
}
