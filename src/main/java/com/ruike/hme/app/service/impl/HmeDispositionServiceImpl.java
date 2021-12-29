package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeDispositionFunctionDTO;
import com.ruike.hme.api.dto.HmeDispositionGroupDTO;
import com.ruike.hme.api.dto.HmeDispositionGroupDetailDTO;
import com.ruike.hme.app.service.HmeDispositionService;
import com.ruike.hme.infra.mapper.HmeDispositionMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.method.domain.entity.MtDispositionFunction;
import tarzan.method.domain.entity.MtDispositionGroup;
import tarzan.method.domain.entity.MtDispositionGroupMember;
import tarzan.method.domain.repository.MtDispositionFunctionRepository;
import tarzan.method.domain.repository.MtDispositionGroupMemberRepository;
import tarzan.method.domain.repository.MtDispositionGroupRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description 处置方法与处置组功能Impl
 *
 * @author quan.luo@hand-china.com 2020/11/24 10:01
 */
@Service
public class HmeDispositionServiceImpl implements HmeDispositionService {

    @Autowired
    private HmeDispositionMapper hmeDispositionMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtDispositionFunctionRepository mtDispositionFunctionRepository;

    @Autowired
    private MtDispositionGroupRepository mtDispositionGroupRepository;

    @Autowired
    private MtDispositionGroupMemberRepository mtDispositionGroupMemberRepository;

    @Override
    public Page<HmeDispositionFunctionDTO> queryFunctionByCondition(Long tenantId, PageRequest pageRequest,
                                                                    HmeDispositionFunctionDTO hmeDispositionFunctionDTO) {
        return PageHelper.doPage(pageRequest, () -> hmeDispositionMapper.queryFunctionByCondition(tenantId, hmeDispositionFunctionDTO));
    }

