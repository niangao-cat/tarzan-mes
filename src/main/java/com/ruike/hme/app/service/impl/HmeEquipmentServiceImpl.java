package com.ruike.hme.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.api.dto.HmeEmployeeAttendanceDto8;
import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.api.dto.HmeEquipmentLocatingDTO;
import com.ruike.hme.api.dto.HmeEquipmentUpadteDTO;
import com.ruike.hme.app.service.HmeEquipmentService;
import com.ruike.hme.domain.entity.HmeEqManageTaskDoc;
import com.ruike.hme.domain.entity.HmeEqManageTaskDocLine;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocLineRepository;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocRepository;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEqManageTaskDocLineMapper;
import com.ruike.hme.infra.mapper.HmeEqManageTaskDocMapper;
import com.ruike.hme.infra.mapper.HmeEquipmentMapper;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.calendar.domain.entity.MtCalendarShift;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO6;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO7;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;
import tarzan.modeling.domain.vo.MtModWorkcellVO2;
import tarzan.modeling.infra.mapper.MtModWorkcellMapper;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ValueType.*;
import static io.tarzan.common.domain.util.MtBaseConstants.QUALITY_STATUS.NG;
import static io.tarzan.common.domain.util.MtBaseConstants.QUALITY_STATUS.OK;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

/**
 * 设备表应用服务默认实现
 *
 * @author xu.deng01@hand-china.com 2020-06-03 18:27:09
 */
@Service
@Slf4j
public class HmeEquipmentServiceImpl implements HmeEquipmentService {

    @Autowired
    private HmeEquipmentRepository repository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepository;
    @Autowired
    private HmeEqManageTaskDocRepository hmeEqManageTaskDocRepository;
    @Autowired
    private HmeEqManageTaskDocMapper hmeEqManageTaskDocMapper;
    @Autowired
    private HmeEqManageTaskDocLineRepository hmeEqManageTaskDocLineRepository;
    @Autowired
    private HmeEqManageTaskDocLineMapper hmeEqManageTaskDocLineMapper;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private HmeEquipmentMapper hmeEquipmentMapper;

