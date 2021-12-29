package tarzan.iface.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.tarzan.common.domain.util.StringHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO;
import tarzan.iface.domain.vo.MtSitePlantReleationVO1;
import tarzan.iface.domain.vo.MtSitePlantReleationVO2;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.iface.infra.mapper.MtSitePlantReleationMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;

/**
 * ERP工厂与站点映射关系 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:40:02
 */
@Component
public class MtSitePlantReleationRepositoryImpl extends BaseRepositoryImpl<MtSitePlantReleation>
                implements MtSitePlantReleationRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtSitePlantReleationMapper mtSitePlantReleationMapper;

    @Override
    public List<MtSitePlantReleationVO1> itemMaterialSiteIdBatchQuery(Long tenantId, MtSitePlantReleationVO dto) {
        // 1.参数合规性校验
        if (CollectionUtils.isEmpty(dto.getItemCodeList())) {
            throw new MtException("MT_INTERFACE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INTERFACE_0001", "INTERFACE", "itemCodeList", "【API:itemMaterialSiteIdBatchQuery】"));


        }
        if (CollectionUtils.isEmpty(dto.getPlantCodeList())) {
            throw new MtException("MT_INTERFACE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INTERFACE_0001", "INTERFACE", "plantCode", "【API:itemMaterialSiteIdBatchQuery】"));


        }
        // 2.在表中mt_site_plant_releation中查找
        MtSitePlantReleationVO3 mtInterFaceVO3 = new MtSitePlantReleationVO3();
        mtInterFaceVO3.setPlantCodes(dto.getPlantCodeList());
        mtInterFaceVO3.setSiteType(dto.getSiteType());
        List<MtSitePlantReleation> mtSitePlantReleations =
                        mtSitePlantReleationMapper.getsiteIdList(tenantId, mtInterFaceVO3);
        if (mtSitePlantReleations == null) {
            throw new MtException("MT_INTERFACE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INTERFACE_0003", "INTERFACE", "【API:itemMaterialSiteIdBatchQuery】"));
        }
        // 将itemCodeList转为itemIdList
        String whereInValuesSql = StringHelper.getWhereInValuesSql("item.MATERIAL_CODE", dto.getItemCodeList(), 1000);
        List<MtMaterial> materialIdList = mtSitePlantReleationMapper.getMaterialIdList(tenantId, whereInValuesSql);

        if (CollectionUtils.isEmpty(materialIdList)) {
            return Collections.emptyList();
        }
        List<String> materialIds = materialIdList.stream().map(t -> t.getMaterialId()).collect(Collectors.toList());
        Map<String, String> map = materialIdList.stream()
                        .collect(Collectors.toMap(MtMaterial::getMaterialId, MtMaterial::getMaterialCode));
        List<MtSitePlantReleationVO1> result = new ArrayList<>();

        MtSitePlantReleationVO2 mtInterFaceVO2 = new MtSitePlantReleationVO2();
        mtInterFaceVO2.setMaterialIds(materialIds);
        mtInterFaceVO2.setSiteIds(mtSitePlantReleations.stream().map(t -> t.getSiteId()).collect(Collectors.toList()));
        List<MtMaterialSite> materiaSitelList =
                        mtSitePlantReleationMapper.getMaterialSitelList(tenantId, mtInterFaceVO2);
        for (MtSitePlantReleation t : mtSitePlantReleations) {
            List<MtMaterialSite> temp = materiaSitelList.stream().filter(e -> e.getSiteId().equals(t.getSiteId()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(temp)) {
                result.addAll(temp.stream()
                                .map(e -> new MtSitePlantReleationVO1(t.getPlantCode(), map.get(e.getMaterialId()),
                                                t.getSiteId(), e.getMaterialId(), e.getMaterialSiteId()))
                                .collect(Collectors.toList()));
            }
        }
        return result;
    }

    @Override
    public List<MtSitePlantReleation> getRelationByPlantAndSiteType(Long tenantId, MtSitePlantReleationVO3 dto) {
        return mtSitePlantReleationMapper.getsiteIdList(tenantId, dto);
    }
}
