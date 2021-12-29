package tarzan.dispatch.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.actual.api.dto.MtOpWkcDispatchRelDTO;
import tarzan.actual.api.dto.MtOpWkcDispatchRelDTO1;
import tarzan.dispatch.app.service.MtOperationWkcDispatchRelService;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO5;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO8;

/**
 * 工艺和工作单元调度关系表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:55:05
 */
@Service
public class MtOperationWkcDispatchRelServiceImpl implements MtOperationWkcDispatchRelService {

    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    

    @Override
    public Page<MtOpWkcDispatchRelDTO1> queryOpWkcDispatchRelListForUi(Long tenantId, MtOpWkcDispatchRelDTO dto,
                    PageRequest pageRequest) {
        MtOpWkcDispatchRelVO5 vo5 = new MtOpWkcDispatchRelVO5();
        vo5.setOperationId(dto.getOperationId());
        vo5.setWorkcellId(dto.getWorkcellId());
        vo5.setStepName(dto.getStepName());
        Page<MtOpWkcDispatchRelVO8> page =
                        PageHelper.doPageAndSort(pageRequest, () -> mtOperationWkcDispatchRelRepository
                                        .propertyLimitOperationWkcQuery(tenantId, vo5, MtBaseConstants.YES));

        Page<MtOpWkcDispatchRelDTO1> result = new Page<>();
        result.setNumber(page.getNumber());
        result.setNumberOfElements(page.getNumberOfElements());
        result.setSize(page.getSize());
        result.setTotalElements(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());

        List<MtOpWkcDispatchRelDTO1> contents = new ArrayList<>();
        for (MtOpWkcDispatchRelVO8 vo8 : page) {
            MtOpWkcDispatchRelDTO1 dto1 = new MtOpWkcDispatchRelDTO1();
            dto1.setOperationWkcDispatchRelId(vo8.getOperationWkcDispatchRelId());
            dto1.setOperationId(vo8.getOperationId());
            dto1.setStepName(vo8.getStepName());
            dto1.setWorkcellId(vo8.getWorkcellId());
            dto1.setPriority(vo8.getPriority());
            dto1.setWorkcellCode(vo8.getWorkcellCode());
            dto1.setWorkcellName(vo8.getWorkcellName());
            dto1.setOperationName(vo8.getOperationName());
            dto1.setDescription(vo8.getDescription());
            contents.add(dto1);
        }
        result.setContent(contents);
        return result;
    }

    @Override
    public MtOpWkcDispatchRelDTO1 queryOpWkcDispatchRelDetailForUi(Long tenantId, String operationWkcDispatchRelId) {
        MtOpWkcDispatchRelVO5 vo5 = new MtOpWkcDispatchRelVO5();
        vo5.setOperationWkcDispatchRelId(operationWkcDispatchRelId);
        List<MtOpWkcDispatchRelVO8> vo8List = mtOperationWkcDispatchRelRepository
                        .propertyLimitOperationWkcQuery(tenantId, vo5, MtBaseConstants.NO);

        if (CollectionUtils.isEmpty(vo8List)) {
            return null;
        }
        MtOpWkcDispatchRelVO8 vo8 = vo8List.get(0);
        MtOpWkcDispatchRelDTO1 result = new MtOpWkcDispatchRelDTO1();
        result.setOperationWkcDispatchRelId(vo8.getOperationWkcDispatchRelId());
        result.setOperationId(vo8.getOperationId());
        result.setStepName(vo8.getStepName());
        result.setWorkcellId(vo8.getWorkcellId());
        result.setPriority(vo8.getPriority());
        result.setWorkcellCode(vo8.getWorkcellCode());
        result.setWorkcellName(vo8.getWorkcellName());
        result.setOperationName(vo8.getOperationName());
        result.setDescription(vo8.getDescription());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveOpWkcDispatchRelForUi(Long tenantId, MtOpWkcDispatchRelDTO dto) {
        // 保存逻辑
        MtOpWkcDispatchRelVO5 mtOpWkcDispatchRelVO5 = new MtOpWkcDispatchRelVO5();
        mtOpWkcDispatchRelVO5.setOperationWkcDispatchRelId(dto.getOperationWkcDispatchRelId());
        mtOpWkcDispatchRelVO5.setStepName(dto.getStepName());
        mtOpWkcDispatchRelVO5.setWorkcellId(dto.getWorkcellId());
        mtOpWkcDispatchRelVO5.setOperationId(dto.getOperationId());
        mtOpWkcDispatchRelVO5.setPriority(dto.getPriority());
        return mtOperationWkcDispatchRelRepository.operationWkcRelUpdate(tenantId, mtOpWkcDispatchRelVO5);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> deleteOpWkcDispatchRelForUi(Long tenantId, List<String> operationWkcDispatchRelIds) {
        // 删除逻辑
        List<String> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(operationWkcDispatchRelIds)) {
            for (String id : operationWkcDispatchRelIds) {
                MtOpWkcDispatchRelVO5 vo5 = new MtOpWkcDispatchRelVO5();
                vo5.setOperationWkcDispatchRelId(id);
                String s = mtOperationWkcDispatchRelRepository.operationWkcRelDelete(tenantId, vo5);
                result.add(s);
            }
        }
        return result;
    }

}
