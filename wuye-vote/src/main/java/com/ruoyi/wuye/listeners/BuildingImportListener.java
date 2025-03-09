package com.ruoyi.wuye.listeners;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

import com.ruoyi.wuye.domain.buildings.WuyeBuilding;
import com.ruoyi.wuye.domain.dto.BuildingImportDTO;
import com.ruoyi.wuye.service.buildings.IWuyeBuildingService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingImportListener implements ReadListener<BuildingImportDTO> {

    private final IWuyeBuildingService buildingService;
    private final Map<String, Long> communityMap = new HashMap<>();
    private final Map<String, Long> buildingMap = new HashMap<>();
    private List<WuyeBuilding> buildingList = new ArrayList<>();

    public BuildingImportListener(IWuyeBuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @Override
    public void invoke(BuildingImportDTO data, AnalysisContext context) {
        if (data == null) {
            return;
        }

        // 处理小区
        if (StringUtils.hasText(data.getCommunity())) {
            Long communityId = communityMap.get(data.getCommunity());
            if (communityId == null) {
                WuyeBuilding community = new WuyeBuilding();
                community.setName(data.getCommunity());
                community.setAddress(data.getAddress());
                community.setLevel(1L);
                community.setPid(0L);
                buildingService.insertWuyeBuilding(community);
                communityId = community.getId();
                communityMap.put(data.getCommunity(), communityId);
            }

            // 处理楼栋
            if (StringUtils.hasText(data.getBuilding())) {
                String buildingKey = data.getCommunity() + "-" + data.getBuilding();
                Long buildingId = buildingMap.get(buildingKey);
                if (buildingId == null) {
                    WuyeBuilding building = new WuyeBuilding();
                    building.setName(data.getBuilding());
                    building.setPid(communityId);
                    building.setLevel(2L);
                    buildingService.insertWuyeBuilding(building);
                    buildingId = building.getId();
                    buildingMap.put(buildingKey, buildingId);
                }

                // 处理单元
                if (StringUtils.hasText(data.getUnit())) {
                    WuyeBuilding unit = new WuyeBuilding();
                    unit.setName(data.getUnit());
                    unit.setPid(Long.valueOf(buildingId));
                    unit.setLevel(3L);
                    buildingList.add(unit);
                }
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!buildingList.isEmpty()) {
            for(WuyeBuilding building : buildingList) {
                buildingService.insertWuyeBuilding(building);
            }
        }
    }
}