example
C:\Users\jwayeonjoo\NewGraphhopper\example\src\main\java\com\graphhopper\example\HeadingExample.java: GraphHopper 라이브러리를 사용하여 헤딩 제약 조건을 포함한 라우팅을 사용하는 방법을 보여줍니다.
C:\Users\jwayeonjoo\NewGraphhopper\example\src\main\java\com\graphhopper\example\IsochroneExample.java: GraphHopper 라이브러리를 사용하여 특정 위치에서 주어진 시간 안에 할 수 있는 영역(등시선)을 동원하는 Java 코드 예제를 제공합니다
C:\Users\jwayeonjoo\NewGraphhopper\example\src\main\java\com\graphhopper\example\LocationIndexExample.java: 위치 인덱스,  GPS 좌표를 도로에 스냅하거나 라우팅 애플리케이션에서 근처 그래프 요소를 찾는 기능을 구현해야 하는 개발자에게 유용합니다.
C:\Users\jwayeonjoo\NewGraphhopper\example\src\main\java\com\graphhopper\example\LowLevelAPIExample.java: 그래프 생성, 기본적인라우팅, 라우팅 가속화(알고리즘을 여기서 해야할지도?)
C:\Users\jwayeonjoo\NewGraphhopper\example\src\main\java\com\graphhopper\example\RoutingExample.java: 경로 계산, 특정 유형 도로 피하기, 속도제한, 대체경로 계산(이것도 참고하면 좋을듯)
C:\Users\jwayeonjoo\NewGraphhopper\example\src\main\java\com\graphhopper\example\RoutingExampleTC.java: 회전비용, 도로변 제약 조건 지정


C:\Users\jwayeonjoo\NewGraphhopper\example\src\test\java\com\graphhopper\example\HeadingExampleTest.java: 캐시 디렉토리 제거, 올바르게 작동하는지 확인
C:\Users\jwayeonjoo\NewGraphhopper\example\src\test\java\com\graphhopper\example\LocationIndexExampleTest.java: 올바르게 작동하는지 확인
C:\Users\jwayeonjoo\NewGraphhopper\example\src\test\java\com\graphhopper\example\LowLevelAPIExampleTest.java: 올바르게 작동하는지 확인
C:\Users\jwayeonjoo\NewGraphhopper\example\src\test\java\com\graphhopper\example\RoutingExampleTest.java: 예외 발생 안시키고 동작하는지 확인, 캐시 삭제, 올바르게 작동하는지 확인


map-matching
C:\Users\jwayeonjoo\NewGraphhopper\map-matching\src\main\java\com\graphhopper\matching\Distributions.java: 확률 분포 함수를 구현하는 클래스
C:\Users\jwayeonjoo\NewGraphhopper\map-matching\src\main\java\com\graphhopper\matching\EdgeMatch.java: 도로망의 가장자리와 하나 이상의 GPS 상태 간의 일치를 나타냄
C:\Users\jwayeonjoo\NewGraphhopper\map-matching\src\main\java\com\graphhopper\matching\HmmProbabilities.java: 은닉 마르코프 모델(HMM)에 대한 확률 계산을 구현
C:\Users\jwayeonjoo\NewGraphhopper\map-matching\src\main\java\com\graphhopper\matching\MapMatching.java: GPS 추적을 도로망과 일치시키는 맵 매칭 알고리즘을 구현4
C:\Users\jwayeonjoo\NewGraphhopper\map-matching\src\main\java\com\graphhopper\matching\MatchResult.java: 맵 매칭 작업의 결과를 나타내는 클래스
C:\Users\jwayeonjoo\NewGraphhopper\map-matching\src\main\java\com\graphhopper\matching\Observation.java: 맵 매칭 시스템에서 단일 GPS 관측 지점을 나타내는 클래스
C:\Users\jwayeonjoo\NewGraphhopper\map-matching\src\main\java\com\graphhopper\matching\ObservationWithCandidateStates.java:맵 매칭 프로세스에서 관측치(일반적으로 GPS 지점)와 해당 후보 상태를 나타냄
C:\Users\jwayeonjoo\NewGraphhopper\map-matching\src\main\java\com\graphhopper\matching\SequenceState.java: 은닉 마르코프 모델(HMM)의 가장 가능성 있는 시퀀스에서 상태를 나타냄
C:\Users\jwayeonjoo\NewGraphhopper\map-matching\src\main\java\com\graphhopper\matching\State.java: 맵 매칭 후보 또는 매칭된 지점을 나타내는 클래스
C:\Users\jwayeonjoo\NewGraphhopper\map-matching\src\main\java\com\graphhopper\matching\Transition.java: 맵 매칭 알고리즘에서 두 연속 후보 간의 전환을 나타내는 클래스


