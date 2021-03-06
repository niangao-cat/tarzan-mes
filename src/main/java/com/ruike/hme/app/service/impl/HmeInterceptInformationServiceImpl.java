package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeInterceptInformationDTO;
import com.ruike.hme.app.service.HmeInterceptInformationService;
import com.ruike.hme.domain.entity.HmeInterceptInformation;
import com.ruike.hme.domain.entity.HmeInterceptObject;
import com.ruike.hme.domain.entity.HmeInterceptWorkcell;
import com.ruike.hme.domain.repository.HmeInterceptInformationRepository;
import com.ruike.hme.domain.repository.HmeInterceptObjectRepository;
import com.ruike.hme.domain.repository.HmeInterceptReleaseRepository;
import com.ruike.hme.domain.repository.HmeInterceptWorkcellRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeInterceptInformationMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.app.service.MtErrorMessageService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseAppService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ??????????????????????????????????????????
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:07
 */
@Service
public class HmeInterceptInformationServiceImpl extends BaseAppService implements HmeInterceptInformationService {
    private final HmeInterceptInformationMapper hmeInterceptInformationMapper;
    private final MtUserClient mtUserClient;
    private final MtNumrangeRepository mtNumrangeRepository;
    private final HmeInterceptInformationRepository hmeInterceptInformationRepository;
    private final HmeInterceptWorkcellRepository hmeInterceptWorkcellRepository;
    private final HmeInterceptObjectRepository hmeInterceptObjectRepository;
    private final HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    private final HmeInterceptReleaseRepository hmeInterceptReleaseRepository;
    private final MtModWorkcellRepository mtModWorkcellRepository;
    private final LovAdapter lovAdapter;
    private final WmsSiteRepository wmsSiteRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    public HmeInterceptInformationServiceImpl(HmeInterceptInformationMapper hmeInterceptInformationMapper, MtUserClient mtUserClient,
                                              MtNumrangeRepository mtNumrangeRepository,
                                              HmeInterceptInformationRepository hmeInterceptInformationRepository,
                                              HmeInterceptWorkcellRepository hmeInterceptWorkcellRepository,
                                              HmeInterceptObjectRepository hmeInterceptObjectRepository,
                                              HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper,
                                              HmeInterceptReleaseRepository hmeInterceptReleaseRepository,
                                              MtModWorkcellRepository mtModWorkcellRepository,
                                              LovAdapter lovAdapter,
                                              WmsSiteRepository wmsSiteRepository,
                                              MtErrorMessageRepository mtErrorMessageRepository) {
        this.hmeInterceptInformationMapper = hmeInterceptInformationMapper;
        this.mtUserClient = mtUserClient;
        this.mtNumrangeRepository = mtNumrangeRepository;
        this.hmeInterceptInformationRepository = hmeInterceptInformationRepository;
        this.hmeInterceptWorkcellRepository = hmeInterceptWorkcellRepository;
        this.hmeInterceptObjectRepository = hmeInterceptObjectRepository;
        this.hmeWorkOrderManagementMapper = hmeWorkOrderManagementMapper;
        this.hmeInterceptReleaseRepository = hmeInterceptReleaseRepository;
        this.mtModWorkcellRepository = mtModWorkcellRepository;
        this.lovAdapter = lovAdapter;
        this.wmsSiteRepository = wmsSiteRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    @Override
    @ProcessLovValue
    public Page<HmeInterceptInformationVO> queryInterceptInformation(Long tenantId, HmeInterceptInformationDTO dto, PageRequest pageRequest) {
        Page<HmeInterceptInformationVO> page = PageHelper.doPage(pageRequest, () -> hmeInterceptInformationMapper.queryHmeInterceptInformation(tenantId, dto));
        List<Long> userIdList = new ArrayList<>();
        page.getContent().forEach(hmeInterceptInformationVO -> {
            userIdList.add(hmeInterceptInformationVO.getInterceptBy());
        });
        //?????????????????????id??????
        List<Long> distinctUserIdList = userIdList.stream().distinct().collect(Collectors.toList());
        //???????????????id????????????????????????
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(distinctUserIdList)) {
            //????????????
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, distinctUserIdList);
        }
        for (HmeInterceptInformationVO hmeInterceptInformationVO : page.getContent()) {
            //????????????
            hmeInterceptInformationVO.setInterceptByName(userInfoMap.getOrDefault(hmeInterceptInformationVO.getInterceptBy(),
                    new MtUserInfo()).getRealName());
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInterceptInformation(Long tenantId, HmeInterceptInformationVO hmeInterceptInformationVO) {
        //?????????????????????   ????????????siteId
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
        mtNumrange.setObjectCode("INTERCEPT_NUM");
        mtNumrange.setSiteId(siteId);
        String number = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //??????????????????
        hmeInterceptInformationVO.setInterceptNum(number);
        HmeInterceptInformation hmeInterceptInformation = new HmeInterceptInformation();
        hmeInterceptInformation.setInterceptNum(hmeInterceptInformationVO.getInterceptNum());
        hmeInterceptInformation.setDimension(hmeInterceptInformationVO.getDimension());
        //???????????????????????????????????????NEW
        hmeInterceptInformation.setTenantId(tenantId);
        hmeInterceptInformation.setStatus("NEW");
        hmeInterceptInformation.setRemark(hmeInterceptInformationVO.getRemark());
        hmeInterceptInformation.setInterceptBy(userId);
        hmeInterceptInformation.setInterceptDate(CommonUtils.currentTimeGet());
        hmeInterceptInformationRepository.insertSelective(hmeInterceptInformation);

    }

    /**
     * ??????????????????
     *
     * @param tenantId ??????ID
     * @param interceptId ?????????id
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeInterceptObjectVO>
     */
    public List<HmeInterceptObjectVO> selectMaterialType(Long tenantId, String interceptId){
        //??????????????????????????????
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        if(StringUtils.isEmpty(siteId)){
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
        }
        List<HmeInterceptObjectVO> interceptObjectVOList = hmeInterceptObjectRepository.selectMaterialType(tenantId , siteId , interceptId);

        interceptObjectVOList.forEach(item ->{
            if(StringUtils.isBlank(item.getMaterialType())){
                item.setMaterialType(HmeConstants.MaterialTypeCode.LOT);
            }
        });

        return interceptObjectVOList;
    }

    @Override
    @ProcessLovValue
    public HmePopupWindowNumberVO queryInterceptPopupWindow(Long tenantId, String interceptId, PageRequest pageRequest) {
        //????????????
        Page<HmePopupWindowVO> page = new Page<>();
        //???????????????id
        HmeInterceptInformation hmeInterceptInformation = new HmeInterceptInformation();
        hmeInterceptInformation.setInterceptId(interceptId);
        //???????????????????????????,????????????????????????
        HmeInterceptInformation information = hmeInterceptInformationRepository.selectOne(hmeInterceptInformation);
        //????????????????????????
        Long countRelease = 0L;
        //??????????????????
        Long countIntercept = 0L;
        HmePopupWindowNumberVO hmePopupWindowNumberVO = new HmePopupWindowNumberVO();

        //??????????????????
        List<HmeInterceptObjectVO> interceptObjectVOList = selectMaterialType(tenantId , interceptId);
        List<String> snMaterialIdList = new ArrayList<>();
        List<String> lotTimeMaterialIdList = new ArrayList<>();
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

        //???????????????????????????
        if (StringUtils.equals(information.getDimension(), "LAB_CODE")) {
            //?????????????????????????????????????????????code ??????id ?????????????????? ??????????????? ???????????? ???????????????
            List<HmePopupWindowVO> queryInterceptExperimentList = hmeInterceptInformationMapper.queryInterceptExperimentList(tenantId, interceptId);
            List<HmePopupWindowVO> popupWindowVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(queryInterceptExperimentList)) {
                //????????????
                popupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptExperimentList);
                for (HmePopupWindowVO hmePopupWindowVO : popupWindowVOList) {
                    if (StringUtils.equals(hmePopupWindowVO.getStatus(), "RELEASED")) {
                        countRelease++;
                    }
                    if (StringUtils.equals(hmePopupWindowVO.getStatus(), "INTERCEPT")) {
                        countIntercept++;
                    }
                }
            }
            hmePopupWindowNumberVO.setInterceptNumber(countIntercept);
            hmePopupWindowNumberVO.setReleaseNumber(countRelease);
            //????????????
            page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), popupWindowVOList);
            hmePopupWindowNumberVO.setHmePopupWindowVOList(page);
        }
        //??????????????????
        if (StringUtils.equals(information.getDimension(), "WO")) {
            //?????????????????????????????????????????????code ??????id ?????????????????? ???????????????
            List<HmePopupWindowVO> queryInterceptDimensionList = hmeInterceptInformationMapper.queryInterceptDimensionList(tenantId, interceptId);
            List<HmePopupWindowVO> hmePopupWindowVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(queryInterceptDimensionList)) {
                //????????????
                hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptDimensionList);
                for (HmePopupWindowVO hmePopupWindowVO : hmePopupWindowVOList) {
                    if (StringUtils.equals(hmePopupWindowVO.getStatus(), "RELEASED")) {
                        countRelease++;
                    }
                    if (StringUtils.equals(hmePopupWindowVO.getStatus(), "INTERCEPT")) {
                        countIntercept++;
                    }
                }
            }
            hmePopupWindowNumberVO.setInterceptNumber(countIntercept);
            hmePopupWindowNumberVO.setReleaseNumber(countRelease);
            //????????????
            page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmePopupWindowVOList);
            hmePopupWindowNumberVO.setHmePopupWindowVOList(page);
        }
        //????????????????????????
        if (StringUtils.equals(information.getDimension(), "SN")) {
            //?????????????????????????????????????????????code ??????id ?????????????????? ???????????????
            List<HmePopupWindowVO> queryInterceptSerialNumberList = hmeInterceptInformationMapper.queryInterceptSerialNumber(tenantId, interceptId);
            List<HmePopupWindowVO> hmePopupWindowVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(queryInterceptSerialNumberList)) {
                //????????????
                hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptSerialNumberList);
                for (HmePopupWindowVO hmePopupWindowVO : hmePopupWindowVOList) {
                    if (StringUtils.equals(hmePopupWindowVO.getStatus(), "RELEASED")) {
                        countRelease++;
                    }
                    if (StringUtils.equals(hmePopupWindowVO.getStatus(), "INTERCEPT")) {
                        countIntercept++;
                    }
                }
            }
            hmePopupWindowNumberVO.setInterceptNumber(countIntercept);
            hmePopupWindowNumberVO.setReleaseNumber(countRelease);
            //????????????
            page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmePopupWindowVOList);
            hmePopupWindowNumberVO.setHmePopupWindowVOList(page);
        }
        //?????????????????????????????????
        if (StringUtils.equals(information.getDimension(), "LOT")) {
            //?????????????????????????????????????????????code ??????id ?????????????????? ???????????????
            List<HmePopupWindowVO> queryInterceptComponentBatchList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(snMaterialIdList)){
                List<HmePopupWindowVO> queryInterceptComponentBatchList1 = hmeInterceptInformationMapper.querySnInterceptByLot(tenantId,interceptId,snMaterialIdList);
                if(CollectionUtils.isNotEmpty(queryInterceptComponentBatchList1)){
                    queryInterceptComponentBatchList.addAll(queryInterceptComponentBatchList1);
                }
            }
            if(CollectionUtils.isNotEmpty(lotTimeMaterialIdList)){
                List<HmePopupWindowVO> queryInterceptComponentBatchList1 = hmeInterceptInformationMapper.queryLotTimeInterceptByLot(tenantId,interceptId,lotTimeMaterialIdList);
                if(CollectionUtils.isNotEmpty(queryInterceptComponentBatchList1)){
                    queryInterceptComponentBatchList.addAll(queryInterceptComponentBatchList1);
                }
            }
            List<HmePopupWindowVO> hmePopupWindowVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(queryInterceptComponentBatchList)) {

                hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptComponentBatchList);
                for (HmePopupWindowVO hmePopupWindowVO : hmePopupWindowVOList) {
                    if (StringUtils.equals(hmePopupWindowVO.getStatus(), "RELEASED")) {
                        countRelease++;
                    }
                    if (StringUtils.equals(hmePopupWindowVO.getStatus(), "INTERCEPT")) {
                        countIntercept++;
                    }
                }
            }
            hmePopupWindowNumberVO.setInterceptNumber(countIntercept);
            hmePopupWindowNumberVO.setReleaseNumber(countRelease);
            //????????????
            page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmePopupWindowVOList);
            hmePopupWindowNumberVO.setHmePopupWindowVOList(page);
        }

        //????????????????????????????????????
        if (StringUtils.equals(information.getDimension(), "SUPPLIER_LOT")) {
            //?????????????????????????????????????????????code ??????id ?????????????????? ???????????????
            List<HmePopupWindowVO> queryInterceptComponentSupplierBatchList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(snMaterialIdList)){
                List<HmePopupWindowVO> queryInterceptComponentSupplierBatchList1 = hmeInterceptInformationMapper.querySnInterceptBySupplierLot(tenantId,interceptId,snMaterialIdList);
                if(CollectionUtils.isNotEmpty(queryInterceptComponentSupplierBatchList1)){
                    queryInterceptComponentSupplierBatchList.addAll(queryInterceptComponentSupplierBatchList1);
                }
            }
            if(CollectionUtils.isNotEmpty(lotTimeMaterialIdList)){
                List<HmePopupWindowVO> queryInterceptComponentSupplierBatchList1 = hmeInterceptInformationMapper.queryLotTimeInterceptBySupplierLot(tenantId,interceptId,lotTimeMaterialIdList);
                if(CollectionUtils.isNotEmpty(queryInterceptComponentSupplierBatchList1)){
                    queryInterceptComponentSupplierBatchList.addAll(queryInterceptComponentSupplierBatchList1);
                }
            }

            List<HmePopupWindowVO> hmePopupWindowVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(queryInterceptComponentSupplierBatchList)) {
                hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptComponentSupplierBatchList);
                //????????????
                for (HmePopupWindowVO hmePopupWindowVO : hmePopupWindowVOList) {
                    if (StringUtils.equals(hmePopupWindowVO.getStatus(), "RELEASED")) {
                        countRelease++;
                    }
                    if (StringUtils.equals(hmePopupWindowVO.getStatus(), "INTERCEPT")) {
                        countIntercept++;
                    }
                }
            }
            hmePopupWindowNumberVO.setInterceptNumber(countIntercept);
            hmePopupWindowNumberVO.setReleaseNumber(countRelease);
            //????????????
            page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmePopupWindowVOList);
            hmePopupWindowNumberVO.setHmePopupWindowVOList(page);
        }
        return hmePopupWindowNumberVO;
    }

    @Override
    public List<HmePopupWindowVO> export(Long tenantId, String interceptId) {
        List<HmePopupWindowVO> hmePopupWindowVOList = null;
        //???????????????id
        HmeInterceptInformation hmeInterceptInformation = new HmeInterceptInformation();
        hmeInterceptInformation.setInterceptId(interceptId);
        //???????????????????????????,????????????????????????
        HmeInterceptInformation information = hmeInterceptInformationRepository.selectOne(hmeInterceptInformation);

        //???????????????????????????
        if (StringUtils.equals(information.getDimension(), "LAB_CODE")) {
            //?????????????????????????????????????????????code ??????id ?????????????????? ???????????????
            List<HmePopupWindowVO> queryInterceptExperimentList = hmeInterceptInformationMapper.queryInterceptExperimentList(tenantId, interceptId);
            //????????????
            hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptExperimentList);
        }
        //??????????????????
        if (StringUtils.equals(information.getDimension(), "WO")) {
            //?????????????????????????????????????????????code ??????id ?????????????????? ???????????????
            List<HmePopupWindowVO> queryInterceptDimensionList = hmeInterceptInformationMapper.queryInterceptDimensionList(tenantId, interceptId);
            //????????????
            hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptDimensionList);
        }
        //????????????????????????
        if (StringUtils.equals(information.getDimension(), "SN")) {
            //?????????????????????????????????????????????code ??????id ?????????????????? ???????????????
            List<HmePopupWindowVO> queryInterceptSerialNumberList = hmeInterceptInformationMapper.queryInterceptSerialNumber(tenantId, interceptId);
            //????????????
            hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptSerialNumberList);
        }
        //?????????????????????????????????
        if (StringUtils.equals(information.getDimension(), "LOT")) {
            //?????????????????????????????????????????????code ??????id ?????????????????? ???????????????
            List<HmePopupWindowVO> queryInterceptComponentBatchList = hmeInterceptInformationMapper.queryInterceptComponentBatch(tenantId, interceptId);
            //????????????
            hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptComponentBatchList);
        }
        //????????????????????????????????????
        if (StringUtils.equals(information.getDimension(), "SUPPLIER_LOT")) {
            //?????????????????????????????????????????????code ??????id ?????????????????? ???????????????
            List<HmePopupWindowVO> queryInterceptComponentSupplierBatchList = hmeInterceptInformationMapper.queryInterceptComponentSupplierBatch(tenantId, interceptId);
            //????????????
            hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptComponentSupplierBatchList);

        }
        return hmePopupWindowVOList;
    }

    private List<HmePopupWindowVO> allInterceptDimension(Long tenantId, String interceptId, List<HmePopupWindowVO> queryInterceptExperimentList) {

        //?????????????????????????????????id
        List<HmeInterceptReleaseVO> interceptReleasesList = hmeInterceptInformationMapper.whetherExistence(tenantId, interceptId);
        //???????????????????????????
        List<String> materialLotIdList = new ArrayList<>();
        //???????????????
        List<LovValueDTO> statusLovValueDTOList = lovAdapter.queryLovValue("HME.INTERCEPT_STATUS", tenantId);
        //???????????????
        List<LovValueDTO> dimensionLovValueDTOList = lovAdapter.queryLovValue("HME.INTERCEPT_DIMENSION", tenantId);
        //???????????????
        if (CollectionUtils.isNotEmpty(interceptReleasesList)) {
            interceptReleasesList.forEach(release -> {
                //????????????id
                materialLotIdList.add(release.getMaterialLotId());
            });
        }
        List<String> materialLotCodeList = new ArrayList<>();
        //???queryInterceptExperimentList ??????????????????????????? ???????????????????????????, ?????????
        for (HmePopupWindowVO intercept : queryInterceptExperimentList) {
            //???????????????sn??? ?????????????????????
            if (materialLotIdList.contains(intercept.getMaterialLotId())) {
                //???????????????????????? windowVOList ?????????????????????????????????
                intercept.setStatus("RELEASED");

            } else {
                if (StringUtils.equals(intercept.getStatusInformation(), "PART_INTERCEPT")) {
                    //???????????????????????????????????????????????????????????????????????????????????????
                    intercept.setStatus(intercept.getStatusObject());

                } else {
                    //????????????????????????????????????????????????????????????

                    intercept.setStatus(intercept.getStatusInformation());

                }
            }
            for (LovValueDTO lovValueDTO : statusLovValueDTOList) {
                if (StringUtils.equals(lovValueDTO.getValue(), intercept.getStatus())) {
                    intercept.setStatusMeaning(lovValueDTO.getMeaning());
                }
            }
            for (LovValueDTO valueDTO : dimensionLovValueDTOList) {
                if (StringUtils.equals(valueDTO.getValue(), intercept.getDimension())) {
                    intercept.setDimensionMeaning(valueDTO.getMeaning());
                }
            }
            //????????????????????????????????????????????????id
            materialLotCodeList.add(intercept.getMaterialLotId());
        }

        //??????????????????, hmePopupWindowVOList?????????????????????,??????id,????????????id,??????????????????????????????????????????????????????
        List<HmePopupWindowVO> hmePopupWindowVOList = hmeInterceptInformationMapper.queryWorkcellId(tenantId, materialLotCodeList);
        if (CollectionUtils.isEmpty(hmePopupWindowVOList)) {
            return queryInterceptExperimentList;
        }
        List<String> workcellIdList = hmePopupWindowVOList.stream().map(e -> e.getWorkcellId()).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(workcellIdList)) {
            List<HmePopupWindowVO> workcellCodeAndNameList = hmeInterceptInformationMapper.queryWorkcellCodeAndName(tenantId, workcellIdList);

            Map<String, HmePopupWindowVO> processMap = workcellCodeAndNameList.stream().collect(Collectors.toMap(HmePopupWindowVO::getWorkcellId, t -> t));
            List<MtModWorkcell> mtModWorkcellList = mtModWorkcellRepository.selectByCondition(Condition.builder(MtModWorkcell.class).andWhere(Sqls.custom()
                    .andIn(MtModWorkcell.FIELD_WORKCELL_ID, workcellIdList)).build());
            Map<String, String> materialRelMap = mtModWorkcellList.stream().collect(Collectors.toMap(MtModWorkcell::getWorkcellId, MtModWorkcell::getWorkcellName));

            Map<String, List<HmePopupWindowVO>> hmePopupWindowMap = hmePopupWindowVOList.stream().collect(Collectors.groupingBy(e -> this.splice(e)));
            queryInterceptExperimentList.forEach(hmePopupWindowVO -> {
                List<HmePopupWindowVO> hmePopupWindowVOList1 = hmePopupWindowMap.get(hmePopupWindowVO.getMaterialLotId());
                //??????
                if (CollectionUtils.isNotEmpty(hmePopupWindowVOList1)) {
                    HmePopupWindowVO windowVO = hmePopupWindowVOList1.stream().max(Comparator.comparing(HmePopupWindowVO::getSiteInDate)).get();
                    if (Objects.nonNull(windowVO)) {
                        hmePopupWindowVO.setWorkcellId(windowVO.getWorkcellId());
                        hmePopupWindowVO.setSiteInDate(windowVO.getSiteInDate());
                        //???????????????????????????
                        hmePopupWindowVO.setWorkcellName(materialRelMap.get(hmePopupWindowVO.getWorkcellId()));
                        //V20211011 modify by penglin.sui for hui.gu ???????????????????????????????????????????????????
                        HmePopupWindowVO popupWindowVO = processMap.getOrDefault(hmePopupWindowVO.getWorkcellId() , null);
                        if(Objects.nonNull(popupWindowVO)) {
                            hmePopupWindowVO.setWorkcellCode(popupWindowVO.getWorkcellCode());
                            hmePopupWindowVO.setWorkcellNameDescription(popupWindowVO.getWorkcellNameDescription());
                        }
                    }
                }
            });
        }
        return queryInterceptExperimentList;
    }


    private String splice(HmePopupWindowVO e) {
        //?????????id??????????????????
        StringBuffer sb = new StringBuffer();
        sb.append(e.getMaterialLotId());
        return sb.toString();
    }
}
