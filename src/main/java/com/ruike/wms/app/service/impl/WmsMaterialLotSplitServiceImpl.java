package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsMaterialLotSplitDTO;
import com.ruike.wms.app.service.WmsMaterialLotSplitService;
import com.ruike.wms.domain.repository.WmsMaterialLotSplitRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO2;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO3;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;

import java.util.List;

/**
 * @description: 条码拆分应用服务实现
 *
 * @author: chaonan.hu@hand-china.com 2020-09-07 15:23:27
 **/
@Service
public class WmsMaterialLotSplitServiceImpl implements WmsMaterialLotSplitService {

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private WmsMaterialLotSplitRepository wmsMaterialLotSplitRepository;

    @Override
    public WmsMaterialLotSplitVO3 scanSourceBarcode(Long tenantId, String materialLotCode) {
        //校验条码是否存在于物料批表中
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(materialLotCode);
            setEnableFlag("Y");
        }});
        if(mtMaterialLot == null){
            throw new MtException("WMS_MTLOT_SPLIT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MTLOT_SPLIT_0001", "WMS", materialLotCode));
        }
        //2020-11-27 add by chaonan.hu for yiwei.zhou 增加盘点停用标识的校验
        if("Y".equals(mtMaterialLot.getStocktakeFlag())){
            throw new MtException("WMS_COST_CENTER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0034", "WMS", materialLotCode));
        }
        //在制品验证
        wmsMaterialLotSplitRepository.materialLotMfFlagVerify(tenantId, mtMaterialLot);

        //2020-12-01 add by chaonan.hu for zhenyong.ban 增加对原始条码预装标识的校验
        wmsMaterialLotSplitRepository.materialLotVfVerify(tenantId, mtMaterialLot);

        //校验条码状态是否属于值集“WMS.SPLIT_MTLOT_STATUS”
        boolean statusFlag = false;
        //调用API{materialLotLimitAttrQuery} 获取条码状态
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
            setAttrName("STATUS");
        }});
        if(CollectionUtils.isEmpty(mtExtendAttrVOS) || StringUtils.isEmpty(mtExtendAttrVOS.get(0).getAttrValue())){
            throw new MtException("WMS_MTLOT_SPLIT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MTLOT_SPLIT_0004", "WMS"));
        }
        String status = mtExtendAttrVOS.get(0).getAttrValue();
        List<LovValueDTO> splitStatusList = lovAdapter.queryLovValue("WMS.SPLIT_MTLOT_STATUS", tenantId);
        for (LovValueDTO lovValueDTO:splitStatusList) {
            if(lovValueDTO.getValue().equals(status)){
                statusFlag = true;
                break;
            }
        }
        if(!statusFlag){
            String statusMeaning = lovAdapter.queryLovMeaning("WMS.MTLOT.STATUS", tenantId, status);
            throw new MtException("WMS_MTLOT_SPLIT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MTLOT_SPLIT_0002", "WMS", materialLotCode, statusMeaning));
        }
        return wmsMaterialLotSplitRepository.scanSourceBarcode(tenantId, mtMaterialLot);
    }

    @Override
    public WmsMaterialLotSplitVO2 scanBarcode(Long tenantId, String materialLotCode) {
        //校验条码是否存在于物料批表中
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(materialLotCode);
        }});
        if(mtMaterialLot != null){
            throw new MtException("WMS_MTLOT_SPLIT_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MTLOT_SPLIT_0003", "WMS", materialLotCode));
        }
        WmsMaterialLotSplitVO2 result = new WmsMaterialLotSplitVO2();
        result.setMaterialLotCode(materialLotCode);
        return result;
    }

    @Override
    public List<WmsMaterialLotSplitVO3> splitBarcode(Long tenantId, WmsMaterialLotSplitDTO dto) {
        return wmsMaterialLotSplitRepository.splitBarcode(tenantId, dto);
    }
}