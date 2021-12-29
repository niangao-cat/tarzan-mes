package com.ruike.hme.domain.repository;

import java.util.List;

import com.ruike.hme.api.dto.HmeToolDTO;
import com.ruike.hme.domain.vo.HmeToolVO;
import com.ruike.hme.domain.vo.HmeToolVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeTool;
import tarzan.modeling.domain.entity.MtModWorkcell;

/**
 * 工装基础数据表资源库
 *
 * @author li.zhang13@hand-china.com 2021-01-07 10:06:45
 */
public interface HmeToolRepository extends BaseRepository<HmeTool> {

    Page<HmeToolVO> selectHmeTOOLs(PageRequest pageRequest, Long tenantId, HmeToolDTO hmeTooldto);

    /**
     * 工位扫描
     *
     * @param tenantId 租户ID
     * @param workcellCode 工位编码
     * @author penglin.sui@hand-china.com 2021/1/12 19:50
     * @return tarzan.modeling.domain.entity.MtModWorkcell
     */
    MtModWorkcell workcellScan(Long tenantId, String workcellCode);

    /**
     * 根据工位获取工装信息
     *
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param pageRequest 分页
     * @author penglin.sui@hand-china.com 2021/1/8 16:42
     * @return java.util<com.ruike.hme.domain.entity.HmeTool>
     */
    Page<HmeToolVO2> hmeToolQuery(Long tenantId, String workcellId, PageRequest pageRequest);

    /**
     * 工装管理保存
     *
     * @param tenantId 租户ID
     * @param dtoList 工装管理保存参数
     * @author penglin.sui@hand-china.com 2021/1/8 16:42
     * @return java.util<com.ruike.hme.domain.vo.HmeToolHmeToolVO2>
     */
    List<HmeTool> hmeToolSave(Long tenantId, List<HmeTool> dtoList);

    /**
     * 工装基础数据表列表 导出
     *
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-22 17:32
     * @return List<HmeToolVO>
     */
    List<HmeToolVO> listExport(Long tenantId, HmeToolDTO dto);
}
