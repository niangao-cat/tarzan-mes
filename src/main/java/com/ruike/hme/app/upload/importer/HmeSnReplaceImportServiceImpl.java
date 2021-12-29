package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeSnReplaceDTO;
import com.ruike.hme.domain.repository.HmeSnReplaceRepository;
import com.ruike.hme.domain.vo.HmeSnReplaceVO;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.vo.MtEoVO;
import tarzan.order.domain.vo.MtEoVO38;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SN替换导入
 *
 * @author chaonan.hu@hand-china.com 2020-11-4 14:43:23
 */
@ImportService(templateCode = "HME.SN_REPLACE")
public class HmeSnReplaceImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeSnReplaceRepository hmeSnReplaceRepository;


    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        // 获取当前用户默认工厂
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeSnReplaceDTO> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeSnReplaceDTO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeSnReplaceDTO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // 去掉收尾空格
                importVO.setOldMaterialLotCode(importVO.getOldMaterialLotCode().trim());
                importVO.setNewMaterialLotCode(importVO.getNewMaterialLotCode().trim());
                importVOList.add(importVO);
            }
            //数据校验
            dataCheck(tenantId, importVOList);
            //封装更新数据
            HmeSnReplaceVO hmeSnReplaceVO = new HmeSnReplaceVO();
            List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
            List<MtEoVO> eoMessageList = new ArrayList<>();
            for (HmeSnReplaceDTO dto:importVOList) {
                MtMaterialLot oldMtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                    setTenantId(tenantId);
                    setSiteId(siteId);
                    setMaterialLotCode(dto.getOldMaterialLotCode());
                }});
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(oldMtMaterialLot.getMaterialLotId());
                mtMaterialLotVO20.setMaterialLotCode(dto.getNewMaterialLotCode());
                mtMaterialLotVO20.setIdentification(dto.getNewMaterialLotCode());
                mtMaterialLotVO20List.add(mtMaterialLotVO20);
                if(StringUtils.isNotEmpty(oldMtMaterialLot.getEoId())){
                    MtEoVO mtEoVO = new MtEoVO();
                    mtEoVO.setEoId(oldMtMaterialLot.getEoId());
                    mtEoVO.setIdentification(dto.getNewMaterialLotCode());
                    eoMessageList.add(mtEoVO);
                }
                hmeSnReplaceVO.setMtMaterialLotVO20List(mtMaterialLotVO20List);
                hmeSnReplaceVO.setEoMessageList(eoMessageList);
            }
            //数据更新
            hmeSnReplaceRepository.updateSnReplaceData(tenantId, hmeSnReplaceVO);
        }
        return true;
    }

    /**
     * 数据校验
     *
     * @param tenantId
     * @param importVoList
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/4 14:46:23
     */
    private void dataCheck(Long tenantId, List<HmeSnReplaceDTO> importVoList) {
        List<String> oldMaterialLotCodeList = importVoList.stream().map(HmeSnReplaceDTO::getOldMaterialLotCode).distinct().collect(Collectors.toList());
        List<String> newMaterialLotCodeList = importVoList.stream().map(HmeSnReplaceDTO::getNewMaterialLotCode).distinct().collect(Collectors.toList());
        if(oldMaterialLotCodeList.size() != importVoList.size()){
            throw new MtException("HME_SN_REPLACE_0004", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_SN_REPLACE_0004", "HME", "原"));
        }
        if(newMaterialLotCodeList.size() != importVoList.size()){
            throw new MtException("HME_SN_REPLACE_0004", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_SN_REPLACE_0004", "HME", "新"));
        }
    }
}
