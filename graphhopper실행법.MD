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

*이때 config-example.yml 수정은 nano ~/graphhopper/config-example.yml

-----------------------------
# http가 아닌 https로 바꾸는 경우(현재위치 탐색 위해)
java -Ddw.graphhopper.datareader.file=south-korea-latest.osm.pbf -jar ~/graphhopper/web/target/graphhopper-web-*.jar server ~/graphhopper/config-example.yml

-------------------------------
* 인증서 (pw 631422)

dabiih@dabiih:~/graphhopper$ openssl req -x509 -newkey rsa:4096 -keyout ~/certs/graphhopper.key -out ~/certs/graphhopper.crt -days 365 -nodes
............+......+......+.+..+.+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*..................+............+..+.......+........+.......+...............+...+........+...+....+.....+.+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*.+...+...........+.+............+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
.+..+....+......+..+...+...+.......+.........+.........+..+....+..............+....+..+.........+......+...................+.....+.+...+...........+......+..................+.+.....+....+........+...............+.......+..+.+..+.......+...+.....+.+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*...........+.....+.+......+...+..+......+.......+......+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*..+..........+.....+.........+....................................+.+..+.......+........+.....................+.+......+.........+......+..+..........+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-----
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:KR
State or Province Name (full name) [Some-State]:Seoul
Locality Name (eg, city) []:Seoul
Organization Name (eg, company) [Internet Widgits Pty Ltd]:Personal
Organizational Unit Name (eg, section) []:.
Common Name (e.g. server FQDN or YOUR name) []:172.25.168.177
Email Address []:dabiih@ewhain.net

-> 인증서 common name 172.25.168.177
->인증서 .p12 저장 비밀번호는 631422
-----------------------
* config-example.yml 파일 수정

nano ~/graphhopper/config-example.yml

**server: application_connectors 수정

https://172.25.168.177:8989/maps/?profile=car&layer=OpenStreetMap

위의 링크로 들어가기




