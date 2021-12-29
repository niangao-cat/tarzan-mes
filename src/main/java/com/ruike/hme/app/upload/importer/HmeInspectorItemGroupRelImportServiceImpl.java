package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeInspectorItemGroupRelDTO2;
import com.ruike.hme.domain.entity.HmeInspectorItemGroupRel;
import com.ruike.hme.domain.repository.HmeInspectorItemGroupRelRepository;
import com.ruike.hme.infra.mapper.HmeInspectorItemGroupRelMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HmeInspectorItemGroupRelImportServiceImpl
 * 检验员与物料组关系导入
 * @author: chaonan.hu@hand-china.com 2021-03-30 15:50:23
 **/
@ImportService(templateCode = "HME.INSPECTOR_ITEM_GROUP_REL")
public class HmeInspectorItemGroupRelImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeInspectorItemGroupRelMapper hmeInspectorItemGroupRelMapper;
    @Autowired
    private HmeInspectorItemGroupRelRepository hmeInspectorItemGroupRelRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeInspectorItemGroupRelDTO2> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeInspectorItemGroupRelDTO2 importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeInspectorItemGroupRelDTO2.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                importVOList.add(importVO);
            }
            //数据校验
            dataCheck(tenantId, importVOList);
            //业务逻辑
            importData(tenantId, importVOList);
        }
        return true;
    }

    /**
     * 数据校验
     *
     * @param tenantId
     * @param importVoList
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/30 04:27:57
     * @return void
     */
    private void dataCheck(Long tenantId, List<HmeInspectorItemGroupRelDTO2> importVoList) {
        //导入表格数据中不能有重复数据
        Map<String, List<HmeInspectorItemGroupRelDTO2>> map = importVoList.stream().collect(Collectors.groupingBy(t -> {
            return t.getLoginName() + "," + t.getInspectPowerType() + "," + t.getItemGroupCode();
        }));
        for (Map.Entry<String, List<HmeInspectorItemGroupRelDTO2>> entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                String inspectPowerTypeMeaning = lovAdapter.queryLovMeaning("QMS.INSPECT_POWER_TYPE", tenantId, entry.getValue().get(0).getInspectPowerType());
                throw new MtException("HME_INSPECTOR_ITEM_GROUP_REL_0001", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_INSPECTOR_ITEM_GROUP_REL_0001", "HME",
                                entry.getValue().get(0).getLoginName(), inspectPowerTypeMeaning, entry.getValue().get(0).getItemGroupCode()));
            }
        }
    }

    /**
     * 导入数据具体业务逻辑
     *
     * @param tenantId
     * @param importVoList
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/30 04:33:49
     * @return void
     */
    void importData(Long tenantId, List<HmeInspectorItemGroupRelDTO2> importVoList){
        for (HmeInspectorItemGroupRelDTO2 importVO:importVoList) {
            Long userId = hmeInspectorItemGroupRelMapper.getUserIdByLoginName(importVO.getLoginName());
            HmeInspectorItemGroupRel hmeInspectorItemGroupRel = new HmeInspectorItemGroupRel();
            hmeInspectorItemGroupRel.setTenantId(tenantId);
            hmeInspectorItemGroupRel.setUserId(userId);
            hmeInspectorItemGroupRel.setPrivilegeType(importVO.getInspectPowerType());
            hmeInspectorItemGroupRel.setItemGroupId(importVO.getItemGroupCode());
            hmeInspectorItemGroupRel.setEnableFlag(importVO.getEnableFlag());
            hmeInspectorItemGroupRelRepository.insertSelective(hmeInspectorItemGroupRel);
        }
    }
}