navigation
C:\Users\jwayeonjoo\NewGraphhopper\navigation\src\main\java\com\graphhopper\navigation\DistanceConfig.java: 거리 및 단위 체계(미터법 또는 영국식 단위)에 따라 음성 지침을 구성
C:\Users\jwayeonjoo\NewGraphhopper\navigation\src\main\java\com\graphhopper\navigation\DistanceUtils.java: 거리 변환 및 단위 시스템을 처리하기 위한 유틸리티 함수와 열거형을 제공하는 클래스
C:\Users\jwayeonjoo\NewGraphhopper\navigation\src\main\java\com\graphhopper\navigation\NavigateResource.java: Mapbox Navigation SDK와 호환되는 엔드포인트를 구현하여 방향 및 경로 기능을 제공
C:\Users\jwayeonjoo\NewGraphhopper\navigation\src\main\java\com\graphhopper\navigation\NavigateResponseConverter.java: GraphHopper 응답을 Mapbox API 사양을 따르는 JSON 형식으로 변환하는 역할
C:\Users\jwayeonjoo\NewGraphhopper\navigation\src\main\java\com\graphhopper\navigation\VoiceInstructionConfig.java: 내비게이션 시스템에서 음성 지침을 생성하기 위한 클래스

C:\Users\jwayeonjoo\NewGraphhopper\navigation\src\test\java\com\graphhopper\navigation\NavigateResourceTest.java: 테스트 코드
C:\Users\jwayeonjoo\NewGraphhopper\navigation\src\test\java\com\graphhopper\navigation\NavigateResponseConverterTest.java: 테스트 코드
C:\Users\jwayeonjoo\NewGraphhopper\navigation\src\test\java\com\graphhopper\navigation\NavigateResponseConverterTranslationMapTest.java: 테스트 코드
C:\Users\jwayeonjoo\NewGraphhopper\navigation\src\test\java\com\graphhopper\navigation\VoiceInstructionConfigTest.java: 테스트 코드


reader-gtfs
C:\Users\jwayeonjoo\NewGraphhopper\reader-gtfs\src\main\java\com\conveyal\gtfs\error\DateParseError.java: GTFS(일반 대중교통 피드 사양) 피드에서 날짜 필드를 구문 분석할 때 발생하는 오류
C:\Users\jwayeonjoo\NewGraphhopper\reader-gtfs\src\main\java\com\conveyal\gtfs\error\DuplicateKeyError.java:  동일한 기본 키를 가진 다른 개체가 이미 존재하기 때문에 GTFS 엔터티를 테이블에 추가할 수 없는 경우 발생하는 오류
C:\Users\jwayeonjoo\NewGraphhopper\reader-gtfs\src\main\java\com\conveyal\gtfs\error\EmptyFieldError.java: GTFS(일반 대중교통 피드 사양) 피드의 필수 필드가 비어 있거나 누락된 경우 발생하는 오류
C:\Users\jwayeonjoo\NewGraphhopper\reader-gtfs\src\main\java\com\conveyal\gtfs\error\GeneralError.java: 자체적인 특정 오류 클래스가 없는 GTFS(일반 교통 피드 사양) 로딩 문제

