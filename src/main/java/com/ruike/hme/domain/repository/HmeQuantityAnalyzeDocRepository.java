package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeQuantityAnalyzeDocDTO;
import com.ruike.hme.api.dto.query.QualityAnalyzeQuery;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeDoc;
import com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO;
import com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO2;
import com.ruike.hme.domain.vo.QualityAnalyzeRepresentationLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 质量文件头表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-01-19 11:10:06
 */
public interface HmeQuantityAnalyzeDocRepository extends BaseRepository<HmeQuantityAnalyzeDoc> {

    /**
     * 质量文件头表数据查询
     *
     * @param tenantId    租户ID
     * @param dto         查询条件
     * @param pageRequest 分页信息
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 14:04:58
     */
    @Deprecated
    Page<HmeQuantityAnalyzeDocVO> quantityAnalyzeDocQuery(Long tenantId, HmeQuantityAnalyzeDocDTO dto, PageRequest pageRequest);

    /**
     * 质量文件表数据查询
     *
     * @param tenantId    租户ID
     * @param docId       头ID
     * @param pageRequest 分页信息
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO2>
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 15:42:04
     */
    Page<HmeQuantityAnalyzeDocVO2> quantityAnalyzeLineQuery(Long tenantId, String docId, PageRequest pageRequest);

    /**
     * 查询列表
     *
     * @param tenantId    tenantId
     * @param query       查询条件
     * @param pageRequest 分页查询
     * @return java.util.List<com.ruike.hme.api.dto.representation.QualityAnalyzeRepresentation.Line>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/8 11:00:07
     */
    Page<QualityAnalyzeRepresentationLineVO> pagedList(Long tenantId,
                                                       QualityAnalyzeQuery query,
                                                       PageRequest pageRequest);
}
