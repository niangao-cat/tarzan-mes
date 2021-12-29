package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeEmployeeAssignDTO;
import com.ruike.hme.api.dto.HmeEmployeeAssignDTO3;
import com.ruike.hme.api.dto.HmeQualificationDTO2;
import com.ruike.hme.api.dto.HmeQualificationDTO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEmployeeAssign;

import java.text.ParseException;
import java.util.List;

/**
 * 人员与资质关系表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-06-16 11:13:32
 */
public interface HmeEmployeeAssignRepository extends BaseRepository<HmeEmployeeAssign> {

    /**
     * 根据员工Id查询人员与资质关系
     *
     * @param tenantId    租户ID
     * @param employeeId  员工ID
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeEmployeeAssignDTO>
     * @author chaonan.hu 2020/6/17
     */
    Page<HmeEmployeeAssignDTO> query(Long tenantId, String employeeId, PageRequest pageRequest);

    /**
     * 创建人员与资质关系
     *
     * @param hmeEmployeeAssign 关系数据列表
     * @return com.ruike.hme.domain.entity.HmeEmployeeAssign
     * @author chaonan.hu 2020/6/17
     */
    List<HmeEmployeeAssign> createOrUpdate(List<HmeEmployeeAssign> hmeEmployeeAssign);

    /**
     * 资质查询LOV
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeQualificationDTO3>
     * @author chaonan.hu
     * @date 2020/6/17
     */
    Page<HmeQualificationDTO3> listForUi(Long tenantId, HmeQualificationDTO3 dto, PageRequest pageRequest);
}
