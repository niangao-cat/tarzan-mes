package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsBarcodeDTO;
import com.ruike.wms.api.dto.WmsSoDeliveryDetailQueryDTO;
import com.ruike.wms.domain.vo.WmsInstructionActualDetailVO;
import com.ruike.wms.domain.vo.WmsProdPrepareExecVO;
import com.ruike.wms.domain.vo.WmsProductPrepareDetailVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * <p>
 * 出货单明细 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 11:10
 */
public interface WmsSoDeliveryDetailRepository {
    /**
     * 根据条件查询列表
     *
     * @param tenantId          租户
     * @param instructionIdList 单据行列表
     * @param dto               查询条件
     * @param pageRequest       分页参数
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSoDeliveryDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 10:36:01
     */
    Page<WmsSoDeliveryDetailVO> selectListByCondition(Long tenantId,
                                                      List<String> instructionIdList,
                                                      WmsSoDeliveryDetailQueryDTO dto,
                                                      PageRequest pageRequest);

    /**
     * 根据行Id查询备货明细列表
     *
     * @param tenantId      租户
     * @param instructionId 行ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/15 05:20:50
     */
    List<WmsProductPrepareDetailVO> selectPrepareListByLineId(Long tenantId,
                                                              String instructionId);

    /**
     * 根据单据和条码查询单据明细
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @param barcodeList      条码列表
     * @return java.util.List<tarzan.actual.domain.entity.MtInstructionActualDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/15 06:45:56
     */
    List<WmsInstructionActualDetailVO> selectListByDocAndBarcode(Long tenantId,
                                                                 String instructionDocId,
                                                                 List<WmsBarcodeDTO> barcodeList);

    /**
     * 查询执行明细列表
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProdPrepareExecVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/16 09:39:59
     */
    List<WmsProdPrepareExecVO> selectExecuteListByDocId(Long tenantId,
                                                        String instructionDocId);
}
