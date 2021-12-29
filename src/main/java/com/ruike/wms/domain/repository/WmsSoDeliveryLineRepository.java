package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.vo.WmsProductPrepareLineVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * 发货单行 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 20:18
 */
public interface WmsSoDeliveryLineRepository {
    /**
     * 根据单据Id查询列表
     *
     * @param tenantId         租户
     * @param pageRequest      分页参数
     * @param instructionDocId 单据ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSoDeliveryLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 07:54:00
     */
    Page<WmsSoDeliveryLineVO> listByDocId(Long tenantId,
                                          PageRequest pageRequest,
                                          String instructionDocId);

    /**
     * 根据单据ID查询备货行列表
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 10:31:04
     */
    List<WmsProductPrepareLineVO> prepareListGet(Long tenantId,
                                                 String instructionDocId);
}