    /**
     * @param tenantId    租户ID
     * @param condition   查询条件
     * @param pageRequest 分页条件
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEquipmentVO>
     * @Description: 设备台账管理-获取设备基础信息
     * @author: Deng Xu
     * @date 2020/6/3 18:48
     * @version 1.0
     */
    @Override
    @ProcessLovValue
    public Page<HmeEquipmentVO> listForUi(Long tenantId, HmeEquipmentVO condition, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> repository.queryEquipmentList(tenantId, condition));
    }

    /**
     *@description 设备台账管理-导出
     *@author wenzhang.yu@hand-china.com
     *@date 2021/3/15 15:48
     *@param tenantId
     *@param condition
     *@param exportParam
     *@return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentVO5>
     **/
    @Override
    @ProcessLovValue
    public List<HmeEquipmentVO5> listForExport(Long tenantId, HmeEquipmentVO condition, ExportParam exportParam) {
        List<HmeEquipmentVO> hmeEquipmentVOS = repository.queryEquipmentList(tenantId, condition);
        List<HmeEquipmentVO5> result = new ArrayList<>(hmeEquipmentVOS.size());
        BeanCopier copier = BeanCopier.create(HmeEquipmentVO.class, HmeEquipmentVO5.class, false);
        long i=1L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(HmeEquipmentVO src:hmeEquipmentVOS )
        {
            HmeEquipmentVO5 temp = new HmeEquipmentVO5();
            copier.copy(src, temp, null);
            temp.setOrderNum(i++);
            // 格式化时间
            if (temp.getWarrantyDate() != null) {
                temp.setWarrantyDateStr(sdf.format(temp.getWarrantyDate()));
            }
            if (temp.getPostingDate() != null) {
                temp.setPostingDateStr(sdf.format(temp.getPostingDate()));
            }
            result.add(temp);
        }
        return result;
    }

    /**
     * @param tenantId  租户ID
     * @param condition 查询条件（设备ID）
     * @return : com.ruike.hme.domain.vo.HmeEquipmentVO
     * @Description: 设备台账管理-根据设备ID获取设备基础信息
     * @author: Deng Xu
     * @date 2020/6/4 14:25
     * @version 1.0
     */
    @Override
    @ProcessLovValue
    public HmeEquipmentVO queryOneForUi(Long tenantId, HmeEquipmentVO condition) {
        if (StringUtils.isEmpty(condition.getEquipmentId())) {
            throw new MtException("HME_EQUIPMENT_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_003", "HME"));
        }
        List<HmeEquipmentVO> returnList = repository.queryEquipmentList(tenantId, condition);
        if (CollectionUtils.isEmpty(returnList) || null == returnList.get(0)) {
            return new HmeEquipmentVO();
        }
        return returnList.get(0);
    }

    /**
     * @param tenantId 租户ID
     * @param dto      设备台账信息DTO
     * @return : com.ruike.hme.domain.entity.HmeEquipment
     * @Description: 设备台账管理-新增&更新设备基础信息
     * @author: Deng Xu
     * @date 2020/6/3 18:49
     * @version 1.0
     */
    @Override
    public HmeEquipment saveForUi(Long tenantId, HmeEquipment dto) {
        return repository.equipmentBasicPropertyUpdate(tenantId, dto);
    }

    @Override
    public Page<HmeEquipmentHisVO2> queryWorkcellHisForUi(Long tenantId, HmeEquipmentHisVO dto, PageRequest pageRequest) {
        return repository.queryWorkcellHisForUi(tenantId, dto, pageRequest);
    }

    /**
     * 设备信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    @ProcessLovValue
    public HmeEquipmentVO3 queryOneInfo(Long tenantId, HmeEquipment dto) {
        //必输校验
        if(StringUtils.isEmpty(dto.getAssetEncoding())){
            throw new MtException("HME_OP_TIME_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_OP_TIME_0001", "HME", "条码"));
        }
        //判断输入条码是任务单号还是设备编码
        HmeEqManageTaskDoc hmeEqManageTaskDoc = hmeEqManageTaskDocRepository.selectOne(new HmeEqManageTaskDoc() {{
            setTenantId(tenantId);
            setDocNum(dto.getAssetEncoding());
        }});
        HmeEquipmentVO3 hmeEquipmentVO3 = new HmeEquipmentVO3();
        String assetEncoding = "";
        if(hmeEqManageTaskDoc != null){
            //如果是任务单号
            HmeEquipment hmeEquipment = hmeEquipmentRepository.selectByPrimaryKey(hmeEqManageTaskDoc.getEquipmentId());
            assetEncoding = hmeEquipment.getAssetEncoding();
//            HmeEquipmentVO3 hmeEquipmentVO3 = hmeEquipmentMapper.queryOneInfo(tenantId, hmeEquipment.getAssetEncoding());
            hmeEquipmentVO3.setEquipmentFlag(false);
            hmeEquipmentVO3.setTypeFlag(hmeEqManageTaskDoc.getDocType());
            hmeEquipmentVO3.setTaskDocId(hmeEqManageTaskDoc.getTaskDocId());
        }else{
            HmeEquipment hmeEquipment = hmeEquipmentMapper.selectOne(new HmeEquipment() {{
                setTenantId(tenantId);
                setAssetEncoding(dto.getAssetEncoding());
            }});
            if(hmeEquipment != null){
                //如果是设备
//                HmeEquipmentVO3 hmeEquipmentVO3 = repository.queryOneInfo(tenantId, dto);
                assetEncoding = hmeEquipment.getAssetEncoding();
                hmeEquipmentVO3.setEquipmentFlag(true);
            }else{
                //	扫描设备编码/任务单号不存在,请重新输入!
                throw new MtException("HME_EQUIPMENT_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_005", "HME"));
            }
        }
        if(StringUtils.isNotBlank(assetEncoding)){
            List<HmeEquipmentVO3> hmeEquipmentVO3List = hmeEquipmentMapper.queryMultiInfo(tenantId, assetEncoding);
            StringBuilder stationStringBuilder = new StringBuilder();
            StringBuilder workcellNameStringBuilder = new StringBuilder();
            int loopCount = 0;
            for (HmeEquipmentVO3 item:hmeEquipmentVO3List
            ) {
                if(loopCount == 0){
                    hmeEquipmentVO3.setAssetName(item.getAssetName());
                    hmeEquipmentVO3.setEquipmentId(item.getEquipmentId());
                    hmeEquipmentVO3.setSupplier(item.getSupplier());
                    hmeEquipmentVO3.setModel(item.getModel());
                    hmeEquipmentVO3.setWorkcellId(item.getStationId());
                    hmeEquipmentVO3.setWorkcellCode(item.getWorkcellCode());
                }
                if(stationStringBuilder.length() == 0){
                    stationStringBuilder.append(item.getStationId());
                }else{
                    stationStringBuilder.append("," + item.getStationId());
                }
                if(workcellNameStringBuilder.length() == 0){
                    workcellNameStringBuilder.append(item.getWorkcellName());
                }else{
                    workcellNameStringBuilder.append("," + item.getWorkcellName());
                }
                loopCount++;
            }
            if(stationStringBuilder.length() > 0) {
                hmeEquipmentVO3.setStationId(stationStringBuilder.toString());
            }
            if(workcellNameStringBuilder.length() > 0) {
                hmeEquipmentVO3.setWorkcellName(workcellNameStringBuilder.toString());
            }
        }
        return hmeEquipmentVO3;
    }

    /**
     * 工位扫描-登入校验
     *
     * @param tenantId 租户ID
     * @param dto      扫描数据
     * @return
     */
    @Override
    public HmeEoJobSnVO7 workcellScan(Long tenantId, HmeEoJobSnDTO dto) {
        //
        MtModWorkcellVO1 mtModWorkcellVO1 = new MtModWorkcellVO1();
        mtModWorkcellVO1.setWorkcellCode(dto.getWorkcellCode());
        mtModWorkcellVO1.setWorkcellType("STATION");
        List<String> workcellIds = mtModWorkcellRepository.propertyLimitWorkcellQuery(tenantId, mtModWorkcellVO1);

        if (CollectionUtils.isEmpty(workcellIds)) {
            // 当前工位无效,请检查工位条码
            throw new MtException("HME_EO_JOB_SN_001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_001", "HME"));
        }

        MtModWorkcellVO2 workcellVO = mtModWorkcellMapper.selectWorkcellById(tenantId, workcellIds.get(0));

        HmeEoJobSnVO7 hmeEoJobSnVO7 = new HmeEoJobSnVO7();
        hmeEoJobSnVO7.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnVO7.setWorkcellId(workcellVO.getWorkcellId());
        hmeEoJobSnVO7.setWorkcellName(workcellVO.getWorkcellName());

        // 获取工序
        MtModOrganizationVO2 processParam = new MtModOrganizationVO2();
        processParam.setTopSiteId(dto.getSiteId());
        processParam.setOrganizationId(workcellVO.getWorkcellId());
        processParam.setOrganizationType("WORKCELL");
        processParam.setParentOrganizationType("WORKCELL");
        processParam.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> processList =
                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, processParam);
        if (CollectionUtils.isEmpty(processList)) {
            // 请先维护Wkc工序工位关系
            throw new MtException("HME_EO_JOB_SN_002",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_002", "HME"));
        } else {
            // 返回工序
            hmeEoJobSnVO7.setProcessId(processList.get(0).getOrganizationId());
            MtModWorkcell process =
                    mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, hmeEoJobSnVO7.getProcessId());
            hmeEoJobSnVO7.setProcessName(process.getWorkcellName());
            // 获取工艺路线
            MtOpWkcDispatchRelVO7 wkcDispatchParam = new MtOpWkcDispatchRelVO7();
            wkcDispatchParam.setWorkcellId(hmeEoJobSnVO7.getProcessId());
            List<MtOpWkcDispatchRelVO6> opWkcDispatchRelVO6List = mtOperationWkcDispatchRelRepository
                    .wkcLimitAvailableOperationQuery(tenantId, wkcDispatchParam);
            if (CollectionUtils.isNotEmpty(opWkcDispatchRelVO6List)) {
                // 获取当前工位对应的工艺信息
                List<String> operationIdList = opWkcDispatchRelVO6List.stream()
                        .map(MtOpWkcDispatchRelVO6::getOperationId).collect(Collectors.toList());
                hmeEoJobSnVO7.setOperationIdList(operationIdList);
                if (HmeConstants.JobType.TIME_PROCESS.equals(dto.getJobType())) {
                    if (CollectionUtils.isNotEmpty(operationIdList)) {
                        if (operationIdList.size() > 1) {
                            // 时效工位不允许有多个工艺
                            throw new MtException("HME_EO_JOB_SN_042", mtErrorMessageRepository
                                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_042", "HME"));
                        } else {
                            hmeEoJobSnVO7.setOperationId(operationIdList.get(0));
                        }
                    }
                }
            } else {
                // 请先维护Wkc工序工艺关系
                throw new MtException("HME_EO_JOB_SN_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_021", "HME"));
            }

            // 获取工段
            MtModOrganizationVO2 lineParam = new MtModOrganizationVO2();
            lineParam.setTopSiteId(dto.getSiteId());
            lineParam.setOrganizationId(hmeEoJobSnVO7.getProcessId());
            lineParam.setOrganizationType("WORKCELL");
            lineParam.setParentOrganizationType("WORKCELL");
            lineParam.setQueryType("BOTTOM");
            List<MtModOrganizationItemVO> lineVOList =
                    mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, lineParam);
            if (CollectionUtils.isEmpty(lineVOList)) {
                // 请先维护Wkc工序工段关系
                throw new MtException("HME_EO_JOB_SN_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_023", "HME"));
            } else {
                String wkcLineId = lineVOList.get(0).getOrganizationId();
                MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, wkcLineId);
                if (Objects.isNull(mtWkcShiftVO3)) {
                    throw new MtException("HME_EO_JOB_SN_026", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_026", "HME"));
                }
                hmeEoJobSnVO7.setWkcShiftId(mtWkcShiftVO3.getWkcShiftId());
                hmeEoJobSnVO7.setShiftCode(mtWkcShiftVO3.getShiftCode());
                hmeEoJobSnVO7.setShiftDate(mtWkcShiftVO3.getShiftDate());
            }
            return hmeEoJobSnVO7;
        }
    }

    /**
     * 获取设备点检内容
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    @Override
    public HmeEqManageTaskInfoVO2 equipmentContent(Long tenantId, HmeEquipmentLocatingDTO dto, PageRequest pageRequest) {
        HmeEqManageTaskInfoVO2 result = new HmeEqManageTaskInfoVO2();
        List<HmeEqManageTaskInfoVO> hmeEqManageTaskInfoVOS1 = new ArrayList<>();
        if(StringUtils.isNotEmpty(dto.getTaskDocId())){
            //如果扫描的是任务单
            hmeEqManageTaskInfoVOS1 = hmeEqManageTaskDocMapper.equipmentContent(tenantId, Collections.singletonList(dto.getTaskDocId()));
        }else{
            //如果扫描的是设备,判断是否是第一天上班
            //获取organizationId
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getTopSiteId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setOrganizationId(dto.getWorkcellId());
            mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("TOP");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            //获取shiftDate shiftCode
            MtCalendarShiftVO mtCalendarShiftVO = new MtCalendarShiftVO();
            mtCalendarShiftVO.setWorkcellId(mtModOrganizationItemVOS.get(0).getOrganizationId());
//        mtCalendarShiftVO.setShiftDate(new Date());
            //获取当前时间只要年月日
            final Date currentDate = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(currentDate);
            Date parse = null;
            try {
                parse = simpleDateFormat.parse(format);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mtCalendarShiftVO.setShiftDate(parse);
            mtCalendarShiftVO.setShiftCode(dto.getShiftCode());
            MtCalendarShift mtCalendarShift = mtCalendarShiftRepository.calendarPreviousShiftGet(tenantId, mtCalendarShiftVO);
            //判断是否为一天
//        boolean sameDay = DateUtils.isSameDay(parse, mtCalendarShift.getShiftDate());
            boolean sameDay = isSameDate(parse, mtCalendarShift.getShiftDate());
            //班检第一部分
            HmeEqManageTaskDoc hmeEqManageTaskDoc = new HmeEqManageTaskDoc();
            hmeEqManageTaskDoc.setEquipmentId(dto.getEquipmentId());
            hmeEqManageTaskDoc.setDocType("C");
            hmeEqManageTaskDoc.setTaskCycle("0.5");
            //转换一下localDate转为date
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = dto.getShiftDate().atStartOfDay().atZone(zone).toInstant();
            Date date = Date.from(instant);
            hmeEqManageTaskDoc.setShiftDate(date);
            hmeEqManageTaskDoc.setShiftCode(dto.getShiftCode());
            //获取设备点检任务信息
            List<HmeEqManageTaskDoc> hmeEqManageTaskDocList = hmeEqManageTaskDocRepository.select(hmeEqManageTaskDoc);
            if (CollectionUtils.isNotEmpty(hmeEqManageTaskDocList)) {
                List<String> taskDocIdList = hmeEqManageTaskDocList.stream().map(HmeEqManageTaskDoc::getTaskDocId).collect(Collectors.toList());
                hmeEqManageTaskInfoVOS1.addAll(hmeEqManageTaskDocMapper.equipmentContent(tenantId, taskDocIdList));
            }
            // 无论是否同一天 都查询当天设备任务
            HmeEqManageTaskDoc hmeEqManageTaskDoc1 = new HmeEqManageTaskDoc();
            hmeEqManageTaskDoc1.setEquipmentId(dto.getEquipmentId());
            hmeEqManageTaskDoc1.setDocType("C");
            hmeEqManageTaskDoc1.setTaskCycle("0.5");
            hmeEqManageTaskDoc1.setCreationDate(parse);
            //获取当天的23:59:59
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date parseTo = calendar.getTime();
            //获取设备点检任务信息
            List<HmeEqManageTaskDoc> hmeEqManageTaskDocs = hmeEqManageTaskDocMapper.queryManageTask(tenantId, hmeEqManageTaskDoc1, parseTo);
            if (CollectionUtils.isNotEmpty(hmeEqManageTaskDocs)) {
                List<String> taskDocIdList = hmeEqManageTaskDocs.stream().map(HmeEqManageTaskDoc::getTaskDocId).collect(Collectors.toList());
                //第二种情况不为空
                List<HmeEqManageTaskInfoVO> hmeEqManageTaskInfoVOS = hmeEqManageTaskDocMapper.equipmentContent(tenantId, taskDocIdList);
                hmeEqManageTaskInfoVOS1.addAll(hmeEqManageTaskInfoVOS);
            }
        }
        List<HmeEqManageTaskInfoVO> hmeEqManageTaskInfoVOS = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmeEqManageTaskInfoVOS1);
        Page<HmeEqManageTaskInfoVO> page = new Page<>(hmeEqManageTaskInfoVOS, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), hmeEqManageTaskInfoVOS1.size());
        result.setPageData(page);
        //点检项总数量
        int totalQty = hmeEqManageTaskInfoVOS1.size();
        result.setTotalQty(totalQty);
        //已完成点检项数量
        int completeQty = 0;
        for (HmeEqManageTaskInfoVO hmeEqManageTaskInfoVO : hmeEqManageTaskInfoVOS1) {
            if (StringUtils.isNotEmpty(hmeEqManageTaskInfoVO.getResult())) {
                completeQty++;
            }
        }
        result.setCompleteQty(completeQty);
        if (completeQty == 0) {
            result.setStatus("未点检");
        } else if (completeQty < totalQty) {
            result.setStatus("点检中");
        } else {
            result.setStatus("已点检");
        }
        return result;
    }

    /**
     * 获取保养内容
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    @Override
    @ProcessLovValue
    public HmeEqManageTaskInfoVO2 maintainContent(Long tenantId, HmeEquipmentLocatingDTO dto, PageRequest pageRequest) {
        HmeEqManageTaskInfoVO2 result = new HmeEqManageTaskInfoVO2();
        //获取保养任务信息
        Page<HmeEqManageTaskInfoVO> pageData = new Page<>();
        //保养项总数量
        int totalQty = 0;
        //已完成保养项数量
        int completeQty = 0;
        final Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(currentDate);
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(StringUtils.isNotEmpty(dto.getTaskDocId())){
            //如果扫描的是任务单
            pageData = PageHelper.doPage(pageRequest, () -> hmeEqManageTaskDocMapper.equipmentContent(tenantId, Collections.singletonList(dto.getTaskDocId())));
            totalQty = Long.valueOf(pageData.getTotalElements()).intValue();
            completeQty = hmeEqManageTaskDocMapper.getCompleteQty(tenantId, Collections.singletonList(dto.getTaskDocId()));
            if(CollectionUtils.isNotEmpty(pageData)){
                result.setDocStatus(pageData.get(0).getDocStatus());
            }
        }else{
            //如果扫描的是设备
            HmeEqManageTaskDoc hmeEqManageTaskDoc = new HmeEqManageTaskDoc();
            hmeEqManageTaskDoc.setEquipmentId(dto.getEquipmentId());
            hmeEqManageTaskDoc.setDocType("M");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);

            hmeEqManageTaskDoc.setCreationDate(parse);
            if("YEAR".equals(dto.getTimeType())){
                hmeEqManageTaskDoc.setTaskCycle("365");
                calendar.add(Calendar.DAY_OF_MONTH, -365);
            }else if("HALF".equals(dto.getTimeType())){
                hmeEqManageTaskDoc.setTaskCycle("180");
                calendar.add(Calendar.DAY_OF_MONTH, -180);
            }else if("SEASON".equals(dto.getTimeType())){
                hmeEqManageTaskDoc.setTaskCycle("90");
                calendar.add(Calendar.DAY_OF_MONTH, -90);
            }else if ("MONTH".equals(dto.getTimeType())){
                hmeEqManageTaskDoc.setTaskCycle("30");
                calendar.add(Calendar.DAY_OF_MONTH, -30);
            }else if("WEEK".equals(dto.getTimeType())) {
                hmeEqManageTaskDoc.setTaskCycle("7");
                calendar.add(Calendar.DAY_OF_MONTH, -7);
            }else if("DAY".equals(dto.getTimeType())) {
                hmeEqManageTaskDoc.setTaskCycle("1");
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }else if("SHIFT".equals(dto.getTimeType())) {
                hmeEqManageTaskDoc.setTaskCycle("0.5");
                calendar.add(Calendar.HOUR_OF_DAY, -12);
            }
            Date parseFrom = calendar.getTime();
            List<HmeEqManageTaskDoc> hmeEqManageTaskDocList = hmeEqManageTaskDocMapper.queryManageTask3(tenantId, hmeEqManageTaskDoc, parseFrom);
            if(CollectionUtils.isNotEmpty(hmeEqManageTaskDocList)) {
                result.setDocStatus(hmeEqManageTaskDocList.get(0).getDocStatus());
            }
            if (CollectionUtils.isNotEmpty(hmeEqManageTaskDocList)) {
                List<String> taskDocIdList = hmeEqManageTaskDocList.stream().map(HmeEqManageTaskDoc::getTaskDocId).collect(Collectors.toList());
                pageData = PageHelper.doPage(pageRequest, () -> hmeEqManageTaskDocMapper.equipmentContent(tenantId, taskDocIdList));
                totalQty = Long.valueOf(pageData.getTotalElements()).intValue();
                completeQty = hmeEqManageTaskDocMapper.getCompleteQty(tenantId, taskDocIdList);
            }
        }
        result.setPageData(pageData);
        result.setTotalQty(totalQty);
        result.setCompleteQty(completeQty);
        //状态
        if (completeQty == 0) {
            result.setStatus("未保养");
        } else if (completeQty < totalQty) {
            result.setStatus("保养中");
        } else {
            result.setStatus("已保养");
        }
        return result;
    }

    /**
     * 点检&保养按钮修改
     *
     * @param tenantId
     * @param dto
     */
    @Override
    public HmeEqManageTaskDocLine update(Long tenantId, HmeEquipmentUpadteDTO dto) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? 1L : curUser.getUserId();
        HmeEqManageTaskDocLine hmeEqManageTaskDocLine = new HmeEqManageTaskDocLine();
        switch (dto.getValueType()) {
            case DECISION_VALUE:
                hmeEqManageTaskDocLine.setTenantId(tenantId);
                hmeEqManageTaskDocLine.setTaskDocId(dto.getTaskDocId());
                hmeEqManageTaskDocLine.setManageTagId(dto.getManageTagId());
                hmeEqManageTaskDocLine.setTaskDocLineId(dto.getTaskDocLineId());
                hmeEqManageTaskDocLine.setResult(dto.getResult());
                hmeEqManageTaskDocLine.setCheckValue(dto.getCheckValue());
                hmeEqManageTaskDocLine.setCheckBy(userId);
                hmeEqManageTaskDocLine.setCheckDate(new Date());
                hmeEqManageTaskDocLine.setWkcId(dto.getWorkcellId());
                hmeEqManageTaskDocLineMapper.updateByPrimaryKeySelective(hmeEqManageTaskDocLine);
                //判断所有内容是否修改完进行修改头表
                this.changesAll(tenantId, dto, userId);
                return hmeEqManageTaskDocLine;
            case VALUE:
                hmeEqManageTaskDocLine.setTenantId(tenantId);
                hmeEqManageTaskDocLine.setTaskDocId(dto.getTaskDocId());
                hmeEqManageTaskDocLine.setManageTagId(dto.getManageTagId());
                hmeEqManageTaskDocLine.setTaskDocLineId(dto.getTaskDocLineId());
                hmeEqManageTaskDocLine.setCheckValue(dto.getCheckValue());
                hmeEqManageTaskDocLine.setResult(dto.getResult());
                hmeEqManageTaskDocLine.setCheckBy(userId);
                hmeEqManageTaskDocLine.setCheckDate(new Date());
                hmeEqManageTaskDocLine.setWkcId(dto.getWorkcellId());
                hmeEqManageTaskDocLineMapper.updateByPrimaryKeySelective(hmeEqManageTaskDocLine);
                //判断所有内容是否修改完进行修改头表
                this.changesAll(tenantId, dto, userId);
                return hmeEqManageTaskDocLine;
            case TEXT:
                hmeEqManageTaskDocLine.setTaskDocId(dto.getTaskDocId());
                hmeEqManageTaskDocLine.setManageTagId(dto.getManageTagId());
                hmeEqManageTaskDocLine.setTaskDocLineId(dto.getTaskDocLineId());
                hmeEqManageTaskDocLine.setTenantId(tenantId);
                hmeEqManageTaskDocLine.setCheckValue(dto.getCheckValue());
                hmeEqManageTaskDocLine.setResult("OK");
                hmeEqManageTaskDocLine.setCheckBy(userId);
                hmeEqManageTaskDocLine.setCheckDate(new Date());
                hmeEqManageTaskDocLine.setWkcId(dto.getWorkcellId());
                hmeEqManageTaskDocLineMapper.updateByPrimaryKeySelective(hmeEqManageTaskDocLine);
                //判断所有内容是否修改完进行修改头表
                this.changesAll(tenantId, dto, userId);
                return hmeEqManageTaskDocLine;
            default:
                return null;
        }
    }

    @Override
    public HmeEqManageTaskDocLine maintainEquipmentUpdate(Long tenantId, HmeEqManageTaskDocLine hmeEqManageTaskDocLine) {
        if (StringUtils.isBlank(hmeEqManageTaskDocLine.getTaskDocLineId())) {
            throw new MtException("HME_EQUIPMENT_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_008", "HME"));
        }
        // 只有保养 才能更新备注
        HmeEqManageTaskDoc hmeEqManageTaskDoc = hmeEqManageTaskDocMapper.selectByPrimaryKey(hmeEqManageTaskDocLine.getTaskDocId());
        if (!HmeConstants.ObjectTypeCode.MAINTAIN.equals(hmeEqManageTaskDoc.getDocType())) {
            throw new MtException("HME_EQUIPMENT_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_009", "HME"));
        }
        hmeEqManageTaskDocLineMapper.updateByPrimaryKeySelective(hmeEqManageTaskDocLine);
        return hmeEqManageTaskDocLine;
    }

    @Override
    @ProcessLovValue
    public Page<HmeEquipmentHisVO3> queryEquipmentHis(Long tenantId, String equipmentId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeEquipmentMapper.queryEquipmentHis(tenantId, equipmentId));
    }

    private String appendContent(String header, List<String> paramList) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(header);
        for (String str : paramList) {
            if (org.apache.commons.lang.StringUtils.isBlank(str)) {
                str = "";
            }
            sbf.append("*").append(str);
        }
        return sbf.toString();
    }

    /**
     * 生成二维码
     *
     * @param basePath
     * @param uuid
     * @param hmeEquipmentVO
     * @param barcodeImageFileList
     * @return
     */
    private String getQrcodePath(String basePath, String uuid, HmeEquipmentVO hmeEquipmentVO, List<File> barcodeImageFileList) {
        String qrcodePath = basePath + "/" + uuid + "_" + hmeEquipmentVO.getAssetEncoding() + "_qrcode.png";
        File qrcodeImageFile = new File(qrcodePath);
        barcodeImageFileList.add(qrcodeImageFile);
        try {
            String content = hmeEquipmentVO.getEncryptionAssetEncoding();
            CommonQRCodeUtil.encode(content, qrcodePath, qrcodePath, true);
            log.info("<====生成二维码完成！{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== HmeEquipmentServiceImpl.getQrcodePath Error", e);
            throw new MtException(e.getMessage());
        }
        return qrcodePath;
    }

    @Override
    public void print(Long tenantId, HmeEquipmentVO6 hmeEquipmentVO6, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(hmeEquipmentVO6.getHmeEquipmentVOList())) {
            return;
        }
        // 目前两个按钮打印按钮 已打印的不能再打印 记录打印标识 补打印不限制 2-补打印 1-打印
        if (!StringUtils.equals(hmeEquipmentVO6.getPrintType(), HmeConstants.ConstantValue.STRING_TWO)) {
            List<String> assetEncodingList = hmeEquipmentVO6.getHmeEquipmentVOList().stream().map(HmeEquipmentVO::getAssetEncoding).collect(Collectors.toList());
            List<HmeEquipment> hmeEquipmentList = hmeEquipmentMapper.selectByCondition(Condition.builder(HmeEquipment.class).andWhere(Sqls.custom()
                    .andEqualTo(HmeEquipment.FIELD_TENANT_ID, tenantId)
                    .andIn(HmeEquipment.FIELD_ASSET_ENCODING, assetEncodingList)).build());
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            // 批量更新设备打印标识
            hmeEquipmentMapper.batchUpdateEquipmentPrintFlag(tenantId, userId, hmeEquipmentList);
        }
        //确定根目录
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("创建临时文件夹失败!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }
        String uuid = UUID.randomUUID().toString();
        String qrcodePath = "";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        String pdfName = "";
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<File> qrcodeImageFileList = new ArrayList<File>();

        //组装参数
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

        //循环需要打印的数据，单个打印
        for (int i = 0; i < hmeEquipmentVO6.getHmeEquipmentVOList().size(); i++) {
            int num = i % 2;
            HmeEquipmentVO hmeEquipmentVO = hmeEquipmentVO6.getHmeEquipmentVOList().get(i);
            // 二维码
            qrcodePath = getQrcodePath(basePath, uuid, hmeEquipmentVO, qrcodeImageFileList);
            imgMap.put("qrcodeImage" + num, qrcodePath);
            pdfName = "/hme_equipment_print_template.pdf";
            formMap.put("assetEncoding" + num, hmeEquipmentVO.getAssetEncoding());
            formMap.put("assetName" + num, hmeEquipmentVO.getAssetName());

            if (num == 1 || i == hmeEquipmentVO6.getHmeEquipmentVOList().size() - 1) {
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
                imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

            }
        }

        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + pdfName, pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== HmeEquipmentServiceImpl.print.multiplePage Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //将文件转化成流进行输出
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //设置相应参数
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //将文件转化成流进行输出
            bis = new BufferedInputStream(new FileInputStream(pdfPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("<==== HmeEquipmentServiceImpl.print.outputPDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("<==== HmeEquipmentServiceImpl.print.closeIO Error", e);
            }
        }

        //删除临时文件
        for (File file : qrcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== HmeEquipmentServiceImpl.print.qrcodeImageFileList Failed: {}", qrcodePath);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== HmeEquipmentServiceImpl.print.pdfFile Failed: {}", qrcodePath);
        }
    }

    @Override
    public void printCheck(Long tenantId, HmeEquipmentVO6 hmeEquipmentVO6) {
        if (CollectionUtils.isNotEmpty(hmeEquipmentVO6.getHmeEquipmentVOList())) {
            // 目前两个按钮打印按钮 已打印的不能再打印 记录打印标识 补打印不限制 Y-补打印 N-打印
            if (!StringUtils.equals(hmeEquipmentVO6.getPrintType(), HmeConstants.ConstantValue.STRING_TWO)) {
                List<String> assetEncodingList = hmeEquipmentVO6.getHmeEquipmentVOList().stream().map(HmeEquipmentVO::getAssetEncoding).collect(Collectors.toList());
                List<HmeEquipment> hmeEquipmentList = hmeEquipmentMapper.selectByCondition(Condition.builder(HmeEquipment.class).andWhere(Sqls.custom()
                        .andEqualTo(HmeEquipment.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeEquipment.FIELD_ASSET_ENCODING, assetEncodingList)).build());
                List<HmeEquipment> filterEquipmentList = hmeEquipmentList.stream().filter(he -> StringUtils.equals(he.getAttribute3(), HmeConstants.ConstantValue.YES)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(filterEquipmentList)) {
                    List<String> assetEncodings = filterEquipmentList.stream().map(HmeEquipment::getAssetEncoding).collect(Collectors.toList());
                    throw new MtException("HME_EQUIPMENT_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_012", "HME", StringUtils.join(assetEncodings, ",")));
                }
            }
        }
    }

    //判断所有的点检或者保养是否全部修改完
    public void changesAll(Long tenantId, HmeEquipmentUpadteDTO dto, Long userId) {
        //判断所有的result为空（都不为空所以的操作完成修改头表）
        HmeEqManageTaskDocLine hmeEqManageTaskDocLine1 = new HmeEqManageTaskDocLine();
        hmeEqManageTaskDocLine1.setTaskDocId(dto.getTaskDocId());
        List<HmeEqManageTaskDocLine> taskDocLineList = hmeEqManageTaskDocLineRepository.select(hmeEqManageTaskDocLine1);
        List<String> resList = taskDocLineList.stream().map(HmeEqManageTaskDocLine::getResult).collect(Collectors.toList());
        ArrayList<Boolean> list = new ArrayList<>();
        Boolean notEmpty = null;
        for (String res : resList) {
            notEmpty = ObjectUtil.isNotEmpty(res);
            list.add(notEmpty);
        }
        if (!list.contains(false)) {
            this.updateHead(tenantId, dto, userId);
        }
    }

    //行表的所有的点检和保养修改完后修改头表
    public void updateHead(Long tenantId, HmeEquipmentUpadteDTO dto, Long userId) {
        //全部修改完后修改头表
        HmeEqManageTaskDoc hmeEqManageTaskDoc = new HmeEqManageTaskDoc();
        hmeEqManageTaskDoc.setTenantId(tenantId);
        hmeEqManageTaskDoc.setTaskDocId(dto.getTaskDocId());
        hmeEqManageTaskDoc.setCheckBy(userId);
        hmeEqManageTaskDoc.setWkcId(dto.getWorkcellId());
        hmeEqManageTaskDoc.setCheckDate(new Date());
        hmeEqManageTaskDoc.setDocStatus(HmeConstants.ConstantValue.COMPLETED);

        HmeEqManageTaskDocLine hmeEqManageTaskDocLine = new HmeEqManageTaskDocLine();
        hmeEqManageTaskDocLine.setTaskDocId(dto.getTaskDocId());
        hmeEqManageTaskDocLine.setTenantId(tenantId);
        //查看行表里面的result是不是都是OK，有一项不是则为NG
        List<HmeEqManageTaskDocLine> hmeEqManageTaskDocLineList = hmeEqManageTaskDocLineRepository.select(hmeEqManageTaskDocLine);
        List<String> collect = hmeEqManageTaskDocLineList.stream().map(HmeEqManageTaskDocLine::getResult).collect(Collectors.toList());
        for (String result : collect) {
            if (NG.equals(result)) {
                hmeEqManageTaskDoc.setCheckResult(NG);
                break;
            } else {
                hmeEqManageTaskDoc.setCheckResult(OK);
            }
        }
        hmeEqManageTaskDocMapper.updateByPrimaryKeySelective(hmeEqManageTaskDoc);
    }

    //判断时间是否为同一天
    private static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

}
