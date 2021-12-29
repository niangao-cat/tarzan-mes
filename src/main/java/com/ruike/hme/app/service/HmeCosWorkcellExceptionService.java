package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosWorkcellExceptionDTO;
import com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * COS工位加工异常汇总表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/13 13:05
 */
public interface HmeCosWorkcellExceptionService {
    /**
     *
     * COS工位加工异常汇总表查询
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-13 13:56
     * @return com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO
     */
    Page<HmeCosWorkcellExceptionVO> queryList(Long tenantId, HmeCosWorkcellExceptionDTO dto, PageRequest pageRequest);
}
