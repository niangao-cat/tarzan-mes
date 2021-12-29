package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeInterceptInformationService;
import com.ruike.hme.app.service.HmeInterceptReleaseService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeInterceptInformation;
import com.ruike.hme.domain.entity.HmeInterceptObject;
import com.ruike.hme.domain.entity.HmeInterceptRelease;
import com.ruike.hme.domain.repository.HmeInterceptInformationRepository;
import com.ruike.hme.domain.repository.HmeInterceptObjectRepository;
import com.ruike.hme.domain.repository.HmeInterceptReleaseRepository;
import com.ruike.hme.domain.repository.HmeMaterialLotLabCodeRepository;
import com.ruike.hme.domain.vo.HmeInterceptObjectVO;
import com.ruike.hme.domain.vo.HmeInterceptReleaseVO;
import com.ruike.hme.domain.vo.HmePopupWindowVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeInterceptInformationMapper;
import com.ruike.hme.infra.mapper.HmeInterceptReleaseMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseAppService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 拦截例外放行表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:10
 */
@Service
public class HmeInterceptReleaseServiceImpl extends BaseAppService implements HmeInterceptReleaseService {

    private final HmeInterceptReleaseMapper hmeInterceptReleaseMapper;
    private final MtUserClient mtUserClient;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final HmeInterceptReleaseRepository hmeInterceptReleaseRepository;
    private final HmeInterceptInformationRepository hmeInterceptInformationRepository;
    private final HmeInterceptObjectRepository hmeInterceptObjectRepository;
    private final MtWorkOrderRepository mtWorkOrderRepository;
    private final MtEoRepository mtEoRepository;
    private final HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository;
    private final MtExtendSettingsMapper mtExtendSettingsMapper;
    private final HmeInterceptInformationMapper hmeInterceptInformationMapper;
    private final HmeInterceptInformationService hmeInterceptInformationService;


    public HmeInterceptReleaseServiceImpl(HmeInterceptReleaseMapper hmeInterceptReleaseMapper,
                                          MtUserClient mtUserClient,
                                          MtErrorMessageRepository mtErrorMessageRepository,
                                          MtMaterialLotRepository mtMaterialLotRepository,
                                          HmeInterceptReleaseRepository hmeInterceptReleaseRepository,
                                          HmeInterceptInformationRepository hmeInterceptInformationRepository,
                                          HmeInterceptObjectRepository hmeInterceptObjectRepository,
                                          MtWorkOrderRepository mtWorkOrderRepository,
                                          MtEoRepository mtEoRepository,
                                          HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository,
                                          MtExtendSettingsMapper mtExtendSettingsMapper,
                                          HmeInterceptInformationMapper hmeInterceptInformationMapper,
                                          HmeInterceptInformationService hmeInterceptInformationService) {
        this.hmeInterceptReleaseMapper = hmeInterceptReleaseMapper;
        this.mtUserClient = mtUserClient;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.hmeInterceptReleaseRepository = hmeInterceptReleaseRepository;
        this.hmeInterceptInformationRepository = hmeInterceptInformationRepository;
        this.hmeInterceptObjectRepository = hmeInterceptObjectRepository;
        this.mtWorkOrderRepository = mtWorkOrderRepository;
        this.mtEoRepository = mtEoRepository;
        this.hmeMaterialLotLabCodeRepository = hmeMaterialLotLabCodeRepository;
        this.mtExtendSettingsMapper = mtExtendSettingsMapper;
        this.hmeInterceptInformationMapper = hmeInterceptInformationMapper;
        this.hmeInterceptInformationService = hmeInterceptInformationService;
    }


