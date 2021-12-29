package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeVirtualNum;
import com.ruike.hme.domain.repository.HmeVirtualNumRepository;
import com.ruike.hme.domain.vo.HmeVirtualNumVO;
import com.ruike.hme.infra.mapper.HmeVirtualNumMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 虚拟号基础表 资源库实现
 *
 * @author wenzhang.yu@hand-china.com 2020-09-28 19:47:55
 */
@Component
@Slf4j
public class HmeVirtualNumRepositoryImpl extends BaseRepositoryImpl<HmeVirtualNum> implements HmeVirtualNumRepository {

    @Autowired
    private HmeVirtualNumMapper hmeVirtualNumMapper;

    /**
     *
     * @Description 查询条码下的虚拟号
     *
     * @author yuchao.wang
     * @date 2020/9/29 23:07
     * @param tenantId 租户ID
     * @param materialLotCodeList 条码号
     * @return java.util.List<com.ruike.hme.domain.vo.HmeVirtualNumVO>
     *
     */
    @Override
    @ProcessLovValue
    public List<HmeVirtualNumVO> queryVirtualNumByBarcode(Long tenantId, List<String> materialLotCodeList) {
        return hmeVirtualNumMapper.queryVirtualNumByBarcode(tenantId, materialLotCodeList);
    }

    /**
     *
     * @Description 根据物料批ID查询最大虚拟号ID对应的工单ID
     *
     * @author yuchao.wang
     * @date 2020/9/29 23:28
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return String
     *
     */
    @Override
    public HmeVirtualNum queryMaxVirtualNumWoId(Long tenantId, String materialLotId) {
        return hmeVirtualNumMapper.queryMaxVirtualNumWoId(tenantId, materialLotId);
    }

    /**
     *
     * @Description 投料更新虚拟号
     *
     * @author yuchao.wang
     * @date 2020/9/30 12:26
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param virtualIdList 虚拟号ID列表
     * @return void
     *
     */
    @Override
    public void batchUpdateVirtualNumForRelease(Long tenantId, String eoId, List<String> virtualIdList) {
        if (CollectionUtils.isNotEmpty(virtualIdList)) {

            log.info("batchUpdateVirtualNumForRelease-eoId:" + eoId);

            int batchNum = 500;
            Long userId = (Objects.nonNull(DetailsHelper.getUserDetails()) && Objects.nonNull(DetailsHelper.getUserDetails().getUserId())) ? DetailsHelper.getUserDetails().getUserId() : -1L;
            List<List<String>> list = InterfaceUtils.splitSqlList(virtualIdList, batchNum);
            list.forEach(item -> hmeVirtualNumMapper.batchUpdateVirtualNumForRelease(tenantId, eoId, userId, item));

            log.info("batchUpdateVirtualNumForRelease-end");
        }
    }

    /**
     *
     * @Description 投料退回更新虚拟号
     *
     * @author yuchao.wang
     * @date 2020/10/5 19:44
     * @param tenantId 租户ID
     * @param eoId eoId
     * @return void
     *
     */
    @Override
    public void updateVirtualNumForReleaseBack(Long tenantId, String eoId) {
        Long userId = (Objects.nonNull(DetailsHelper.getUserDetails()) && Objects.nonNull(DetailsHelper.getUserDetails().getUserId()))
                ? DetailsHelper.getUserDetails().getUserId() : -1L;
        hmeVirtualNumMapper.updateVirtualNumForReleaseBack(tenantId, eoId, userId);
    }
}
