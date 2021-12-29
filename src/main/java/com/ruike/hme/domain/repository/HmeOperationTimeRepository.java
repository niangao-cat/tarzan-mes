package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeOperationTimeDto;
import com.ruike.hme.api.dto.HmeOperationTimeDto2;
import com.ruike.hme.api.dto.HmeOperationTimeDto3;
import com.ruike.hme.api.dto.HmeOperationTimeDto4;
import com.ruike.hme.domain.entity.HmeOperationTimeMaterial;
import com.ruike.hme.domain.entity.HmeOperationTimeObject;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeOperationTime;

import java.util.List;

/**
 * 工艺时效要求表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-08-11 11:44:06
 */
public interface HmeOperationTimeRepository extends BaseRepository<HmeOperationTime> {

    /**
     * 分页查询工艺时效要求表
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/11 15:22:01
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeOperationTimeVO>
     */
    Page<HmeOperationTimeVO> query(Long tenantId, HmeOperationTimeDto4 dto, PageRequest pageRequest);

    /**
     * 创建或者更新工艺时效要求表
     *
     * @param tenantId 租户ID
     * @param hmeOperationTimeList 创建或更新数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/11 14:49:57
     * @return java.util.List<com.ruike.hme.domain.entity.HmeOperationTime>
     */
    List<HmeOperationTime> createOrUpdate(Long tenantId, List<HmeOperationTime> hmeOperationTimeList);

    /**
     * 分页查询关联物料
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/11 20:31:22
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeOperationTimeVO2>
     */
    Page<HmeOperationTimeVO2> queryMaterial(Long tenantId, HmeOperationTimeDto dto, PageRequest pageRequest);

    /**
     * 创建或者更新关联物料
     *
     * @param tenantId 租户ID
     * @param list 创建或更新物料数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/11 17:19:25
     * @return java.util.List<com.ruike.hme.domain.entity.HmeOperationTimeMaterial>
     */
    List<HmeOperationTimeMaterial> createOrUpdateMaterial(Long tenantId, List<HmeOperationTimeMaterial> list);

    /**
     * 分页查询关联对象
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 10:26:34
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeOperationTimeVO3>
     */
    Page<HmeOperationTimeVO3> queryObject(Long tenantId, HmeOperationTimeDto2 dto, PageRequest pageRequest);

    /**
     * 创建或者更新关联对象
     *
     * @param tenantId 租户ID
     * @param list 创建或更新对象数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 10:00:07
     * @return java.util.List<com.ruike.hme.domain.entity.HmeOperationTimeObject>
     */
    List<HmeOperationTimeObject> createOrUpdateObject(Long tenantId, List<HmeOperationTimeObject> list);

    /**
     * 工艺时效历史查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 14:00:48
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeOperationTimeVO4>
     */
    Page<HmeOperationTimeVO4> queryHis(Long tenantId, HmeOperationTimeDto3 dto, PageRequest pageRequest);

    /**
     * 关联物料历史查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 14:23:08
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeOperationTimeVO5>
     */
    Page<HmeOperationTimeVO5> queryMaterialHis(Long tenantId, HmeOperationTimeDto3 dto, PageRequest pageRequest);

    /**
     * 关联对象历史查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 14:34:34
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeOperationTimeVO6>
     */
    Page<HmeOperationTimeVO6> queryObjectHis(Long tenantId, HmeOperationTimeDto3 dto, PageRequest pageRequest);
}
