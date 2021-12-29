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
 * 拦截单信息表应用服务默认实现
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
        //对拿到的拦截人id去重
        List<Long> distinctUserIdList = userIdList.stream().distinct().collect(Collectors.toList());
        //根据拦截人id去获取拦截人姓名
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(distinctUserIdList)) {
            //批量获取
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, distinctUserIdList);
        }
        for (HmeInterceptInformationVO hmeInterceptInformationVO : page.getContent()) {
            //设置姓名
            hmeInterceptInformationVO.setInterceptByName(userInfoMap.getOrDefault(hmeInterceptInformationVO.getInterceptBy(),
                    new MtUserInfo()).getRealName());
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInterceptInformation(Long tenantId, HmeInterceptInformationVO hmeInterceptInformationVO) {
        //信息表数据添加   获取站点siteId
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
        mtNumrange.setObjectCode("INTERCEPT_NUM");
        mtNumrange.setSiteId(siteId);
        String number = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //设置拦截单号
        hmeInterceptInformationVO.setInterceptNum(number);
        HmeInterceptInformation hmeInterceptInformation = new HmeInterceptInformation();
        hmeInterceptInformation.setInterceptNum(hmeInterceptInformationVO.getInterceptNum());
        hmeInterceptInformation.setDimension(hmeInterceptInformationVO.getDimension());
        //新建信息拦截单时状态默认为NEW
        hmeInterceptInformation.setTenantId(tenantId);
        hmeInterceptInformation.setStatus("NEW");
        hmeInterceptInformation.setRemark(hmeInterceptInformationVO.getRemark());
        hmeInterceptInformation.setInterceptBy(userId);
        hmeInterceptInformation.setInterceptDate(CommonUtils.currentTimeGet());
        hmeInterceptInformationRepository.insertSelective(hmeInterceptInformation);

    }

    /**
     * 查询物料类型
     *
     * @param tenantId 租户ID
     * @param interceptId 拦截单id
     * @return java.util.Map<java.lang.String,com.ruike.hme.domain.vo.HmeInterceptObjectVO>
     */
    public List<HmeInterceptObjectVO> selectMaterialType(Long tenantId, String interceptId){
        //查询当前用户默认站点
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
        //手动分页
        Page<HmePopupWindowVO> page = new Page<>();
        //拿到拦截单id
        HmeInterceptInformation hmeInterceptInformation = new HmeInterceptInformation();
        hmeInterceptInformation.setInterceptId(interceptId);
        //找到弹窗对应拦截弹,需要判断拦截维度
        HmeInterceptInformation information = hmeInterceptInformationRepository.selectOne(hmeInterceptInformation);
        //统计释放状态数量
        Long countRelease = 0L;
        //统计拦截数量
        Long countIntercept = 0L;
        HmePopupWindowNumberVO hmePopupWindowNumberVO = new HmePopupWindowNumberVO();

        //查询物料类型
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

        //拦截维度是实验代码
        if (StringUtils.equals(information.getDimension(), "LAB_CODE")) {
            //获取拦截维度是实验代码下的条码code 条码id 拦截对象状态 拦截单对象 拦截维度 拦截号等等
            List<HmePopupWindowVO> queryInterceptExperimentList = hmeInterceptInformationMapper.queryInterceptExperimentList(tenantId, interceptId);
            List<HmePopupWindowVO> popupWindowVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(queryInterceptExperimentList)) {
                //封装数据
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
            //手动分页
            page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), popupWindowVOList);
            hmePopupWindowNumberVO.setHmePopupWindowVOList(page);
        }
        //拦截维度工单
        if (StringUtils.equals(information.getDimension(), "WO")) {
            //获取拦截维度是实验代码下的条码code 条码id 拦截对象状态 拦截单对象
            List<HmePopupWindowVO> queryInterceptDimensionList = hmeInterceptInformationMapper.queryInterceptDimensionList(tenantId, interceptId);
            List<HmePopupWindowVO> hmePopupWindowVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(queryInterceptDimensionList)) {
                //封装数据
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
            //手动分页
            page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmePopupWindowVOList);
            hmePopupWindowNumberVO.setHmePopupWindowVOList(page);
        }
        //拦截维度为序列号
        if (StringUtils.equals(information.getDimension(), "SN")) {
            //获取拦截维度是实验代码下的条码code 条码id 拦截对象状态 拦截单对象
            List<HmePopupWindowVO> queryInterceptSerialNumberList = hmeInterceptInformationMapper.queryInterceptSerialNumber(tenantId, interceptId);
            List<HmePopupWindowVO> hmePopupWindowVOList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(queryInterceptSerialNumberList)) {
                //封装数据
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
            //手动分页
            page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmePopupWindowVOList);
            hmePopupWindowNumberVO.setHmePopupWindowVOList(page);
        }
        //拦截维度为组件库存批次
        if (StringUtils.equals(information.getDimension(), "LOT")) {
            //获取拦截维度是实验代码下的条码code 条码id 拦截对象状态 拦截单对象
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
            //手动分页
            page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmePopupWindowVOList);
            hmePopupWindowNumberVO.setHmePopupWindowVOList(page);
        }

        //拦截维度为组件供应商批次
        if (StringUtils.equals(information.getDimension(), "SUPPLIER_LOT")) {
            //获取拦截维度是实验代码下的条码code 条码id 拦截对象状态 拦截单对象
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
                //封装数据
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
            //手动分页
            page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmePopupWindowVOList);
            hmePopupWindowNumberVO.setHmePopupWindowVOList(page);
        }
        return hmePopupWindowNumberVO;
    }

    @Override
    public List<HmePopupWindowVO> export(Long tenantId, String interceptId) {
        List<HmePopupWindowVO> hmePopupWindowVOList = null;
        //拿到拦截单id
        HmeInterceptInformation hmeInterceptInformation = new HmeInterceptInformation();
        hmeInterceptInformation.setInterceptId(interceptId);
        //找到弹窗对应拦截弹,需要判断拦截维度
        HmeInterceptInformation information = hmeInterceptInformationRepository.selectOne(hmeInterceptInformation);

        //拦截维度是实验代码
        if (StringUtils.equals(information.getDimension(), "LAB_CODE")) {
            //获取拦截维度是实验代码下的条码code 条码id 拦截对象状态 拦截单对象
            List<HmePopupWindowVO> queryInterceptExperimentList = hmeInterceptInformationMapper.queryInterceptExperimentList(tenantId, interceptId);
            //封装数据
            hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptExperimentList);
        }
        //拦截维度工单
        if (StringUtils.equals(information.getDimension(), "WO")) {
            //获取拦截维度是实验代码下的条码code 条码id 拦截对象状态 拦截单对象
            List<HmePopupWindowVO> queryInterceptDimensionList = hmeInterceptInformationMapper.queryInterceptDimensionList(tenantId, interceptId);
            //封装数据
            hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptDimensionList);
        }
        //拦截维度为序列号
        if (StringUtils.equals(information.getDimension(), "SN")) {
            //获取拦截维度是实验代码下的条码code 条码id 拦截对象状态 拦截单对象
            List<HmePopupWindowVO> queryInterceptSerialNumberList = hmeInterceptInformationMapper.queryInterceptSerialNumber(tenantId, interceptId);
            //封装数据
            hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptSerialNumberList);
        }
        //拦截维度为组件库存批次
        if (StringUtils.equals(information.getDimension(), "LOT")) {
            //获取拦截维度是实验代码下的条码code 条码id 拦截对象状态 拦截单对象
            List<HmePopupWindowVO> queryInterceptComponentBatchList = hmeInterceptInformationMapper.queryInterceptComponentBatch(tenantId, interceptId);
            //封装数据
            hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptComponentBatchList);
        }
        //拦截维度为组件供应商批次
        if (StringUtils.equals(information.getDimension(), "SUPPLIER_LOT")) {
            //获取拦截维度是实验代码下的条码code 条码id 拦截对象状态 拦截单对象
            List<HmePopupWindowVO> queryInterceptComponentSupplierBatchList = hmeInterceptInformationMapper.queryInterceptComponentSupplierBatch(tenantId, interceptId);
            //封装数据
            hmePopupWindowVOList = this.allInterceptDimension(tenantId, interceptId, queryInterceptComponentSupplierBatchList);

        }
        return hmePopupWindowVOList;
    }

    private List<HmePopupWindowVO> allInterceptDimension(Long tenantId, String interceptId, List<HmePopupWindowVO> queryInterceptExperimentList) {

        //拿到所选拦截框下的条码id
        List<HmeInterceptReleaseVO> interceptReleasesList = hmeInterceptInformationMapper.whetherExistence(tenantId, interceptId);
        //拦截单下的放行条码
        List<String> materialLotIdList = new ArrayList<>();
        //取状态值集
        List<LovValueDTO> statusLovValueDTOList = lovAdapter.queryLovValue("HME.INTERCEPT_STATUS", tenantId);
        //取维度值集
        List<LovValueDTO> dimensionLovValueDTOList = lovAdapter.queryLovValue("HME.INTERCEPT_DIMENSION", tenantId);
        //装进集合里
        if (CollectionUtils.isNotEmpty(interceptReleasesList)) {
            interceptReleasesList.forEach(release -> {
                //获取条码id
                materialLotIdList.add(release.getMaterialLotId());
            });
        }
        List<String> materialLotCodeList = new ArrayList<>();
        //给queryInterceptExperimentList 里边塞各种需要的值 现在里边有拦截维度, 拦截号
        for (HmePopupWindowVO intercept : queryInterceptExperimentList) {
            //遍历每一个sn号 包含在释放表里
            if (materialLotIdList.contains(intercept.getMaterialLotId())) {
                //设置状态为已放行 windowVOList 给前端所要展示字段集合
                intercept.setStatus("RELEASED");

            } else {
                if (StringUtils.equals(intercept.getStatusInformation(), "PART_INTERCEPT")) {
                    //对应拦截单状态，若状态为部分拦截，则与对应拦截对象状态相同
                    intercept.setStatus(intercept.getStatusObject());

                } else {
                    //若状态非部分拦截，则与对应拦截单状态相同

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
            //拿到拦截维度是实验代码的所有条码id
            materialLotCodeList.add(intercept.getMaterialLotId());
        }

        //查询当前工位, hmePopupWindowVOList中包含进站时间,条码id,根据条码id,找到最大的进站时间，也就是最新的记录
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
                //进站
                if (CollectionUtils.isNotEmpty(hmePopupWindowVOList1)) {
                    HmePopupWindowVO windowVO = hmePopupWindowVOList1.stream().max(Comparator.comparing(HmePopupWindowVO::getSiteInDate)).get();
                    if (Objects.nonNull(windowVO)) {
                        hmePopupWindowVO.setWorkcellId(windowVO.getWorkcellId());
                        hmePopupWindowVO.setSiteInDate(windowVO.getSiteInDate());
                        //设置弹窗的当前工位
                        hmePopupWindowVO.setWorkcellName(materialRelMap.get(hmePopupWindowVO.getWorkcellId()));
                        //V20211011 modify by penglin.sui for hui.gu 找不到就返回空白在拦截详情的弹窗中
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
        //用条码id去构造新对象
        StringBuffer sb = new StringBuffer();
        sb.append(e.getMaterialLotId());
        return sb.toString();
    }
}
