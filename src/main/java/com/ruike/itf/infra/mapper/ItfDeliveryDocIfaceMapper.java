package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfDeliveryDocIface;
import com.ruike.itf.domain.vo.ItfPoDeliveryRelHandlerVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 送货单接口头表Mapper
 *
 * @author yapeng.yao@hand-china.com 2020-09-04 16:29:21
 */
public interface ItfDeliveryDocIfaceMapper extends BaseMapper<ItfDeliveryDocIface> {

    /**
     * 根据订单号、订单行号获取InstructionId
     *
     * @param tenantId
     * @param materialId
     * @param poId
     * @param poLineNum
     * @return
     */
    String queryInstructionIdByPoLineNum(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "poId") String poId, @Param(value = "poLineNum") String poLineNum, @Param("materialId") String materialId);

    /**
     * 送货单行判退回传接口
     *
     * @param tenantId
     * @return
     */
    List<ItfPoDeliveryRelHandlerVO> selectTHDeliveryDoc(@Param(value = "tenantId") Long tenantId, @Param("iqcHeaderId") String iqcHeaderId);
}
