package com.ruike.itf.domain.service.impl;

import com.ruike.hme.domain.entity.HmeQuantityAnalyzeDoc;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.hme.domain.repository.HmeQuantityAnalyzeDocRepository;
import com.ruike.hme.domain.repository.HmeQuantityAnalyzeLineRepository;
import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4;
import com.ruike.hme.infra.mapper.HmeQuantityAnalyzeDocMapper;
import com.ruike.hme.infra.mapper.HmeQuantityAnalyzeLineMapper;
import com.ruike.itf.app.assembler.QualityAnalyzeIfaceAssembler;
import com.ruike.itf.config.QualityAnalyzeFileServerConfig;
import com.ruike.itf.domain.entity.ItfQualityAnalyzeIface;
import com.ruike.itf.domain.entity.ItfQualityAnalyzeLineIface;
import com.ruike.itf.domain.repository.ItfQualityAnalyzeIfaceRepository;
import com.ruike.itf.domain.repository.ItfQualityAnalyzeLineIfaceRepository;
import com.ruike.itf.domain.service.QualityAnalyzeIfaceDomainService;
import com.ruike.itf.domain.vo.QualityAnalyzeIfaceVO;
import com.ruike.itf.domain.vo.QualityAnalyzeLineIfaceVO;
import com.ruike.itf.infra.mapper.ItfQualityAnalyzeIfaceMapper;
import com.ruike.itf.infra.util.SftpUtils;
import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.domain.repository.WmsPoDeliveryRelRepository;
import com.ruike.wms.infra.util.FileUtils;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.itf.domain.vo.QualityAnalyzeIfaceVO.TYPE_D;
import static com.ruike.itf.domain.vo.QualityAnalyzeIfaceVO.TYPE_G;
import static com.ruike.itf.infra.constant.ItfConstant.AnalyzeTypeG.*;
import static com.ruike.itf.infra.constant.ItfConstant.LovCode.ITF_QUALITY_ANALYZE_D_MAP;
import static com.ruike.itf.infra.constant.ItfConstant.LovCode.ITF_QUALITY_ANALYZE_G_MAP;
import static com.ruike.itf.infra.constant.ItfConstant.ProcessStatus.*;

/**
 * <p>
 * 质量文件分析接口领域服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/6 10:19
 */
@Service
@Slf4j
public class QualityAnalyzeIfaceDomainServiceImpl implements QualityAnalyzeIfaceDomainService {
    private final ItfQualityAnalyzeIfaceRepository ifaceRepository;
    private final ItfQualityAnalyzeIfaceMapper ifaceMapper;
    private final ItfQualityAnalyzeLineIfaceRepository lineIfaceRepository;
    private final WmsPoDeliveryRelRepository poDeliveryRelRepository;
    private final QualityAnalyzeFileServerConfig qualityAnalyzeFileServerConfig;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final HmeQuantityAnalyzeDocRepository quantityAnalyzeDocRepository;
    private final HmeQuantityAnalyzeDocMapper hmeQuantityAnalyzeDocMapper;
    private final MtMaterialRepository materialRepository;
    private final HmeQuantityAnalyzeLineRepository quantityAnalyzeLineRepository;
    private final HmeQuantityAnalyzeLineMapper quantityAnalyzeLineMapper;
    private final QualityAnalyzeIfaceAssembler assembler;
    private final LovAdapter lovAdapter;

