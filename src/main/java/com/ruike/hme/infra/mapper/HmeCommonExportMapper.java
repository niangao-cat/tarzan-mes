package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.HmeTagExportVO;
import com.ruike.hme.domain.vo.HmeTagGroupExportVO;
import com.ruike.hme.domain.vo.HmeTagGroupObjectExportVO;
import org.apache.ibatis.annotations.Param;
import tarzan.general.api.dto.MtTagDTO;
import tarzan.general.api.dto.MtTagGroupDTO2;

import java.util.List;

/**
 * 公共导出
 *
 * @author sanfeng.zhang@hand-china.com 2021/3/31 11:18
 */
public interface HmeCommonExportMapper {

    /**
     * 数据项导出
     *
     * @param tenantId
     * @param mtTagDTO
     * @author sanfeng.zhang@hand-china.com 2021/3/31 11:35
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagExportVO>
     */
    List<HmeTagExportVO> tagExport(@Param("tenantId") Long tenantId, @Param("dto") MtTagDTO mtTagDTO);

    /**
     * 数据收集组&数据项关系导出
     *
     * @param tenantId
     * @param mtTagGroupDTO2
     * @author sanfeng.zhang@hand-china.com 2021/3/31 14:14
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagGroupExportVO>
     */
    List<HmeTagGroupExportVO> tagGroupExport(@Param("tenantId") Long tenantId, @Param("dto") MtTagGroupDTO2 mtTagGroupDTO2);

    /**
     * 数据收集组&关联对象导出
     *
     * @param tenantId
     * @param mtTagGroupDTO2
     * @author sanfeng.zhang@hand-china.com 2021/3/31 19:08
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagGroupObjectExportVO>
     */
    List<HmeTagGroupObjectExportVO> tagGroupObjectExport(@Param("tenantId") Long tenantId, @Param("dto") MtTagGroupDTO2 mtTagGroupDTO2);
}
