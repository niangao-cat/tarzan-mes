package tarzan.modeling.app.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ruike.hme.domain.entity.HmeOrganizationUnitRel;
import com.ruike.hme.domain.repository.HmeOrganizationUnitRelRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import tarzan.modeling.api.dto.MtModWorkcellDTO2;
import tarzan.modeling.api.dto.MtModWorkcellDTO3;
import tarzan.modeling.api.dto.MtModWorkcellDTO4;
import tarzan.modeling.app.service.MtModWorkcellService;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.entity.MtModWorkcellManufacturing;
import tarzan.modeling.domain.entity.MtModWorkcellSchedule;
import tarzan.modeling.domain.repository.MtModWorkcellManufacturingRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.repository.MtModWorkcellScheduleRepository;
import tarzan.modeling.domain.vo.MtModWorkcellManufacturingVO;
import tarzan.modeling.domain.vo.MtModWorkcellScheduleVO;
import tarzan.modeling.domain.vo.MtModWorkcellVO3;
import tarzan.modeling.infra.mapper.MtModWorkcellMapper;

/**
 * 工作单元应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@Service
public class MtModWorkcellServiceImpl implements MtModWorkcellService {

    private static final String MT_MOD_WORKCELL_ATTR = "mt_mod_workcell_attr";

    @Autowired
    private MtModWorkcellMapper mtModWorkcellMapper;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModWorkcellManufacturingRepository mtModWorkcellManufacturingRepository;

    @Autowired
    private MtModWorkcellScheduleRepository mtModWorkcellScheduleRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private HmeOrganizationUnitRelRepository hmeOrganizationUnitRelRepository;

    @Override
    public Page<MtModWorkcellVO3> queryForUi(Long tenantId, MtModWorkcellDTO2 dto, PageRequest pageRequest) {
        MtModWorkcell mtModWorkcell = new MtModWorkcell();
        mtModWorkcell.setWorkcellCode(dto.getWorkcellCode());
        mtModWorkcell.setWorkcellName(dto.getWorkcellName());
        mtModWorkcell.setDescription(dto.getWorkcellDesc());
        mtModWorkcell.setEnableFlag(dto.getEnableFlag());
        mtModWorkcell.setWorkcellType(dto.getWorkcellType());
        mtModWorkcell.setWorkcellLocation(dto.getWorkcellLocation());

        Page<MtModWorkcell> workcells = PageHelper.doPage(pageRequest,
                        () -> this.mtModWorkcellMapper.selectForEmptyStringForUi(tenantId, mtModWorkcell));
        List<MtGenType> workcellTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "WORKCELL_TYPE");

        List<MtModWorkcellVO3> list = new ArrayList<MtModWorkcellVO3>();
        MtModWorkcellVO3 mtModWorkcellVO3 = null;
        for (MtModWorkcell workcell : workcells) {
            mtModWorkcellVO3 = new MtModWorkcellVO3();
            BeanUtils.copyProperties(workcell, mtModWorkcellVO3);
            String workcellType = mtModWorkcellVO3.getWorkcellType();

            if (StringUtils.isNotEmpty(workcellType)) {
                Optional<MtGenType> optional =
                                workcellTypes.stream().filter(t -> t.getTypeCode().equals(workcellType)).findFirst();
                if (optional.isPresent()) {
                    mtModWorkcellVO3.setWorkcellTypeDesc(optional.get().getDescription());
                }
            }
            list.add(mtModWorkcellVO3);
        }

        Page<MtModWorkcellVO3> result = new Page<MtModWorkcellVO3>();
        result.setNumber(workcells.getNumber());
        result.setSize(workcells.getSize());
        result.setTotalElements(workcells.getTotalElements());
        result.setTotalPages(workcells.getTotalPages());
        result.setNumberOfElements(workcells.getNumberOfElements());
        result.setContent(list);

        return result;
    }

    @Override
    public MtModWorkcellDTO3 queryInfoForUi(Long tenantId, String workcellId) {
        if (StringUtils.isEmpty(workcellId)) {
            return null;
        }

        MtModWorkcellDTO3 dto = new MtModWorkcellDTO3();
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, workcellId);
        if (null == mtModWorkcell) {
            return dto;
        }

        MtModWorkcellVO3 mtModWorkcellVO3 = new MtModWorkcellVO3();
        BeanUtils.copyProperties(mtModWorkcell, mtModWorkcellVO3);
        if (null != mtModWorkcellVO3.getWorkcellType()) {
            List<MtGenType> workcellTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "WORKCELL_TYPE");
            Optional<MtGenType> workcellType = workcellTypes.stream()
                            .filter(t -> t.getTypeCode().equals(mtModWorkcellVO3.getWorkcellType())).findFirst();
            if (workcellType.isPresent()) {
                mtModWorkcellVO3.setWorkcellTypeDesc(workcellType.get().getDescription());
            }
        }
        dto.setWorkcell(mtModWorkcellVO3);

        MtModWorkcellManufacturing mtModWorkcellManufacturing =
                        mtModWorkcellManufacturingRepository.workcellManufacturingPropertyGet(tenantId, workcellId);
        if (null != mtModWorkcellManufacturing) {
            MtModWorkcellManufacturingVO mtModWorkcellManufacturingVO = new MtModWorkcellManufacturingVO();
            BeanUtils.copyProperties(mtModWorkcellManufacturing, mtModWorkcellManufacturingVO);
            dto.setWorkcellManufacturing(mtModWorkcellManufacturingVO);
        }


        MtModWorkcellSchedule mtModWorkcellSchedule =
                        mtModWorkcellScheduleRepository.workcellSchedulePropertyGet(tenantId, workcellId);
        if (null != mtModWorkcellSchedule) {
            MtModWorkcellScheduleVO mtModWorkcellScheduleVO = new MtModWorkcellScheduleVO();
            BeanUtils.copyProperties(mtModWorkcellSchedule, mtModWorkcellScheduleVO);
            if (null != mtModWorkcellScheduleVO.getRateType()) {
                List<MtGenType> rateTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "RATE_TYPE");
                Optional<MtGenType> rateType = rateTypes.stream()
                                .filter(t -> t.getTypeCode().equals(mtModWorkcellScheduleVO.getRateType())).findFirst();
                if (rateType.isPresent()) {
                    mtModWorkcellScheduleVO.setRateTypeDesc(rateType.get().getDescription());
                }
            }
            dto.setWorkcellSchedule(mtModWorkcellScheduleVO);
        }

        List<MtExtendAttrDTO> workcellAttrs =
                        mtExtendSettingsService.attrQuery(tenantId, workcellId, MT_MOD_WORKCELL_ATTR);
        dto.setWorkcellAttrs(workcellAttrs);

        //2020-07-28 add by sanfeng.zhang 组织职能关系
        dto.setOrganizationUnit(hmeOrganizationUnitRelRepository.queryOrganizationUnitRel(tenantId,workcellId));
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveForUi(Long tenantId, MtModWorkcellDTO4 dto) {
        if (null == dto || null == dto.getWorkcell()) {
            return null;
        }

        MtModWorkcell mtModWorkcell = new MtModWorkcell();
        BeanUtils.copyProperties(dto.getWorkcell(), mtModWorkcell);
        mtModWorkcell.setWorkcellCategory("");
        String workcellId = mtModWorkcellRepository.workcellBasicPropertyUpdate(tenantId, mtModWorkcell, "Y");
        if (CollectionUtils.isNotEmpty(dto.getWorkcellAttrs())) {
            mtExtendSettingsService.attrSave(tenantId, MT_MOD_WORKCELL_ATTR, workcellId, null, dto.getWorkcellAttrs());
        }

        if (null != dto.getWorkcellManufacturing()) {
            MtModWorkcellManufacturing mtModWorkcellManufacturing = new MtModWorkcellManufacturing();
            BeanUtils.copyProperties(dto.getWorkcellManufacturing(), mtModWorkcellManufacturing);

            mtModWorkcellManufacturing.setWorkcellId(workcellId);
            mtModWorkcellManufacturingRepository.workcellManufacturingPropertyUpdate(tenantId,
                            mtModWorkcellManufacturing, "Y");
        }

        if (null != dto.getWorkcellSchedule()) {
            MtModWorkcellSchedule mtModWorkcellSchedule = new MtModWorkcellSchedule();
            BeanUtils.copyProperties(dto.getWorkcellSchedule(), mtModWorkcellSchedule);

            mtModWorkcellSchedule.setWorkcellId(workcellId);
            mtModWorkcellScheduleRepository.workcellSchedulePropertyUpdate(tenantId, mtModWorkcellSchedule, "Y");
        }

        //2020-7-28 add by sanfeng,zhang 组织职能关系
        if(null != dto.getOrganizationUnit()){
            HmeOrganizationUnitRel unitRel = new HmeOrganizationUnitRel();
            BeanUtils.copyProperties(dto.getOrganizationUnit(), unitRel);
            hmeOrganizationUnitRelRepository.saveOrganizationUnitRel(tenantId, unitRel);
        }
        return workcellId;
    }

}
