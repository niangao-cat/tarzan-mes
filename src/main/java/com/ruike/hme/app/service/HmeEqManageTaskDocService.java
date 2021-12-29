package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEqTaskDocLineQueryDTO;
import com.ruike.hme.api.dto.HmeEqTaskDocQueryDTO;
import com.ruike.hme.domain.entity.HmeEqManageTaskDoc;
import com.ruike.hme.domain.vo.HmeEqTaskDocAndLineExportVO;
import com.ruike.hme.domain.vo.HmeEqTaskHisVO;
import org.hzero.core.base.AopProxy;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseService;

/**
 * 设备管理任务单表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:11
 */
public interface HmeEqManageTaskDocService {

    /**
     * 按周期生成设备管理任务单（废弃2020-08-13）
     *
     * @param tenantId
     * @param cycle
     */
    void createEqTaskDoc(Long tenantId, String cycle);

    /**
     * 按周期生成设备管理任务单
     *
     * @param tenantId
     */
    void createEqTaskDocPlus(Long tenantId);

    /**
     * 编辑保养任务行
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2020/10/16 14:28
     * @return void
     */
    HmeEqTaskDocLineQueryDTO updateEqCheckDoc(Long tenantId, HmeEqTaskDocLineQueryDTO dto);

    /**
     * 任务单查询
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<HmeEqTaskDocQueryDTO> queryTaskDocList(Long tenantId, HmeEqTaskDocQueryDTO dto, PageRequest pageRequest);

    /**
     * 任务单行查询
     *
     * @param tenantId
     * @param taskDocId
     * @param pageRequest
     * @return
     */
    Page<HmeEqTaskDocLineQueryDTO> queryTaskDocLineList(Long tenantId, String taskDocId, PageRequest pageRequest);

    /**
     * 编辑任务头
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmeEqTaskDocQueryDTO
     * @author sanfeng.zhang@hand-china.com 2021/3/4 20:17
     */
    HmeEqTaskDocQueryDTO updateTaskDoc(Long tenantId, HmeEqTaskDocQueryDTO dto);

    /**
     * 任务行修改历史
     *
     * @param tenantId
     * @param taskDocLineId
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEqTaskHisVO>
     * @author sanfeng.zhang@hand-china.com 2021/3/4 22:44
     */
    Page<HmeEqTaskHisVO> taskHistoryListQuery(Long tenantId, String taskDocLineId, PageRequest pageRequest);

    /**
     * 定时更新设备任务状态
     *
     * @param tenantId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/5 14:12
     */
    void updateEqChangeStatus(Long tenantId);

    /**
     * 任务单查询--导出
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeEqTaskDocQueryDTO> listExport(Long tenantId, HmeEqTaskDocQueryDTO dto);

    /**
     * 任务行导出
     *
     * @param tenantId
     * @param taskDocId
     * @author sanfeng.zhang@hand-china.com 2021/5/10 15:38
     * @return java.util.List<com.ruike.hme.api.dto.HmeEqTaskDocLineQueryDTO>
     */
    List<HmeEqTaskDocLineQueryDTO> listLineExport(Long tenantId, String taskDocId);

    /**
     * 任务单和任务单行导出
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeEqTaskDocAndLineExportVO> listHeadAndLineExport(Long tenantId, HmeEqTaskDocQueryDTO dto);
}
