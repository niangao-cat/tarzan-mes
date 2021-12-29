package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeNcDetailDTO;
import com.ruike.hme.domain.entity.HmeNcDetail;
import com.ruike.hme.domain.repository.HmeNcDetailRepository;
import com.ruike.hme.infra.mapper.HmeNcDetailMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Xiong Yi 2020/07/11 0:29
 * @description:
 */
@Component
@Slf4j
public class HmeNcDetailRepositoryImpl implements HmeNcDetailRepository {

    @Autowired
    private HmeNcDetailMapper hmeNcDetailMapper;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    /**
     * 功能描述: <br>
     * <工序不良查询>
     *
     * @Param: [tenantId, dto, pageRequest]
     * @Return: io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeBadnessVO>
     * @Author: Xiong Yi
     * @Date: 2020-7-11 0:34
     */

    @Override
    @ProcessLovValue
    public Page<HmeNcDetail> hmeNcDetailList(Long tenantId, HmeNcDetailDTO dto, PageRequest pageRequest) {
        dto.setEndTime(Objects.isNull(dto.getEndTime()) ? (new Date().compareTo(DateUtils.addDays(dto.getBeginTime(), 31)) > 0 ? DateUtils.addDays(dto.getBeginTime(), 31) : new Date()) : dto.getEndTime());
        String siteIdByUserId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

        MtModOrganizationVO2 vo2 = new MtModOrganizationVO2();
        vo2.setTopSiteId(siteIdByUserId);
        /* 获取 站点 车间 产线 对应 rootId*/
        Boolean flag = false;
        /*站点*/
        ArrayList<String> rootIds = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getSiteId())) {
            vo2.setOrganizationType("WORKCELL");
            vo2.setParentOrganizationId(dto.getSiteId());
            vo2.setParentOrganizationType("SITE");
            vo2.setQueryType("BOTTOM");

            List<MtModOrganizationItemVO> organizationList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, vo2);

            if (CollectionUtils.isNotEmpty(organizationList)) {
                rootIds.addAll(organizationList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()));
            }
            flag = true;
        }
        //车间
        if (StringUtils.isNotBlank(dto.getWorkshopId())) {
            vo2.setOrganizationType("WORKCELL");
            vo2.setParentOrganizationId(dto.getWorkshopId());
            vo2.setParentOrganizationType("AREA");
            vo2.setQueryType("BOTTOM");

            List<MtModOrganizationItemVO> organizationList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, vo2);

            if (CollectionUtils.isNotEmpty(organizationList)) {
                rootIds.addAll(organizationList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()));
            }
            flag = true;
        }
        //产线
        if (StringUtils.isNotBlank(dto.getProdLineId())) {
            vo2.setOrganizationType("WORKCELL");
            vo2.setParentOrganizationId(dto.getProdLineId());
            vo2.setParentOrganizationType("PROD_LINE");
            vo2.setQueryType("BOTTOM");

            List<MtModOrganizationItemVO> organizationList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, vo2);

            if (CollectionUtils.isNotEmpty(organizationList)) {
                rootIds.addAll(organizationList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()));
            }
            flag = true;
        }
        List<String> workCellIdList = new ArrayList<>();
        workCellIdList.addAll(rootIds.stream().distinct().collect(Collectors.toList()));
        if (flag && CollectionUtils.isEmpty(workCellIdList)) {
            return new Page<HmeNcDetail>(Collections.EMPTY_LIST, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), 0);
        }

        Page<HmeNcDetail> hmeNcDetails = PageHelper.doPage(pageRequest, () -> hmeNcDetailMapper.queryHmeNcDetailStation(tenantId, dto, workCellIdList));

        if (CollectionUtils.isNotEmpty(hmeNcDetails)) {
            //查询产线
            List<HmeNcDetail> productionLineIdNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getProductionLineId()))
                    .collect(Collectors.toList());
            Map<String, MtModProductionLine> mtModProductionLineMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(productionLineIdNotBlankList)) {
                List<String> prodLineIdList = productionLineIdNotBlankList.stream().map(HmeNcDetail::getProductionLineId).distinct().collect(Collectors.toList());
                List<MtModProductionLine> mtModProductionLineList = mtModProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, prodLineIdList);
                if (CollectionUtils.isNotEmpty(mtModProductionLineList)) {
                    mtModProductionLineMap = mtModProductionLineList.stream().collect(Collectors.toMap(t -> t.getProdLineId(), t -> t));
                }
            }

            //查询工段、工序、提交工位
            List<HmeNcDetail> lineWorkcellIdNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getLineWorkcellId()))
                    .collect(Collectors.toList());
            List<HmeNcDetail> processIdNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getProcessId()))
                    .collect(Collectors.toList());
            List<HmeNcDetail> rootCauseWorkcellNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getRootCauseWorkcell()))
                    .collect(Collectors.toList());
            List<String> workcellIdList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(lineWorkcellIdNotBlankList)) {
                List<String> lineWorkcellIdList = lineWorkcellIdNotBlankList.stream().map(HmeNcDetail::getLineWorkcellId).distinct().collect(Collectors.toList());
                workcellIdList.addAll(lineWorkcellIdList);
            }
            if (CollectionUtils.isNotEmpty(processIdNotBlankList)) {
                List<String> processIdList = processIdNotBlankList.stream().map(HmeNcDetail::getProcessId).distinct().collect(Collectors.toList());
                workcellIdList.addAll(processIdList);
            }
            if (CollectionUtils.isNotEmpty(rootCauseWorkcellNotBlankList)) {
                List<String> rootCauseWorkcellList = rootCauseWorkcellNotBlankList.stream().map(HmeNcDetail::getRootCauseWorkcell).distinct().collect(Collectors.toList());
                workcellIdList.addAll(rootCauseWorkcellList);
            }
            Map<String, MtModWorkcell> mtModWorkcellMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(workcellIdList)) {
                workcellIdList = workcellIdList.stream().distinct().collect(Collectors.toList());
                List<MtModWorkcell> mtModWorkcellList = mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIdList);
                if (CollectionUtils.isNotEmpty(mtModWorkcellList)) {
                    mtModWorkcellMap = mtModWorkcellList.stream().collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));
                }
            }

            //处置组
            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.NC_PROCESS_METHOD", tenantId);

            //查询转型物料
            List<HmeNcDetail> transMaterialIdNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getTransMaterialId()))
                    .collect(Collectors.toList());
            Map<String, MtMaterialVO> materialMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(transMaterialIdNotBlankList)) {
                List<String> materialIdList = transMaterialIdNotBlankList.stream().map(HmeNcDetail::getTransMaterialId).distinct().collect(Collectors.toList());
                List<MtMaterialVO> mtMaterialList = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIdList);
                if (CollectionUtils.isNotEmpty(mtMaterialList)) {
                    materialMap = mtMaterialList.stream().collect(Collectors.toMap(t -> t.getMaterialId(), t -> t));
                }
            }


            //处理人备注
            List<HmeNcDetail> ncRecordIdNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getNcRecordId()))
                    .collect(Collectors.toList());
            Map<String, MtNcRecord> ncRecordMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(ncRecordIdNotBlankList)) {
                List<String> ncRecordIdList = ncRecordIdNotBlankList.stream().map(HmeNcDetail::getNcRecordId).distinct().collect(Collectors.toList());
                List<MtNcRecord> mtNcRecordList = hmeNcDetailMapper.querySubCommentsByRecordIds(tenantId, ncRecordIdList);
                if (CollectionUtils.isNotEmpty(mtNcRecordList)) {
                    ncRecordMap = mtNcRecordList.stream().collect(Collectors.toMap(t -> t.getNcRecordId(), t -> t));
                }
            }
            //获取申请人、申请人
            List<HmeNcDetail> createByNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getCreatedBy()))
                    .collect(Collectors.toList());
            List<HmeNcDetail> closedUserNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getClosedUserId()))
                    .collect(Collectors.toList());
            List<Long> userIdList = new ArrayList<>();
            Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(createByNotBlankList)) {
                List<Long> createByList = createByNotBlankList.stream().map(item -> Long.parseLong(item.getCreatedBy())).distinct().collect(Collectors.toList());
                userIdList.addAll(createByList);
            }
            if (CollectionUtils.isNotEmpty(closedUserNotBlankList)) {
                List<Long> closeUserIdList = closedUserNotBlankList.stream().map(item -> Long.parseLong(item.getClosedUserId())).distinct().collect(Collectors.toList());
                userIdList.addAll(closeUserIdList);
            }
            if (CollectionUtils.isNotEmpty(userIdList)) {
                userInfoMap = userClient.userInfoBatchGet(tenantId, userIdList);
            }

            for (HmeNcDetail vo : hmeNcDetails
            ) {
                //是否冻结
                if (StringUtils.isBlank(vo.getFreezeFlag())){
                    vo.setFreezeFlag("N");
                }
                //产线
                MtModProductionLine mtModProductionLine = mtModProductionLineMap.getOrDefault(vo.getProductionLineId(), null);
                vo.setDescription(Objects.isNull(mtModProductionLine) ? "" : mtModProductionLine.getDescription());

                //工段
                MtModWorkcell lineWorkcell = mtModWorkcellMap.getOrDefault(vo.getLineWorkcellId(), null);
                vo.setWorkcellName(Objects.isNull(lineWorkcell) ? "" : lineWorkcell.getWorkcellName());

                //工序
                MtModWorkcell process = mtModWorkcellMap.getOrDefault(vo.getProcessId(), null);
                vo.setProcess(Objects.isNull(process) ? "" : process.getWorkcellName());

                //处置组id
                if (StringUtils.isNotBlank(vo.getProcessMethod())) {
                    List<LovValueDTO> collect = lovValueDTOS.stream().filter(f -> StringUtils.equals(f.getValue(), vo.getProcessMethod())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(collect)) {
                        vo.setProcessMethodName(collect.get(0).getMeaning());
                    }
                }

                if (StringUtils.isNotBlank(vo.getTransMaterialId())) {
                    MtMaterialVO mtMaterial = materialMap.getOrDefault(vo.getTransMaterialId(), null);
                    if (mtMaterial != null) {
                        //转型物料编码
                        vo.setTransMaterialCode(mtMaterial.getMaterialCode());
                        // 转型物料描述字段
                        vo.setTransMaterialDes(mtMaterial.getMaterialName());
                    }
                }

                //提交工位
                if (StringUtils.isNotBlank(vo.getRootCauseWorkcell())) {
                    MtModWorkcell mtModWorkcell = mtModWorkcellMap.getOrDefault(vo.getRootCauseWorkcell(), null);
                    if (mtModWorkcell != null) {
                        vo.setRootCauseWorkcellName(mtModWorkcell.getWorkcellName());
                    }
                }

                //处理人备注
                MtNcRecord mtNcRecord = ncRecordMap.getOrDefault(vo.getNcRecordId(), null);
                if (Objects.nonNull(mtNcRecord)) {
                    vo.setSubComments(mtNcRecord.getComments());
                }

                if (StringUtils.isBlank(vo.getMaterialLotNum())) {
                    vo.setMaterialLotNum(vo.getMaterialLotId());
                }
                //获取申请人
                if (StringUtils.isNotBlank(vo.getCreatedBy())) {
                    MtUserInfo mtUserInfo = userInfoMap.getOrDefault(Long.valueOf(vo.getCreatedBy()), null);
                    if (Objects.nonNull(mtUserInfo)) {
                        vo.setRealName(mtUserInfo.getRealName());
                    }
                }
                //获取处理人
                if (StringUtils.isNotBlank(vo.getClosedUserId())) {
                    MtUserInfo mtUserInfo = userInfoMap.getOrDefault(Long.valueOf(vo.getClosedUserId()), null);
                    if (Objects.nonNull(mtUserInfo)) {
                        vo.setConductor(mtUserInfo.getRealName());
                    }
                }
            }
        }

        return hmeNcDetails;
    }

    @Override
    public List<HmeNcDetail> ncExcelExport(Long tenantId, HmeNcDetailDTO dto) {
        dto.setEndTime(Objects.isNull(dto.getEndTime()) ? (new Date().compareTo(DateUtils.addDays(dto.getBeginTime(), 31)) > 0 ? DateUtils.addDays(dto.getBeginTime(), 31) : new Date()) : dto.getEndTime());
        String siteIdByUserId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

        MtModOrganizationVO2 vo2 = new MtModOrganizationVO2();
        vo2.setTopSiteId(siteIdByUserId);
        /* 获取 站点 车间 产线 对应 rootId*/
        Boolean flag = false;
        /*站点*/
        ArrayList<String> rootIds = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getSiteId())) {
            vo2.setOrganizationType("WORKCELL");
            vo2.setParentOrganizationId(dto.getSiteId());
            vo2.setParentOrganizationType("SITE");
            vo2.setQueryType("BOTTOM");

            List<MtModOrganizationItemVO> organizationList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, vo2);

            if (CollectionUtils.isNotEmpty(organizationList)) {
                rootIds.addAll(organizationList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()));
            }
            flag = true;
        }
        //车间
        if (StringUtils.isNotBlank(dto.getWorkshopId())) {
            vo2.setOrganizationType("WORKCELL");
            vo2.setParentOrganizationId(dto.getWorkshopId());
            vo2.setParentOrganizationType("AREA");
            vo2.setQueryType("BOTTOM");

            List<MtModOrganizationItemVO> organizationList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, vo2);

            if (CollectionUtils.isNotEmpty(organizationList)) {
                rootIds.addAll(organizationList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()));
            }
            flag = true;
        }
        //产线
        if (StringUtils.isNotBlank(dto.getProdLineId())) {
            vo2.setOrganizationType("WORKCELL");
            vo2.setParentOrganizationId(dto.getProdLineId());
            vo2.setParentOrganizationType("PROD_LINE");
            vo2.setQueryType("BOTTOM");

            List<MtModOrganizationItemVO> organizationList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, vo2);

            if (CollectionUtils.isNotEmpty(organizationList)) {
                rootIds.addAll(organizationList.stream().map(MtModOrganizationItemVO::getOrganizationId).collect(Collectors.toList()));
            }
            flag = true;
        }
        List<String> workCellIdList = rootIds.stream().distinct().collect(Collectors.toList());
        if (flag && CollectionUtils.isEmpty(workCellIdList)) {
            return new ArrayList<>();
        }

        List<HmeNcDetail> hmeNcDetails = hmeNcDetailMapper.queryHmeNcDetailStation(tenantId, dto, workCellIdList);

        List<HmeNcDetail> transMaterialIdNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getTransMaterialId())).collect(Collectors.toList());
        Map<String, MtMaterialVO> materialVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(transMaterialIdNotBlankList)) {
            List<String> materialIdList = transMaterialIdNotBlankList.stream().map(HmeNcDetail::getTransMaterialId).distinct().collect(Collectors.toList());
            List<MtMaterialVO> materialVOList = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIdList);
            materialVOMap = materialVOList.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, t -> t));
        }

        List<HmeNcDetail> processMethodNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getProcessMethod())).collect(Collectors.toList());
        List<LovValueDTO> lovValueDTOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(processMethodNotBlankList)) {
            lovValueDTOS = lovAdapter.queryLovValue("HME.NC_PROCESS_METHOD", tenantId);
        }

        List<HmeNcDetail> mtModProductionNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getProductionLineId())).collect(Collectors.toList());
        Map<String, MtModProductionLine> mtModProductionVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtModProductionNotBlankList)) {
            List<String> mtModProductionList = mtModProductionNotBlankList.stream().map(HmeNcDetail::getProductionLineId).distinct().collect(Collectors.toList());
            List<MtModProductionLine> mtModProductionVOList = mtModProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, mtModProductionList);
            mtModProductionVOMap = mtModProductionVOList.stream().collect(Collectors.toMap(MtModProductionLine::getProdLineId, t -> t));
        }

        List<HmeNcDetail> mtModWorkcellNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getRootCauseWorkcell())).collect(Collectors.toList());
        List<HmeNcDetail> mtLineWorkcellNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getLineWorkcellId())).collect(Collectors.toList());
        List<HmeNcDetail> mtProcessWorkcellNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getProcessId())).collect(Collectors.toList());
        Map<String, MtModWorkcell> workcellVOMap = new HashMap<>();
        List<String> workcellIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mtModWorkcellNotBlankList)) {
            List<String> workcellList = mtModWorkcellNotBlankList.stream().map(HmeNcDetail::getRootCauseWorkcell).distinct().collect(Collectors.toList());
            workcellIdList.addAll(workcellList);
        }
        if (CollectionUtils.isNotEmpty(mtLineWorkcellNotBlankList)) {
            List<String> workcellList = mtLineWorkcellNotBlankList.stream().map(HmeNcDetail::getLineWorkcellId).distinct().collect(Collectors.toList());
            workcellIdList.addAll(workcellList);
        }
        if (CollectionUtils.isNotEmpty(mtProcessWorkcellNotBlankList)) {
            List<String> workcellList = mtLineWorkcellNotBlankList.stream().map(HmeNcDetail::getProcessId).distinct().collect(Collectors.toList());
            workcellIdList.addAll(workcellList);
        }
        if (CollectionUtils.isNotEmpty(workcellIdList)) {
            workcellIdList = workcellIdList.stream().distinct().collect(Collectors.toList());
            List<MtModWorkcell> workcellVOList = mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIdList);
            workcellVOMap = workcellVOList.stream().collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));
        }

        List<HmeNcDetail> mtNcRecordNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getNcRecordId())).collect(Collectors.toList());
        Map<String, MtNcRecord> ncRecordVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtNcRecordNotBlankList)) {
            List<String> ncRecordList = mtNcRecordNotBlankList.stream().map(HmeNcDetail::getNcRecordId).distinct().collect(Collectors.toList());
            List<MtNcRecord> ncRecordVOList = hmeNcDetailMapper.querySubCommentsByRecordIds(tenantId, ncRecordList);
            List<MtNcRecord> commontsNotBlankNcRecordVOList = ncRecordVOList.stream().filter(item -> StringUtils.isNotBlank(item.getComments()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(commontsNotBlankNcRecordVOList)) {
                ncRecordVOMap = commontsNotBlankNcRecordVOList.stream().collect(Collectors.toMap(t -> t.getNcRecordId(), t -> t));
            }
        }

        List<HmeNcDetail> userClientNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getCreatedBy())).collect(Collectors.toList());
        List<HmeNcDetail> closedUserNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getClosedUserId())).collect(Collectors.toList());
        List<Long> userIdList = new ArrayList<>();
        Map<Long, MtUserInfo> userClientVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userClientNotBlankList)) {
            List<Long> userClientList = userClientNotBlankList.stream().map(item -> Long.parseLong(item.getCreatedBy())).distinct().collect(Collectors.toList());
            userIdList.addAll(userClientList);
        }
        if (CollectionUtils.isNotEmpty(closedUserNotBlankList)) {
            List<Long> closedUserList = closedUserNotBlankList.stream().map(item -> Long.parseLong(item.getClosedUserId())).distinct().collect(Collectors.toList());
            userIdList.addAll(closedUserList);
        }
        if (CollectionUtils.isNotEmpty(userIdList)) {
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            userClientVOMap = userClient.userInfoBatchGet(tenantId, userIdList);
        }

