package tarzan.actual.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.actual.api.dto.MtEoStepWipDTO;
import tarzan.actual.api.dto.MtEoStepWipDTO2;
import tarzan.actual.app.service.MtEoStepWipService;
import tarzan.actual.infra.mapper.MtEoStepWipMapper;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.repository.MtRouterRepository;

/**
 * 执行作业在制品应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:30
 */
@Service
public class MtEoStepWipServiceImpl implements MtEoStepWipService {

    @Autowired
    private MtEoStepWipMapper mtEoStepWipMapper;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Override
    public Page<MtEoStepWipDTO2> eoStepWipReportForUi(Long tenantId, MtEoStepWipDTO dto, PageRequest pageRequest) {

        // 查询逻辑
        Page<MtEoStepWipDTO2> page =
                PageHelper.doPage(pageRequest, () -> mtEoStepWipMapper.eoStepWipReportForUi(tenantId, dto));

        List<String> routerIds = page.stream().map(MtEoStepWipDTO2::getRouterId).collect(Collectors.toList());

        Map<String, String> routerMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(routerIds)) {
            List<MtRouter> mtRouters = mtRouterRepository.routerBatchGet(tenantId, routerIds);
            if (CollectionUtils.isNotEmpty(mtRouters)) {
                routerMap = mtRouters.stream()
                        .collect(Collectors.toMap(MtRouter::getRouterId, MtRouter::getRouterName));
            }
        }

        Map<String, String> finalRouterMap = routerMap;
        page.forEach(t -> t.setRouterName(finalRouterMap.get(t.getRouterId())));
        return page;
    }
}
