package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeRouterLabCodeDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.app.service.MtGenTypeService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO5;
import io.tarzan.common.domain.vo.MtGenTypeVO3;
import io.tarzan.common.domain.vo.MtGenTypeVO4;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * HmeRouterLabCodeImportServiceImpl
 * 工艺实验代码导入
 * @author: chaonan.hu@hand-china.com 2021/03/31 10:32:12
 **/
@ImportService(templateCode = "HME.ROUTER_LABCODE")
public class HmeRouterLabCodeImportServiceImpl implements IBatchImportService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtGenTypeService mtGenTypeService;
    @Autowired
    private MtRouterRepository mtRouterRepository;
    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtEventRepository mtEventRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeRouterLabCodeDTO> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeRouterLabCodeDTO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeRouterLabCodeDTO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                importVOList.add(importVO);
            }
            //数据重复性校验
            dataCheck(tenantId, importVOList);
            //业务逻辑
            importData(tenantId, importVOList);
        }
        return true;
    }

    /**
     * 数据重复性校验
     *
     * @param tenantId 租户ID
     * @param importVoList 导入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/31 11:21:27
     * @return java.lang.String
     */
    private void dataCheck(Long tenantId, List<HmeRouterLabCodeDTO> importVoList){
        //校验导入表格中是否存在重复性数据
        Map<String, List<HmeRouterLabCodeDTO>> map = importVoList.stream().collect(Collectors.groupingBy(t -> {
            return t.getSiteCode() + "," + t.getRouterName() + "," + t.getRevision() + "," + t.getRouterType() + "," + t.getStepName();
        }));
        for (Map.Entry<String, List<HmeRouterLabCodeDTO>> entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                throw new MtException("MT_GENERAL_0006", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "MT_GENERAL_0006", "GENERAL"));
            }
        }
    }

    /**
     * 导入具体业务逻辑
     *
     * @param tenantId 租户ID
     * @param importVoList 导入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/31 11:28:21
     * @return void
     */
    private void importData(Long tenantId, List<HmeRouterLabCodeDTO> importVoList){
        MtGenTypeVO3 mtGenTypeVO3 = new MtGenTypeVO3();
        // 事件ID
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("ROUTER_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        //查询类型维护中ROUTER_TYPE的所有值
        mtGenTypeVO3.setTypeGroup("ROUTER_TYPE");
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(0);
        pageRequest.setSize(9999);
        Page<MtGenTypeVO4> mtGenTypeVO4Page = mtGenTypeService.listGenTypeForUi(tenantId, mtGenTypeVO3, pageRequest);
        for (HmeRouterLabCodeDTO importVO:importVoList) {
            String routerType = null;
            if(CollectionUtils.isNotEmpty(mtGenTypeVO4Page.getContent())){
                //在类型维护中找到对应的routerType
                List<MtGenTypeVO4> mtGenTypeVO4List = mtGenTypeVO4Page.getContent().stream().filter(item -> importVO.getRouterType().equals(item.getDescription())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(mtGenTypeVO4List)){
                    routerType = mtGenTypeVO4List.get(0).getTypeCode();
                    //根据工艺路线编码+版本+工艺类型查询工艺
                    MtRouter mtRouterQuery = new MtRouter();
                    mtRouterQuery.setTenantId(tenantId);
                    mtRouterQuery.setRouterName(importVO.getRouterName());
                    mtRouterQuery.setRevision(importVO.getRevision());
                    mtRouterQuery.setRouterType(routerType);
                    MtRouter mtRouter = mtRouterRepository.selectOne(mtRouterQuery);
                    if(Objects.nonNull(mtRouter)){
                        //根据工艺+步骤识别码查询工艺步骤
                        MtRouterStep mtRouterStep = mtRouterStepRepository.selectOne(new MtRouterStep() {{
                            setTenantId(tenantId);
                            setRouterId(mtRouter.getRouterId());
                            setStepName(importVO.getStepName());
                        }});
                        if(Objects.nonNull(mtRouterStep)){
                            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
                            //新增或更新扩展值LAB_CODE、REMARK
                            MtExtendVO5 labCodeAttr = new MtExtendVO5();
                            labCodeAttr.setAttrName("LAB_CODE");
                            labCodeAttr.setAttrValue(importVO.getLabCode());
                            mtExtendVO5List.add(labCodeAttr);
                            if(StringUtils.isNotBlank(importVO.getRemark())){
                                MtExtendVO5 remarkAttr = new MtExtendVO5();
                                remarkAttr.setAttrName("REMARK");
                                remarkAttr.setAttrValue(importVO.getRemark());
                                mtExtendVO5List.add(remarkAttr);
                            }
                            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_router_step_attr", mtRouterStep.getRouterStepId(),
                                    eventId, mtExtendVO5List);
                        }
                    }
                }
            }
        }
    }
}
