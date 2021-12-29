package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCosCheckBarcodesDTO;
import com.ruike.hme.domain.vo.HmeCosCheckBarcodesVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * description
 *
 * @author li.zhang 2021/01/19 12:25
 */
public interface HmeCosCheckBarcodesRepository {

    /**
     * 查询分页列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return HmeCosCheckBarcodesVO
     * @author li.zhang13@hand-china.com
     */
    Page<HmeCosCheckBarcodesVO> selectCheckBarcodes(String tenantId, HmeCosCheckBarcodesDTO dto, PageRequest pageRequest);
}
