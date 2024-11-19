cd C:/Users/dabii/git/graphhopper

java -Ddw.graphhopper.datareader.file=berlin-latest.osm.pbf -jar web/target/graphhopper-web-*.jar server config-example.yml
*베를린임

**south korea 링크
https://download.geofabrik.de/asia/south-korea.html
위 링크에서 south-korea-latest.osm.pbf 받았으면 아래의 명령어로 바꾸어 실행

java -Xmx4g -Ddw.graphhopper.datareader.file=south-korea-latest.osm.pbf -jar web/target/graphhopper-web-10.0-SNAPSHOT.jar server config-example.yml


 http://localhost:8989/
