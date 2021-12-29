package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeSnLabCodeDTO;
import com.ruike.hme.domain.entity.HmeSnLabCode;
import com.ruike.hme.domain.repository.HmeSnLabCodeRepository;
import com.ruike.hme.infra.mapper.HmeSnLabCodeMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * HmeSnLabCodeImportServiceImpl
 * SN实验代码导入
 * @author: chaonan.hu@hand-china.com 2021/04/01 09:56:15
 **/
@ImportService(templateCode = "HME.SN_LABCODE")
public class HmeSnLabCodeImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private HmeSnLabCodeRepository hmeSnLabCodeRepository;
    @Autowired
    private HmeSnLabCodeMapper hmeSnLabCodeMapper;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeSnLabCodeDTO> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeSnLabCodeDTO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeSnLabCodeDTO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                importVOList.add(importVO);
            }
            //导入数据重复性校验
            dataCheck(tenantId, importVOList);
            //业务逻辑
            importData(tenantId, importVOList);
        }
        return true;
    }

    /**
     * 表格导入数据重复性校验
     *
     * @param tenantId 租户ID
     * @param importVoList 导入整体数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/1 10:00:07
     * @return void
     */
    private void dataCheck(Long tenantId, List<HmeSnLabCodeDTO> importVoList){
        Map<String, List<HmeSnLabCodeDTO>> map = importVoList.stream().collect(Collectors.groupingBy(t -> {
            return t.getSiteCode() + "," + t.getMaterialLotCode() + "," + t.getOperationName() + "," + t.getRevision();
        }));
        for (Map.Entry<String, List<HmeSnLabCodeDTO>> entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                throw new MtException("MT_GENERAL_0006", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "MT_GENERAL_0006", "GENERAL"));
            }
        }
    }

    /**
     * 导入数据具体业务逻辑
     *
     * @param tenantId 租户ID
     * @param importVoList 导入整体数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/1 10:03:53
     * @return void
     */
    private void importData(Long tenantId, List<HmeSnLabCodeDTO> importVoList){
        for (HmeSnLabCodeDTO importVO:importVoList) {
            //查询站点
            MtModSite mtModSite = mtModSiteRepository.selectOne(new MtModSite() {{
                setTenantId(tenantId);
                setSiteCode(importVO.getSiteCode());
            }});
            if(Objects.nonNull(mtModSite)){
                //查询条码
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                    setTenantId(tenantId);
                    setMaterialLotCode(importVO.getMaterialLotCode());
                }});
                if(Objects.nonNull(mtMaterialLot)){
                    //查询工艺
                    MtOperation mtOperation = mtOperationRepository.selectOne(new MtOperation() {{
                        setTenantId(tenantId);
                        setSiteId(mtModSite.getSiteId());
                        setOperationName(importVO.getOperationName());
                        setRevision(importVO.getRevision());
                    }});
                    if(Objects.nonNull(mtOperation)){
                        //根据条码ID+工艺ID在表hme_sn_lab_code中查询数据，有则更新，无则新增
                        HmeSnLabCode hmeSnLabCode = hmeSnLabCodeRepository.selectOne(new HmeSnLabCode() {{
                            setTenantId(tenantId);
                            setMaterialLotId(mtMaterialLot.getMaterialLotId());
                            setOperationId(mtOperation.getOperationId());
                        }});
                        if(Objects.nonNull(hmeSnLabCode)){
                            //更新
                            hmeSnLabCode.setLabCode(importVO.getLabCode());
                            hmeSnLabCode.setRemark(importVO.getRemark());
                            hmeSnLabCode.setEnabledFlag(importVO.getEnableFlag());
                            hmeSnLabCodeMapper.updateByPrimaryKey(hmeSnLabCode);
                        }else{
                            //新增
                            hmeSnLabCode = new HmeSnLabCode();
                            hmeSnLabCode.setTenantId(tenantId);
                            hmeSnLabCode.setOperationId(mtOperation.getOperationId());
                            hmeSnLabCode.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                            hmeSnLabCode.setLabCode(importVO.getLabCode());
                            hmeSnLabCode.setRemark(importVO.getRemark());
                            hmeSnLabCode.setEnabledFlag(importVO.getEnableFlag());
                            hmeSnLabCodeRepository.insertSelective(hmeSnLabCode);
                        }
                    }
                }
            }
        }
    }
}

