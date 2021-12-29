package tarzan.method.app.service.impl;

import io.choerodon.mybatis.pagehelper.PageHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import io.tarzan.common.domain.vo.MtGenTypeVO2;

import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.method.api.dto.MtBomDTO;
import tarzan.method.api.dto.MtBomDTO1;
import tarzan.method.api.dto.MtBomDTO5;
import tarzan.method.api.dto.MtBomDTO6;
import tarzan.method.app.service.MtBomService;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.vo.MtBomComponentVO14;
import tarzan.method.domain.vo.MtBomVO2;
import tarzan.method.domain.vo.MtBomVO9;
import tarzan.method.infra.mapper.MtBomMapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 装配清单头应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Service
public class MtBomServiceImpl implements MtBomService {

    @Autowired
    private MtBomMapper mtBomMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;

    @Override
    public Page<MtBomDTO> listUi(Long tenantId, PageRequest pageRequest, MtBomDTO1 dto) {
        // 查询基表
        MtBom param = new MtBom();
        BeanUtils.copyProperties(dto, param);
        param.setTenantId(tenantId);

        Criteria criteria = new Criteria(param);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtBom.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtBom.FIELD_BOM_NAME, Comparison.LIKE));
        whereFields.add(new WhereField(MtBom.FIELD_REVISION, Comparison.LIKE));
        whereFields.add(new WhereField(MtBom.FIELD_DESCRIPTION, Comparison.LIKE));
        whereFields.add(new WhereField(MtBom.FIELD_BOM_TYPE, Comparison.EQUAL));
        whereFields.add(new WhereField(MtBom.FIELD_BOM_STATUS, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        Page<MtBom> base = PageHelper.doPageAndSort(pageRequest, () -> {
            return mtBomMapper.selectOptional(param, criteria);
        });

        // 查询类型
        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setModule("BOM");
        queryType.setTypeGroup("BOM_TYPE");
        List<MtGenType> types = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);

        // 查询状态
        MtGenStatusVO2 queryStatus = new MtGenStatusVO2();
        queryStatus.setModule("BOM");
        queryStatus.setStatusGroup("BOM_STATUS");
        List<MtGenStatus> status = mtGenStatusRepository.groupLimitStatusQuery(tenantId, queryStatus);

        // 拼接结果
        Page<MtBomDTO> result = new Page<MtBomDTO>();
        result.setTotalPages(base.getTotalPages());
        result.setTotalElements(base.getTotalElements());
        result.setNumberOfElements(base.getNumberOfElements());
        result.setSize(base.getSize());
        result.setNumber(base.getNumber());
        List<MtBomDTO> voList = new ArrayList<MtBomDTO>();
        for (MtBom ever : base) {
            MtBomDTO vo = new MtBomDTO();
            BeanUtils.copyProperties(ever, vo);
            Optional<MtGenType> typeOp =
                            types.stream().filter(t -> t.getTypeCode().equals(ever.getBomType())).findFirst();
            typeOp.ifPresent(mtGenType -> vo.setBomTypeDesc(mtGenType.getDescription()));
            Optional<MtGenStatus> statusOp =
                            status.stream().filter(t -> t.getStatusCode().equals(ever.getBomStatus())).findFirst();
            statusOp.ifPresent(mtGenStatus -> vo.setBomStatusDesc(mtGenStatus.getDescription()));
            voList.add(vo);
        }
        result.setContent(voList);
        return result;
    }

    @Override
    public MtBomVO9 bomRecordForUi(Long tenantId, String bomId) {
        if (StringUtils.isEmpty(bomId)) {
            return null;
        }

        MtBomVO9 vo = this.mtBomMapper.selectBomAndCompById(tenantId, bomId);
        if (null == vo) {
            return null;
        }
        //组件行 lineNumber进行排序
        if(CollectionUtils.isNotEmpty(vo.getComponentList())){
            vo.setComponentList(vo.getComponentList().stream().sorted(Comparator.comparing(MtBomComponentVO14::getLineNumber)).collect(Collectors.toList()));
        }

        List<MtGenType> bomTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "BOM", "BOM_TYPE");
        List<MtGenStatus> bomStatuz = this.mtGenStatusRepository.getGenStatuz(tenantId, "BOM", "BOM_STATUS");

        if (StringUtils.isNotEmpty(vo.getBomType())) {
            Optional<MtGenType> bomType =
                            bomTypes.stream().filter(t -> t.getTypeCode().equals(vo.getBomType())).findFirst();
            if (bomType.isPresent()) {
                vo.setBomTypeDesc(bomType.get().getDescription());
            }
        }

        if (StringUtils.isNotEmpty(vo.getBomStatus())) {
            Optional<MtGenStatus> bomStatus =
                            bomStatuz.stream().filter(t -> t.getStatusCode().equals(vo.getBomStatus())).findFirst();
            if (bomStatus.isPresent()) {
                vo.setBomStatusDesc(bomStatus.get().getDescription());
            }
        }

        List<MtBomComponentVO14> bomComponents = vo.getComponentList();

        if (CollectionUtils.isNotEmpty(bomComponents)) {
            //剔除sql查询bomComponentId为空的数据
            bomComponents.removeIf(bc -> StringUtils.isEmpty(bc.getBomComponentId()));
            if (CollectionUtils.isNotEmpty(bomComponents)) {
                List<MtGenType> bomComponentTypes =
                                this.mtGenTypeRepository.getGenTypes(tenantId, "BOM", "BOM_COMPONENT_TYPE");
                bomComponents.removeIf(bc -> StringUtils.isEmpty(bc.getBomComponentId()));

                List<String> materialIds = bomComponents.stream().map(MtBomComponentVO14::getMaterialId)
                                .collect(Collectors.toList());
                List<MtMaterialVO> materials =
                                this.mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);

                // 查询物料单位信息
                List<String> uomIds =
                                materials.stream().map(MtMaterialVO::getPrimaryUomId).collect(Collectors.toList());
                List<MtUomVO> uoms = CollectionUtils.isNotEmpty(uomIds)
                                ? this.mtUomRepository.uomPropertyBatchGet(tenantId, uomIds)
                                : new ArrayList<>();

                for (MtBomComponentVO14 bomComponent : bomComponents) {
                    Optional<MtGenType> bomComponentType = bomComponentTypes.stream()
                                    .filter(t -> t.getTypeCode().equals(bomComponent.getBomComponentType()))
                                    .findFirst();
                    if (bomComponentType.isPresent()) {
                        bomComponent.setBomComponentTypeDesc(bomComponentType.get().getDescription());
                    }

                    Optional<MtMaterialVO> material = materials.stream()
                                    .filter(t -> t.getMaterialId().equals(bomComponent.getMaterialId())).findFirst();
                    if (material.isPresent()) {
                        bomComponent.setMaterialCode(material.get().getMaterialCode());
                        bomComponent.setMaterialName(material.get().getMaterialName());

                        Optional<MtUomVO> uom = uoms.stream()
                                        .filter(t -> t.getUomId().equals(material.get().getPrimaryUomId())).findFirst();
                        if (uom.isPresent()) {
                            bomComponent.setUomName(uom.get().getUomName());
                        }
                    }

                    //2020/10/7 add by sanfeng.zhang for wangcan 增加组件版本
                    MtExtendSettings lineAttribute7Attr = new MtExtendSettings();
                    lineAttribute7Attr.setAttrName("lineAttribute7");
                    List<MtExtendAttrVO> attrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponent.getBomComponentId(), Collections.singletonList(lineAttribute7Attr));
                    if(CollectionUtils.isNotEmpty(attrVOList)){
                        bomComponent.setBomVersion(attrVOList.get(0).getAttrValue());
                    }
                }
            }
        }

        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String bomCopyForUi(Long tenantId, MtBomDTO6 dto) {
        // 将接收对象转换为MtbomVo2对象，重新赋值
        MtBomVO2 vo = new MtBomVO2();
        vo.setBomId(dto.getBomId());
        vo.setBomName(dto.getBomNameNew());
        vo.setRevision(dto.getRevisionNew());
        vo.setBomType(dto.getBomTypeNew());
        return this.mtBomRepository.bomCopy(tenantId, vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveBomForUi(Long tenantId, MtBomDTO5 dto) {
        MtBom mtBom = new MtBom();
        BeanUtils.copyProperties(dto, mtBom);
        return this.mtBomRepository.bomPropertyUpdate(tenantId, mtBom,"Y");
    }

}
