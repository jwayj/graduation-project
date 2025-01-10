1. Example 폴더 안에 GeoJsonExporter.java SaveGeoJson.java geojson_map.html 세 개의 파일을 추가(별도로 올려놓겠음)
2. RoutingExample의 main 메소드에 아래 코드를 추가(hopper.close(); 코드 위에 추가함)

        ResponsePath path = routing(hopper);
        String geoJson = GeoJsonExporter.toGeoJSON(path);
        System.out.println("GeoJSON:\n" + geoJson);

        try {
            SaveGeoJson.saveToFile(geoJson, "route.geojson");
            System.out.println("GeoJSON saved to route.geojson");
        } catch (IOException e) {
            System.err.println("Error saving GeoJSON: " + e.getMessage());
        }

3.RoutingExample의 routing 메소드의 void형식을 ResponsePath로, 마지막줄에 return rsp.getBest(); 추가함

4.https://marketplace.visualstudio.com/items?itemName=ritwickdey.LiveServer 링크 접속해서 설치

5.RoutingExample을 실행, 성공적으로 실행되면 마지막에 'GeoJSON saved to route.geojson'이라는 문구가 뜨면서 graphhopper 최상위 위치에 route.geojson이라는 이름의 파일이 생성되어 있을거임

6.이 json파일을 example파일로 옮기기

7.vscode의 html파일을 우클릭하면 Open with Live Server클릭해서 열면 지도에 표시되어있음(그냥 폴더에 있는 html파일로 열면 표시가 안되어서 이렇게 함)

수정해야 할 것
1. geoJson 파일이 example폴더 안에 저장되도록 코드 수정
2. 출발지, 도착지를 입력받는 페이지를 만들어서 확인버튼을 누르면 웹에서 geoJson 파일 생성->바로 반영해서 지도에 띄우도록 구현?