    @Override
    public List<MtGenType> functionTypeQuery(Long tenantId) {
        return mtGenTypeRepository.selectByCondition(Condition.builder(MtGenType.class).andWhere(Sqls.custom()
                .andEqualTo(MtGenType.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtGenType.FIELD_TYPE_GROUP, "FUNCTION_TYPE")).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtDispositionFunction functionSave(Long tenantId, MtDispositionFunction mtDispositionFunction) {
        mtDispositionFunction.setTenantId(tenantId);
        List<MtDispositionFunction> mtDispositionFunctionList = mtDispositionFunctionRepository.selectByCondition(Condition.builder(MtDispositionFunction.class).andWhere(Sqls.custom()
                .andEqualTo(MtDispositionFunction.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtDispositionFunction.FIELD_ROUTER_ID, mtDispositionFunction.getRouterId())
                .andEqualTo(MtDispositionFunction.FIELD_DISPOSITION_FUNCTION, mtDispositionFunction.getDispositionFunction())
                .andEqualTo(MtDispositionFunction.FIELD_FUNCTION_TYPE, mtDispositionFunction.getFunctionType())
                .andNotEqualTo(MtDispositionFunction.FIELD_DISPOSITION_FUNCTION_ID, mtDispositionFunction.getDispositionFunctionId(), true)).build());
        if (CollectionUtils.isNotEmpty(mtDispositionFunctionList)) {
            throw new MtException("数据重复请检查！");
        }
        // 保存或修改
        if (StringUtils.isNotBlank(mtDispositionFunction.getDispositionFunctionId())) {
            mtDispositionFunctionRepository.updateByPrimaryKeySelective(mtDispositionFunction);
        } else {
            mtDispositionFunctionRepository.insertSelective(mtDispositionFunction);
        }
        return mtDispositionFunction;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> functionDel(Long tenantId, List<String> dispositionFunctionIdList) {
        if (CollectionUtils.isEmpty(dispositionFunctionIdList)) {
            throw new MtException("请选择需要删除的数据！");
        }
        List<MtDispositionFunction> mtDispositionFunctionList = new ArrayList<>();
        dispositionFunctionIdList.forEach(s -> {
            MtDispositionFunction mtDispositionFunction = new MtDispositionFunction();
            mtDispositionFunction.setTenantId(tenantId);
            mtDispositionFunction.setDispositionFunctionId(s);
            mtDispositionFunctionList.add(mtDispositionFunction);
        });
        mtDispositionFunctionRepository.batchDeleteByPrimaryKey(mtDispositionFunctionList);
        return dispositionFunctionIdList;
    }

    @Override
    public Page<HmeDispositionGroupDTO> queryGroupByCondition(Long tenantId, PageRequest pageRequest,
                                                              HmeDispositionGroupDTO hmeDispositionGroupDTO) {
        return PageHelper.doPage(pageRequest, () -> hmeDispositionMapper.queryGroupByCondition(tenantId, hmeDispositionGroupDTO));
    }

    @Override
    public Page<HmeDispositionFunctionDTO> groupDetailQuery(Long tenantId, PageRequest pageRequest, String dispositionGroupId) {
        return PageHelper.doPage(pageRequest, () -> hmeDispositionMapper.groupDetailQuery(tenantId, dispositionGroupId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String groupSaveOrUpdate(Long tenantId, HmeDispositionGroupDetailDTO hmeDispositionGroupDetailDTO) {
        MtDispositionGroup mtDispositionGroup = new MtDispositionGroup();
        mtDispositionGroup.setTenantId(tenantId);
        mtDispositionGroup.setDispositionGroup(hmeDispositionGroupDetailDTO.getDispositionGroup());
        mtDispositionGroup.setSiteId(hmeDispositionGroupDetailDTO.getSiteId());
        mtDispositionGroup.setDescription(hmeDispositionGroupDetailDTO.getDescription());
        List<MtDispositionGroup> mtDispositionGroupList = mtDispositionGroupRepository.selectByCondition(Condition.builder(MtDispositionGroup.class).andWhere(Sqls.custom()
                .andEqualTo(MtDispositionGroup.FIELD_TENANT_ID, tenantId)
                .andEqualTo(MtDispositionGroup.FIELD_DISPOSITION_GROUP, hmeDispositionGroupDetailDTO.getDispositionGroup())
                .andEqualTo(MtDispositionGroup.FIELD_SITE_ID, hmeDispositionGroupDetailDTO.getSiteId())
                .andNotEqualTo(MtDispositionGroup.FIELD_DISPOSITION_GROUP_ID, hmeDispositionGroupDetailDTO.getDispositionGroupId(), true)).build());
        if (CollectionUtils.isNotEmpty(mtDispositionGroupList)) {
            throw new MtException("处置组数据重复请检查！");
        }
        // 校验是否有重复的处置方法
        if (CollectionUtils.isNotEmpty(hmeDispositionGroupDetailDTO.getHmeDispositionFunctionDtoList())) {
            Map<String, List<HmeDispositionFunctionDTO>> dispositionFunctionIdMap = hmeDispositionGroupDetailDTO.getHmeDispositionFunctionDtoList().stream()
                    .collect(Collectors.groupingBy(HmeDispositionFunctionDTO::getDispositionFunctionId));
            if (dispositionFunctionIdMap.size() != hmeDispositionGroupDetailDTO.getHmeDispositionFunctionDtoList().size()) {
                throw new MtException("处置方法数据重复请检查！");
            }
        }
        // 判断保存或者修改
        if (StringUtils.isNotBlank(hmeDispositionGroupDetailDTO.getDispositionGroupId())) {
            mtDispositionGroup.setDispositionGroupId(hmeDispositionGroupDetailDTO.getDispositionGroupId());
            mtDispositionGroupRepository.updateByPrimaryKeySelective(mtDispositionGroup);
            if (CollectionUtils.isNotEmpty(hmeDispositionGroupDetailDTO.getHmeDispositionFunctionDtoList())) {
                saveOrUpdateLine(mtDispositionGroup, hmeDispositionGroupDetailDTO.getHmeDispositionFunctionDtoList());
            }
        } else {
            mtDispositionGroupRepository.insertSelective(mtDispositionGroup);
            if (CollectionUtils.isNotEmpty(hmeDispositionGroupDetailDTO.getHmeDispositionFunctionDtoList())) {
                saveOrUpdateLine(mtDispositionGroup, hmeDispositionGroupDetailDTO.getHmeDispositionFunctionDtoList());
            }
        }
        return mtDispositionGroup.getDispositionGroupId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String groupMemberDel(Long tenantId, HmeDispositionFunctionDTO hmeDispositionFunctionDTO) {
        MtDispositionGroupMember mtDispositionGroupMember = new MtDispositionGroupMember();
        mtDispositionGroupMember.setDispositionGroupMemberId(hmeDispositionFunctionDTO.getDispositionGroupMemberId());
        mtDispositionGroupMemberRepository.deleteByPrimaryKey(mtDispositionGroupMember);
        return hmeDispositionFunctionDTO.getDispositionGroupMemberId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> groupDel(Long tenantId, List<String> dispositionGroupIdList) {
        if (CollectionUtils.isEmpty(dispositionGroupIdList)) {
            throw new MtException("请选择需要删除的数据！");
        }
        for (String dispositionGroupId : dispositionGroupIdList) {
            MtDispositionGroup mtDispositionGroup = new MtDispositionGroup();
            mtDispositionGroup.setDispositionGroupId(dispositionGroupId);
            mtDispositionGroupRepository.deleteByPrimaryKey(dispositionGroupId);
            List<MtDispositionGroupMember> mtDispositionGroupMemberList = mtDispositionGroupMemberRepository.selectByCondition(Condition
                    .builder(MtDispositionGroupMember.class).andWhere(Sqls.custom()
                            .andEqualTo(MtDispositionGroupMember.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtDispositionGroupMember.FIELD_DISPOSITION_GROUP_ID, dispositionGroupId)).build());
            if (CollectionUtils.isNotEmpty(mtDispositionGroupMemberList)) {
                mtDispositionGroupMemberRepository.batchDeleteByPrimaryKey(mtDispositionGroupMemberList);
            }
        }
        return dispositionGroupIdList;
    }

    /**
     * 保存或修改行数据
     *
     * @param mtDispositionGroup            头数据
     * @param hmeDispositionFunctionDtoList 行数据
     */
    private void saveOrUpdateLine(MtDispositionGroup mtDispositionGroup,
                                  List<HmeDispositionFunctionDTO> hmeDispositionFunctionDtoList) {
        for (HmeDispositionFunctionDTO hmeDispositionFunctionDTO : hmeDispositionFunctionDtoList) {
            MtDispositionGroupMember mtDispositionGroupMember = new MtDispositionGroupMember();
            mtDispositionGroupMember.setTenantId(mtDispositionGroup.getTenantId());
            mtDispositionGroupMember.setDispositionFunctionId(hmeDispositionFunctionDTO.getDispositionFunctionId());
            mtDispositionGroupMember.setDispositionGroupId(mtDispositionGroup.getDispositionGroupId());
            mtDispositionGroupMember.setSequence(hmeDispositionFunctionDTO.getSequence());
            if (StringUtils.isNotBlank(hmeDispositionFunctionDTO.getDispositionGroupMemberId())) {
                mtDispositionGroupMember.setDispositionGroupMemberId(hmeDispositionFunctionDTO.getDispositionGroupMemberId());
                mtDispositionGroupMemberRepository.updateByPrimaryKeySelective(mtDispositionGroupMember);
            } else {
                mtDispositionGroupMemberRepository.insertSelective(mtDispositionGroupMember);
            }
        }
    }
}
