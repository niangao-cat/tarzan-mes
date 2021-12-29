package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.QmsPqcHeaderQueryDTO;
import com.ruike.qms.domain.vo.QmsPqcHeaderQueryVO;
import com.ruike.qms.domain.vo.QmsPqcLineDetailsVO;
import com.ruike.qms.domain.vo.QmsPqcLineQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 巡检单查询应用服务
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/05 12:15
 */
public interface QmsPqcDocService {
    /**
     * 巡检单头查询
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-08 10:07
     * @param tenantId    租户id
     * @param pageRequest 分页参数
     * @param dto 头Id
     * @return com.ruike.qms.domain.vo.QmsPqcHeaderQueryVO
     */
    Page<QmsPqcHeaderQueryVO> qmsPqcDocQueryHeader(PageRequest pageRequest, Long tenantId, QmsPqcHeaderQueryDTO dto);

    /**
     * 巡检单行查询
     * @param tenantId    租户id
     * @param pageRequest 分页参数
     * @param pqcHeaderId 头Id
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-08 10:07
     * @return com.ruike.qms.domain.vo.QmsPqcLineQueryVO
     */
    Page<QmsPqcLineQueryVO> qmsPqcDocQueryLine(Long tenantId, PageRequest pageRequest, String pqcHeaderId);

    /**
     * 巡检单行明细查询
     * @param tenantId    租户id
     * @param pageRequest 分页参数
     * @param pqcLineId 头Id
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-08 10:07
     * @return com.ruike.qms.domain.vo.QmsPqcLineDetailsVO
     */
    Page<QmsPqcLineDetailsVO> qmsPqcDocQueryLineDetails(Long tenantId, PageRequest pageRequest, String pqcLineId);

    /**
     * 巡检单头导出
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/4/19 17:01
     * @return java.util.List<com.ruike.qms.domain.vo.QmsPqcHeaderQueryVO>
     */
    List<QmsPqcHeaderQueryVO> listHeaderExport(Long tenantId, QmsPqcHeaderQueryDTO dto);
}
