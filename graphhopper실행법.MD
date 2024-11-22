# window에서 하든 ubuntu에서 하든 config-example.yml에 road-access 추가해야 함!!
# window에서 graphhopper 실행

cd C:/Users/dabii/git/graphhopper

java -Ddw.graphhopper.datareader.file=berlin-latest.osm.pbf -jar web/target/graphhopper-web-*.jar server config-example.yml

*위 코드는 베를린 지도 기준

**south korea 링크

https://download.geofabrik.de/asia/south-korea.html

위 링크에서 south-korea-latest.osm.pbf 받았으면 아래의 명령어로 바꾸어 실행

java -Xmx4g -Ddw.graphhopper.datareader.file=south-korea-latest.osm.pbf -jar web/target/graphhopper-web-11.0-SNAPSHOT.jar server config-example.yml


 http://localhost:8989/


------------------------------------

# ubuntu에서 graphhopper 실행법

cd graphhopper

java -Ddw.graphhopper.datareader.file=south-korea-latest.osm.pbf -jar web/target/graphhopper-web-*.jar server config-example.yml



