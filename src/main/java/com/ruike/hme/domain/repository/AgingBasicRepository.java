package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeAgingBasicDTO;
import com.ruike.hme.domain.entity.HmeAgingBasic;
import com.ruike.hme.domain.vo.HmeAgingBasicVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 老化基础数据资源库
 *
 * @author junfeng.chen@hand-china.com 2021-03-02 11:56:36
 */
public interface AgingBasicRepository extends BaseRepository<HmeAgingBasic> {

    /**
     * 老化基础数据列表
     *
     * @param pageRequest
     * @param dto
     * @param tenantId
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-03 14:23
     * @return HmeAgingBasicVO
     */
    Page<HmeAgingBasicVO> pageList(PageRequest pageRequest, HmeAgingBasicDTO dto, Long tenantId);

    /**
     * 老化基础数据保存
     *
     * @param vo
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-03 14:24
     * @return void
     */
    void save(HmeAgingBasicVO vo , Long tenantId);
}
