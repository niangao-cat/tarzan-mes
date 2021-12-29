package tarzan.method.app.service;

import java.util.List;

import tarzan.method.api.dto.MtBomSiteAssignDTO;
import tarzan.method.domain.vo.MtBomSiteAssignVO;

/**
 * 装配清单站点分配应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSiteAssignService {

    List<MtBomSiteAssignVO> listBomSiteAssignUi(Long tenantId, String bomId);

    String saveBomSiteAssignForUi(Long tenantId, MtBomSiteAssignDTO dto);
    
}