    @Override
    public Page<HmeInterceptReleaseVO> queryInterceptRelease(Long tenantId, String interceptId, PageRequest pageRequest) {
        Page<HmeInterceptReleaseVO> page = PageHelper.doPage(pageRequest, () -> hmeInterceptReleaseMapper.queryInterceptObject(tenantId, interceptId));
        List<Long> userIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            page.getContent().forEach(hmeInterceptReleaseVO -> {
                userIdList.add(hmeInterceptReleaseVO.getReleaseBy());
            });
            //对userIdList去重
            List<Long> distinctUserIdList = userIdList.stream().distinct().collect(Collectors.toList());
            Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(distinctUserIdList)) {
                userInfoMap = mtUserClient.userInfoBatchGet(tenantId, distinctUserIdList);
            }
            for (HmeInterceptReleaseVO hmeInterceptReleaseVO : page.getContent()) {
                //获取放行人
                hmeInterceptReleaseVO.setReleaseByName(userInfoMap.getOrDefault(hmeInterceptReleaseVO.getReleaseBy(), new MtUserInfo()).getRealName());
            }
            //信息头表状态更新为部分拦截,sn不为空,
            HmeInterceptInformation hmeInterceptInformation = new HmeInterceptInformation();
            hmeInterceptInformation.setStatus("PART_INTERCEPT");
            hmeInterceptInformation.setInterceptId(interceptId);
            //更新拦截信息表
            hmeInterceptInformationMapper.updateByPrimaryKeySelective(hmeInterceptInformation);
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInterceptRelease(Long tenantId, String interceptId, List<HmeInterceptReleaseVO> hmeInterceptReleaseVOList) {
        //唯一性校验o
        Map<String, List<HmeInterceptReleaseVO>> interceptListMap = hmeInterceptReleaseVOList.stream().collect(Collectors.groupingBy(e -> this.splice(e)));
        if (interceptListMap.size() != hmeInterceptReleaseVOList.size()) {
            interceptListMap.forEach((releaseKey, releaseValue) -> {
                if (releaseValue.size() > 1) {
                    //根据输入SN数据，校验输入数据中是否存在相同数据，是时报错
                    throw new MtException("HME_INTERCEPT_RELEASE_003", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "HME_INTERCEPT_RELEASE_003", "HME", releaseValue.get(0).getMaterialLotCode()
                    ));
                }
            });
        }
        //存在性，有效性校验
        hmeInterceptReleaseVOList.forEach(hmeInterceptReleaseVO -> {
            MtMaterialLot mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setMaterialLotCode(hmeInterceptReleaseVO.getMaterialLotCode());
            MtMaterialLot materialLot = mtMaterialLotRepository.selectOne(mtMaterialLot);
            //存在
            if (Objects.isNull(materialLot) || !StringUtils.equals(materialLot.getEnableFlag(), "Y")) {
                //存在时检查字段ENABLE_FLAG是否等于“Y”。任一结果为是时则报错：输入数据【${1}】已失效或不存在,请检查，消息值集：HME_INTERCEPT_RELEASE_001
                throw new MtException("HME_INTERCEPT_RELEASE_001", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "HME_INTERCEPT_RELEASE_001", "HME", hmeInterceptReleaseVO.getMaterialLotCode()
                ));
            }
        });
        //当放行 SN输入时检查放行SN是否在拦截对象表的拦截维度内，是时报错：输入数据【${1}】不在拦截单【${2}】中,请检查，消息值集：HME_INTERCEPT_RELEASE_002

        HmeInterceptInformation hmeInterceptInformation = new HmeInterceptInformation();
        hmeInterceptInformation.setInterceptId(interceptId);
        HmeInterceptInformation information = hmeInterceptInformationRepository.selectOne(hmeInterceptInformation);
        HmeInterceptObject hmeInterceptObject = new HmeInterceptObject();
        hmeInterceptObject.setInterceptId(information.getInterceptId());
        //查找同一个拦截id下的所有拦截对象
        List<HmeInterceptObject> objectList = hmeInterceptObjectRepository.select(hmeInterceptObject);

        if (CollectionUtils.isNotEmpty(objectList)) {
            List<String> materialLotCodeList = new ArrayList<>();

            List<String> snMaterialIdList = new ArrayList<>();
            List<String> lotTimeMaterialIdList = new ArrayList<>();
            if(StringUtils.equals(information.getDimension() , "LOT")
            || StringUtils.equals(information.getDimension() , "SUPPLIER_LOT")){
                //查询物料类型
                List<HmeInterceptObjectVO> interceptObjectVOList = hmeInterceptInformationService.selectMaterialType(tenantId , interceptId);
                if(CollectionUtils.isNotEmpty(interceptObjectVOList)){
                    snMaterialIdList = interceptObjectVOList.stream()
                            .filter(item -> HmeConstants.MaterialTypeCode.SN.equals(item.getMaterialType()))
                            .map(HmeInterceptObjectVO :: getMaterialId)
                            .distinct().collect(Collectors.toList());

                    lotTimeMaterialIdList = interceptObjectVOList.stream()
                            .filter(item -> HmeConstants.MaterialTypeCode.LOT.equals(item.getMaterialType()) || HmeConstants.MaterialTypeCode.TIME.equals(item.getMaterialType()))
                            .map(HmeInterceptObjectVO :: getMaterialId)
                            .distinct().collect(Collectors.toList());
                }
            }

            switch (information.getDimension()){
                case "WO":
                    //工单维度
                    materialLotCodeList = hmeInterceptReleaseMapper.queryIndetification(tenantId , interceptId);
                    break;
                case "LAB_CODE":
                    //实验代码维度
                    List<HmePopupWindowVO> popupWindowVOList1 = hmeInterceptInformationMapper.queryInterceptExperimentList(tenantId, interceptId);
                    if (CollectionUtils.isNotEmpty(popupWindowVOList1)) {
                        materialLotCodeList = popupWindowVOList1.stream().map(HmePopupWindowVO::getSnNumber).collect(Collectors.toList());
                    }
                    break;
                case "LOT":
                    //库存批次校验
                    List<HmePopupWindowVO> popupWindowVOList2 = new ArrayList<>();
                    if(CollectionUtils.isNotEmpty(snMaterialIdList)){
                        List<HmePopupWindowVO> popupWindowVOList2_1 = hmeInterceptInformationMapper.querySnInterceptByLot(tenantId,interceptId,snMaterialIdList);
                        if(CollectionUtils.isNotEmpty(popupWindowVOList2_1)){
                            popupWindowVOList2.addAll(popupWindowVOList2_1);
                        }
                    }
                    if(CollectionUtils.isNotEmpty(lotTimeMaterialIdList)){
                        List<HmePopupWindowVO> popupWindowVOList2_2 = hmeInterceptInformationMapper.queryLotTimeInterceptByLot(tenantId,interceptId,lotTimeMaterialIdList);
                        if(CollectionUtils.isNotEmpty(popupWindowVOList2_2)){
                            popupWindowVOList2.addAll(popupWindowVOList2_2);
                        }
                    }

                    if (CollectionUtils.isNotEmpty(popupWindowVOList2)) {
                        materialLotCodeList = popupWindowVOList2.stream().map(HmePopupWindowVO::getSnNumber).collect(Collectors.toList());
                    }
                    break;
                case "SUPPLIER_LOT":
                    //供应商批次校验
                    List<HmePopupWindowVO> popupWindowVOList3 = new ArrayList<>();

                    if(CollectionUtils.isNotEmpty(snMaterialIdList)){
                        List<HmePopupWindowVO> popupWindowVOList3_1 = hmeInterceptInformationMapper.querySnInterceptBySupplierLot(tenantId,interceptId,snMaterialIdList);
                        if(CollectionUtils.isNotEmpty(popupWindowVOList3_1)){
                            popupWindowVOList3.addAll(popupWindowVOList3_1);
                        }
                    }
                    if(CollectionUtils.isNotEmpty(lotTimeMaterialIdList)){
                        List<HmePopupWindowVO> popupWindowVOList3_2 = hmeInterceptInformationMapper.queryLotTimeInterceptBySupplierLot(tenantId,interceptId,lotTimeMaterialIdList);
                        if(CollectionUtils.isNotEmpty(popupWindowVOList3_2)){
                            popupWindowVOList3.addAll(popupWindowVOList3_2);
                        }
                    }

                    if (CollectionUtils.isNotEmpty(popupWindowVOList3)) {
                        materialLotCodeList = popupWindowVOList3.stream().map(HmePopupWindowVO::getSnNumber).collect(Collectors.toList());
                    }
                    break;
                default:
                    break;
            }
            //统一校验
            for (HmeInterceptReleaseVO hmeInterceptReleaseVO : hmeInterceptReleaseVOList
                 ) {
                if (!materialLotCodeList.contains(hmeInterceptReleaseVO.getMaterialLotCode())) {
                    //输入数据【${1}】不在拦截单【${2}】中,请检查
                    throw new MtException("HME_INTERCEPT_RELEASE_002", mtErrorMessageRepository.getErrorMessageWithModule(
                            tenantId, "HME_INTERCEPT_RELEASE_002", "HME", hmeInterceptReleaseVO.getMaterialLotCode(),
                            information.getInterceptNum()
                    ));
                }
            }
        }
        //添加数据
        List<HmeInterceptRelease> hmeInterceptReleaseList = new ArrayList<>();
        hmeInterceptReleaseVOList.forEach(hmeInterceptReleaseVO -> {
            MtMaterialLot mtMaterialLot = new MtMaterialLot();
            mtMaterialLot.setMaterialLotCode(hmeInterceptReleaseVO.getMaterialLotCode());
            MtMaterialLot materialLot = mtMaterialLotRepository.selectOne(mtMaterialLot);
            if (Objects.nonNull(materialLot)) {
                //设置各个属性值
                HmeInterceptRelease hmeInterceptRelease = new HmeInterceptRelease();
                hmeInterceptRelease.setTenantId(tenantId);
                hmeInterceptRelease.setInterceptId(information.getInterceptId());
                hmeInterceptRelease.setMaterialLotId(materialLot.getMaterialLotId());
                //设置放行人
                hmeInterceptRelease.setReleaseBy(hmeInterceptReleaseVO.getReleaseBy());
                hmeInterceptReleaseList.add(hmeInterceptRelease);
            }
        });
        //批量保存
        hmeInterceptReleaseRepository.batchInsertSelective(hmeInterceptReleaseList);

    }

    private String splice(HmeInterceptReleaseVO hmeInterceptReleaseVO) {
        StringBuffer sb = new StringBuffer();
        sb.append(hmeInterceptReleaseVO.getMaterialLotCode());
        return sb.toString();
    }


}
