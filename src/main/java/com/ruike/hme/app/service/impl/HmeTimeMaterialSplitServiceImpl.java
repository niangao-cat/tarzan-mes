package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO;
import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO2;
import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO3;
import com.ruike.hme.app.service.HmeTimeMaterialSplitService;
import com.ruike.hme.domain.repository.HmeTimeMaterialSplitRepository;
import com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO;
import com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO2;
import com.ruike.hme.infra.mapper.HmeMaterialTransferMapper;
import com.ruike.wms.domain.repository.WmsMaterialLotSplitRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * 时效物料分装 应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-09-12 11:17:08
 */
@Service
public class HmeTimeMaterialSplitServiceImpl implements HmeTimeMaterialSplitService {

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeTimeMaterialSplitRepository hmeTimeMaterialSplitRepository;
    @Autowired
    private WmsMaterialLotSplitRepository wmsMaterialLotSplitRepository;
    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private HmeMaterialTransferMapper hmeMaterialTransferMapper;

    @Override
    public HmeTimeMaterialSplitVO scanBarcode(Long tenantId, String materialLotCode) {
        //校验条码是否存在于物料批表中
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(materialLotCode);
            setEnableFlag("Y");
        }});
        if (mtMaterialLot == null) {
            throw new MtException("WMS_MTLOT_SPLIT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MTLOT_SPLIT_0001", "WMS", materialLotCode));
        }
        //2020-11-27 add by chaonan.hu for yiwei.zhou 增加盘点停用标识的校验
        if (YES.equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("WMS_COST_CENTER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0034", "WMS", materialLotCode));
        }
        //20210329 add by sanfeng.zhang for zhenyong.ban 校验用户是否有对该条码的操作权限
        // 当前用户
        Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
        // 查询条码所在仓库
        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
        MtModLocator warehouse = mtModLocatorRepository.selectByPrimaryKey(mtModLocator.getParentLocatorId());
        if (warehouse == null) {
            throw new MtException("HME_SPLIT_RECORD_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0019", "HME", mtModLocator.getLocatorCode()));
        }
        List<String> privilegeIdList = hmeMaterialTransferMapper.queryDocPrivilegeByWarehouse(tenantId, userId, warehouse.getLocatorId());
        if (CollectionUtils.isEmpty(privilegeIdList)) {
            throw new MtException("WMS_COST_CENTER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0067", "WMS", warehouse.getLocatorCode()));
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
        if (CollectionUtils.isEmpty(mtExtendAttrVOS) || StringUtils.isEmpty(mtExtendAttrVOS.get(0).getAttrValue())) {
            throw new MtException("WMS_MTLOT_SPLIT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MTLOT_SPLIT_0004", "WMS"));
        }
        String status = mtExtendAttrVOS.get(0).getAttrValue();
        List<LovValueDTO> splitStatusList = lovAdapter.queryLovValue("WMS.SPLIT_MTLOT_STATUS", tenantId);
        for (LovValueDTO lovValueDTO : splitStatusList) {
            if (lovValueDTO.getValue().equals(status)) {
                statusFlag = true;
                break;
            }
        }
        if (!statusFlag) {
            String statusMeaning = lovAdapter.queryLovMeaning("WMS.MTLOT.STATUS", tenantId, status);
            throw new MtException("WMS_MTLOT_SPLIT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MTLOT_SPLIT_0002", "WMS", materialLotCode, statusMeaning));
        }
        //校验条码是否处于有效期内
        Date dateTimeFrom = null;
        Date dateTimeTo = null;
        String dateTimeFromStr = null;
        String dateTimeToStr = null;
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("ENABLE_DATE");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            dateTimeFrom = DateUtil.string2Date(mtExtendAttrVOS.get(0).getAttrValue(), "yyyy-MM-dd HH:mm:ss");
            dateTimeFromStr = mtExtendAttrVOS.get(0).getAttrValue();
        }
        mtMaterialLotAttrVO2.setAttrName("DEADLINE_DATE");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS)) {
            dateTimeTo = DateUtil.string2Date(mtExtendAttrVOS.get(0).getAttrValue(), "yyyy-MM-dd HH:mm:ss");
            dateTimeToStr = mtExtendAttrVOS.get(0).getAttrValue();
        }
        if(dateTimeFrom != null && dateTimeTo != null){
            Date nowDate = new Date();
            if(nowDate.compareTo(dateTimeFrom) < 0 || nowDate.compareTo(dateTimeTo) > 0){
                throw new MtException("HME_TIME_MA_SPLIT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TIME_MA_SPLIT_0001", "HME"));
            }
        }
        return hmeTimeMaterialSplitRepository.scanBarcode(tenantId, mtMaterialLot, dateTimeFromStr, dateTimeToStr);
    }

    @Override
    public HmeTimeMaterialSplitVO timeSubmit(Long tenantId, HmeTimeMaterialSplitDTO dto) {
        if (StringUtils.isEmpty(dto.getMaterialLotId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "条码Id"));
        }
        if (dto.getMinute() == null) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "时长"));
        }
        if(StringUtils.isEmpty(dto.getTimeUom())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "单位"));
        }
        return hmeTimeMaterialSplitRepository.timeSubmit(tenantId, dto);
    }

    @Override
    public HmeTimeMaterialSplitVO2 confirm(Long tenantId, HmeTimeMaterialSplitDTO2 dto) {
        if(StringUtils.isNotEmpty(dto.getTargetMaterialLotCode())){
            //校验条码是否存在于物料批表中
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(dto.getTargetMaterialLotCode());
            }});
            if(mtMaterialLot != null){
                //2020-09-18 edit by chaonan.hu fou yiwei.zhou 存在校验条码是否为失效且数量为0，是校验通过，否则报错
                if (!NO.equals(mtMaterialLot.getEnableFlag()) || mtMaterialLot.getPrimaryUomQty() != 0D) {
                    throw new MtException("WMS_MTLOT_SPLIT_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_MTLOT_SPLIT_0005", "WMS", dto.getTargetMaterialLotCode()));
                }
            }
        }
        //目标条码分装数量不得大于来源条码数量
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(dto.getSourceMaterialLotId());
        if(mtMaterialLot.getPrimaryUomQty() < dto.getQty().doubleValue()){
            throw new MtException("HME_TIME_MA_SPLIT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_TIME_MA_SPLIT_0002", "HME"));
        }
        return hmeTimeMaterialSplitRepository.confirm(tenantId, dto);
    }
}
