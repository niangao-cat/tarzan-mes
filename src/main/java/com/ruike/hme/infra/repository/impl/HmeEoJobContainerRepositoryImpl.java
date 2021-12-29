package com.ruike.hme.infra.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ruike.hme.domain.repository.HmeEoJobSnLotMaterialRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO1;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;
import tarzan.inventory.domain.vo.MtContainerVO13;
import tarzan.inventory.infra.mapper.MtContainerMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.order.domain.entity.MtEo;
import com.ruike.hme.domain.entity.HmeEoJobContainer;
import tarzan.order.domain.repository.MtEoRepository;
import com.ruike.hme.domain.repository.HmeEoJobContainerRepository;
import com.ruike.hme.domain.vo.HmeEoJobContainerVO;
import com.ruike.hme.domain.vo.HmeEoJobContainerVO2;
import com.ruike.hme.infra.mapper.HmeEoJobContainerMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 工序作业平台-容器 资源库实现
 *
 * @author liyuan.lv@hand-china.com 2020-03-23 12:48:53
 */
@Component
public class HmeEoJobContainerRepositoryImpl extends BaseRepositoryImpl<HmeEoJobContainer> implements HmeEoJobContainerRepository {

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEoJobContainerMapper mapper;
    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    @Autowired
    private MtContainerMapper mtContainerMapper;

