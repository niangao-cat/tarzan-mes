package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO2;
import com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO3;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanOrPostIface;
import com.ruike.itf.domain.vo.ItfSoDeliveryChanOrPostVO;
import com.ruike.itf.domain.vo.ItfSoDeliveryChanOrPostVO3;
import com.ruike.itf.domain.vo.ItfSoDeliveryChanOrPostVO5;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交货单修改过账接口头表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-07-09 16:58:10
 */
public interface ItfSoDeliveryChanOrPostIfaceMapper extends BaseMapper<ItfSoDeliveryChanOrPostIface> {

    /**
     * 批量查询单据信息
     * @param tenantId
     * @param instructionDocIdList
     * @return java.util.List<com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO2>
     * @author sanfeng.zhang@hand-china.com 2021/7/12
     */
    List<ItfSoDeliveryChanOrPostDTO2> instructionDocPropertyBatchGet(@Param("tenantId") Long tenantId, @Param("instructionDocIdList") List<String> instructionDocIdList);

    /**
     * 批量查询单据行信息
     * @param tenantId
     * @param instructionIdList
     * @return java.util.List<com.ruike.itf.api.dto.ItfSoDeliveryChanOrPostDTO3>
     * @author sanfeng.zhang@hand-china.com 2021/7/12
     */
    List<ItfSoDeliveryChanOrPostDTO3> instructionPropertyBatchGet(@Param("tenantId") Long tenantId, @Param("instructionIdList") List<String> instructionIdList);

    /**
     * 获取物料类型
     * @param tenantId
     * @param materialIdList
     * @param siteId
     * @return java.util.List<com.ruike.itf.domain.vo.ItfSoDeliveryChanOrPostVO>
     * @author sanfeng.zhang@hand-china.com 2021/7/12
     */
    List<ItfSoDeliveryChanOrPostVO> queryMaterialTypeByMaterialIdsAndSiteId(@Param("tenantId") Long tenantId, @Param("materialIdList") List<String> materialIdList, @Param("siteId") String siteId);

    /**M
     * 获取指令实绩信息
     *
     * @param tenantId
     * @param instructionId
     * @author sanfeng.zhang@hand-china.com 2021/7/13 7:03
     * @return java.util.List<com.ruike.itf.domain.vo.ItfSoDeliveryChanOrPostVO3>
     */
    List<ItfSoDeliveryChanOrPostVO3> instructionActualDetailQuery(@Param("tenantId") Long tenantId, @Param("instructionId") String instructionId);

    /**
     * 获取行的指令实际数量
     * @param tenantId
     * @param instructionIdList
     * @return java.util.List<com.ruike.itf.domain.vo.ItfSoDeliveryChanOrPostVO3>
     * @author sanfeng.zhang@hand-china.com 2021/7/13
     */
    List<ItfSoDeliveryChanOrPostVO3> instructionActualQtyBatchGet(@Param("tenantId") Long tenantId, @Param("instructionIdList") List<String> instructionIdList);

    /**
     * 查询过账的交货单
     * @param tenantId
     * @return java.util.List<com.ruike.itf.domain.vo.ItfSoDeliveryChanOrPostVO5>
     * @author sanfeng.zhang@hand-china.com 2021/7/16
     */
    List<ItfSoDeliveryChanOrPostVO5> queryPostSoDeliveryList(@Param("tenantId") Long tenantId);
}
