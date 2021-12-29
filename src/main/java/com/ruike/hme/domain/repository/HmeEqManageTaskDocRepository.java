package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeEqTaskDocQueryDTO;
import com.ruike.hme.api.dto.HmeEquipmentDTO;
import com.ruike.hme.domain.vo.HmeEqManageTaskCreateVO;
import com.ruike.hme.domain.vo.HmeEqTaskDocAndLineExportVO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEqManageTaskDoc;

import java.util.List;

/**
 * 设备管理任务单表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:11
 */
public interface HmeEqManageTaskDocRepository extends BaseRepository<HmeEqManageTaskDoc> , AopProxy<HmeEqManageTaskDocRepository> {

    /**
     * 创建任务单据
     *
     * @param tenantId
     * @param vo
     * @param eqDto
     */
    void createTaskDoc(Long tenantId, HmeEqManageTaskCreateVO vo, HmeEquipmentDTO eqDto);

    /**
     * 任务单查询
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeEqTaskDocQueryDTO> queryTaskDocList(Long tenantId, HmeEqTaskDocQueryDTO dto);

    /**
     * 任务单查询导出
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<com.ruike.hme.api.dto.HmeEqTaskDocQueryDTO>
     * @author sanfeng.zhang@hand-china.com 2021/3/10 19:30
     */
    List<HmeEqTaskDocQueryDTO> queryExportTaskDocList(Long tenantId, HmeEqTaskDocQueryDTO dto);

    /**
     * 任务单和任务行查询导出
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeEqTaskDocAndLineExportVO> queryTaskDocListAndTaskDocLineList(Long tenantId, HmeEqTaskDocQueryDTO dto);
}
