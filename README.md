# 25-1 졸업프로젝트 그로쓰 18팀 - 코드의정상화

<!-- Template for PROJECT REPORT of CapstoneDesign 2025-2H, initially written by khyoo -->
<!-- 본 파일은 2025년도 컴공 졸업프로젝트의 <1차보고서> 작성을 위한 기본 양식입니다. -->
<!-- 아래에 "*"..."*" 표시는 italic체로 출력하기 위해서 사용한 것입니다. -->
<!-- "내용"에 해당하는 부분을 지우고, 여러분 과제의 내용을 작성해 주세요. -->

# Team-Info
| (1) 과제명 | *Graphhopper와 Multi-Factor Route Optimization Algorithm을 이용한 러너 맞춤형 경로 추천 서비스*
|:---  |---  |
| (2) 팀 번호 / 팀 이름 | *18-코드의 정상화* |
| (3) 팀 구성원 | 좌연주 (2271059): 리더, *BE, FE, AI* <br> 최서연 (2076410): 팀원, *BE, FE, AI* <br> 성은재 (2176181) : 팀원, *BE, FE, AI*			 |
| (4) 팀 지도교수 | 심재형 교수님 |
| (5) 과제 분류 | *산학과제* |
| (6) 과제 키워드 | *경로추천, 피드백, GraphHopper*  |
| (7) 과제 내용 요약 | *RunPT의 기능은 크게 네 가지가 있다. <br> 첫 번째로 RunPT는 사용자가 입력한 정보를 반영하여 러닝 경로를 생성한다. 이를 위해서 사용자에게 거리, 경사, 출발지, 목적지를 입력받으며 입력된 정보를 바탕으로 조건을 만족하는 경로를 탐색한다. 이때 다른 지도 서비스와는 다르게 RunPT의 경우 출발지와 목적지가 같은 위치이더라도 경로를 탐색할 수 있다. <br> 두 번째로 RunPT는 러닝 기록(경로, 시간, 거리, 페이스)을 생성한다. 사용자는 기록 페이지에서 자신의 지난 기록을 조회할 수 있으며 이를 바탕으로 기록과 동일한 경로로 러닝할 수도 있다. <br> 세 번째로 RunPT는 피드백 기능을 제공한다. 사용자는 러닝 후 피드백을 작성할 수 있으며 이를 바탕으로 RunPT는 경로의 가중치와 추천 경로를 수정하여 사용자에게 맞춤형 경로를 제공한다.<br> 마지막으로 RunPT는 뱃지라는 명예 보상 시스템을 제공하여 사용자가 탐색한 경로를 완주할 때에 뱃지를 얻을 수 있다. 이로 하여금 사용자는 러닝에 대한 동기를 부여받을 수 있게 된다.* |

<br>

