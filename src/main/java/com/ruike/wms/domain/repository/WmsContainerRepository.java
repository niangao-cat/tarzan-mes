package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.vo.WmsContainerVO;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;

import java.util.List;

/**
 * <p>
 * 容器 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/16 16:29
 */
public interface WmsContainerRepository {

    /**
     * 通过编码查询容器信息
     *
     * @param tenantId      租户
     * @param containerCode 容器编码
     * @return com.ruike.wms.domain.vo.WmsContainerVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/16 04:26:10
     */
    WmsContainerVO getInfoByCode(Long tenantId,
                                 String containerCode);

    /**
     * 查询容器下的物料批
     *
     * @param tenantId    租户
     * @param containerId 容器ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/17 05:38:13
     */
    List<WmsMaterialLotAttrVO> getMaterialLotInContainer(Long tenantId, String containerId);
}
