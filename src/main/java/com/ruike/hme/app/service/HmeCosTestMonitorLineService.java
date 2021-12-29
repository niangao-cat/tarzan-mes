package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosTestMonitorRequestDTO;
import com.ruike.hme.api.dto.HmeCosTestMonitorRequestOneDTO;
import com.ruike.hme.domain.entity.HmeCosTestMonitorLine;
import com.ruike.hme.domain.vo.HmeCosTestMonitorLineVO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorResponseVO;
import com.ruike.itf.api.dto.ItfCosTestMonitorDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * COS测试良率监控行表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:13
 */
public interface HmeCosTestMonitorLineService {

    /**
     * COS测试良率监控行表查询参数
     *
     * @param tenantId           租户id
     * @param cosMonitorHeaderId 头表id
     * @param pageRequest        分页参数
     * @return
     */
    Page<HmeCosTestMonitorLineVO> queryCosMonitorLine(Long tenantId, String cosMonitorHeaderId, PageRequest pageRequest);


    /**
     * 保存 COS测试良率监控行表
     *
     * @param tenantId                    租户id
     * @param hmeCosTestMonitorLineVOList 添加数据
     * @param cosMonitorHeaderId          头id
     * @return
     */
    List<HmeCosTestMonitorLine> saveCosMonitorLine(Long tenantId, List<HmeCosTestMonitorLineVO> hmeCosTestMonitorLineVOList, String cosMonitorHeaderId);

    /**
     * COS测试良率监控行表放行
     *
     * @param tenantId                租户id
     * @param hmeCosTestMonitorLineVO 行数据
     * @param cosMonitorHeaderId      头表主键
     * @return
     */
    HmeCosTestMonitorResponseVO passMonitorLine(Long tenantId, List<HmeCosTestMonitorLineVO> hmeCosTestMonitorLineVO, String cosMonitorHeaderId);

    /**
     * 更新行表
     *
     * @param tenantId                        租户id
     * @param hmeCosTestMonitorRequestOneDTO esb返回数据
     * @return
     */
    HmeCosTestMonitorResponseVO updateLine(Long tenantId, HmeCosTestMonitorRequestOneDTO hmeCosTestMonitorRequestOneDTO);

    /**
     * 插入接口表数据
     *
     * @param tenantId                租户id
     * @param hmeCosTestMonitorLineVO 行数据
     * @param cosMonitorHeaderId      头id
     * @param cid                     cid
     */
    void insertIface(Long tenantId, HmeCosTestMonitorLineVO hmeCosTestMonitorLineVO, String cosMonitorHeaderId, String cid);

    /**
     * 组装数据发送到ESB
     *
     * @param tenantId                    租户id
     * @param hmeCosTestMonitorLineVOList 行数据
     * @param cosMonitorHeaderId          头id
     * @param cid                         cid
     */
    void OaCOSTestMonitorInsertRestProxy(Long tenantId, List<HmeCosTestMonitorLineVO> hmeCosTestMonitorLineVOList, String cosMonitorHeaderId, String cid);

    /**
     * cos测试良率回传
     *
     * @param itfCosTestMonitorDTOList
     * @author sanfeng.zhang@hand-china.com 2021/10/21 12:16
     * @return com.ruike.hme.domain.vo.HmeCosTestMonitorResponseVO
     */
    HmeCosTestMonitorResponseVO updateCosMonitorStatus(List<ItfCosTestMonitorDTO> itfCosTestMonitorDTOList);
}
