package tarzan.iface.app.service.impl;

import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.domain.entity.ItfStandardOperationIface;
import com.ruike.itf.infra.mapper.ItfStandardOperationIfaceMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tarzan.iface.app.service.MtStandardOperationIfaceService;
import tarzan.iface.domain.entity.MtStandardOperationIface;
import tarzan.iface.domain.repository.MtStandardOperationIfaceRepository;
import tarzan.iface.infra.mapper.MtStandardOperationIfaceMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 标准工序接口表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
@Slf4j
@Service
public class MtStandardOperationIfaceServiceImpl implements MtStandardOperationIfaceService {

    @Autowired
    private MtStandardOperationIfaceMapper mtStandardOperationIfaceMapper;

    @Autowired
    private ItfStandardOperationIfaceMapper itfStandardOperationIfaceMapper;

    @Autowired
    private MtStandardOperationIfaceRepository mtStandardOperationIfaceRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    @Override
    public List<MtStandardOperationIface> invoke(List<ItfSapIfaceDTO> dto) {
        Long batchId = Long.valueOf(this.customDbRepository.getNextKey("mt_standard_operation_iface_cid_s"));
        Date createDate = new Date();

        List<MtStandardOperationIface> ifaces = new ArrayList<>();
        List<MtStandardOperationIface> errorList = new ArrayList<>();
        for (ItfSapIfaceDTO dto1 : dto) {
            ItfStandardOperationIface itfStandardOperationIface = new ItfStandardOperationIface();
            MtStandardOperationIface mtStandardOperationIface = new MtStandardOperationIface();
            mtStandardOperationIface.setTenantId(tenantId);
            mtStandardOperationIface.setBatchId(Double.valueOf(batchId));
            mtStandardOperationIface.setPlantCode("1000");
            mtStandardOperationIface.setOperationCode(Strings.isEmpty(dto1.getVLSCH()) ? "" : dto1.getVLSCH());
            mtStandardOperationIface.setOperationDescription(Strings.isEmpty(dto1.getTXT()) ? null : dto1.getTXT());
            mtStandardOperationIface.setErpCreatedBy(-1l);
            mtStandardOperationIface.setErpCreationDate(createDate);
            mtStandardOperationIface.setErpLastUpdatedBy(-1l);
            mtStandardOperationIface.setErpLastUpdateDate(createDate);
            mtStandardOperationIface.setObjectVersionNumber(-1l);
            mtStandardOperationIface.setCreatedBy(-1l);
            mtStandardOperationIface.setCreationDate(createDate);
            mtStandardOperationIface.setLastUpdatedBy(-1l);
            mtStandardOperationIface.setLastUpdateDate(createDate);
            mtStandardOperationIface.setStatus("N");
            mtStandardOperationIface.setCid(batchId);
            mtStandardOperationIface.setIfaceId(this.customDbRepository.getNextKey("mt_standard_operation_iface_s"));
            if (Strings.isEmpty(dto1.getVLSCH()) || Strings.isEmpty(dto1.getTXT())) {
                mtStandardOperationIface.setOperationCode(dto1.getVLSCH());
                mtStandardOperationIface.setMessage("工艺编码，工艺描述不允许为空，请核查！");
                errorList.add(mtStandardOperationIface);
                ifaces.add(mtStandardOperationIface);
                BeanUtils.copyProperties(mtStandardOperationIface, itfStandardOperationIface);
                itfStandardOperationIfaceMapper.insertSelective(itfStandardOperationIface);
                continue;
            }
            ifaces.add(mtStandardOperationIface);
            BeanUtils.copyProperties(mtStandardOperationIface, itfStandardOperationIface);
            itfStandardOperationIfaceMapper.insertSelective(itfStandardOperationIface);
            mtStandardOperationIfaceMapper.insertSelective(mtStandardOperationIface);
        }
        // 调用API
        mtStandardOperationIfaceRepository.standardOperationInterfaceImport(tenantId);
        return errorList;
    }
}
