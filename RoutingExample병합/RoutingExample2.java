package com.graphhopper.example;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.LMProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.util.CustomModel;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.json.Statement;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.storage.index.Snap;

public class RoutingExample2 {

    public static void main(String[] args) {
        String relDir = args.length == 1 ? args[0] : "";
        GraphHopper hopper = createGraphHopperInstance(relDir + "south-korea-latest.osm.pbf");
    
        double targetDistance = 5000; // 목표 거리: 5km
    
        try {
            // 출발지 정의
            GHPoint startPoint = new GHPoint(37.5665, 126.9780); // 서울 시청 근처
    
            // 경유지 생성 (랜덤 5개, 범위 확장)
            PointList waypoints = generateRandomWaypoints(hopper, startPoint, 5, targetDistance * 0.5, targetDistance);
            System.out.println("Generated Waypoints: " + waypoints);
    
            // *** 여기부터 경로 생성 코드 추가 ***
            PointList avoidPoints = new PointList(); // 회피할 포인트 리스트 생성
            ResponsePath fullPath = null; // 최종 경로 저장
            GHPoint previousPoint = startPoint; // 출발점을 초기화
    
            // Waypoints를 순회하며 경로 생성
            for (int i = 0; i < waypoints.size(); i++) {
                GHPoint currentPoint = new GHPoint(waypoints.getLat(i), waypoints.getLon(i));
                ResponsePath path = findPathAvoiding(hopper, previousPoint, currentPoint, avoidPoints);
                if (path == null) {
                    System.out.println("경로 생성 실패: " + previousPoint + " → " + currentPoint);
                    return; // 실패 시 종료
                }
    
                // 전체 경로에 추가
                fullPath = (fullPath == null) ? path : combinePaths(fullPath, path);
    
                // AvoidPoints에 현재 포인트 추가
                avoidPoints.add(currentPoint.lat, currentPoint.lon);
                previousPoint = currentPoint; // 이전 포인트 갱신
            }
    
            // Waypoints를 순회한 후 닫힌 도형을 위한 경로 생성 (마지막 Waypoint → 출발지)
            ResponsePath closingPath = findPathAvoiding(hopper, previousPoint, startPoint, avoidPoints);
            if (closingPath != null) {
                fullPath = combinePaths(fullPath, closingPath);
            }
    
            // GeoJSON 출력
            PointList startPointList = new PointList();
            startPointList.add(startPoint.lat, startPoint.lon); // 출발지 추가
    
            String geoJson = GeoJsonExporter.toGeoJSON(fullPath, waypoints, startPointList);
            System.out.println("GeoJSON:\n" + geoJson);
    
            // GeoJSON 저장
            SaveGeoJson.saveToFile(geoJson, "route.geojson");
            System.out.println("GeoJSON saved to route.geojson");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hopper.close();
        }
        
    }
            

    static GraphHopper createGraphHopperInstance(String ghLoc) {
        // 기존 캐시 삭제
        File cacheDir = new File("target/routing-graph-cache");
        if (cacheDir.exists()) {
            for (File file : cacheDir.listFiles()) {
                file.delete();
            }
            cacheDir.delete();
            System.out.println("Existing cache deleted.");
        }
    
        // GraphHopper 인스턴스 생성 및 설정
        GraphHopper hopper = new GraphHopper();
        hopper.setOSMFile(ghLoc);
        hopper.setGraphHopperLocation("target/routing-graph-cache");
    
        // 필요한 모든 Encoded Values 추가
        hopper.setEncodedValuesString("foot_access, foot_average_speed, road_class, max_speed");
    
        // CustomModel 설정
        CustomModel customModel = new CustomModel();
    
        // 우선순위 설정
        customModel.getPriority().add(Statement.If("road_class == RESIDENTIAL", Statement.Op.MULTIPLY, "0.8"));
        customModel.getPriority().add(Statement.If("road_class == FOOTWAY", Statement.Op.MULTIPLY, "1.2"));
        customModel.getPriority().add(Statement.Else(Statement.Op.MULTIPLY, "1.0")); // 기본값
    
        // 속도 설정
        customModel.getSpeed().add(Statement.If("max_speed > 50", Statement.Op.LIMIT, "50"));
        customModel.getSpeed().add(Statement.Else(Statement.Op.LIMIT, "30"));
    
        // 거리 영향도 설정
        customModel.setDistanceInfluence(70.0);
    
        // Profile 설정
        Profile footProfile = new Profile("foot")
                .setWeighting("custom")
                .setCustomModel(customModel);
        hopper.setProfiles(footProfile);
    
        // CH 및 LM 설정
        hopper.getCHPreparationHandler().setCHProfiles(new CHProfile("foot"));
        hopper.getLMPreparationHandler().setLMProfiles(new LMProfile("foot"));
    
        // Import or load
        hopper.importOrLoad();
    
        return hopper;
    }
    
    
    
    

