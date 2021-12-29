package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeTagExportVO;
import com.ruike.hme.domain.vo.HmeTagGroupExportVO;
import com.ruike.hme.domain.vo.HmeTagGroupObjectExportVO;
import tarzan.general.api.dto.MtTagDTO;
import tarzan.general.api.dto.MtTagGroupDTO2;

import java.util.List;

/**
 * 公用导出
 *
 * @author sanfeng.zhang@hand-china.com 2021/3/31 10:21
 */
public interface HmeCommonExportRepository {

    /**
     * 数据项导出
     *
     * @param tenantId
     * @param mtTagDTO
     * @author sanfeng.zhang@hand-china.com 2021/3/31 10:40
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagExportVO>
     */
    List<HmeTagExportVO> tagExport(Long tenantId, MtTagDTO mtTagDTO);

    /**
     * 数据收集组&数据项关系导出
     *
     * @param tenantId
     * @param mtTagGroupDTO2
     * @author sanfeng.zhang@hand-china.com 2021/3/31 10:41
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagGroupExportVO>
     */
    List<HmeTagGroupExportVO> tagGroupExport(Long tenantId, MtTagGroupDTO2 mtTagGroupDTO2);

    /**
     * 数据收集组&关联对象导出
     *
     * @param tenantId
     * @param mtTagGroupDTO2
     * @author sanfeng.zhang@hand-china.com 2021/3/31 18:59
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagGroupObjectExportVO>
     */
    List<HmeTagGroupObjectExportVO> tagGroupObjectExport(Long tenantId, MtTagGroupDTO2 mtTagGroupDTO2);

}


