package tarzan.material.app.service;


import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseService;
import tarzan.material.api.dto.MtUomDTO5;
import tarzan.material.api.dto.MtUomDTO6;
import tarzan.material.domain.entity.MtUom;

/**
 * 单位应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:10:08
 */
public interface MtUomService extends BaseService<MtUom> {

    /**
     * 查询UOM
     * 
     * @author benjamin
     * @date 2019-08-15 10:08
     * @param tenantId 租户Id
     * @param dto MtUomDTO5
     * @param pageRequest PageRequest
     * @return list
     */
    Page<MtUom> queryUomForUi(Long tenantId, MtUomDTO5 dto, PageRequest pageRequest);

    /**
     * 保存UOM
     * 
     * @author benjamin
     * @date 2019-08-15 10:13
     * @param tenantId 租户Id
     * @param dto MtUomDTO6
     * @return MtUom
     */
    MtUom saveUomForUi(Long tenantId, MtUomDTO6 dto);

}
