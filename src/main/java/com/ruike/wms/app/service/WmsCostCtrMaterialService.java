package com.ruike.wms.app.service;

import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsCostCtrMaterialVO;
import org.hzero.core.base.AopProxy;

import java.util.List;
import java.util.Map;

/**
 * 成本中心领料单执行应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-04-15 15:28
 */
public interface WmsCostCtrMaterialService extends AopProxy<WmsCostCtrMaterialService> {

    /**
     * 单据扫码查询
     * @author jiangling.zheng@hand-china.com 2020-04-15 10:33:51
     * @param tenantId
     * @param docBarCode
     * @return
     */
    WmsCostCtrMaterialDTO docQuery(Long tenantId, String docBarCode);

    /**
     * 条码查询
     * @author jiangling.zheng@hand-china.com 2020-04-15 10:33:51
     * @param tenantId
     * @param dto
     * @return
     */
    WmsCostCtrMaterialDTO11 containerOrMaterialLotQuery(Long tenantId, WmsCostCtrMaterialDTO4 dto);

    /**
     * 检验条码质量状态
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2020/11/18 9:34
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> checkMaterialLotQuality(Long tenantId, WmsCostCtrMaterialDTO4 dto);

    /**
     * 执行
     * @author jiangling.zheng@hand-china.com 2020-04-15 10:33:51
     * @param tenantId
     * @param docDto
     * @return
     */
    WmsCostCtrMaterialDTO execute(Long tenantId, WmsCostCtrMaterialDTO7 docDto);

    /**
     * 明细查询
     * @author jiangling.zheng@hand-china.com 2020-04-15 10:33:51
     * @param tenantId
     * @param dto
     * @return
     */
    WmsCostCtrMaterialDTO9 docDetailQuery(Long tenantId, WmsCostCtrMaterialDTO8 dto);

    /**
     * 删除
     * @author jiangling.zheng@hand-china.com 2020-04-15 10:33:51
     * @param tenantId
     * @param dto
     * @return
     */
    WmsCostCtrMaterialDTO9 deleteMaterialLot(Long tenantId, WmsCostCtrMaterialDTO8 dto);

    List<WmsCostCtrMaterialVO> lightOnOrOff(Long tenantId, List<ItfLightTaskIfaceDTO> dtoList);
}
