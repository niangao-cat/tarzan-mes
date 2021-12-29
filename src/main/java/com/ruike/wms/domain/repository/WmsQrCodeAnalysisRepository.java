package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsQrCodeAnalysisDTO;
import org.hzero.core.base.AopProxy;

import java.util.Map;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-8-25 10:31
 */
public interface WmsQrCodeAnalysisRepository extends AopProxy<WmsQrCodeAnalysisRepository> {

    /**
     * 验证数据
     *
     * @author jiangling.zheng@hand-china.com 2020-8-24 15:46
     * @param tenantId
     * @param dto
     * @return
     *
     */
    boolean dataValidate(Long tenantId, WmsQrCodeAnalysisDTO dto);

    /**
     * 条码创建
     *
     * @author jiangling.zheng@hand-china.com 2020-8-24 21:23
     * @param tenantId
     * @param dto
     * @param isTrue
     */
    void materialLotCreate(Long tenantId, WmsQrCodeAnalysisDTO dto, boolean isTrue);

    /**
     * 条码拆分
     *
     * @author jiangling.zheng@hand-china.com 2020-8-24 22:03
     * @param tenantId
     * @param dto
     * @param materialLotId
     */
    void materialLotSplit(Long tenantId, WmsQrCodeAnalysisDTO dto, String materialLotId);
}