    @Override
    public HmeEoJobContainerVO2 updateEoJobContainer(Long tenantId, HmeEoJobContainerVO hmeEoJobContainerVO) {
        //非空校验 add by yuchao.wang for tianyang.xie at 2020.9.12
        if (StringUtils.isBlank(hmeEoJobContainerVO.getWorkcellId())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位ID"));
        }
        if (StringUtils.isBlank(hmeEoJobContainerVO.getContainerCode())) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "容器条码"));
        }

        HmeEoJobContainer hmeEoJobContainer = wkcLimitJobContainerGet(tenantId, hmeEoJobContainerVO.getWorkcellId());
        boolean isExistsContainer = true;
        if (Objects.isNull(hmeEoJobContainer)) {
            isExistsContainer = false;

            // 获取当前时间
            final Date currentDate = new Date(System.currentTimeMillis());
            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();

            hmeEoJobContainer = new HmeEoJobContainer();
            hmeEoJobContainer.setTenantId(tenantId);
            hmeEoJobContainer.setWorkcellId(hmeEoJobContainerVO.getWorkcellId());
            hmeEoJobContainer.setSiteInDate(currentDate);
            hmeEoJobContainer.setSiteInBy(userId);
        }

        // 验证容器条码是否存在
        MtContainerVO13 containerParam = new MtContainerVO13();
        containerParam.setContainerCode(hmeEoJobContainerVO.getContainerCode());
        List<String> containerIds = mtContainerRepository.propertyLimitContainerQuery(tenantId, containerParam);
        if (CollectionUtils.isEmpty(containerIds) || StringUtils.isBlank(containerIds.get(0))) {
            throw new MtException("HME_EO_JOB_SN_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_016", "HME"));
        }

        hmeEoJobContainer.setContainerCode(hmeEoJobContainerVO.getContainerCode());
        hmeEoJobContainer.setContainerId(containerIds.get(0));

        if (isExistsContainer) {
            mapper.updateByPrimaryKeySelective(hmeEoJobContainer);
        } else {
            self().insertSelective(hmeEoJobContainer);
        }

        //判断容器是否装载SN
        MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(hmeEoJobContainer.getContainerId());
        if (Objects.isNull(mtContainer)) {
            throw new MtException("HME_EO_JOB_SN_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_016", "HME"));
        }
        MtModLocator mtModLocator = hmeEoJobSnLotMaterialRepository.selectLocator(tenantId,HmeConstants.LocaltorType.DEFAULT_STORAGE,hmeEoJobContainerVO.getWorkcellId());

        if(!mtContainer.getLocatorId().equals(mtModLocator.getLocatorId())){
            int count = mtContainerLoadDetailRepository.selectCountByCondition(Condition.builder(MtContainerLoadDetail.class)
                    .andWhere(Sqls.custom().andEqualTo(MtContainerLoadDetail.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtContainerLoadDetail.FIELD_CONTAINER_ID,hmeEoJobContainer.getContainerId())
                            .andEqualTo(MtContainerLoadDetail.FIELD_LOAD_OBJECT_TYPE,HmeConstants.LoadTypeCode.MATERIAL_LOT)).build());
            if(count > 0){
                //容器货位与当前工段默认存储货位不一致
                throw new MtException("HME_EO_JOB_SN_095", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_095", "HME"));
            }else{
                if(StringUtils.isBlank(mtContainer.getCurrentContainerId())) {
                    //容器没有上层容器，更新容器货位
                    MtContainer updateMtContainer = new MtContainer();
                    updateMtContainer.setContainerId(mtContainer.getContainerId());
                    updateMtContainer.setLocatorId(mtModLocator.getLocatorId());
                    mtContainerMapper.updateByPrimaryKeySelective(updateMtContainer);
                }else{
                    //查询上层容器
                    MtContainer topMtContainer = mtContainerRepository.selectByPrimaryKey(mtContainer.getCurrentContainerId());
                    if (Objects.isNull(topMtContainer)) {
                        throw new MtException("HME_EO_JOB_SN_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_016", "HME"));
                    }
                    if(!topMtContainer.getLocatorId().equals(mtModLocator.getLocatorId())){
                        //上层容器货位与当前工段默认存储货位不一致
                        throw new MtException("HME_EO_JOB_SN_099", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_099", "HME"));
                    }else{
                        //容器没有上层容器，更新容器货位
                        MtContainer updateMtContainer = new MtContainer();
                        updateMtContainer.setContainerId(mtContainer.getContainerId());
                        updateMtContainer.setLocatorId(mtModLocator.getLocatorId());
                        mtContainerMapper.updateByPrimaryKeySelective(updateMtContainer);
                    }
                }
            }
        }

        return constructVO(tenantId, hmeEoJobContainerVO.getWorkcellId());
    }

    @Override
    public HmeEoJobContainerVO2 constructVO(Long tenantId, String workcellId) {
        HmeEoJobContainer hmeEoJobContainer = wkcLimitJobContainerGet(tenantId, workcellId);
        HmeEoJobContainerVO2 jobContainerVo2 = new HmeEoJobContainerVO2();
        if(Objects.isNull(hmeEoJobContainer)){
            return jobContainerVo2;
        }
        MtContainer mtContainer = mtContainerRepository.containerPropertyGet(tenantId, hmeEoJobContainer.getContainerId());
        MtContainerType mtContainerType = mtContainerTypeRepository.containerTypePropertyGet(tenantId, mtContainer.getContainerTypeId());

        jobContainerVo2.setContainerId(hmeEoJobContainer.getContainerId());
        jobContainerVo2.setJobContainerId(hmeEoJobContainer.getJobContainerId());
        jobContainerVo2.setMaxLoadQty(mtContainerType.getCapacityQty());

        MtContLoadDtlVO10 contLoadDtlParam = new MtContLoadDtlVO10();
        contLoadDtlParam.setContainerId(hmeEoJobContainer.getContainerId());
        contLoadDtlParam.setAllLevelFlag(HmeConstants.ConstantValue.NO);
        List<MtContLoadDtlVO4> mtContLoadDtlVo1List = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlParam);

        if (CollectionUtils.isNotEmpty(mtContLoadDtlVo1List)) {
            List<String> materialLotIds = mtContLoadDtlVo1List.stream().filter(t -> t.getMaterialLotId() != null).map(MtContLoadDtlVO4::getMaterialLotId)
                    .distinct().collect(Collectors.toList());
            List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIds);
            jobContainerVo2.setMaterialLotList(mtMaterialLotList);

        }
        BeanUtils.copyProperties(hmeEoJobContainer, jobContainerVo2);

        return jobContainerVo2;
    }

    @Override
    public void unLoadEoJobContainer(Long tenantId, HmeEoJobContainerVO hmeEoJobContainerVO) {
        HmeEoJobContainer hmeEoJobContainer = wkcLimitJobContainerGet(tenantId, hmeEoJobContainerVO.getWorkcellId());
        if(Objects.nonNull(hmeEoJobContainer)){
            self().deleteByPrimaryKey(hmeEoJobContainer);
        }
    }

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
    @Override
    public void deleteNotCurrentWkcData(Long tenantId, String containerId, String workcellId) {
        mapper.deleteNotCurrentWkcData(tenantId, containerId, workcellId);
    }

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchOutSite(Long tenantId, Long userId, List<String> jobContainerIdList) {
        if (CollectionUtils.isNotEmpty(jobContainerIdList)) {
            List<List<String>> splitIdList = InterfaceUtils.splitSqlList(jobContainerIdList, SQL_ITEM_COUNT_LIMIT);
            for (List<String> ids : splitIdList) {
                mapper.batchOutSite(tenantId, userId, ids);
            }
        }
    }

    @Override
    public HmeEoJobContainerVO2 eoJobContainerPropertyGet(Long tenantId, String jobContainerId) {
        if (StringUtils.isEmpty(jobContainerId)) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "jobContainerId", "【API:eoJobContainerPropertyGet】"));
        }
        HmeEoJobContainer jobContainerParam = new HmeEoJobContainer();
        jobContainerParam.setTenantId(tenantId);
        jobContainerParam.setJobContainerId(jobContainerId);
        HmeEoJobContainer hmeEoJobContainer = mapper.selectOne(jobContainerParam);

        return Objects.isNull(hmeEoJobContainer) ? null : constructVO(tenantId, hmeEoJobContainer.getWorkcellId());
    }

    @Override
    public HmeEoJobContainer wkcLimitJobContainerGet(Long tenantId, String workcellId) {
        HmeEoJobContainer jobContainerParam = new HmeEoJobContainer();
        jobContainerParam.setTenantId(tenantId);
        jobContainerParam.setWorkcellId(workcellId);

        return mapper.selectOne(jobContainerParam);
    }


}
