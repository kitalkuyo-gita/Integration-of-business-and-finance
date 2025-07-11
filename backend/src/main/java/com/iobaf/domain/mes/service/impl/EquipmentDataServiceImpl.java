package com.iobaf.domain.mes.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.mes.entity.EquipmentData;
import com.iobaf.domain.mes.repository.EquipmentDataRepository;
import com.iobaf.domain.mes.service.EquipmentDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备数据服务实现类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@Service
public class EquipmentDataServiceImpl implements EquipmentDataService {

    @Autowired
    private EquipmentDataRepository equipmentDataRepository;

    @Override
    public IPage<EquipmentData> getEquipmentDataPage(Page<EquipmentData> page,
                                                    Long equipmentId,
                                                    String dataType) {
        log.info("查询设备数据列表，参数：equipmentId={}, dataType={}", equipmentId, dataType);
        
        try {
            return equipmentDataRepository.selectEquipmentDataPage(page, equipmentId, dataType);
        } catch (Exception e) {
            log.error("查询设备数据列表失败", e);
            throw new RuntimeException("查询设备数据列表失败", e);
        }
    }

    @Override
    public EquipmentData getEquipmentDataById(Long dataId) {
        log.info("根据ID查询设备数据详情，dataId={}", dataId);
        
        try {
            return equipmentDataRepository.selectById(dataId);
        } catch (Exception e) {
            log.error("查询设备数据详情失败，dataId={}", dataId, e);
            throw new RuntimeException("查询设备数据详情失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createEquipmentData(EquipmentData data) {
        log.info("创建设备数据，数据信息：{}", data);
        
        try {
            // 设置创建时间
            data.setCreatedAt(LocalDateTime.now());
            
            int result = equipmentDataRepository.insert(data);
            
            if (result > 0) {
                log.info("设备数据创建成功，dataId={}", data.getId());
                return true;
            } else {
                log.error("设备数据创建失败");
                return false;
            }
        } catch (Exception e) {
            log.error("创建设备数据失败", e);
            throw new RuntimeException("创建设备数据失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEquipmentData(EquipmentData data) {
        log.info("更新设备数据信息，dataId={}", data.getId());
        
        try {
            // 设置更新时间
            data.setUpdatedAt(LocalDateTime.now());
            
            int result = equipmentDataRepository.updateById(data);
            
            if (result > 0) {
                log.info("设备数据更新成功，dataId={}", data.getId());
                return true;
            } else {
                log.error("设备数据更新失败，dataId={}", data.getId());
                return false;
            }
        } catch (Exception e) {
            log.error("更新设备数据失败，dataId={}", data.getId(), e);
            throw new RuntimeException("更新设备数据失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEquipmentData(Long dataId) {
        log.info("删除设备数据，dataId={}", dataId);
        
        try {
            int result = equipmentDataRepository.deleteById(dataId);
            
            if (result > 0) {
                log.info("设备数据删除成功，dataId={}", dataId);
                return true;
            } else {
                log.error("设备数据删除失败，dataId={}", dataId);
                return false;
            }
        } catch (Exception e) {
            log.error("删除设备数据失败，dataId={}", dataId, e);
            throw new RuntimeException("删除设备数据失败", e);
        }
    }

    @Override
    public List<EquipmentData> getEquipmentDataByEquipmentId(Long equipmentId) {
        log.info("根据设备ID查询设备数据，equipmentId={}", equipmentId);
        
        try {
            return equipmentDataRepository.selectByEquipmentId(equipmentId);
        } catch (Exception e) {
            log.error("查询设备数据失败，equipmentId={}", equipmentId, e);
            throw new RuntimeException("查询设备数据失败", e);
        }
    }

    @Override
    public List<EquipmentData> getEquipmentDataByType(String dataType) {
        log.info("根据数据类型查询设备数据，dataType={}", dataType);
        
        try {
            return equipmentDataRepository.selectByDataType(dataType);
        } catch (Exception e) {
            log.error("查询设备数据失败，dataType={}", dataType, e);
            throw new RuntimeException("查询设备数据失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchCreateEquipmentData(List<EquipmentData> dataList) {
        log.info("批量创建设备数据，数据数量={}", dataList.size());
        
        try {
            // 设置创建时间
            dataList.forEach(data -> data.setCreatedAt(LocalDateTime.now()));
            
            int result = equipmentDataRepository.batchInsert(dataList);
            log.info("批量创建设备数据成功，创建数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量创建设备数据失败", e);
            throw new RuntimeException("批量创建设备数据失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateEquipmentData(List<EquipmentData> dataList) {
        log.info("批量更新设备数据，数据数量={}", dataList.size());
        
        try {
            // 设置更新时间
            dataList.forEach(data -> data.setUpdatedAt(LocalDateTime.now()));
            
            int result = equipmentDataRepository.batchUpdate(dataList);
            log.info("批量更新设备数据成功，更新数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量更新设备数据失败", e);
            throw new RuntimeException("批量更新设备数据失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteEquipmentData(List<Long> dataIds) {
        log.info("批量删除设备数据，数据ID数量={}", dataIds.size());
        
        try {
            int result = 0;
            for (Long dataId : dataIds) {
                if (equipmentDataRepository.deleteById(dataId) > 0) {
                    result++;
                }
            }
            log.info("批量删除设备数据成功，删除数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量删除设备数据失败", e);
            throw new RuntimeException("批量删除设备数据失败", e);
        }
    }

    @Override
    public Map<String, Object> getEquipmentDataStatistics() {
        log.info("获取设备数据统计信息");
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 统计各类型数据数量
            int energyDataCount = equipmentDataRepository.countByTypeAndEquipment("ENERGY", null);
            int temperatureDataCount = equipmentDataRepository.countByTypeAndEquipment("TEMPERATURE", null);
            int pressureDataCount = equipmentDataRepository.countByTypeAndEquipment("PRESSURE", null);
            int vibrationDataCount = equipmentDataRepository.countByTypeAndEquipment("VIBRATION", null);
            
            statistics.put("energyDataCount", energyDataCount);
            statistics.put("temperatureDataCount", temperatureDataCount);
            statistics.put("pressureDataCount", pressureDataCount);
            statistics.put("vibrationDataCount", vibrationDataCount);
            statistics.put("totalDataCount", energyDataCount + temperatureDataCount + pressureDataCount + vibrationDataCount);
            
            log.info("设备数据统计信息获取完成");
            return statistics;
        } catch (Exception e) {
            log.error("获取设备数据统计信息失败", e);
            throw new RuntimeException("获取设备数据统计信息失败", e);
        }
    }
} 