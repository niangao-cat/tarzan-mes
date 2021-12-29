package tarzan.method.app.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.api.dto.MtOperationSubstepDTO;
import tarzan.method.app.service.MtOperationSubstepService;
import tarzan.method.domain.entity.MtOperationSubstep;
import tarzan.method.domain.repository.MtOperationSubstepRepository;
import tarzan.method.infra.mapper.MtOperationSubstepMapper;

/**
 * 工艺子步骤应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:19:27
 */
@Service
public class MtOperationSubstepServiceImpl implements MtOperationSubstepService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtOperationSubstepRepository mtOperationSubstepRepository;

    @Autowired
    private MtOperationSubstepMapper mtOperationSubstepMapper;

    @Override
    public Page<MtOperationSubstepDTO> queryOperationSubstepListForUi(Long tenantId, String operationId,
                                                                      PageRequest pageRequest) {
        if (StringUtils.isEmpty(operationId)) {
            return new Page<MtOperationSubstepDTO>();
        }

        return PageHelper.doPage(pageRequest,
                () -> mtOperationSubstepMapper.queryOperationSubstepForUi(tenantId, operationId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeOperationSubstepForUi(Long tenantId, String operationSubstepId) {
        if (StringUtils.isEmpty(operationSubstepId)) {
            return;
        }

        MtOperationSubstep mtOperationSubstep = new MtOperationSubstep();
        mtOperationSubstep.setTenantId(tenantId);
        mtOperationSubstep.setOperationSubstepId(operationSubstepId);
        if (mtOperationSubstepMapper.delete(mtOperationSubstep) == 0) {
            throw new MtException("数据删除失败.");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOperationSubstepForUi(Long tenantId, String operationId, List<MtOperationSubstepDTO> dtoList,
                                          Boolean operationNewFlag) {
        Map<String, Long> substepMap = dtoList.stream()
                .collect(Collectors.groupingBy(MtOperationSubstepDTO::getSubstepId, Collectors.counting()));
        if (substepMap.entrySet().stream().anyMatch(s -> s.getValue() > 1)) {
            throw new MtException("MT_ROUTER_0046",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0046", "ROUTER"));
        }

        Map<Long, Long> sequenceMap = dtoList.stream()
                .collect(Collectors.groupingBy(MtOperationSubstepDTO::getSequence, Collectors.counting()));
        if (sequenceMap.entrySet().stream().anyMatch(s -> s.getValue() > 1)) {
            throw new MtException("MT_ROUTER_0047",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0047", "ROUTER"));
        }

        if (operationNewFlag) {
            MtOperationSubstep mtOperationSubstep;
            for (MtOperationSubstepDTO dto : dtoList) {
                mtOperationSubstep = new MtOperationSubstep();
                BeanUtils.copyProperties(dto, mtOperationSubstep);
                mtOperationSubstep.setTenantId(tenantId);
                mtOperationSubstepRepository.insertSelective(mtOperationSubstep);
            }
        } else {
            MtOperationSubstep queryOperationSubstep = new MtOperationSubstep();
            queryOperationSubstep.setOperationId(operationId);
            queryOperationSubstep.setTenantId(tenantId);

            List<MtOperationSubstep> operationSubstepList = mtOperationSubstepRepository.select(queryOperationSubstep);
            if (CollectionUtils.isEmpty(operationSubstepList)) {
                MtOperationSubstep mtOperationSubstep;
                for (MtOperationSubstepDTO dto : dtoList) {
                    mtOperationSubstep = new MtOperationSubstep();
                    BeanUtils.copyProperties(dto, mtOperationSubstep);
                    mtOperationSubstep.setTenantId(tenantId);
                    mtOperationSubstepRepository.insertSelective(mtOperationSubstep);
                }
            } else {
                MtOperationSubstep mtOperationSubstep;
                for (MtOperationSubstepDTO dto : dtoList) {
                    if (operationSubstepList.stream().anyMatch(s -> s.getSequence().equals(dto.getSequence())
                            && !s.getOperationSubstepId().equals(dto.getOperationSubstepId()))) {
                        throw new MtException("MT_ROUTER_0047", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0047", "ROUTER"));
                    }
                    if (operationSubstepList.stream().anyMatch(s -> s.getSubstepId().equals(dto.getSubstepId())
                            && !s.getOperationSubstepId().equals(dto.getOperationSubstepId()))) {
                        throw new MtException("MT_ROUTER_0046", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0046", "ROUTER"));
                    }
                    mtOperationSubstep = new MtOperationSubstep();
                    BeanUtils.copyProperties(dto, mtOperationSubstep);
                    mtOperationSubstep.setTenantId(tenantId);
                    if (StringUtils.isEmpty(dto.getOperationSubstepId())) {
                        mtOperationSubstepRepository.insertSelective(mtOperationSubstep);
                    } else {
                        mtOperationSubstepRepository.updateByPrimaryKeySelective(mtOperationSubstep);
                    }
                }
            }
        }
    }
}
