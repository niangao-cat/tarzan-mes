package tarzan.method.app.service;

import java.util.List;

import tarzan.method.api.dto.MtBomReferencePointDTO;

/**
 * 装配清单行参考点关系应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomReferencePointService {


    List<MtBomReferencePointDTO> listbomReferencePointUi(Long tenantId, String bomComponentId);

    String saveReferencePointForUi(Long tenantId, MtBomReferencePointDTO dto);
    
}
