package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeQualificationDTO;
import com.ruike.hme.api.dto.HmeQualificationQueryDTO;
import com.ruike.hme.domain.entity.HmeQualification;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 资质基础信息表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-06-15 10:32:20
 */
public interface HmeQualificationRepository extends BaseRepository<HmeQualification> {
    /**
     * @param tenantId
     * @param dtos
     * @return java.util.List<com.ruike.qms.domain.entity.QmsTransitionRule>
     * @Description 资质基础信息 新建或更新
     * @auther chaonan.hu
     * @date 2020/6/15
     */
    List<HmeQualification> createOrUpdate(Long tenantId, List<HmeQualification> dtos);

    /**
     * @Description 资质基础信息 查询
     * @param pageRequest
     * @param hmeQualification
     * @param tenantId
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeQualificationDTO>
     * @auther chaonan.hu
     * @date 2020/6/15
    */
    Page<HmeQualificationDTO> query(PageRequest pageRequest, HmeQualificationQueryDTO hmeQualification, Long tenantId);
}
