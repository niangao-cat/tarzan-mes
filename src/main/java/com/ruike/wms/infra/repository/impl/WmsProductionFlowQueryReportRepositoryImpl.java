package com.ruike.wms.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.wms.api.dto.WmsProductionFlowQueryReportDTO;
import com.ruike.wms.domain.repository.WmsProductionFlowQueryReportRepository;
import com.ruike.wms.domain.vo.WmsProductionFlowQueryReportVO;
import com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO;
import com.ruike.wms.infra.mapper.WmsProductionFlowQueryReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.MtUserClient;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ywj
 * @version 0.0.1
 * @description 生产流转查询报表
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @return
 */
@Component
public class WmsProductionFlowQueryReportRepositoryImpl implements WmsProductionFlowQueryReportRepository {

    @Autowired
    WmsProductionFlowQueryReportMapper mapper;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private MtUserClient userClient;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @ProcessLovValue
    public Page<WmsProductionFlowQueryReportVO> eoWorkcellQuery(Long tenantId, PageRequest pageRequest, WmsProductionFlowQueryReportDTO dto) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        if (StringUtils.isEmpty(siteId)) {
            return null;
        }
        // 查询工序流转
        Page<WmsProductionFlowQueryReportVO> dtoList = PageHelper.doPage(pageRequest, () ->mapper.eoWorkcellQuery(tenantId, siteId, dto));

        long lineNum = 0L;
        for (WmsProductionFlowQueryReportVO workcell : dtoList) {
            lineNum = lineNum + 1L;
            workcell.setLineNum(lineNum);
            workcell.setCreateUserName(userClient.userInfoGet(tenantId, workcell.getCreatedBy()).getRealName());
            workcell.setOperatorUserName(userClient.userInfoGet(tenantId, workcell.getOperatorId()).getRealName());
            //加工时长
            if (workcell.getSiteInDate() != null && workcell.getSiteOutDate() != null) {
                long time = workcell.getSiteOutDate().getTime() - workcell.getSiteInDate().getTime();
                long min = 1000 * 60;
                BigDecimal processTime = BigDecimal.valueOf(time).divide(BigDecimal.valueOf(min), 2, BigDecimal.ROUND_HALF_UP);
                workcell.setProcessTime(processTime);
            }
            //返修标识
            if (StringUtils.isNotEmpty(workcell.getIsReworkFlag())) {
                String isRemark = lovAdapter.queryLovMeaning("WMS.FLAG_YN", tenantId, workcell.getIsReworkFlag());
                workcell.setIsRework(isRemark);
            }
            //查询不良信息点击标识
            Long count = mapper.ncInfoFlagQuery(tenantId, workcell.getWorkcellId(), workcell.getEoId());
            if (count > 0) {
                workcell.setNcInfoFlag(true);
            } else {
                workcell.setNcInfoFlag(false);
            }
        }
        return dtoList;
    }
}
