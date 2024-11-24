# window에서 하든 ubuntu에서 하든 config-example.yml에 road-access 추가해야 함!!

* mvn install -DskipTests

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

*이때 config-example.yml 수정은 nano ~/graphhopper/config-example.yml

*만약 window에 있는 폴더를 실행시키는 거라면

cd mnt/Users/dabii/git/graphhopper

java -Ddw.graphhopper.datareader.file=south-korea-latest.osm.pbf -jar web/target/graphhopper-web-*.jar server config-example.yml


-----------------------------
# http가 아닌 https로 바꾸는 경우(현재위치 탐색 위해)
java -Ddw.graphhopper.datareader.file=south-korea-latest.osm.pbf -jar ~/graphhopper/web/target/graphhopper-web-*.jar server ~/graphhopper/config-example.yml

-------------------

* config-example.yml 파일 수정

nano ~/graphhopper/config-example.yml

**server: application_connectors 수정

https://172.25.168.177:8989/maps/?profile=car&layer=OpenStreetMap

위의 링크로 들어가기

-----------------
# nominatim 보기

curl -L -H "User-Agent: MyApplication/1.0" "https://nominatim.openstreetmap.org/search?q=%EC%9D%B4%ED%99%94%EC%97%AC%EC%9E%90%EB%8C%80%ED%95%99%EA%B5%90&format=json&limit=1"




