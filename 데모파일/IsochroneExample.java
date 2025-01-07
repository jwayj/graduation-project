package com.graphhopper.example; // 패키지 선언

//comment: 아이소크론 계산(특정 시간이나 거리, 여기선 시간 안에 도달할 수 있는 모든 영역을 나타내는 것)

import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.isochrone.algorithm.ShortestPathTree;
import com.graphhopper.routing.ev.Subnetwork;
import com.graphhopper.routing.querygraph.QueryGraph;
import com.graphhopper.routing.util.DefaultSnapFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.TraversalMode;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.index.Snap;
import com.graphhopper.util.GHUtility;
import com.graphhopper.util.PMap;

import java.util.concurrent.atomic.AtomicInteger;

public class IsochroneExample {

    public static void main(String[] args) {
        // 명령줄 인자로 OSM 파일 경로를 받거나 기본 경로를 사용
        String relDir = args.length == 1 ? args[0] : "";

        // GraphHopper 인스턴스 생성 및 데이터 초기화
        GraphHopper hopper = createGraphHopperInstance(relDir + "core/files/andorra.osm.pbf");

        // GraphHopper 인스턴스에서 인코딩 매니저 가져오기
        EncodingManager encodingManager = hopper.getEncodingManager();

        // 특정 GPS 좌표를 그래프의 가장 가까운 노드에 스냅하고 쿼리 그래프 생성
        Weighting weighting = hopper.createWeighting(hopper.getProfile("car"), new PMap());
        Snap snap = hopper.getLocationIndex().findClosest(
            42.508679, 1.532078,
            new DefaultSnapFilter(weighting, encodingManager.getBooleanEncodedValue(Subnetwork.key("car")))
        );
        QueryGraph queryGraph = QueryGraph.create(hopper.getBaseGraph(), snap);

        // 아이소크론 계산 수행
        ShortestPathTree tree = new ShortestPathTree(queryGraph, weighting, false, TraversalMode.NODE_BASED);
        // 120초 이내에 도달할 수 있는 모든 노드 찾기
        tree.setTimeLimit(120_000);

        AtomicInteger counter = new AtomicInteger(0);
        // 콜백을 지정하여 각 노드에 대해 수행할 작업 정의
        tree.search(snap.getClosestNode(), label -> {
            // IsoLabel.java에서 더 많은 속성 확인 가능
            // System.out.println("node: " + label.node + ", time: " + label.time + ", distance: " + label.distance);
            counter.incrementAndGet();
        });

        // 200개 이상의 노드가 발견되었는지 확인
        assert counter.get() > 200;
    }

    /**
     * GraphHopper 인스턴스를 초기화하고 설정을 적용하는 메서드
     * - OSM 파일을 로드하고 경로 계산에 필요한 데이터를 초기화
     * - 프로필과 사용자 정의 모델을 설정
     *
     * @param ghLoc OSM 데이터 파일 경로
     * @return 설정된 GraphHopper 객체
     */
    static GraphHopper createGraphHopperInstance(String ghLoc) {
        GraphHopper hopper = new GraphHopper();
        hopper.setOSMFile(ghLoc); // OSM 파일 설정
        hopper.setGraphHopperLocation("target/isochrone-graph-cache"); // 그래프 캐시 위치 설정

        // 차량 프로필 설정 및 사용자 정의 모델 적용
        hopper.setProfiles(new Profile("car").setCustomModel(GHUtility.loadCustomModelFromJar("car.json")));

        hopper.importOrLoad(); // 데이터 가져오기 또는 로드
        return hopper;
    }
}
