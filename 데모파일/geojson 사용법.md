1. HeadingExample.java파일을 아래와 같이 수정

<pre>
<code>
            package com.graphhopper.example;

            import java.util.Arrays;
            
            import com.fasterxml.jackson.databind.ObjectMapper;
            import com.fasterxml.jackson.databind.node.ArrayNode;
            import com.fasterxml.jackson.databind.node.ObjectNode;
            import com.graphhopper.GHRequest;
            import com.graphhopper.GHResponse;
            import com.graphhopper.GraphHopper;
            import com.graphhopper.config.CHProfile;
            import com.graphhopper.config.Profile;
            import static com.graphhopper.json.Statement.If;
            import static com.graphhopper.json.Statement.Op.LIMIT;
            import static com.graphhopper.json.Statement.Op.MULTIPLY;
            import com.graphhopper.util.CustomModel;
            import com.graphhopper.util.Parameters;
            import com.graphhopper.util.PointList;
            import com.graphhopper.util.shapes.GHPoint;
            
            public class HeadingExample {
                public static void main(String[] args) {
                    String relDir = args.length == 1 ? args[0] : "";
                    GraphHopper hopper = createGraphHopperInstance(relDir + "core/files/andorra.osm.pbf");
            
                    without_heading(hopper);
                    with_heading_start(hopper);
                    with_heading_start_stop(hopper);
                    with_heading_stop(hopper);
                    with_heading_start_stop_lower_penalty(hopper);
                }
            
                /**
                 * See {@link RoutingExample#createGraphHopperInstance} for more comments on creating the GraphHopper instance.
                 */
                static GraphHopper createGraphHopperInstance(String ghLoc) {
                    GraphHopper hopper = new GraphHopper();
                    hopper.setOSMFile(ghLoc);
                    hopper.setGraphHopperLocation("target/heading-graph-cache");
                    hopper.setEncodedValuesString("car_access, road_access, car_average_speed");
                    hopper.setProfiles(new Profile("car").
                            setCustomModel(new CustomModel().
                                    addToSpeed(If("true", LIMIT, "car_average_speed")).
                                    addToPriority(If("!car_access", MULTIPLY, "0")).
                                    addToPriority(If("road_access == DESTINATION", MULTIPLY, "0.1"))));
                    hopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));
                    hopper.importOrLoad();
                    return hopper;
                }
            
                static void without_heading(GraphHopper hopper) {
                    GHRequest request = new GHRequest(new GHPoint(42.566757, 1.597751), new GHPoint(42.567396, 1.597807)).
                            setProfile("car");
                    GHResponse response = hopper.route(request);
                    if (response.hasErrors())
                        throw new RuntimeException(response.getErrors().toString());
                    assert Math.round(response.getBest().getDistance()) == 84;
                    //수정 시작
                    //다른 필요한 메소드에 전부 추가해야할듯
                    String geoJson = convertToGeoJSON(response);
                    System.out.println("Without Heading GeoJSON:");
                    System.out.println(geoJson);
                    //수정 끝
                }
            
                static void with_heading_start(GraphHopper hopper) {
                    GHRequest request = new GHRequest(new GHPoint(42.566757, 1.597751), new GHPoint(42.567396, 1.597807)).
                            setHeadings(Arrays.asList(270d)).
                            // important: if CH is enabled on the server-side we need to disable it for each request that uses heading,
                            // because heading is not supported by CH
                                    putHint(Parameters.CH.DISABLE, true).
                            setProfile("car");
                    GHResponse response = hopper.route(request);
                    if (response.hasErrors())
                        throw new RuntimeException(response.getErrors().toString());
                    assert Math.round(response.getBest().getDistance()) == 264;
                }
            
                static void with_heading_start_stop(GraphHopper hopper) {
                    GHRequest request = new GHRequest(new GHPoint(42.566757, 1.597751), new GHPoint(42.567396, 1.597807)).
                            setHeadings(Arrays.asList(270d, 180d)).
                            putHint(Parameters.CH.DISABLE, true).
                            setProfile("car");
                    GHResponse response = hopper.route(request);
                    if (response.hasErrors())
                        throw new RuntimeException(response.getErrors().toString());
                    assert Math.round(response.getBest().getDistance()) == 434;
                }
            
                static void with_heading_stop(GraphHopper hopper) {
                    GHRequest request = new GHRequest(new GHPoint(42.566757, 1.597751), new GHPoint(42.567396, 1.597807)).
                            setHeadings(Arrays.asList(Double.NaN, 180d)).
                            putHint(Parameters.CH.DISABLE, true).
                            setProfile("car");
                    GHResponse response = hopper.route(request);
                    if (response.hasErrors())
                        throw new RuntimeException(response.getErrors().toString());
                    assert Math.round(response.getBest().getDistance()) == 201;
                }
            
                static void with_heading_start_stop_lower_penalty(GraphHopper hopper) {
                    GHRequest request = new GHRequest(new GHPoint(42.566757, 1.597751), new GHPoint(42.567396, 1.597807)).
                            setHeadings(Arrays.asList(270d, 180d)).
                            putHint(Parameters.Routing.HEADING_PENALTY, 10).
                            putHint(Parameters.CH.DISABLE, true).
                            setProfile("car");
                    GHResponse response = hopper.route(request);
                    if (response.hasErrors())
                        throw new RuntimeException(response.getErrors().toString());
                    // same distance as without_heading
                    assert Math.round(response.getBest().getDistance()) == 84;
                }
                // 수정시작
                //geojson 변환 메소드
                private static String convertToGeoJSON(GHResponse response) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode featureCollection = objectMapper.createObjectNode();
                    featureCollection.put("type", "FeatureCollection");
                    ArrayNode features = featureCollection.putArray("features");
                
                    PointList points = response.getBest().getPoints();
                    ObjectNode feature = objectMapper.createObjectNode();
                    feature.put("type", "Feature");
                    ObjectNode geometry = feature.putObject("geometry");
                    geometry.put("type", "LineString");
                    ArrayNode coordinates = geometry.putArray("coordinates");
                
                    for (int i = 0; i < points.size(); i++) {
                        ArrayNode point = coordinates.addArray();
                        point.add(points.getLon(i));
                        point.add(points.getLat(i));
                    }
                
                    ObjectNode properties = feature.putObject("properties");
                    properties.put("distance", response.getBest().getDistance());
                    properties.put("time", response.getBest().getTime());
                
                    features.add(feature);
                
                    try {
                        return objectMapper.writeValueAsString(featureCollection);
                    } catch (Exception e) {
                        throw new RuntimeException("Error converting to GeoJSON", e);
                    }
                }
                //수정끝
            
            }

</code>
</pre>

2. example에서 mvn clean install

3. 터미널창에서 Without Heading GeoJSON:를 찾고 중괄호 안에 있는 모든 텍스트를 복사

4. [geojson.io](https://geojson.io/#map=2/0/20) 사이트에서 오른쪽에 있는 박스에 붙여넣기

위와 같이 다른 파일들과 메소드들도 수정해야할듯..?
