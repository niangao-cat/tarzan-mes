package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeFacYkVO2;
import com.ruike.hme.domain.vo.HmeFacYkHisVO;
import com.ruike.hme.domain.vo.HmeFacYkVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeFacYk;

import java.util.List;

/**
 * FAC-Y宽判定标准表资源库
 *
 * @author TAO.XU02@hand-china.com 2021-02-01 10:45:03
 */
public interface HmeFacYkRepository extends BaseRepository<HmeFacYk> {

    /**
     * FAC-Y宽判定标准表查询
     *
     * @param pageRequest 分页参数
     * @param hmeFacYk    FAC-Y宽判定标准表
     * @param tenantId    租户ID
     * @return com.ruike.hme.domain.vo.HmeFacYkVO
     * @author TAO.XU02@hand-china.com 2021-02-01 10:45:03
     */
    Page<HmeFacYkVO> selectHmeFacYk(PageRequest pageRequest, HmeFacYkVO2 hmeFacYk, Long tenantId);

    /**
     * FAC-Y宽判定标准表新增
     *
     * @param tenantId 租户ID
     * @param hmeFacYk FAC-Y宽判定标准表
     * @param tenantId 租户ID
     * @return void
     * @author TAO.XU02@hand-china.com 2021-02-01 10:45:03
     */
    void insertHmeFacYk(HmeFacYkVO2 hmeFacYk, Long tenantId);

    /**
     * FAC-Y宽判定标准表更新
     *
     * @param tenantId 租户ID
     * @param hmeFacYk FAC-Y宽判定标准表
     * @param tenantId 租户ID
     * @return void
     * @author TAO.XU02@hand-china.com 2021-02-01 10:45:03
     */
    void updateByHmeFacYk(HmeFacYk hmeFacYk, Long tenantId);

    /**
     * FAC-Y宽判定标准历史查询
     *
     * @param tenantId
     * @param facYkId
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeFacYkHisVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/7 10:45
     */
    Page<HmeFacYkHisVO> hisListQuery(Long tenantId, String facYkId, PageRequest pageRequest);

    /**
     * FAC-Y宽判定标准导出
     *
     * @param tenantId
     * @param hmeFacYkVO2
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFacYkVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/7 14:56
     */
    List<HmeFacYkVO> facYkExport(Long tenantId, HmeFacYkVO2 hmeFacYkVO2);
}