    public QualityAnalyzeIfaceDomainServiceImpl(ItfQualityAnalyzeIfaceRepository ifaceRepository, ItfQualityAnalyzeLineIfaceRepository lineIfaceRepository, WmsPoDeliveryRelRepository poDeliveryRelRepository, QualityAnalyzeFileServerConfig qualityAnalyzeFileServerConfig, MtMaterialLotRepository mtMaterialLotRepository, HmeQuantityAnalyzeDocRepository quantityAnalyzeDocRepository, MtMaterialRepository materialRepository, HmeQuantityAnalyzeLineRepository quantityAnalyzeLineRepository, QualityAnalyzeIfaceAssembler assembler, LovAdapter lovAdapter,HmeQuantityAnalyzeDocMapper hmeQuantityAnalyzeDocMapper,ItfQualityAnalyzeIfaceMapper ifaceMapper,HmeQuantityAnalyzeLineMapper quantityAnalyzeLineMapper) {
        this.ifaceRepository = ifaceRepository;
        this.lineIfaceRepository = lineIfaceRepository;
        this.poDeliveryRelRepository = poDeliveryRelRepository;
        this.qualityAnalyzeFileServerConfig = qualityAnalyzeFileServerConfig;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.quantityAnalyzeDocRepository = quantityAnalyzeDocRepository;
        this.materialRepository = materialRepository;
        this.quantityAnalyzeLineRepository = quantityAnalyzeLineRepository;
        this.assembler = assembler;
        this.lovAdapter = lovAdapter;
        this.hmeQuantityAnalyzeDocMapper = hmeQuantityAnalyzeDocMapper;
        this.ifaceMapper = ifaceMapper;
        this.quantityAnalyzeLineMapper = quantityAnalyzeLineMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void invoke(Long tenantId) {
        // 收集未解析的质量文件地址，有则继续处理，无则直接返回
        Set<String> paths = getFilePaths(tenantId);
        if (CollectionUtils.isEmpty(paths)) {
            return;
        }

        // 所有的文件行内容解析到接口对象中
        List<QualityAnalyzeIfaceVO> list = parsePaths(paths);

        String exceptionMsg = Strings.EMPTY;
        try {
            // 执行校验
            validation(list);

            // 校验成功的行插入业务表
            doImport(tenantId, list);
        }catch (Exception e){
            exceptionMsg = e.getMessage() + "【invoke】";
        }finally {
            // 回写接口结果
            if(StringUtils.isNotBlank(exceptionMsg)){
                for (QualityAnalyzeIfaceVO iface : list
                ) {
                    List<QualityAnalyzeLineIfaceVO> lineList = iface.getLineList();
                    for (QualityAnalyzeLineIfaceVO line : lineList
                         ) {
                        line.addErrorMessage(exceptionMsg);
                    }
                    iface.setErrorMessage(exceptionMsg);
                }
            }

            insertInterface(tenantId, list);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void doImport(Long tenantId, List<QualityAnalyzeIfaceVO> list) {
        Map<String, String> elcFieldsMap = lovAdapter.queryLovValue(ITF_QUALITY_ANALYZE_D_MAP, tenantId).stream().collect(Collectors.toMap(LovValueDTO::getValue, LovValueDTO::getMeaning, (a, b) -> a));
        Map<String, String> opticsFieldsMap = lovAdapter.queryLovValue(ITF_QUALITY_ANALYZE_G_MAP, tenantId).stream().collect(Collectors.toMap(LovValueDTO::getValue, LovValueDTO::getMeaning, (a, b) -> a));

        //查询所有material_lot_id
        List<String> materialLotIdList = new ArrayList<>();
        for (QualityAnalyzeIfaceVO rec : list
             ) {
            if (!ERROR.equals(rec.getProcessStatus())) {
                if(CollectionUtils.isNotEmpty(rec.getLineList())){
                    List<String> subMaterialLotIdList = rec.getLineList().stream()
                            .filter(Objects::nonNull)
                            .map(QualityAnalyzeLineIfaceVO::getMaterialLotId)
                            .distinct()
                            .collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(subMaterialLotIdList)) {
                        materialLotIdList.addAll(subMaterialLotIdList);
                    }
                }
            }
        }

        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            materialLotIdList = materialLotIdList.stream()
                    .distinct()
                    .collect(Collectors.toList());
        }

        Map<String, HmeQuantityAnalyzeDoc> quantityAnalyzeDocMap = new HashMap<>();
        Map<String, List<HmeQuantityAnalyzeLine>> quantityAnalyzeLineMap = new HashMap<>();

        if(CollectionUtils.isNotEmpty(materialLotIdList)) {
            //查询条码是否已存在
            List<HmeQuantityAnalyzeDoc> quantityAnalyzeDocList = quantityAnalyzeDocRepository.selectByCondition(Condition.builder(HmeQuantityAnalyzeDoc.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeQuantityAnalyzeDoc.FIELD_TENANT_ID, tenantId)
                            .andIn(HmeQuantityAnalyzeDoc.FIELD_MATERIAL_LOT_ID, materialLotIdList))
                    .build());
            if (CollectionUtils.isNotEmpty(quantityAnalyzeDocList)) {
                quantityAnalyzeDocMap = quantityAnalyzeDocList.stream().collect(Collectors.toMap(item -> item.getMaterialLotId() + "#" + item.getTenantId(), t -> t));

                //查询所有头下所有行
                List<String> qaDocIdList = quantityAnalyzeDocList.stream()
                        .map(HmeQuantityAnalyzeDoc::getQaDocId)
                        .collect(Collectors.toList());

                List<HmeQuantityAnalyzeLine> quantityAnalyzeLineList = quantityAnalyzeLineRepository.selectByCondition(Condition.builder(HmeQuantityAnalyzeLine.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeQuantityAnalyzeLine.FIELD_TENANT_ID, tenantId)
                                .andIn(HmeQuantityAnalyzeLine.FIELD_QA_DOC_ID, qaDocIdList))
                        .build());
                if (CollectionUtils.isNotEmpty(quantityAnalyzeLineList)) {
                    quantityAnalyzeLineMap = quantityAnalyzeLineList.stream().collect(Collectors.groupingBy(e -> e.getQaDocId() + "#" + e.getTenantId()));
                }
            }
        }

