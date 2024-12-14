# java-attendance-precourse
## 기능 요구 사항
2024년 12월 한달 간 출석 관리 시스템
### 기능
- 출석 확인은 캠퍼스에 들어온 후 시스템에 출석 데이터가 저장된 시간 기준
- 시간은 24h 형식
- 월 13:00~18:00, 화~금 10:00~18:00
  - 5분 초과는 지각
  - 30분 초과는 결석
  - 출석 기록 없어도 결석
- 지각, 결석 횟수에 따라 경고 또는 면담.
  - 5회 초과 결석은 제적
  - 지각 3회 == 결석 1회
    - 경고 대상자: 결석 2회 이상
    - 면담 대상자: 결석 3회 이상
    - 제적 대상자: 결석 5회 초과
- 캠퍼스 운영시간 08:00~23:00
- 주말 및 공휴일은 출석 x
- 출석 크루와 기록은 attendances.csv 파일
- 프로그램은 사용자가 종료할 때까지 종료되지 않으며, 해당 기능을 수행한 후 초기 화면으로 돌아간다.
사용자가 잘못된 값을 입력할 경우 "[ERROR]"로 시작하는 메시지와 함께 IllegalArgumentException을 발생시킨 후 `애플리케이션은 종료`되어야 한다.

#### 출석 확인 기능
- 닉네임, 등교 시간 입력
- 출석 후 출석 기록 확인
- 이미 출석한 경우 다시 출석x, 수정 기능 안내
```text
닉네임을 입력해 주세요.
이든
등교 시간을 입력해 주세요.
09:59

12월 05일 화요일 09:59 (출석)
```


#### 출석 수정
- 출석 확인을 수정하려면 `닉네임`, `수정하려는 날짜`, `등교 시간`을 입력
- 수정 후에는 변경 전, 변경 후 출석 기록 확인 가능
```text
출석을 수정하려는 크루의 닉네임을 입력해 주세요.
빙티
수정하려는 날짜(일)를 입력해 주세요.
3
언제로 변경하겠습니까?
09:58

12월 03일 화요일 10:07 (지각) -> 09:58 (출석) 수정 완료!
```
- [ ] 닉네임 입력 기능
- [ ] 해당 닉네임이 존재하는지 여부 확인 기능
- [ ] 해당 닉네임의 출석 기록 불러오는 기능
- [ ] 수정 날짜 입력 기능
- [ ] 수정 날짜에 출석 기록 존재 여부 기능
- [ ] 수정 시간 입력 기능
- [ ] 수정 시간이 출석, 지각, 결석인지 확인하는 기능
- [ ] 이전 출석과 바뀐 출석 출력 기능

#### 크루별 출석 기록 확인
닉네임 입력 시 전날까지의 크루 출석 기록을 확인할 수 있다.
- [ ] 닉네임 입력 기능
- [ ] 해당 닉네임이 존재하는지 여부 확인 기능
- [ ] 해당 닉네임의 출석 기록 불러오는 기능
- [ ] 출석, 지각, 결석 횟수 계산하는 기능
- [ ] 경고, 면담, 제적 대상자인지 확인하는 기능

```text
닉네임을 입력해 주세요.
빙티

이번 달 빙티의 출석 기록입니다.

12월 02일 월요일 13:00 (출석)
12월 03일 화요일 10:07 (지각)
12월 04일 수요일 10:02 (출석)
12월 05일 목요일 10:06 (지각)
12월 06일 금요일 10:01 (출석)
12월 09일 월요일 --:-- (결석)
12월 10일 화요일 10:03 (출석)
12월 11일 수요일 --:-- (결석)
12월 12일 목요일 --:-- (결석)
12월 13일 금요일 10:02 (출석)

출석: 3회
지각: 0회
결석: 3회

면담 대상자입니다.
```
 
### 입력
- [ ] 닉네임 입력
- [ ] 등교 시간 입력
- [ ] 파일 읽기

### 출력
- [ ] 기능 선택 항목, 날짜 또는 시간을 잘못된 형식으로 입력한 경우
`[ERROR] 잘못된 형식을 입력하였습니다.`
- [ ] 등록되지 않은 닉네임을 입력한 경우
`[ERROR] 등록되지 않은 닉네임입니다.`
- [ ] 주말 또는 공휴일에 출석을 확인하거나 수정하는 경우
`[ERROR] 12월 14일 토요일은 등교일이 아닙니다.`
- [ ] 미래 날짜로 출석을 수정하는 경우
`[ERROR] 아직 수정할 수 없습니다.`
- [ ] 등교 시간이 캠퍼스 운영 시간이 아닌 경우
`[ERROR] 캠퍼스 운영 시간에만 출석이 가능합니다.`
- [ ] 이미 출석을 하였는데 다시 출석 확인을 하는 경우
`[ERROR] 이미 출석을 확인하였습니다. 필요한 경우 수정 기능을 이용해 주세요.`


## 생각해볼 수 있는 도메인
1. 출석부
2. 출석 상태
3. 크루 
4. 결석, 지각, 출석과 같은 상태
5. 출석 관리