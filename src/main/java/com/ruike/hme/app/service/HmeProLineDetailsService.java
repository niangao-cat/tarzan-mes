package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeProductionLineDetailsVO;
import com.ruike.hme.domain.vo.HmeProductionQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModWorkcell;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 产线日明细报表-容器应用服务
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:10:50
 */
public interface HmeProLineDetailsService {

    /**
     * 功能描述: 车间信息分页查询
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @return List<MtModArea>
     * @author bao.xu@hand-china.com 2020/7/9 10:58
     */
    Page<MtModArea> queryModArea(Long tenantId, PageRequest pageRequest);

    /**
     * 功能描述: 获取产线日明细信息
     *
     * @param params      产线视图
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @return List<HmeProductionLineDetailsDTO>
     * @throws Exception Exception
     * @author bao.xu@hand-china.com 2020-07-07 11:10
     */
    Page<HmeProductionLineDetailsDTO> queryProductionLineDetails(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params) throws Exception;

    /**
     * 工序信息查询
     *
     * @param params      参数视图
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @return List<MtModWorkcell>
     * @author bao.xu@hand-china.com 2020/7/13 19:46
     */
    Page<MtModWorkcell> selectWorkcells(Long tenantId, PageRequest pageRequest, HmeProductionQueryVO params);

    /**
     * Description: 在制查询报表 查询
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @param params      参数视图
     * @return List<HmeProductionQueryDTO>
     * @author bao.xu@hand-china.com 2020/7/13 11:38
     */
    Page<HmeProductionQueryDTO> queryProductDetails(Long tenantId, PageRequest pageRequest, HmeProductionQueryVO params);

    /**
     * 在制报表-eo信息
     *
     * @param tenantId          租户id
     * @param pageRequest       分页参数
     * @param params            查询参数
     * @return
     */
    Page<HmeProductEoInfoVO> queryProductEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params);


    /**
     * 产量日明细-班组信息
     *
     * @param tenantId
     * @param pageRequest
     * @param params
     * @return
     */
    Page<HmeProductionLineDetailsDTO> queryProductShiftList(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params);

    /**
     * 投产信息
     *
     * @param tenantId    租户id
     * @param pageRequest 分页参数
     * @param params      查询条件
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     * @author sanfeng.zhang@hand-china.com 2020/7/31 16:16
     */
    Page<HmeProductEoInfoVO> queryProductProcessEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params);

    /**
     * 在制报表-导出
     *
     * @param tenantId
     * @param params
     * @param response
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/13 18:29
     */
    void onlineReportExport(Long tenantId, HmeProductionQueryVO params, HttpServletResponse response) throws IOException;
}
