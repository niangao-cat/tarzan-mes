package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeContainerCapacity;
import com.ruike.hme.domain.repository.HmeContainerCapacityRepository;
import com.ruike.hme.domain.vo.HmeContainerCapacityVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeContainerCapacityMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 容器容量表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-10 15:08:58
 */
@Component
public class HmeContainerCapacityRepositoryImpl extends BaseRepositoryImpl<HmeContainerCapacity> implements HmeContainerCapacityRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeContainerCapacityMapper hmeContainerCapacityMapper;

    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public Page<HmeContainerCapacityVO> containerCapacityQuery(Long tenantId, String containerTypeId, PageRequest pageRequest) {
        if(StringUtils.isBlank(containerTypeId)){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME","containerTypeId"));
        }
        Page<HmeContainerCapacityVO> pageObj = PageHelper.doPage(pageRequest,() -> hmeContainerCapacityMapper.containerCapacityQuery(tenantId, containerTypeId));
        for (HmeContainerCapacityVO capacityVO : pageObj.getContent()) {

            MtContainerType mtContainerType = mtContainerTypeRepository.selectByPrimaryKey(capacityVO.getContainerTypeId());
            if(mtContainerType != null){
                //容器类型
                capacityVO.setContainerTypeName(mtContainerType.getContainerTypeDescription());

                //行 列数
                capacityVO.setWidth(mtContainerType.getWidth());
                capacityVO.setLength(mtContainerType.getLength());

                //cos类型
                List<LovValueDTO> cosTypeList = lovAdapter.queryLovValue("HME_COS_TYPE", tenantId);

                List<LovValueDTO> collect = cosTypeList.stream().filter(f -> StringUtils.equals(f.getValue(), capacityVO.getCosType())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(collect)){
                    capacityVO.setCosTypeName(collect.get(0).getMeaning());
                }

                //装载规则
                List<LovValueDTO> rulesLovList = lovAdapter.queryLovValue("HME.LOADING_RULES", tenantId);

                List<LovValueDTO> rulesList = rulesLovList.stream().filter(f -> StringUtils.equals(f.getValue(), capacityVO.getAttribute1())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(rulesList)){
                    capacityVO.setRulesName(rulesList.get(0).getMeaning());
                }
            }

            //工艺
            MtOperation mtOperation = mtOperationRepository.selectByPrimaryKey(capacityVO.getOperationId());
            if(mtOperation != null){
                capacityVO.setOperationName(mtOperation.getOperationName());
                capacityVO.setOperationDesc(mtOperation.getDescription());
            }
        }
        return pageObj;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContainerCapacity(Long tenantId, HmeContainerCapacity hmeContainerCapacity) {
        if(StringUtils.isBlank(hmeContainerCapacity.getContainerCapacityId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME","containerCapacityId"));
        }
        hmeContainerCapacity.setEnableFlag("N");
        hmeContainerCapacityMapper.updateByPrimaryKeySelective(hmeContainerCapacity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeContainerCapacity createContainerCapacity(Long tenantId, HmeContainerCapacity hmeContainerCapacity) {
        //校验
        if(StringUtils.isBlank(hmeContainerCapacity.getContainerTypeId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME","containerTypeId"));
        }

        //工艺
        if(StringUtils.isBlank(hmeContainerCapacity.getOperationId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME","operationId"));
        }

        //芯片类型
        if(StringUtils.isBlank(hmeContainerCapacity.getCosType())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME","cosType"));
        }

        //芯片数
        if(hmeContainerCapacity.getCapacity() == null){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME","capacity"));
        }

        if(Long.valueOf(0).compareTo(hmeContainerCapacity.getCapacity()) > 0){
            throw new MtException("HME_CONTAINER_CAPACITY_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CONTAINER_CAPACITY_002", "HME"));
        }

        //校验唯一性(工艺+COS类型)
        Condition condition = new Condition(HmeContainerCapacity.class);
        condition.and().andEqualTo(HmeContainerCapacity.FIELD_OPERATION_ID, hmeContainerCapacity.getOperationId())
                       .andEqualTo(HmeContainerCapacity.FIELD_COS_TYPE, hmeContainerCapacity.getCosType())
                       .andEqualTo(HmeContainerCapacity.FIELD_CONTAINER_TYPE_ID, hmeContainerCapacity.getContainerTypeId())
                       .andEqualTo(HmeContainerCapacity.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)
                       .andEqualTo(HmeContainerCapacity.FIELD_TENANT_ID, tenantId);
        List<HmeContainerCapacity> capacityList = hmeContainerCapacityMapper.selectByCondition(condition);

        if(StringUtils.isBlank(hmeContainerCapacity.getContainerCapacityId())){
            if(CollectionUtils.isNotEmpty(capacityList)){
                throw new MtException("HME_CONTAINER_CAPACITY_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CONTAINER_CAPACITY_001", "HME",hmeContainerCapacity.getOperationId(),hmeContainerCapacity.getCosType()));
            }

            //若删除数据已有 则更新数据
            Condition capacityCondition = new Condition(HmeContainerCapacity.class);
            capacityCondition.and().andEqualTo(HmeContainerCapacity.FIELD_OPERATION_ID, hmeContainerCapacity.getOperationId())
                    .andEqualTo(HmeContainerCapacity.FIELD_COS_TYPE, hmeContainerCapacity.getCosType())
                    .andEqualTo(HmeContainerCapacity.FIELD_CONTAINER_TYPE_ID, hmeContainerCapacity.getContainerTypeId())
                    .andEqualTo(HmeContainerCapacity.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.NO)
                    .andEqualTo(HmeContainerCapacity.FIELD_TENANT_ID, tenantId);
            List<HmeContainerCapacity> containerCapacityList = hmeContainerCapacityMapper.selectByCondition(capacityCondition);
            if(CollectionUtils.isNotEmpty(containerCapacityList)){
                hmeContainerCapacity.setEnableFlag(HmeConstants.ConstantValue.YES);
                hmeContainerCapacity.setContainerCapacityId(containerCapacityList.get(0).getContainerCapacityId());
                hmeContainerCapacityMapper.updateByPrimaryKeySelective(hmeContainerCapacity);
            }else {
                //新增
                hmeContainerCapacity.setEnableFlag(HmeConstants.ConstantValue.YES);
                hmeContainerCapacity.setTenantId(tenantId);
                self().insertSelective(hmeContainerCapacity);
            }

        }else {
            if(CollectionUtils.isNotEmpty(capacityList)){
                if(!StringUtils.equals(hmeContainerCapacity.getContainerCapacityId(),capacityList.get(0).getContainerCapacityId())){
                    throw new MtException("HME_CONTAINER_CAPACITY_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CONTAINER_CAPACITY_001", "HME",hmeContainerCapacity.getOperationId(),hmeContainerCapacity.getCosType()));
                }
            }
            hmeContainerCapacityMapper.updateByPrimaryKeySelective(hmeContainerCapacity);
        }

        return hmeContainerCapacity;
    }

    @Override
    public void batchCreateContainerCapacity(Long tenantId, List<HmeContainerCapacity> hmeContainerCapacityList) {
        if(CollectionUtils.isNotEmpty(hmeContainerCapacityList)){
            hmeContainerCapacityList.forEach(e ->{
                createContainerCapacity(tenantId,e);
            });
        }
    }
}
