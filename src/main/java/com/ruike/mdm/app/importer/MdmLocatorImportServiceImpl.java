package com.ruike.mdm.app.importer;

import com.ruike.mdm.api.dto.MdmLocatorImportDTO;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.core.redis.RedisHelper;
import org.springframework.beans.factory.annotation.Value;
import tarzan.modeling.api.dto.MtModLocatorDTO5;
import tarzan.modeling.app.service.MtModLocatorService;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存货位导入
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/3 14:26
 */
@ImportService(templateCode = "MDM.LOCATOR")
public class MdmLocatorImportServiceImpl implements IBatchImportService {
    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    private final MtModLocatorService mtModLocatorService;
    private final MtModLocatorRepository mtModLocatorRepository;
    private final RedisHelper redisHelper;

    public MdmLocatorImportServiceImpl(MtModLocatorService mtModLocatorService, MtModLocatorRepository mtModLocatorRepository, RedisHelper redisHelper) {
        this.mtModLocatorService = mtModLocatorService;
        this.mtModLocatorRepository = mtModLocatorRepository;
        this.redisHelper = redisHelper;
    }

    @Override
    public Boolean doImport(List<String> data) {
        for (String json : data) {
            MdmLocatorImportDTO locatorDTO = redisHelper.fromJson(json, MdmLocatorImportDTO.class);
            MtModLocator locator2 = new MtModLocator();
            locator2.setLocatorCode(locatorDTO.getLocatorCode());

            MtModLocator locator = new MtModLocator();
            locator.setLocatorCode(locatorDTO.getWarehouseCode());
            MtModLocatorDTO5 dto = new MtModLocatorDTO5();
            MtModLocator locator1 = mtModLocatorRepository.selectOne(locator);
            dto.setParentLocatorId(locator1.getLocatorId());
            dto.setLocatorCode(locatorDTO.getLocatorCode());
            dto.setLocatorName(locatorDTO.getLocatorNameZh());
            dto.setLocatorCategory("INVENTORY");
            dto.setLocatorType(locatorDTO.getLocatorType());
            Map<String, Map<String, String>> tl = new HashMap<>(10);
            Map<String, String> map = new HashMap<>(10);
            map.put("zh_CN", locatorDTO.getLocatorNameZh());
            map.put("en_GB", locatorDTO.getLocatorNameUs());
            tl.put("LOCATOR_NAME", map);
            dto.set_tls(tl);
            dto.setEnableFlag("Y");
            dto.setNegativeFlag("N");
            dto.setLocatorLocation(locatorDTO.getLocatorLocation());
            mtModLocatorService.saveModLocatorForUi(tenantId, dto);
        }

        return true;
    }

    @Override
    public int getSize() {
        return 500;
    }
}
