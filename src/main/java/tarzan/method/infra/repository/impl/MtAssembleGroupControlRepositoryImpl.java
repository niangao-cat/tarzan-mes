package tarzan.method.infra.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtAssembleControl;
import tarzan.method.domain.entity.MtAssembleGroup;
import tarzan.method.domain.entity.MtAssembleGroupControl;
import tarzan.method.domain.repository.MtAssembleGroupControlRepository;
import tarzan.method.domain.repository.MtAssembleGroupRepository;
import tarzan.method.domain.vo.MtAssembleGroupControlVO;
import tarzan.method.domain.vo.MtAssembleGroupControlVO1;
import tarzan.method.domain.vo.MtAssembleGroupControlVO2;
import tarzan.method.infra.mapper.MtAssembleControlMapper;
import tarzan.method.infra.mapper.MtAssembleGroupControlMapper;
import tarzan.method.infra.mapper.MtAssembleGroupMapper;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

/**
 * 装配组控制，标识具体装配控制下对装配组的控制，装配组可安装的工作单元 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
@Component
public class MtAssembleGroupControlRepositoryImpl extends BaseRepositoryImpl<MtAssembleGroupControl>
                implements MtAssembleGroupControlRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtAssembleGroupControlMapper mtAssembleGroupControlMapper;

    @Autowired
    private MtAssembleControlMapper mtAssembleControlMapper;

    @Autowired
    private MtAssembleGroupMapper mtAssembleGroupMapper;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtAssembleGroupRepository mtAssembleGroupRepository;

    @Override
    public List<String> wkcLimitAvailableAssembleGroupQuery(Long tenantId, MtAssembleGroupControlVO condition) {
        if (StringUtils.isEmpty(condition.getAssembleControlId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:wkcLimitAvailableAssembleGroupQuery】"));
        }
        if (StringUtils.isEmpty(condition.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "workcellId",
                                            "【API:wkcLimitAvailableAssembleGroupQuery】"));
        }

        MtAssembleGroupControl mtAssembleGroupControl = new MtAssembleGroupControl();
        mtAssembleGroupControl.setTenantId(tenantId);
        mtAssembleGroupControl.setAssembleControlId(condition.getAssembleControlId());
        mtAssembleGroupControl.setWorkcellId(condition.getWorkcellId());
        mtAssembleGroupControl.setEnableFlag("Y");
        List<MtAssembleGroupControl> mtAssembleGroupControls =
                        this.mtAssembleGroupControlMapper.select(mtAssembleGroupControl);
        if (CollectionUtils.isEmpty(mtAssembleGroupControls)) {
            return Collections.emptyList();
        }
        return mtAssembleGroupControls.stream().map(MtAssembleGroupControl::getAssembleGroupId)
                        .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String assembleGroupControlUpdate(Long tenantId, MtAssembleGroupControl dto) {
        if (StringUtils.isEmpty(dto.getAssembleControlId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:assembleGroupControlUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getAssembleGroupId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "assembleGroupId", "【API:assembleGroupControlUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "workcellId", "【API:assembleGroupControlUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0001",
                                            "ASSEMBLE_CONTROL", "enableFlag", "【API:assembleGroupControlUpdate】"));
        } else {
            if (!Arrays.asList("", "N", "Y").contains(dto.getEnableFlag())) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0020",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0020",
                                                "ASSEMBLE_CONTROL", "enableFlag", "【API:assembleGroupControlUpdate】"));
            }
        }

        MtAssembleControl mtAssembleControl = new MtAssembleControl();
        mtAssembleControl.setTenantId(tenantId);
        mtAssembleControl.setAssembleControlId(dto.getAssembleControlId());
        mtAssembleControl = this.mtAssembleControlMapper.selectOne(mtAssembleControl);
        if (null == mtAssembleControl) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                            "ASSEMBLE_CONTROL", "assembleControlId",
                                            "【API:assembleGroupControlUpdate】"));
        }

        MtAssembleGroup mtAssembleGroup = new MtAssembleGroup();
        mtAssembleGroup.setTenantId(tenantId);
        mtAssembleGroup.setAssembleGroupId(dto.getAssembleGroupId());
        mtAssembleGroup = this.mtAssembleGroupMapper.selectOne(mtAssembleGroup);
        if (null == mtAssembleGroup || "CLOSED".equals(mtAssembleGroup.getAssembleGroupStatus())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "assembleGroupId", "【API:assembleGroupControlUpdate】"));
        }

        MtModWorkcell mtModWorkcell =
                        this.mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
        if (null == mtModWorkcell || !"Y".equals(mtModWorkcell.getEnableFlag())) {
            throw new MtException("MT_ASSEMBLE_CONTROL_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0002",
                                            "ASSEMBLE_CONTROL", "workcellId", "【API:assembleGroupControlUpdate】"));
        }

        MtAssembleGroupControl mtAssembleGroupControl = new MtAssembleGroupControl();
        mtAssembleGroupControl.setTenantId(tenantId);
        mtAssembleGroupControl.setAssembleControlId(dto.getAssembleControlId());
        mtAssembleGroupControl.setAssembleGroupId(dto.getAssembleGroupId());
        mtAssembleGroupControl.setWorkcellId(dto.getWorkcellId());
        mtAssembleGroupControl.setEnableFlag(dto.getEnableFlag());
        if (StringUtils.isEmpty(dto.getAssembleGroupControlId())) {
            self().insertSelective(mtAssembleGroupControl);
        } else {
            MtAssembleGroupControl tmpMtAssembleGroupControl = new MtAssembleGroupControl();
            tmpMtAssembleGroupControl.setTenantId(tenantId);
            tmpMtAssembleGroupControl.setAssembleGroupControlId(dto.getAssembleGroupControlId());
            tmpMtAssembleGroupControl = this.mtAssembleGroupControlMapper.selectOne(tmpMtAssembleGroupControl);
            if (null == tmpMtAssembleGroupControl) {
                throw new MtException("MT_ASSEMBLE_CONTROL_0003",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ASSEMBLE_CONTROL_0003",
                                                "ASSEMBLE_CONTROL", "assembleGroupControlId",
                                                "【API:assembleGroupControlUpdate】"));
            }

            mtAssembleGroupControl.setAssembleGroupControlId(dto.getAssembleGroupControlId());
            self().updateByPrimaryKeySelective(mtAssembleGroupControl);
        }

        return mtAssembleGroupControl.getAssembleGroupControlId();
    }

    @Override
    public List<MtAssembleGroupControlVO2> propertyLimitAssembleGroupControlPropertyQuery(Long tenantId,
                                                                                          MtAssembleGroupControlVO1 dto) {
        List<MtAssembleGroupControl> list =
                        mtAssembleGroupControlMapper.propertyLimitAssembleGroupControlPropertyQuery(tenantId, dto);
        if (CollectionUtils.isNotEmpty(list)) {
            List<MtAssembleGroupControlVO2> result = new ArrayList<>();
            // 根据第一步获取到的装配组 assembleGroupId列表，调用API{ assembleGroupPropertyBatchGet }获取装配组描述和装配组编码编码
            List<String> assembleGroupIds =
                            list.stream().map(MtAssembleGroupControl::getAssembleGroupId).collect(Collectors.toList());
            List<MtAssembleGroup> groups =
                            mtAssembleGroupRepository.assembleGroupPropertyBatchGet(tenantId, assembleGroupIds);
            Map<String, MtAssembleGroup> groupMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(groups)) {
                groupMap = groups.stream().collect(Collectors.toMap(MtAssembleGroup::getAssembleGroupId, t -> t));
            }
            // 根据第一步获取到的工作单元 workcellId列表，调用API{ workcellBasicPropertyBatchGet }获取工作单元短描述、工作单元长描述和工作单元编码
            List<String> workCellIds =
                            list.stream().map(MtAssembleGroupControl::getWorkcellId).collect(Collectors.toList());
            List<MtModWorkcell> workcells =
                            mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workCellIds);
            Map<String, MtModWorkcell> workcellMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(workcells)) {
                workcellMap = workcells.stream().collect(Collectors.toMap(MtModWorkcell::getWorkcellId, t -> t));
            }
            for (MtAssembleGroupControl l : list) {
                MtAssembleGroupControlVO2 vo2 = new MtAssembleGroupControlVO2();
                vo2.setAssembleGroupControlId(l.getAssembleGroupControlId());
                vo2.setAssembleControlId(l.getAssembleControlId());
                vo2.setAssembleGroupId(l.getAssembleGroupId());
                vo2.setAssembleGroupDescription(null == groupMap.get(l.getAssembleGroupId()) ? null
                                : groupMap.get(l.getAssembleGroupId()).getDescription());
                vo2.setAssembleGroupCode(null == groupMap.get(l.getAssembleGroupId()) ? null
                                : groupMap.get(l.getAssembleGroupId()).getAssembleGroupCode());
                vo2.setWorkcellId(l.getWorkcellId());
                vo2.setWorkcellName(null == workcellMap.get(l.getWorkcellId()) ? null
                                : workcellMap.get(l.getWorkcellId()).getWorkcellName());
                vo2.setWorkcellCode(null == workcellMap.get(l.getWorkcellId()) ? null
                                : workcellMap.get(l.getWorkcellId()).getWorkcellCode());
                vo2.setWorkcellDescription(null == workcellMap.get(l.getWorkcellId()) ? null
                                : workcellMap.get(l.getWorkcellId()).getDescription());
                vo2.setEnableFlag(l.getEnableFlag());
                result.add(vo2);
            }
            return result;
        }
        return null;
    }

}