# Project-Summary
| 항목 | 내용 |
|:---  |---  |
| (1) 문제 정의 | *러닝을 즐기는 사람들은 다양한 경로를 달리거나 원하는 거리와 경사도가 반영된 경로를 달리고 싶어하지만 기존 경로 추천 서비스에서는 단순히 최단 경로만 제공하거나 기존 러닝 앱에서는 단순히 기록 기능만 제공한다. 이 서비스의 Target Customer는 주 3-4회, 5km이상 러닝하는 서울 도심에 거주중인 러너이다. 이 Customer들은 도심에서 다양한 경로를 탐색하고 새로운 장소를 달리는 것에서 즐거움을 느낀다. 하지만 기존의 러닝 앱은 단순히 러닝을 기록하는 서비스 제공에 그치지 않는다. 또한 특정 경사도나 거리를 반영한 경로로 달리기를 원할 때는 직접 경로를 찾아봐야 하는 불편함이 있다.*  |
| (2) 기존연구와의 비교 | *기존에 존재하는 서비스로는 경로 탐색(거의 최단경로) 및 제공을 해주는 지도 앱과 러닝 기록을 기록해주는 기능이 대부분인 서비스로 나뉘어져 있다. 지도 앱의 경우 출발지와 도착지를 입력하면 최단경로로 경로를 탐색해서 안내하며, 러닝 앱은 사용자가 러닝 시작버튼을 누르면 러닝 경로, 시간, 페이스를 기록해준다. 지도 앱의 경우 최단 경로는 잘 안내해주지만 프로젝트에서 해결하고자 하는 문제인 희망 거리만큼의 경로를 제공해주지 못하며, 러닝 앱은 경로를 상세히 기록했지만 경로를 추천해주는 기능은 없어서 사용자가 직접 경로를 찾아봐야한다. RunPT는 위 두 기능을 좀 더 발전시켜서 합친 것으로, 사용자의 취향(경사도, 거리)를 반영한 경로를 탐색해주며 러닝을 기록할 뿐만 아니라 피드백을 통해서 다음 러닝에 더 나은 경험을 제공해줄 수 있다.* |
| (3) 제안 내용 | *원하는 거리 및 경사도를 반영한 러닝 경로 생성 <br> 러닝 기록(경로, 시간, 거리, 페이스) 저장 <br> 피드백 기능을 통해 불만족스러웠던 경로는 다음 러닝에서 배제 <br> 뱃지와 같은 명예 보상 시슴템을 통해 동기부여* |
| (4) 기대효과 및 의의 | *사용자는 RunPT를 이용해 크게 3가지의 기대 효과를 얻을 수 있다. <br> 첫 번째로, 사용자는 단순한 최단경로가 아닌 원하는 거리, 경사도를 반영한 러닝 경로를 제공받을 수 있다. <br> 두 번째로, RunPT는 매번 조금씩 다른 경로를 제공하기 때문에 사용자는 색다른 러닝 경험을 제공받을 수 있다. <br> 세 번째로, 피드백 기능을 통해 추천 경로가 점차 수정되기 때문에, 시간이 지날수록 사용자는 자신에게 맞춤화된 경로를 제공받을 수 있다.<br> 이때 RunPT는 기존의 서비스들과 다르게 단순히 최단경로를 생성하는 것이 아니라 사용자의 니즈를 반영하여 경로를 생성할 수 있으며, 출발지와 도착지가 같은 경우에도 러닝경로를 생성할 수 있고 피드백 기능을 통해 경로를 개선할 수 있다는 점에서 의의가 있다.* |
| (5) 주요 기능 리스트 | *사용자에게 거리, 경사 출발지, 목적지를 입력받음 <br> 입력된 정보를 바탕으로 저건을 만족하는 경로를 탐색함 <br> 출발지와 목적지가 같은 위치이더라도 경로를 탐색할 수 있음 <br> 기록 페이지에서 자신의 지난 기록을 조회할 수 있음 <br> 지난 기록을 바탕으로 그 기록과 동일한 경로로 러닝할 수 있음 <br> 사용자에게 피드백을 제공받은 경로의 가중치를 수정함 <br>  경로의 가중치에 따라 사용자에게 추천하는 경로가 수정됨 <br> 탐색된 경로를 완주하면 뱃지를 부여받음* |

<br>
 
# Project-Design & Implementation
| 항목 | 내용 |
|:---  |---  |
| (1) 요구사항 정의 | *프로젝트를 완성하기 위해 필요한 요구사항을 설명하기에 가장 적합한 방법을 선택하여 기술* <br> 예) <br> - 기능별 상세 요구사항(또는 유스케이스) <br> - 설계 모델(클래스 다이어그램, 클래스 및 모듈 명세서) <br> - UI 분석/설계 모델 <br> - E-R 다이어그램/DB 설계 모델(테이블 구조) |
| (2) 전체 시스템 구성 | *프로젝트를 위하여, SW 전체 시스템의 구조를 보인다. (가능하다면, 사용자도 포함) <br> 주요 SW 모듈을 보이고, 각각의 역할을 기술한다. <br>만약, 오픈소스 혹은 외부 모듈을 사용한다면 이또한 기술한다.* |
| (3) 주요엔진 및 기능 설계 | *프로젝트의 주요 기능 혹은 모듈의 설계내용에 대하여 기술한다 <br> SW 구조 그림에 있는 각 Module의 상세 구현내용을 자세히 기술한다.* |
| (4) 주요 기능의 구현 | *<주요기능리스트>에 정의된 기능 중 최소 2개 이상에 대한 상세 구현내용을 기술한다.* |
| (5) 기타 | *기타 사항을 기술*  |

<br>


# 🔗 관련 링크

우리 팀이 수정 중인 오픈 소스 코드

https://github.com/jwayj/NewGraphhopper