//        List<HmeNcDetail> userClientNotBlankList = hmeNcDetails.stream().filter(item -> StringUtils.isNotBlank(item.getCreatedBy())).collect(Collectors.toList());
//        Map<String,MtUserClient> userClientVOMap = new HashMap<>();
//        if(CollectionUtils.isNotEmpty(userClientNotBlankList)){
//            List<String> userClientList = userClientNotBlankList.stream().map(HmeNcDetail::getCreatedBy).distinct().collect(Collectors.toList());
//            List<MtUserClient> userClientVOList = userClient.userInfoBatchGet(tenantId, Long.valueOf(userClientVOList));
//            userClientVOMap = userClientList.stream().collect(Collectors.toMap(t -> t.getCreatedBy(), t -> t));
//        }

        for (HmeNcDetail vo : hmeNcDetails) {

            //产线

            MtModProductionLine mtModProductionLine = mtModProductionVOMap.getOrDefault(vo.getProductionLineId(), null);
            if (mtModProductionLine != null) {
                vo.setDescription(mtModProductionLine.getDescription());
            }

            //工段
            MtModWorkcell mtModWorkcell = workcellVOMap.getOrDefault(vo.getLineWorkcellId(), null);
            if (mtModWorkcell != null) {
                vo.setWorkcellName(mtModWorkcell.getWorkcellName());
            }

            //工序
            MtModWorkcell mtModWorkcellProcess = workcellVOMap.getOrDefault(vo.getProcessId(), null);
            if (mtModWorkcellProcess != null) {
                vo.setProcess(mtModWorkcellProcess.getWorkcellName());
            }


            //处置组id
            if (StringUtils.isNotBlank(vo.getProcessMethod())) {
                List<LovValueDTO> collect = lovValueDTOS.stream().filter(f -> StringUtils.equals(f.getValue(), vo.getProcessMethod())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    vo.setProcessMethodName(collect.get(0).getMeaning());
                }
            }

            if (StringUtils.isNotBlank(vo.getTransMaterialId())) {
                MtMaterialVO mtMaterial = materialVOMap.getOrDefault(vo.getTransMaterialId(), null);
                if (mtMaterial != null) {
                    //转型物料编码
                    vo.setTransMaterialCode(mtMaterial.getMaterialCode());
                    // 转型物料描述字段
                    vo.setTransMaterialDes(mtMaterial.getMaterialName());
                }
            }

            //提交工位
            if (StringUtils.isNotBlank(vo.getRootCauseWorkcell())) {
                MtModWorkcell mtModWorkcellRootCause = workcellVOMap.getOrDefault(vo.getRootCauseWorkcell(), null);
                if (mtModWorkcellRootCause != null) {
                    vo.setRootCauseWorkcellName(mtModWorkcellRootCause.getWorkcellName());
                }
            }

            //处理人备注
            MtNcRecord mtNcRecords = ncRecordVOMap.getOrDefault(vo.getNcRecordId(), null);
            if (Objects.nonNull(mtNcRecords)) {
                vo.setSubComments(mtNcRecords.getComments());
            }

            if (StringUtils.isBlank(vo.getMaterialLotNum())) {
                vo.setMaterialLotNum(vo.getMaterialLotId());
            }

            //获取申请人
            if (StringUtils.isNotBlank(vo.getCreatedBy())) {
                MtUserInfo mtUserInfo = userClientVOMap.getOrDefault(vo.getCreatedBy(), null);
                if (Objects.nonNull(mtUserInfo)) {
                    vo.setRealName(mtUserInfo.getRealName());
                }
            }
            if (StringUtils.isNotBlank(vo.getClosedUserId())) {
                MtUserInfo mtUserInfo = userClientVOMap.getOrDefault(vo.getClosedUserId(), null);
                if (Objects.nonNull(mtUserInfo)) {
                    vo.setConductor(mtUserInfo.getRealName());
                }
            }
        }
        return hmeNcDetails;
    }
}