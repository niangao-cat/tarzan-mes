package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEoJobDataRecordDto;
import com.ruike.hme.api.dto.HmeEoJobDataRecordReturnDTO;
import com.ruike.hme.domain.vo.HmeEoJobDataRecordVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工序作业平台-数据采集应用服务
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:48:09
 */
public interface HmeEoJobDataRecordService {
    /**
     * 删除补充数据采集记录
     *
     * @param tenantId  租户ID
     * @param deleteVO 包含需要删除的VO集合
     * @author penglin.sui@hand-china.com 2020/7/31 15:41
     */
    void deleteSupplementRecord(Long tenantId, List<HmeEoJobDataRecordVO> deleteVO);

    /**
     * 配送需求平台查询
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页信息
     * @param dto         查询条件
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.dto.WmsDistributionDemandDetailResponseDTO>
     * @author penglin.sui@hand-china.com 2020/7/22 15:19
     */
    Page<HmeEoJobDataRecordVO> supplementRecordQuery(Long tenantId, PageRequest pageRequest, HmeEoJobDataRecordDto dto);


    Page<HmeEoJobDataRecordReturnDTO> summaryReport(Long tenantId, String materialId, String flag, String operationCode, PageRequest pageRequest);

    void summaryReportExport(Long tenantId, String materialId, String flag,String operationCode, HttpServletResponse response);
}
