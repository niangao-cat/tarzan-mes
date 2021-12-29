package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeVisualInspectionDTO;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO2;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO3;
import com.ruike.hme.app.service.HmeVisualInspectionService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeVisualInspectionRepository;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.hme.infra.mapper.HmeVisualInspectionMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 目检完工应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-01-20 14:50:23
 */
@Service
@Slf4j
public class HmeVisualInspectionServiceImpl implements HmeVisualInspectionService {

    @Autowired
    private HmeVisualInspectionRepository hmeVisualInspectionRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeVisualInspectionMapper hmeVisualInspectionMapper;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Override
    public List<HmeVisualInspectionVO> materialLotQuery(Long tenantId, HmeVisualInspectionDTO dto, String jobType) {
        if(StringUtils.isBlank(dto.getWorkcellId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getOperationId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工艺"));
        }
        return hmeVisualInspectionRepository.materialLotQuery(tenantId, dto, jobType);
    }

    @Override
    public List<HmeVisualInspectionVO> materialLotQuery2(Long tenantId, HmeVisualInspectionDTO dto, String jobType) {
        if(StringUtils.isBlank(dto.getWorkcellId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getOperationId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工艺"));
        }
        return hmeVisualInspectionRepository.materialLotQuery2(tenantId, dto, jobType);
    }

    @Override
    public HmeVisualInspectionDTO2 scanMaterialLot(Long tenantId, HmeVisualInspectionDTO2 dto) {
        long startDate = System.currentTimeMillis();
        if(StringUtils.isBlank(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点"));
        }
        if(StringUtils.isBlank(dto.getWorkcellId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getOperationId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工艺"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "物料批"));
        }
        if(StringUtils.isBlank(dto.getWkcShiftId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "班次"));
        }
        //条码校验
        long startDate2 = System.currentTimeMillis();
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        if(Objects.isNull(mtMaterialLot)){
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", dto.getMaterialLotCode()));
        }
        if(!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())){
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0015", "HME", dto.getMaterialLotCode()));
        }
        if (HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag()) || HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        long endDate2 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 条码校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate2 - startDate2));
        //在制品标识校验
        String mfFlagAttr = hmeVisualInspectionMapper.getMaterialLotAttrValueByAttrName(tenantId, mtMaterialLot.getMaterialLotId(), "MF_FLAG");
        if(StringUtils.isBlank(mfFlagAttr) || !HmeConstants.ConstantValue.YES.equals(mfFlagAttr)){
            throw new MtException("HME_NC_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0017", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        long endDate3 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 在制品标识校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate3 - endDate2));
        //是否存在未出站数据
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnMapper.queryEoJobSnByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
        if(CollectionUtils.isNotEmpty(hmeEoJobSnList)){
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeEoJobSnList.get(0).getWorkcellId());
            String jobTypeMeaning = lovAdapter.queryLovMeaning("HME.JOB_TYPE", tenantId, hmeEoJobSnList.get(0).getJobType());
            throw new MtException("HME_CHIP_TRANSFER_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_017", "HME", mtModWorkcell.getWorkcellCode(), jobTypeMeaning));
        }
        long endDate4 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 是否存在未出站数据校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate4 - endDate3));
        //是否存在不良芯片 2021-05-12 edit by choanan.hu for zhenyong.ban 去掉此校验
