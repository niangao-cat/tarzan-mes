package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeManyBarcodeSplitVO;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/8 15:42
 */
public interface HmeManyBarcodeSplitRepository {

    /**
     * 多层条码拆分-扫描条码
     *
     * @param tenantId
     * @param splitVO
     * @return com.ruike.hme.domain.vo.HmeManyBarcodeSplitVO
     * @author sanfeng.zhang@hand-china.com 2021/3/8 15:49
     */
    HmeManyBarcodeSplitVO scanBarcode(Long tenantId, HmeManyBarcodeSplitVO splitVO);
}
