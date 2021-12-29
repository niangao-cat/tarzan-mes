package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeContainerCapacityVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeContainerCapacity;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 容器容量表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-10 15:08:58
 */
public interface HmeContainerCapacityRepository extends BaseRepository<HmeContainerCapacity>, AopProxy<HmeContainerCapacityRepository> {


    /**
     * 容器容量列表
     *
     * @param tenantId              租户id
     * @param containerTypeId  条件
     * @param pageRequest           分页参数
     * @author sanfeng.zhang@hand-china.com 2020/8/10 15:44
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeContainerCapacityVO>
     */
    Page<HmeContainerCapacityVO> containerCapacityQuery(Long tenantId, String containerTypeId,PageRequest pageRequest);


    /**
     * 删除容器容量
     *
     * @param tenantId                  租户id
     * @param hmeContainerCapacity      条件
     * @author sanfeng.zhang@hand-china.com 2020/8/10 16:15
     * @return void
     */
    void deleteContainerCapacity(Long tenantId, HmeContainerCapacity hmeContainerCapacity);

    /**
     * 创建&修改容器容量
     *
     * @param tenantId                  租户id
     * @param hmeContainerCapacity      参数
     * @author sanfeng.zhang@hand-china.com 2020/8/10 16:22
     * @return com.ruike.hme.domain.entity.HmeContainerCapacity
     */
    HmeContainerCapacity createContainerCapacity(Long tenantId, HmeContainerCapacity hmeContainerCapacity);

    /**
     * 批量创建&更新容器容量
     *
     * @param tenantId                      租户id
     * @param hmeContainerCapacityList      参数
     * @author sanfeng.zhang@hand-china.com 2020/8/11 16:44
     * @return void
     */
    void batchCreateContainerCapacity(Long tenantId, List<HmeContainerCapacity> hmeContainerCapacityList);
}