        for (QualityAnalyzeIfaceVO rec : list
             ) {
            if (!ERROR.equals(rec.getProcessStatus())) {

                List<QualityAnalyzeLineIfaceVO> lineList = rec.getLineList();
                for (QualityAnalyzeLineIfaceVO line : lineList
                     ) {
                    HmeQuantityAnalyzeDoc doc = assembler.lineIfaceToDoc(tenantId, line);
                    String exceptionMsg = Strings.EMPTY;
                    try {

                        //V20210629 modify by penglin.sui for hui.ma 判断material_lot_id+tenant_id是否存在，存在则更新hme_quantity_analyze_doc表的qa_type字段以及material_id字段
                        //行表hme_quantity_analyze_line则进行全删全增
                        HmeQuantityAnalyzeDoc hmeQuantityAnalyzeDoc = quantityAnalyzeDocMap.getOrDefault(doc.getMaterialLotId() +  "#" + doc.getTenantId() , null);
                        if(Objects.nonNull(hmeQuantityAnalyzeDoc)){
                            //更新头表
                            doc.setQaDocId(hmeQuantityAnalyzeDoc.getQaDocId());
                            hmeQuantityAnalyzeDocMapper.updateByPrimaryKeySelective(doc);

                            //批量删除行表
                            List<HmeQuantityAnalyzeLine> deleteQuantityAnalyzeLineList = quantityAnalyzeLineMap.getOrDefault(hmeQuantityAnalyzeDoc.getQaDocId() + "#" + doc.getTenantId() , new ArrayList<>());
                            if(CollectionUtils.isNotEmpty(deleteQuantityAnalyzeLineList)){
                                List<String> qaLineIdList = deleteQuantityAnalyzeLineList.stream()
                                        .map(HmeQuantityAnalyzeLine::getQaLineId)
                                        .collect(Collectors.toList());
                                quantityAnalyzeLineMapper.batchDelete(qaLineIdList);
                            }
                        }else {
                            quantityAnalyzeDocRepository.insertSelective(doc);

                            //将当前头添加到map中，防止一次解析相同的文件
                            quantityAnalyzeDocMap.put(doc.getMaterialLotId() +  "#" + doc.getTenantId() , doc);
                        }

                        List<HmeQuantityAnalyzeLine> lines = assembler.lineIfaceToDocLine(tenantId, doc.getQaDocId(), line, TYPE_D.equals(doc.getQaType()) ? elcFieldsMap : opticsFieldsMap);

                        //批量插入
                        quantityAnalyzeLineRepository.batchInsertAnalyzeLine(lines);

                        //将当前所有行添加到map中，防止一次解析相同的文件
                        quantityAnalyzeLineMap.put(doc.getQaDocId() + "#" + doc.getTenantId() , lines);

                        line.setProcessStatus(SUCCESS);
                    }catch (Exception e){
                        exceptionMsg = e.getMessage() + "【doImport】";
                    }finally {
                        if(StringUtils.isNotBlank(exceptionMsg)) {
                            line.addErrorMessage(exceptionMsg);
                        }
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void insertInterface(Long tenantId, List<QualityAnalyzeIfaceVO> list) {
        // 根据行处理状态修改
        List<String> filePathList = new ArrayList<>();
        list.forEach(iface -> {
            // 若行全部校验失败，则头标记为失败
            List<QualityAnalyzeLineIfaceVO> lineList = iface.getLineList();
            List<QualityAnalyzeLineIfaceVO> successLineList = lineList.stream().filter(rec -> SUCCESS.equals(rec.getProcessStatus())).collect(Collectors.toList());
            List<QualityAnalyzeLineIfaceVO> failedLineList = lineList.stream().filter(rec -> ERROR.equals(rec.getProcessStatus())).collect(Collectors.toList());
            if (failedLineList.size() == lineList.size()) {
                iface.setProcessStatus(ERROR);
            } else if (successLineList.size() == lineList.size()) {
                iface.setProcessStatus(SUCCESS);
                //记录本次处理成功的数据
                filePathList.add(iface.getFilePath());
            } else {
                iface.setProcessStatus(PART_SUCCESS);
            }
        });

        //查询所有成功的文件路径数据
        Map<String , List<ItfQualityAnalyzeIface>> quantityAnalyzeIfaceMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(filePathList)){
            List<ItfQualityAnalyzeIface> quantityAnalyzeIfaceList = ifaceRepository.selectByCondition(Condition.builder(ItfQualityAnalyzeIface.class)
                    .andWhere(Sqls.custom().andEqualTo(ItfQualityAnalyzeIface.FIELD_TENANT_ID, tenantId)
                            .andIn(ItfQualityAnalyzeIface.FIELD_FILE_PATH, filePathList)
                            .andNotEqualTo(ItfQualityAnalyzeIface.FIELD_PROCESS_STATUS , SUCCESS))
                    .build());
            if(CollectionUtils.isNotEmpty(quantityAnalyzeIfaceList)){
                quantityAnalyzeIfaceMap = quantityAnalyzeIfaceList.stream().collect(Collectors.groupingBy(e -> e.getFilePath() + "#" + e.getTenantId()));
            }
        }

        //记录成功的filepath的主键ID
        List<ItfQualityAnalyzeIface> qualityAnalyzeIfaceList = new ArrayList<>();
        for (QualityAnalyzeIfaceVO iface : list
             ) {
            if(SUCCESS.equals(iface.getProcessStatus())){
                List<ItfQualityAnalyzeIface> quantityAnalyzeIfaceList = quantityAnalyzeIfaceMap.getOrDefault(iface.getFilePath() + "#" + tenantId , new ArrayList<>());
                for (ItfQualityAnalyzeIface qualityAnalyzeIface : quantityAnalyzeIfaceList
                         ) {
                    ItfQualityAnalyzeIface itfQualityAnalyzeIface = new ItfQualityAnalyzeIface();
                    itfQualityAnalyzeIface.setInterfaceId(qualityAnalyzeIface.getInterfaceId());
                    itfQualityAnalyzeIface.setProcessStatus(SUCCESS);
                    qualityAnalyzeIfaceList.add(itfQualityAnalyzeIface);
                }
            }
            List<QualityAnalyzeLineIfaceVO> lineList = iface.getLineList();
            ItfQualityAnalyzeIface ifaceEntity = assembler.ifaceToEntity(tenantId, iface);
            ifaceRepository.insertSelective(ifaceEntity);
            List<ItfQualityAnalyzeLineIface> qualityAnalyzeLineIfaceList = new ArrayList<>();
            for (QualityAnalyzeLineIfaceVO line : lineList
                 ) {
                qualityAnalyzeLineIfaceList.add(assembler.lineIfaceToEntity(tenantId, ifaceEntity.getInterfaceId(), line));
            }
            if(CollectionUtils.isNotEmpty(qualityAnalyzeLineIfaceList)){
                lineIfaceRepository.batchInsertAnalyzeLineIface(qualityAnalyzeLineIfaceList);
            }
        }

        //V20210629 modify by penglin.sui for hui.ma file_path某一次成功后，根据file_path在表itf_quality_analyze_iface中查找值，然后将process_status字段值为E的更新为S
        if(CollectionUtils.isNotEmpty(qualityAnalyzeIfaceList)) {
            //获取用户
            Long userId = -1L;
            if (!Objects.isNull(DetailsHelper.getUserDetails())
                    && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
                userId = DetailsHelper.getUserDetails().getUserId();
            }
            ifaceMapper.batchUpdateProcess(userId, qualityAnalyzeIfaceList);
        }
    }

    private Set<String> getFilePaths(Long tenantId) {
        List<WmsPoDeliveryRel> relList = poDeliveryRelRepository.selectByCondition(Condition.builder(WmsPoDeliveryRel.class).andWhere(Sqls.custom().andEqualTo(WmsPoDeliveryRel.FIELD_TENANT_ID, tenantId).andIsNotNull(WmsPoDeliveryRel.FIELD_ATTRIBUTE1).andNotEqualTo(WmsPoDeliveryRel.FIELD_ATTRIBUTE1, "")).build());
        Set<String> paths = relList.stream().map(WmsPoDeliveryRel::getAttribute1).collect(Collectors.toSet());
        List<ItfQualityAnalyzeIface> ifaceList = ifaceRepository.selectByCondition(Condition.builder(ItfQualityAnalyzeIface.class).andWhere(Sqls.custom().andEqualTo(ItfQualityAnalyzeIface.FIELD_TENANT_ID, tenantId).andEqualTo(ItfQualityAnalyzeIface.FIELD_PROCESS_STATUS , SUCCESS)).build());
        Set<String> resolvedPaths = ifaceList.stream().map(ItfQualityAnalyzeIface::getFilePath).collect(Collectors.toSet());
        paths.removeAll(resolvedPaths);
        return paths;
    }

    private List<QualityAnalyzeIfaceVO> parsePaths(Set<String> paths) {
        List<QualityAnalyzeIfaceVO> list = new ArrayList<>();
        // 连接服务器下载文件

        paths.forEach(filePath -> {
//            String path = StringUtils.substringBeforeLast(filePath, "/");
//            String fileName = StringUtils.substringAfterLast(filePath, "/");
            Workbook workbook;
            try {
//                workbook = SftpUtils.downloadExcel(qualityAnalyzeFileServerConfig.getHost(), qualityAnalyzeFileServerConfig.getUser(), qualityAnalyzeFileServerConfig.getPort(), qualityAnalyzeFileServerConfig.getPassword(), path, fileName);
                workbook = FileUtils.downloadExcel(filePath);
            } catch (Exception e) {
                log.error(Arrays.toString(e.getStackTrace()));
                throw new CommonException("文件下载失败！请检查文件服务器连接");
            }
            List<QualityAnalyzeLineIfaceVO> lineList = parseFile(workbook);
            if (CollectionUtils.isNotEmpty(lineList)) {
                list.add(new QualityAnalyzeIfaceVO(filePath, lineList));
            }
        });

        return list;
    }

    private List<QualityAnalyzeLineIfaceVO> parseFile(Workbook workbook) {
        List<QualityAnalyzeLineIfaceVO> lineList = new ArrayList<>();
        if (Objects.nonNull(workbook)) {
            // 获取文件中的行列数量
            Sheet sheet = workbook.getSheetAt(0);
            int columnCount = 0;
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            if (sheet.getRow(1) != null) {
                columnCount = sheet.getRow(0).getLastCellNum()
                        - sheet.getRow(0).getFirstCellNum();
            }

            if (columnCount > ItfQualityAnalyzeLineIface.MAX_COLUMN_COUNT) {
                throw new CommonException("超出可解析范围");
            }

            // 循环行解析内容
            DataFormatter df = new DataFormatter();
            if (columnCount > 0 && rowCount > 0) {
                for (int rowNum = 1; rowNum <= rowCount; rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (Objects.nonNull(row) && StringUtils.isNotBlank(Optional.ofNullable(df.formatCellValue(row.getCell(0))).orElse("").trim())) {
                        lineList.add(parseRow(row, columnCount, df));
                    }
                }
            }
        }
        return lineList;
    }

    private QualityAnalyzeLineIfaceVO parseRow(Row row, int columnCount, DataFormatter df) {
        QualityAnalyzeLineIfaceVO line = new QualityAnalyzeLineIfaceVO();
        line.setProcessDate(new Date());
        line.setProcessStatus(NEW);
        // 读取固定字段
        line.setType(df.formatCellValue(row.getCell(0)));
        Cell materialLotCodeCell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        materialLotCodeCell.setCellType(CellType.STRING);
        line.setMaterialLotCode(df.formatCellValue(materialLotCodeCell));
        Cell materialCodeCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        materialCodeCell.setCellType(CellType.STRING);
        line.setMaterialCode(df.formatCellValue(materialCodeCell));
        Cell quantityCell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        quantityCell.setCellType(CellType.STRING);
        line.setQuantity(StringUtils.isBlank(df.formatCellValue(quantityCell)) ? null : new BigDecimal(df.formatCellValue(quantityCell)));
        // 读取动态字段
        for (int col = 4; col < columnCount; col++) {
            Cell cell = row.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (Objects.nonNull(cell)) {
                cell.setCellType(CellType.STRING);
                try {
                    Field field = line.getClass().getDeclaredField(col < ItfQualityAnalyzeLineIface.ATTRIBUTE_COLUMN_COUNT ? "test" + (col - 3) : "attribute" + (col - ItfQualityAnalyzeLineIface.ATTRIBUTE_COLUMN_COUNT + 1));
                    field.setAccessible(true);
                    field.set(line, df.formatCellValue(cell));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    throw new CommonException("字段解析失败，列数为" + col);
                }
            }
        }
        return line;
    }

    private void validation(List<QualityAnalyzeIfaceVO> list) {
        list.forEach(iface -> {
            String message = "";
            message = message.concat(!StringCommonUtils.contains(iface.getType(), TYPE_G, TYPE_D) ? "导入类型错误，请检查！" : "");
            List<QualityAnalyzeLineIfaceVO> lineList = iface.getLineList();
            Set<String> types = lineList.stream().map(QualityAnalyzeLineIfaceVO::getType).collect(Collectors.toSet());
            message = message.concat(types.size() > 1 ? "该模版下存在多种导入类型，请检查！" : "");
            if (StringUtils.isNotBlank(message)) {
                iface.setErrorMessage(message);
            } else {
                lineValidation(lineList);

                //行中有ERROR，则头状态未ERROR
                if (lineList.stream().anyMatch(rec -> ERROR.equals(rec.getProcessStatus()))) {
                    iface.setErrorMessage("error:请查看明细中的报错信息");
                }
            }
        });
    }

    private void lineValidation(List<QualityAnalyzeLineIfaceVO> list) {
        // 默认校验
        defaultLineValidation(list);
        if (list.stream().noneMatch(rec -> ERROR.equals(rec.getProcessStatus()))) {
            if (StringCommonUtils.equalsIgnoreBlank(list.get(0).getType(), TYPE_G)) {
                // 光学校验
                opticsLineValidation(list);
            } else if (StringCommonUtils.equalsIgnoreBlank(list.get(0).getType(), TYPE_D)) {
                // 电学校验
                electricityLineValidation(list);
            }
        }
    }

    private void defaultLineValidation(List<QualityAnalyzeLineIfaceVO> list) {
        // 批量查询条码相关数据
        Set<String> materialLots = list.stream().map(QualityAnalyzeLineIfaceVO::getMaterialLotCode).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        Map<String, Long> materialLotCountMap = list.stream().collect(Collectors.groupingBy(QualityAnalyzeLineIfaceVO::getMaterialLotCode, Collectors.counting()));
        Map<String, MtMaterialLot> dbMaterialLotMap = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom().andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLots)).build()).stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotCode, a -> a, (a, b) -> a));
        Set<String> materialLotIds = dbMaterialLotMap.entrySet().stream().filter(entry -> materialLots.contains(entry.getKey())).map(Map.Entry::getValue).map(MtMaterialLot::getMaterialLotId).collect(Collectors.toSet());
        Set<String> docMaterialLots = quantityAnalyzeDocRepository.selectByCondition(Condition.builder(HmeQuantityAnalyzeDoc.class).andWhere(Sqls.custom().andIn(HmeQuantityAnalyzeDoc.FIELD_MATERIAL_LOT_ID, materialLotIds)).build()).stream().map(HmeQuantityAnalyzeDoc::getMaterialLotId).collect(Collectors.toSet());

        // 批量查询物料相关数据
        Set<String> materials = list.stream().map(QualityAnalyzeLineIfaceVO::getMaterialCode).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        Map<String, MtMaterial> dbMaterialMap = materialRepository.selectByCondition(Condition.builder(MtMaterial.class).andWhere(Sqls.custom().andIn(MtMaterial.FIELD_MATERIAL_CODE, materials)).build()).stream().collect(Collectors.toMap(MtMaterial::getMaterialCode, a -> a, (a, b) -> a));
        log.info("<=========QualityAnalyzeLineIfaceVO-list.size==========>:" + list.size());
        list.forEach(line -> {
            line.addErrorMessage(!StringCommonUtils.contains(line.getType(), TYPE_G, TYPE_D) ? "导入类型错误，请检查！" : "");
            if (StringUtils.isBlank(line.getMaterialLotCode())) {
                line.addErrorMessage("SN号未输入！");
            } else {
                if (!dbMaterialLotMap.containsKey(line.getMaterialLotCode())) {
                    line.addErrorMessage("条码不存在，请检查！");
                } else {
                    if (materialLotCountMap.containsKey(line.getMaterialLotCode()) && materialLotCountMap.get(line.getMaterialLotCode()) > 1) {
                        line.addErrorMessage("条码重复，请检查！");
                    } else {
                        if (docMaterialLots.contains(dbMaterialLotMap.get(line.getMaterialLotCode()).getMaterialLotCode())) {
                            line.addErrorMessage("条码已导入，请检查！");
                        } else {
                            line.setMaterialLotId(dbMaterialLotMap.get(line.getMaterialLotCode()).getMaterialLotId());
                        }
                    }
                }
            }
            if (StringUtils.isBlank(line.getMaterialCode())) {
                line.addErrorMessage("产品编码未输入！");
            } else {
                if (!dbMaterialMap.containsKey(line.getMaterialCode())) {
                    line.addErrorMessage("物料编码不存在，请检查！");
                } else {
                    if (dbMaterialLotMap.containsKey(line.getMaterialLotCode()) && !dbMaterialLotMap.get(line.getMaterialLotCode()).getMaterialId().equals(dbMaterialMap.get(line.getMaterialCode()).getMaterialId())) {
                        line.addErrorMessage("条码物料与产品物料不一致，请检查！");
                    } else {
                        line.setMaterialId(dbMaterialMap.get(line.getMaterialCode()).getMaterialId());
                        log.info("<=========MaterialId==========>:" + line.getMaterialId());
                    }
                }
            }
            line.addErrorMessage(Objects.isNull(line.getQuantity()) ? "数量未输入！" : "");

            log.info("<=========errorMessage==========>:" + line.getProcessMessage());
        });
    }

    private void opticsLineValidation(List<QualityAnalyzeLineIfaceVO> list) {
        list.forEach(line -> {
            line.addErrorMessage(!StringCommonUtils.contains(line.getTest1(), GS, OHQ, CYGX, QT) ? "类型未输入或不合法！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), CYGX) && StringUtils.isBlank(line.getTest2()) ? "批号(预制棒号)未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS, OHQ, CYGX) && StringUtils.isBlank(line.getTest3()) ? "纤芯NA未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS, OHQ, CYGX) && StringUtils.isBlank(line.getTest4()) ? "包层NA未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), CYGX) && StringUtils.isBlank(line.getTest5()) ? "包层泵浦吸收(db/m未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), CYGX) && StringUtils.isBlank(line.getTest6()) ? "纤芯光损耗(db/km)未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), CYGX) && StringUtils.isBlank(line.getTest7()) ? "包层光损耗(db/km)未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS, CYGX) && StringUtils.isBlank(line.getTest8()) ? "纤芯直径(mm)未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS, CYGX) && StringUtils.isBlank(line.getTest9()) ? "包层直径(mm)未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS, CYGX) && StringUtils.isBlank(line.getTest10()) ? "涂覆层直径(mm)未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS, CYGX) && StringUtils.isBlank(line.getTest11()) ? "纤芯包层同心度(mm未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), CYGX) && StringUtils.isBlank(line.getTest12()) ? "筛选测试(Kpsi)未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), CYGX) && StringUtils.isBlank(line.getTest13()) ? "斜效率未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), CYGX) && StringUtils.isBlank(line.getTest14()) ? "双向11A功率未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS) && StringUtils.isBlank(line.getTest15()) ? "光栅类型未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS) && StringUtils.isBlank(line.getTest16()) ? "中心波长未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS) && StringUtils.isBlank(line.getTest17()) ? "带宽未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS) && StringUtils.isBlank(line.getTest18()) ? "反射率未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS) && StringUtils.isBlank(line.getTest19()) ? "SL/SR未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS) && StringUtils.isBlank(line.getTest20()) ? "通泵浦光光纤温度未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS) && StringUtils.isBlank(line.getTest21()) ? "光纤类型未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), GS) && StringUtils.isBlank(line.getTest22()) ? "光纤lot号未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest23()) ? "红光损耗未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest24()) ? "纤芯损耗未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest25()) ? "耦合效率未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest26()) ? "信号纤型号未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest27()) ? "信号光纤lot号未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest28()) ? "信号纤芯直径未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest29()) ? "信号包层直径未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest30()) ? "信号光纤NA未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest31()) ? "信号芯包同心度未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest32()) ? "输出纤型号未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest33()) ? "输出光纤lot号未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest34()) ? "输出纤芯直径未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest35()) ? "输出包层直径未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest36()) ? "输出光纤NA未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest37()) ? "输出芯包同心度未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest38()) ? "单模损耗未输入！" : "");
            line.addErrorMessage(StringCommonUtils.contains(line.getTest1(), OHQ) && StringUtils.isBlank(line.getTest39()) ? "M2未输入！" : "");
        });
    }

    private void electricityLineValidation(List<QualityAnalyzeLineIfaceVO> list) {

    }
}
