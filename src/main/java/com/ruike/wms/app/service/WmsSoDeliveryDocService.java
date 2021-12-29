package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.WmsSoDeliveryQueryDTO;
import com.ruike.wms.api.dto.WmsSoDeliverySubmitDTO;
import com.ruike.wms.domain.vo.WmsSoDeliveryDocVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;


/**
 * <p>
 * 出货单单据 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 15:22
 */
public interface WmsSoDeliveryDocService {

    /**
     * 返回分页列表
     *
     * @param tenantId    租户ID
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSoDeliveryDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 02:22:24
     */
    Page<WmsSoDeliveryDocVO> pageList(Long tenantId,
                                      PageRequest pageRequest,
                                      WmsSoDeliveryQueryDTO dto);

    /**
     * 单据提交
     *
     * @param tenantId 租户
     * @param dto      提交数据
     * @return com.ruike.wms.domain.vo.WmsSoDeliveryDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 04:45:38
     */
    WmsSoDeliveryDocVO submit(Long tenantId,
                              WmsSoDeliverySubmitDTO dto);

    /**
     * 取消
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return com.ruike.wms.domain.vo.WmsSoDeliveryDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 04:45:38
     */
    WmsSoDeliveryDocVO cancel(Long tenantId,
                              String instructionDocId);

    /**
     * 下达
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return com.ruike.wms.domain.vo.WmsSoDeliveryDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 04:45:38
     */
    WmsSoDeliveryDocVO release(Long tenantId,
                               String instructionDocId);

    /**
     * 过账
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return com.ruike.wms.domain.vo.WmsSoDeliveryDocVO
     * @author faming.yang@hand-china.com 2021/7/19 9:51
     */
    WmsSoDeliveryDocVO confirm(Long tenantId,
                               String instructionDocId);

    /**
     * 取消下达
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return com.ruike.wms.domain.vo.WmsSoDeliveryDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 04:45:38
     */
    WmsSoDeliveryDocVO releaseCancel(Long tenantId,
                                     String instructionDocId);
}