//        Long ncCount = hmeVisualInspectionMapper.ncCountByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
//        if(ncCount > 0){
//            throw new MtException("HME_NC_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_NC_0019", "HME", mtMaterialLot.getMaterialLotCode()));
//        }
//        long endDate5 = System.currentTimeMillis();
//        log.info("<====目检完工 条码{}进站 是否存在不良芯片校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate5 - endDate4));
//        //是否存在cos_num不为1的数据 2021-05-19 edit by choanan.hu for zhenyong.ban 去掉此校验
//        Long count = hmeVisualInspectionMapper.countByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
//        if(count > 0){
//            throw new MtException("HME_NC_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_NC_0020", "HME", mtMaterialLot.getMaterialLotCode()));
//        }
        //条码物料与工单物料是否一致的校验
        String workOrderId = hmeVisualInspectionMapper.getMaterialLotAttrValueByAttrName(tenantId, mtMaterialLot.getMaterialLotId(), "WORK_ORDER_ID");
        if(StringUtils.isBlank(workOrderId)){
            throw new MtException("HME_COS_BARCODE_RETEST_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_002", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
        if(Objects.isNull(mtWorkOrder) || !mtMaterialLot.getMaterialId().equals(mtWorkOrder.getMaterialId())){
            throw new MtException("HME_COS_BARCODE_RETEST_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_002", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        long endDate6 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 条码物料与工单物料是否一致校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate6 - endDate4));
        //2021-02-23 14:46 add by chaonan.hu for zhenyong.ban 增加位置未录入热沉编号,不允许目检完工的校验
        List<HmeMaterialLotLoad> hmeMaterialLotLoads = hmeVisualInspectionMapper.noHotSinkDataQuery(tenantId, mtMaterialLot.getMaterialLotId());
        if(CollectionUtils.isNotEmpty(hmeMaterialLotLoads)){
            List<String> positionList = new ArrayList<>();
            for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoads) {
                positionList.add(((char) (64 + hmeMaterialLotLoad.getLoadRow())) + String.valueOf(hmeMaterialLotLoad.getLoadColumn()));
            }
            String position = StringUtils.join(positionList, "-");
            throw new MtException("HME_COS_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_050", "HME", position));
        }
        long endDate7 = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 位置未录入热沉编号校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate7 - endDate6));
        //2021-02-23 14:46 add by chaonan.hu for zhenyong.ban 增加位置未进行性能测试,不允许目检完工的校验
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeVisualInspectionMapper.noFunctionTestDataQuery(tenantId, mtMaterialLot.getMaterialLotId());
        if(CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)){
            List<String> positionList = new ArrayList<>();
            for (HmeMaterialLotLoad hmeMaterialLotLoad : hmeMaterialLotLoadList) {
                positionList.add(((char) (64 + hmeMaterialLotLoad.getLoadRow())) + String.valueOf(hmeMaterialLotLoad.getLoadColumn()));
            }
            String position = StringUtils.join(positionList, "-");
            throw new MtException("HME_COS_051", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_051", "HME", position));
        }
        long endDate = System.currentTimeMillis();
        log.info("<====目检完工 条码{}进站 位置未进行性能测试校验耗时：{}毫秒", dto.getMaterialLotCode(), (endDate - endDate7));
        log.info("<====目检完工 条码{}进站 校验总耗时：{}毫秒", dto.getMaterialLotCode(), (endDate - startDate));
        return hmeVisualInspectionRepository.scanMaterialLot(tenantId, dto);
    }

    @Override
    public HmeVisualInspectionVO2 scanContainer(Long tenantId, String containerCode) {
        if(StringUtils.isBlank(containerCode)){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "容器"));
        }
        //容器校验
        MtContainer mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
            setTenantId(tenantId);
            setContainerCode(containerCode);
        }});
        if(Objects.isNull(mtContainer)){
            throw new MtException("HME_LOAD_CONTAINER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_001", "HME"));
        }
        return hmeVisualInspectionRepository.scanContainer(tenantId, mtContainer.getContainerId());
    }

    @Override
    public HmeVisualInspectionDTO3 materialLotComplete(Long tenantId, HmeVisualInspectionDTO3 dto) {
        if(StringUtils.isBlank(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点"));
        }
        if(StringUtils.isBlank(dto.getWorkcellId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getOperationId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工艺"));
        }
        if(CollectionUtils.isEmpty(dto.getMaterialLotList())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "勾选条码信息"));
        }
        //2021-04-27 15::26 add by chaonan.hu for kang.wang 批量完工时不允许有条码存在不良芯片
        List<String> materialLotIdList = dto.getMaterialLotList().stream().map(HmeVisualInspectionVO::getMaterialLotId).collect(Collectors.toList());
        List<String> ncMaterialLotCodeList = hmeVisualInspectionMapper.ncCountByMaterialLotIdList(tenantId, materialLotIdList);
        if(CollectionUtils.isNotEmpty(ncMaterialLotCodeList)){
            throw new MtException("HME_COS_056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_056", "HME", StringUtils.join(ncMaterialLotCodeList, ',')));
        }
        if(StringUtils.isNotBlank(dto.getContainerCode())){
            MtContainer mtContainer = mtContainerRepository.selectOne(new MtContainer() {{
                setTenantId(tenantId);
                setContainerCode(dto.getContainerCode());
            }});
            if(Objects.isNull(mtContainer)){
                throw new MtException("HME_LOAD_CONTAINER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_001", "HME"));
            }
        }
        //2021-05-31 09:56:06 add by chaonan.hu for zhenyong.ban 增加对条码在制品标识的校验
        String mfIsNMaterialLot = hmeVisualInspectionMapper.getMfIsNMaterialLot(tenantId, materialLotIdList);
        if(StringUtils.isNotBlank(mfIsNMaterialLot)){
            throw new MtException("HME_NC_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0017", "HME", mfIsNMaterialLot));
        }
        //2021-07-05 16:56:12 add by chaonan.hu for peng.zhao  增加对条码的冻结标识、盘点标识的校验
        List<MtMaterialLot> mtMaterialLotList = hmeVisualInspectionMapper.materialLotFreezeStocktakeFlagQuery(tenantId, materialLotIdList);
        mtMaterialLotList = mtMaterialLotList.stream().filter(item -> "Y".equals(item.getStocktakeFlag()) || "Y".equals(item.getFreezeFlag())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(mtMaterialLotList)){
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", mtMaterialLotList.get(0).getMaterialLotCode()));
        }
        return hmeVisualInspectionRepository.materialLotComplete(tenantId, dto);
    }

    @Override
    public HmeVisualInspectionDTO3 siteInCancel(Long tenantId, HmeVisualInspectionDTO3 dto) {
        if(StringUtils.isBlank(dto.getSiteId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "站点"));
        }
        if(StringUtils.isBlank(dto.getWorkcellId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工位"));
        }
        if(StringUtils.isBlank(dto.getOperationId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "工艺"));
        }
        if(CollectionUtils.isEmpty(dto.getMaterialLotList())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "勾选条码信息"));
        }
        for (HmeVisualInspectionVO hmeVisualInspectionVO: dto.getMaterialLotList()) {
            HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(hmeVisualInspectionVO.getJobId());
            if(Objects.nonNull(hmeEoJobSn.getSiteOutDate())){
                throw new MtException("HME_COS_041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_041", "HME", hmeVisualInspectionVO.getMaterialLotCode()));
            }
        }
        return hmeVisualInspectionRepository.siteInCancel(tenantId, dto);
    }
}
