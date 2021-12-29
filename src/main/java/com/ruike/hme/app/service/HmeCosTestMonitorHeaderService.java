package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosTestMonitorHeaderDTO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorHeaderVO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorResponseVO;
import com.ruike.itf.domain.entity.ItfCosMonitorIface;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * COS测试良率监控头表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:12
 */
public interface HmeCosTestMonitorHeaderService {

    /**
     * COS测试良率监控头表查询参数
     *
     * @param tenantId    租户id
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return
     */
    Page<HmeCosTestMonitorHeaderVO> queryCosTestMonitorHeader(Long tenantId, HmeCosTestMonitorHeaderDTO dto, PageRequest pageRequest);

    /**
     * wafer 放行
     *
     * @param tenantId                      租户id
     * @param hmeCosTestMonitorHeaderVOList 数据内容
     * @return
     */
    HmeCosTestMonitorResponseVO passWafer(Long tenantId, List<HmeCosTestMonitorHeaderVO> hmeCosTestMonitorHeaderVOList);

    /**
     * 根据选择的头数据，MES调用ESB接口
     *
     * @param tenantId                      租户Id
     * @param hmeCosTestMonitorHeaderVOList 头数据
     * @param cid                           cid
     */
    void OaCOSTestMonitorInsertRestProxy(Long tenantId, List<HmeCosTestMonitorHeaderVO> hmeCosTestMonitorHeaderVOList, String cid);

    /**
     * 接口表保存数据
     *
     * @param tenantId                  租户id
     * @param hmeCosTestMonitorHeaderVO 头表数据
     * @param cid                       cid
     * @return
     */
    ItfCosMonitorIface insertIface(Long tenantId, HmeCosTestMonitorHeaderVO hmeCosTestMonitorHeaderVO, String cid);


}
