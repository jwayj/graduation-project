package com.graphhopper.example;
//comment: heading이 없을 때, 출발지에만 있을 때, 도착지에만 있을 때, 모두 있을 때로 경로 계산하는 파일임. 이거 사용하려면 나머진 다 필요없고 heading 없을 때 경로 계산하는 부분만 쓸 수 있을 듯?

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.util.CustomModel;
import com.graphhopper.util.Parameters;
import com.graphhopper.util.shapes.GHPoint;

import java.util.Arrays;

import static com.graphhopper.json.Statement.If;
import static com.graphhopper.json.Statement.Op.LIMIT;
import static com.graphhopper.json.Statement.Op.MULTIPLY;

public class HeadingExample {

    public static void main(String[] args) {
        // 명령줄 인자로 OSM 파일 경로를 받거나 기본 경로를 사용
        String relDir = args.length == 1 ? args[0] : "";

        // GraphHopper 인스턴스 생성 및 데이터 초기화
        GraphHopper hopper = createGraphHopperInstance(relDir + "core/files/andorra.osm.pbf");

        // 각 함수 호출을 통해 다양한 조건의 경로 계산 예제 실행
        without_heading(hopper); // Heading 없이 기본 경로 계산
        with_heading_start(hopper); // 시작 지점에 Heading 설정 후 경로 계산
        with_heading_start_stop(hopper); // 시작과 종료 지점 모두에 Heading 설정
        with_heading_stop(hopper); // 종료 지점에만 Heading 설정
        with_heading_start_stop_lower_penalty(hopper); // Heading 패널티를 낮춘 상태로 시작과 종료 지점에 Heading 설정
    }

