package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosBarCodeExceptionDTO;
import com.ruike.hme.api.dto.HmeCosWorkcellExceptionDTO;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 *COS条码加工异常汇总报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:28
 */
public interface HmeCosBarCodeExceptionService {
    /**
     *
     * COS条码加工异常汇总报表
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:26
     * @return com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
     */
    Page<HmeCosBarCodeExceptionVO> queryList(Long tenantId, HmeCosBarCodeExceptionDTO dto, PageRequest pageRequest);
}