    static PointList generateRandomWaypoints(GraphHopper hopper, GHPoint start, int numWaypoints, double minDistance, double maxDistance) {
        Random random = new Random();
        LocationIndex locationIndex = hopper.getLocationIndex();
        PointList waypoints = new PointList();
    
        for (int i = 0; i < numWaypoints; i++) {
            for (int attempts = 0; attempts < 100; attempts++) { // 최대 100번 시도
                double distance = minDistance + (maxDistance - minDistance) * random.nextDouble();
                double angle = random.nextDouble() * 2 * Math.PI;
    
                double deltaLat = (distance / 111000) * Math.cos(angle);
                double deltaLon = (distance / (111000 * Math.cos(Math.toRadians(start.lat)))) * Math.sin(angle);
    
                double lat = start.lat + deltaLat;
                double lon = start.lon + deltaLon;
    
                if (Double.isNaN(lat) || Double.isNaN(lon)) continue;
    
                GHPoint candidate = new GHPoint(lat, lon);
                Snap snap = locationIndex.findClosest(candidate.lat, candidate.lon, EdgeFilter.ALL_EDGES);
    
                if (snap.isValid()) {
                    boolean isTooClose = false;
                    GHPoint snappedPoint = snap.getSnappedPoint();
                    
                    for (int j = 0; j < waypoints.size(); j++) {
                        double existingLat = waypoints.getLat(j);
                        double existingLon = waypoints.getLon(j);
                        GHPoint existingPoint = new GHPoint(existingLat, existingLon);
    
                        // calculateDistance 메서드 호출
                        if (calculateDistance(existingPoint, snappedPoint) < minDistance / 2) {
                            isTooClose = true;
                            break;
                        }
                    }
    
                    if (!isTooClose) {
                        waypoints.add(snappedPoint.lat, snappedPoint.lon);
                        break;
                    }
                }
            }
        }
    
        return waypoints;
    }
    

    static ResponsePath findPath(GraphHopper hopper, GHPoint start, GHPoint end) {
        GHRequest request = new GHRequest()
                .addPoint(start)
                .addPoint(end)
                .setProfile("foot")
                .setAlgorithm("astar")
                .putHint("ch.disable", true);

        GHResponse response = hopper.route(request);
        if (response.hasErrors()) {
            response.getErrors().forEach(error -> System.err.println("Error during routing: " + error.getMessage()));
            return null;
        }

        return response.getBest();
    }

    static ResponsePath findPathAvoiding(GraphHopper hopper, GHPoint start, GHPoint end, PointList avoidPoints) {
        GHRequest request = new GHRequest()
                .addPoint(start)
                .addPoint(end)
                .setProfile("foot")
                .setAlgorithm("astar")
                .putHint("ch.disable", true);
    
        if (avoidPoints != null && !avoidPoints.isEmpty()) {
            request.putHint("routing.avoid_points", avoidPoints);
        }
    
        GHResponse response = hopper.route(request);
        if (response.hasErrors()) {
            response.getErrors().forEach(error -> System.err.println("Error during routing: " + error.getMessage()));
            return null;
        }
    
        return response.getBest();
    }

    

    static ResponsePath combinePaths(ResponsePath... paths) {
        ResponsePath combinedPath = new ResponsePath();
    
        PointList combinedPoints = new PointList();
        double totalDistance = 0;
        long totalTime = 0;
    
        for (ResponsePath path : paths) {
            if (combinedPoints.isEmpty()) {
                combinedPoints.add(path.getPoints());
            } else {
                PointList newPoints = path.getPoints().copy(1, path.getPoints().size());
                for (int i = 0; i < newPoints.size(); i++) {
                    if (!pointListContains(combinedPoints, newPoints.getLat(i), newPoints.getLon(i))) {
                        combinedPoints.add(newPoints.getLat(i), newPoints.getLon(i));
                    }
                }
            }
            totalDistance += path.getDistance();
            totalTime += path.getTime();
        }
    
        combinedPath.setPoints(combinedPoints);
        combinedPath.setDistance(totalDistance);
        combinedPath.setTime(totalTime);
    
        return combinedPath;
    }

    // 두 점 사이의 거리를 계산하는 메서드
    static double calculateDistance(GHPoint point1, GHPoint point2) {
        double earthRadius = 6371000; // 지구 반지름 (미터 단위)
        double dLat = Math.toRadians(point2.lat - point1.lat);
        double dLon = Math.toRadians(point2.lon - point1.lon);
        double lat1 = Math.toRadians(point1.lat);
        double lat2 = Math.toRadians(point2.lat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    static boolean pointListContains(PointList points, double lat, double lon) {
        for (int i = 0; i < points.size(); i++) {
            if (points.getLat(i) == lat && points.getLon(i) == lon) {
                return true;
            }
        }
        return false;
    }
    
    
}  
