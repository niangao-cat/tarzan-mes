package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmePreSelectionDTO;
import com.ruike.hme.api.dto.HmePreSelectionReturnDTO;
import com.ruike.hme.infra.mapper.HmePreSelectionMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmePreSelection;
import com.ruike.hme.domain.repository.HmePreSelectionRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预挑选基础表 资源库实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-18 15:00:33
 */
@Component
public class HmePreSelectionRepositoryImpl extends BaseRepositoryImpl<HmePreSelection> implements HmePreSelectionRepository {

    @Autowired
    private HmePreSelectionMapper hmePreSelectionMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Override
    public List<HmePreSelectionReturnDTO> workOrderQuery(Long tenantId, HmePreSelectionDTO dto) {
        return hmePreSelectionMapper.workOrderQuery(tenantId,dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialLotBatchUnBindingContainer(Long tenantId, List<String> materialLotIdList) {
        //将条码在物料批表中对应的CURRENT_CONTAINER_ID，TOP_CONTAINER_ID置为空
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        hmePreSelectionMapper.batchUpdateMaterialLotContainerId(tenantId, userId, materialLotIdList);
        //将条码在表mt_container_load_detail中LOAD_OBJECT_TYPE为MATERIAL_LOT的数据删除
        List<MtContainerLoadDetail> mtContainerLoadDetailList = mtContainerLoadDetailRepository.selectByCondition(Condition.builder(MtContainerLoadDetail.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(MtContainerLoadDetail.FIELD_TENANT_ID, tenantId)
                        .andIn(MtContainerLoadDetail.FIELD_LOAD_OBJECT_ID, materialLotIdList)
                        .andEqualTo(MtContainerLoadDetail.FIELD_LOAD_OBJECT_TYPE, "MATERIAL_LOT"))
                .build());
        if (CollectionUtils.isNotEmpty(mtContainerLoadDetailList)) {
            mtContainerLoadDetailRepository.batchDeleteByPrimaryKey(mtContainerLoadDetailList);
        }
    }
}