    /**
     * GraphHopper 인스턴스를 초기화하는 메서드
     * - OSM 파일을 로드하고 경로 계산에 필요한 데이터를 초기화
     * - CH(Contraction Hierarchies) 프로필과 커스텀 모델을 설정
     * 
     * @param ghLoc OSM 데이터 파일 경로
     * @return 설정된 GraphHopper 객체
     */
    static GraphHopper createGraphHopperInstance(String ghLoc) {
        GraphHopper hopper = new GraphHopper();
        hopper.setOSMFile(ghLoc); // OSM 파일 설정
        hopper.setGraphHopperLocation("target/heading-graph-cache"); // 캐시 디렉토리 설정

        // 차량 프로필 설정 및 사용자 정의 모델 적용
        hopper.setProfiles(new Profile("car")
            .setCustomModel(new CustomModel()
                .addToSpeed(If("true", LIMIT, "car_average_speed")) // 속도 제한 설정
                .addToPriority(If("!car_access", MULTIPLY, "0")) // 차량 접근 불가 도로 우선순위 0
                .addToPriority(If("road_access == DESTINATION", MULTIPLY, "0.1")))); // 목적지 접근 도로 우선순위 낮춤

        // CH(Contraction Hierarchies) 프로필 설정
        hopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));

        hopper.importOrLoad(); // 데이터 가져오기 또는 기존 데이터 로드
        return hopper;
    }

    /**
     * 기본 경로 계산
     * - Heading(방향 설정) 없이 단순히 두 지점 간 최단 경로를 계산
     * 
     * @param hopper GraphHopper 객체
     */
    static void without_heading(GraphHopper hopper) {
        // 출발지와 목적지 설정
        GHRequest request = new GHRequest(new GHPoint(42.566757, 1.597751), new GHPoint(42.567396, 1.597807))
            .setProfile("car"); // 차량 프로필 사용

        // 경로 계산 요청
        GHResponse response = hopper.route(request);

        // 오류 처리
        if (response.hasErrors()) {
            throw new RuntimeException(response.getErrors().toString());
        }

        // 결과 검증: 기대 거리와 일치하는지 확인
        assert Math.round(response.getBest().getDistance()) == 84;
    }

    /**
     * 시작 지점에 Heading 설정 후 경로 계산
     * - 출발지의 특정 방향(예: 서쪽)을 기준으로 경로를 계산
     * 
     * @param hopper GraphHopper 객체
     */
    static void with_heading_start(GraphHopper hopper) {
        // 출발지에서 서쪽(270도) 방향으로 시작하도록 Heading 설정
        GHRequest request = new GHRequest(new GHPoint(42.566757, 1.597751), new GHPoint(42.567396, 1.597807))
            .setHeadings(Arrays.asList(270d)) // Heading 설정: 서쪽
            .putHint(Parameters.CH.DISABLE, true) // CH 비활성화
            .setProfile("car");

        // 경로 계산
        GHResponse response = hopper.route(request);

        // 오류 처리
        if (response.hasErrors()) {
            throw new RuntimeException(response.getErrors().toString());
        }

        // 결과 검증: 기대 거리와 일치하는지 확인
        assert Math.round(response.getBest().getDistance()) == 264;
    }

    /**
     * 시작과 종료 지점에 모두 Heading 설정 후 경로 계산
     * - 출발지와 목적지 각각에 특정 방향(예: 서쪽, 남쪽)을 기준으로 경로를 계산
     * 
     * @param hopper GraphHopper 객체
     */
    static void with_heading_start_stop(GraphHopper hopper) {
        // 출발지(서쪽)와 목적지(남쪽)의 Heading 설정
        GHRequest request = new GHRequest(new GHPoint(42.566757, 1.597751), new GHPoint(42.567396, 1.597807))
            .setHeadings(Arrays.asList(270d, 180d)) // 시작: 서쪽, 종료: 남쪽
            .putHint(Parameters.CH.DISABLE, true) // CH 비활성화
            .setProfile("car");

        // 경로 계산
        GHResponse response = hopper.route(request);

        // 오류 처리
        if (response.hasErrors()) {
            throw new RuntimeException(response.getErrors().toString());
        }

        // 결과 검증
        assert Math.round(response.getBest().getDistance()) == 434;
    }

    /**
     * 종료 지점에만 Heading 설정 후 경로 계산
     * - 출발지는 제한 없이, 목적지만 특정 방향(남쪽)으로 설정
     * 
     * @param hopper GraphHopper 객체
     */
    static void with_heading_stop(GraphHopper hopper) {
        // 목적지의 Heading 설정
        GHRequest request = new GHRequest(new GHPoint(42.566757, 1.597751), new GHPoint(42.567396, 1.597807))
            .setHeadings(Arrays.asList(Double.NaN, 180d)) // 출발지 Heading 없음, 종료: 남쪽
            .putHint(Parameters.CH.DISABLE, true)
            .setProfile("car");

        // 경로 계산
        GHResponse response = hopper.route(request);

        // 오류 처리
        if (response.hasErrors()) {
            throw new RuntimeException(response.getErrors().toString());
        }

        // 결과 검증
        assert Math.round(response.getBest().getDistance()) == 201;
    }

    /**
     * Heading 설정 및 패널티 조정 후 경로 계산
     * - 시작과 종료 지점 모두에 Heading 설정
     * - Heading 패널티 값을 낮춰 더 유연한 경로를 허용
     * 
     * @param hopper GraphHopper 객체
     */
    static void with_heading_start_stop_lower_penalty(GraphHopper hopper) {
        // 시작과 종료 지점의 Heading 설정 및 패널티 감소
        GHRequest request = new GHRequest(new GHPoint(42.566757, 1.597751), new GHPoint(42.567396, 1.597807))
            .setHeadings(Arrays.asList(270d, 180d)) // 시작: 서쪽, 종료: 남쪽
            .putHint(Parameters.CH.DISABLE, true) // CH 비활성화
            .putHint(Parameters.Routing.HEADING_PENALTY, 50) // Heading 패널티 값을 낮춤
            .setProfile("car");

        // 경로 계산
        GHResponse response = hopper.route(request);

        // 오류 처리
        if (response.hasErrors()) {
            throw new RuntimeException(response.getErrors().toString());
        }

        // 결과 검증
        assert Math.round(response.getBest().getDistance()) == 434;
    }
}
