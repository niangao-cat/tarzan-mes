package tarzan.modeling.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.api.dto.MtModSiteDTO;
import tarzan.modeling.api.dto.MtModSiteDTO2;
import tarzan.modeling.api.dto.MtModSiteDTO3;
import tarzan.modeling.api.dto.MtModSiteDTO4;
import tarzan.modeling.domain.vo.MtModSiteVO2;

/**
 * 站点应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModSiteService {

    Page<MtModSiteVO2> queryForUi(Long tenantId, MtModSiteDTO2 dto, PageRequest pageRequest);

    MtModSiteDTO3 queryInfoForUi(Long tenantId, String siteId);

    String saveForUi(Long tenantId, MtModSiteDTO4 dto);

    List<MtModSiteDTO> queryUserSiteForUi(Long tenantId, Long userId, String siteType);
}
