package tarzan.calendar.app.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.calendar.api.dto.MtCalendarDTO;
import tarzan.calendar.api.dto.MtCalendarLovDTO;
import tarzan.calendar.app.service.MtCalendarService;
import tarzan.calendar.domain.entity.MtCalendar;
import tarzan.calendar.domain.repository.MtCalendarRepository;
import tarzan.calendar.domain.vo.MtCalendarVO3;
import tarzan.calendar.infra.mapper.MtCalendarMapper;

/**
 * 工作日历应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:30:15
 */
@Service
public class MtCalendarServiceImpl implements MtCalendarService {

    @Autowired
    private MtCalendarMapper mtCalendarMapper;

    @Autowired
    private MtCalendarRepository mtCalendarRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public Page<MtCalendarDTO> queryCalendarListForUi(Long tenantId, MtCalendarDTO dto, PageRequest pageRequest) {
        // 设置结果
        Page<MtCalendarDTO> result = new Page<MtCalendarDTO>();

        // 查询基础
        MtCalendar mtCalendar = new MtCalendar();
        mtCalendar.setCalendarId(dto.getCalendarId());
        mtCalendar.setCalendarCode(dto.getCalendarCode());
        mtCalendar.setCalendarType(dto.getCalendarType());
        mtCalendar.setDescription(dto.getDescription());
        mtCalendar.setEnableFlag(dto.getEnableFlag());
        Page<MtCalendar> mtCalendars = PageHelper.doPage(pageRequest,
                        () -> mtCalendarRepository.calendarQueryForUi(tenantId, mtCalendar));
        if (mtCalendars == null || CollectionUtils.isEmpty(mtCalendars)) {
            return result;
        }

        // 获取类型数据
        List<MtGenType> genTypes = mtGenTypeRepository.getGenTypes(tenantId, MtBaseConstants.GEN_TYPE_MODULE.CALENDAR,
                        MtBaseConstants.GEN_TYPE_GROUP.CALENDAR_TYPE);

        // 转为Map数据
        Map<String, MtGenType> mtGenTypeMap = null;
        if (CollectionUtils.isNotEmpty(genTypes)) {
            mtGenTypeMap = genTypes.stream().collect(Collectors.toMap(t -> t.getTypeCode(), t -> t));
        }

        result.setTotalPages(mtCalendars.getTotalPages());
        result.setTotalElements(mtCalendars.getTotalElements());
        result.setNumberOfElements(mtCalendars.getNumberOfElements());
        result.setSize(mtCalendars.getSize());
        result.setNumber(mtCalendars.getNumber());

        List<MtCalendarDTO> contentList = Collections.synchronizedList(new ArrayList<>(mtCalendars.size()));

        Map<String, MtGenType> finalMtGenTypeMap = mtGenTypeMap;

        // 整理结果数据
        mtCalendars.stream().forEach(calendar -> {
            MtCalendarDTO mtCalendarDTO = new MtCalendarDTO();
            mtCalendarDTO.setCalendarId(calendar.getCalendarId());
            mtCalendarDTO.setCalendarCode(calendar.getCalendarCode());
            mtCalendarDTO.setCalendarType(calendar.getCalendarType());
            mtCalendarDTO.setDescription(calendar.getDescription());
            mtCalendarDTO.setEnableFlag(calendar.getEnableFlag());

            if (MapUtils.isNotEmpty(finalMtGenTypeMap)) {
                MtGenType mtGenType = finalMtGenTypeMap.get(calendar.getCalendarType());
                if (mtGenType != null) {
                    mtCalendarDTO.setCalendarTypeDesc(mtGenType.getDescription());
                }
            }

            contentList.add(mtCalendarDTO);
        });

        result.setContent(contentList);
        return result;
    }

    @Override
    public Page<MtCalendarLovDTO> queryCalendarLovForUi(Long tenantId, MtCalendarDTO dto, PageRequest pageRequest) {
        // 设置结果
        Page<MtCalendarLovDTO> result = new Page<MtCalendarLovDTO>();

        // 查询基础
        MtCalendar mtCalendar = new MtCalendar();
        mtCalendar.setCalendarId(dto.getCalendarId());
        mtCalendar.setCalendarCode(dto.getCalendarCode());
        mtCalendar.setCalendarType(dto.getCalendarType());
        mtCalendar.setDescription(dto.getDescription());
        mtCalendar.setEnableFlag(dto.getEnableFlag());
        Page<MtCalendar> mtCalendarPage = PageHelper.doPage(pageRequest,
                        () -> mtCalendarRepository.calendarQueryForUi(tenantId, mtCalendar));
        if (mtCalendarPage == null || CollectionUtils.isEmpty(mtCalendarPage)) {
            return result;
        }

        // 获取类型数据
        List<MtGenType> genTypes = mtGenTypeRepository.getGenTypes(tenantId, MtBaseConstants.GEN_TYPE_MODULE.CALENDAR,
                        MtBaseConstants.GEN_TYPE_GROUP.CALENDAR_TYPE);

        // 转为Map数据
        Map<String, MtGenType> mtGenTypeMap = null;
        if (CollectionUtils.isNotEmpty(genTypes)) {
            mtGenTypeMap = genTypes.stream().collect(Collectors.toMap(t -> t.getTypeCode(), t -> t));
        }

        Map<String, MtGenType> finalMtGenTypeMap = mtGenTypeMap;

        // 整理结果数据
        if (CollectionUtils.isNotEmpty(mtCalendarPage)) {
            result.setTotalPages(mtCalendarPage.getTotalPages());
            result.setTotalElements(mtCalendarPage.getTotalElements());
            result.setNumberOfElements(mtCalendarPage.getNumberOfElements());
            result.setSize(mtCalendarPage.getSize());
            result.setNumber(mtCalendarPage.getNumber());

            List<MtCalendarLovDTO> contentList = Collections.synchronizedList(new ArrayList<>(mtCalendarPage.size()));
            mtCalendarPage.parallelStream().forEach(calendar -> {
                MtCalendarLovDTO mtCalendarLovDTO = new MtCalendarLovDTO();
                mtCalendarLovDTO.setCalendarId(calendar.getCalendarId());
                mtCalendarLovDTO.setCalendarCode(calendar.getCalendarCode());
                mtCalendarLovDTO.setCalendarType(calendar.getCalendarType());
                mtCalendarLovDTO.setDescription(calendar.getDescription());
                mtCalendarLovDTO.setEnableFlag(calendar.getEnableFlag());

                if (MapUtils.isNotEmpty(finalMtGenTypeMap)) {
                    MtGenType mtGenType = finalMtGenTypeMap.get(calendar.getCalendarType());
                    if (mtGenType != null) {
                        mtCalendarLovDTO.setCalendarTypeDesc(mtGenType.getDescription());
                    }
                }
                contentList.add(mtCalendarLovDTO);
            });

            result.setContent(contentList);
        }
        return result;
    }

