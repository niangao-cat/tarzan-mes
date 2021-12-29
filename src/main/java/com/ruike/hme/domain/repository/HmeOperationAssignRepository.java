package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeQualificationDTO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeOperationAssign;

import java.util.List;

/**
 * 资质与工艺关系表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-06-16 19:08:35
 */
public interface HmeOperationAssignRepository extends BaseRepository<HmeOperationAssign> {


    /**
     * @Description 根据工序id查询工序资质关系
     * @param tenantId
     * @param operationId
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeQualificationDTO2>
     * @auther chaonan.hu
     * @date 2020/6/16
    */
    Page<HmeQualificationDTO2> query(Long tenantId, String operationId, PageRequest pageRequest);
    /**
     * @Description 创建资质与工艺关系
     * @param hmeOperationAssigns
     * @return com.ruike.hme.domain.entity.HmeOperationAssign
     * @auther chaonan.hu
     * @date 2020/6/16
    */
    List<HmeOperationAssign> create(List<HmeOperationAssign> hmeOperationAssigns);
}
