package com.ruike.hme.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEoJobContainer;
import com.ruike.hme.domain.vo.HmeEoJobContainerVO;
import com.ruike.hme.domain.vo.HmeEoJobContainerVO2;

import java.util.*;

/**
 * 工序作业平台-容器资源库
 *
 * @author liyuan.lv@hand-china.com 2020-03-23 12:48:53
 */
public interface HmeEoJobContainerRepository extends BaseRepository<HmeEoJobContainer>, AopProxy<HmeEoJobContainerRepository> {
    /**
     * 更新当前工位绑定容器
     * @param tenantId
     * @param hmeEoJobContainerVO
     * @return
     */
    HmeEoJobContainerVO2 updateEoJobContainer(Long tenantId, HmeEoJobContainerVO hmeEoJobContainerVO);

    /**
     * 根据工序作业容器ID获取实体数据
     * @param tenantId
     * @param jobContainerId
     * @return
     */
    HmeEoJobContainerVO2 eoJobContainerPropertyGet(Long tenantId, String jobContainerId);

    /**
     * 根据工位ID获取实体数据
     * @param tenantId
     * @param workcellId
     * @return
     */
    HmeEoJobContainer wkcLimitJobContainerGet(Long tenantId, String workcellId);

    /**
     * 获取工序作业容器实体信息
     * @param tenantId
     * @param workcellId
     * @return
     */
    HmeEoJobContainerVO2 constructVO(Long tenantId, String workcellId);

    /**
     * 卸载当前工位绑定容器
     * @param tenantId
     * @param hmeEoJobContainerVO
     * @return
     */
    void unLoadEoJobContainer(Long tenantId, HmeEoJobContainerVO hmeEoJobContainerVO);

    /**
     *
     * @Description 根据容器ID删除非当前工位的数据
     *
     * @author yuchao.wang
     * @date 2020/9/12 11:41
     * @param tenantId 租户ID
     * @param containerId 容器ID
     * @param workcellId 工位ID
     * @return void
     *
     */
    void deleteNotCurrentWkcData(Long tenantId, String containerId, String workcellId);

    /**
     *
     * @Description 根据JobContainerId批量出站
     *
     * @author yuchao.wang
     * @date 2021/1/18 9:55
     * @param tenantId 租户
     * @param userId 用户ID
     * @param jobContainerIdList jobContainerIdList
     * @return void
     *
     */
    void batchOutSite(Long tenantId, Long userId, List<String> jobContainerIdList);

}