    @Override
    public MtCalendarDTO saveCalendarForUi(Long tenantId, MtCalendarDTO dto) {
        MtCalendar mtCalendar = new MtCalendar();
        mtCalendar.setTenantId(tenantId);
        mtCalendar.setCalendarId(dto.getCalendarId());
        mtCalendar.setCalendarCode(dto.getCalendarCode());
        mtCalendar.setCalendarType(dto.getCalendarType());
        mtCalendar.setDescription(dto.getDescription());
        mtCalendar.setEnableFlag(dto.getEnableFlag());
        mtCalendar.set_tls(dto.get_tls());

        // 校验唯一性
        MtCalendar mtCalendarUnique = new MtCalendar();
        mtCalendarUnique.setTenantId(tenantId);
        mtCalendarUnique.setCalendarCode(dto.getCalendarCode());
        mtCalendarUnique = mtCalendarMapper.selectOne(mtCalendarUnique);
        if (mtCalendarUnique != null && !mtCalendarUnique.getCalendarId().equals(dto.getCalendarId())) {
            throw new MtException("MT_CALENDAR_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_CALENDAR_0016", "CALENDAR"));
        }

        // 判断新增还是更新
        if (StringUtils.isNotEmpty(dto.getCalendarId())) {
            // 更新
            mtCalendarRepository.updateByPrimaryKeySelective(mtCalendar);
        } else {
            // 新增
            mtCalendarRepository.insertSelective(mtCalendar);
        }

        // 整理结果数据
        MtCalendarDTO mtCalendarDTO = new MtCalendarDTO();
        mtCalendarDTO.setCalendarId(mtCalendar.getCalendarId());
        mtCalendarDTO.setCalendarCode(mtCalendar.getCalendarCode());
        mtCalendarDTO.setCalendarType(mtCalendar.getCalendarType());
        mtCalendarDTO.setDescription(mtCalendar.getDescription());
        mtCalendarDTO.setEnableFlag(mtCalendar.getEnableFlag());

        // 获取类型数据
        List<MtGenType> genTypes = mtGenTypeRepository.getGenTypes(tenantId, MtBaseConstants.GEN_TYPE_MODULE.CALENDAR,
                        MtBaseConstants.GEN_TYPE_GROUP.CALENDAR_TYPE);

        // 回写类型描述
        if (CollectionUtils.isNotEmpty(genTypes)) {
            Optional<MtGenType> any = genTypes.stream()
                            .filter(t -> t.getTypeCode().equals(mtCalendar.getCalendarType())).findAny();
            if (any.isPresent()) {
                mtCalendarDTO.setCalendarTypeDesc(any.get().getDescription());
            }
        }
        return mtCalendarDTO;
    }

    /**
     * UI根据calendarId查询日历信息
     *
     * @author chuang.yang
     * @date 2019/12/30
     * @param tenantId
     * @param calendarId
     * @return tarzan.calendar.api.dto.MtCalendarDTO
     */
    @Override
    public MtCalendarDTO getCalendarForUi(Long tenantId, String calendarId) {
        if (StringUtils.isEmpty(calendarId)) {
            return null;
        }

        MtCalendarVO3 mtCalendarVO3 = mtCalendarRepository.calendarGet(tenantId, calendarId);

        MtCalendarDTO result = null;
        if (mtCalendarVO3 != null) {
            // 获取类型数据
            List<MtGenType> genTypes = mtGenTypeRepository.getGenTypes(tenantId,
                            MtBaseConstants.GEN_TYPE_MODULE.CALENDAR, MtBaseConstants.GEN_TYPE_GROUP.CALENDAR_TYPE);

            result = new MtCalendarDTO();
            result.setCalendarId(mtCalendarVO3.getCalendarId());
            result.setCalendarCode(mtCalendarVO3.getCalendarCode());
            result.setCalendarType(mtCalendarVO3.getCalendarType());
            result.setDescription(mtCalendarVO3.getDescription());
            result.setEnableFlag(mtCalendarVO3.getEnableFlag());

            // 回写类型描述
            if (CollectionUtils.isNotEmpty(genTypes)) {
                Optional<MtGenType> any = genTypes.stream()
                                .filter(t -> t.getTypeCode().equals(mtCalendarVO3.getCalendarType())).findAny();
                if (any.isPresent()) {
                    result.setCalendarTypeDesc(any.get().getDescription());
                }
            }
        }

        return result;
    }
}
