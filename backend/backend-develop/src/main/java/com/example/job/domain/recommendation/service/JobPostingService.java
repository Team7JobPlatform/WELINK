package com.example.job.domain.recommendation.service;

import com.example.job.domain.recommendation.dto.JobPostingDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // ★ 인증키 (기존 키 유지)
    private final String KEY = "7adb8c79f0b39b023435a8f94059a1e0e30d45083ef1d1bb74e69b43bbcf08d4";

    private final String API_URL = "https://apis.data.go.kr/1051000/recruitment/list";

    public JobPostingDto getJobPosting(String companyName) {
        try {
            // 1. URL 및 파라미터 설정
            String encodedKey = URLEncoder.encode(KEY, StandardCharsets.UTF_8);
            String encodedName = URLEncoder.encode(companyName, StandardCharsets.UTF_8);

            String urlStr = API_URL +
                    "?serviceKey=" + encodedKey +
                    "&resultType=json" +
                    "&pageNo=1" +
                    "&numOfRows=100" + // 검색 확률 높이기 위해 100개 조회
                    "&insttNm=" + encodedName;

            URI uri = URI.create(urlStr.replace(" ", "%20"));

            System.out.println("====== [DEBUG] 요청 주소: " + uri.toString());

            // 2. API 호출
            String response = restTemplate.getForObject(uri, String.class);
            // (로그가 너무 길면 주석 처리)
            // System.out.println("====== [DEBUG] 응답 데이터: " + response);

            // 3. 데이터 파싱 (수정된 부분 ⭐)
            JsonNode root = objectMapper.readTree(response);

            // "resultCode"가 200이 아니면 에러 처리 (가끔 숫자로 옴)
            // (보내주신 로그에선 resultCode: 200 이었음)

            // ★ [핵심] items가 아니라 'result' 배열을 바로 가져옵니다!
            JsonNode resultArray = root.path("result");

            if (resultArray.isArray() && resultArray.size() > 0) {
                // 배열을 돌면서 기업명이 일치하는지 확인 (API 검색이 정확하지 않을 수 있어서)
                for (JsonNode item : resultArray) {
                    String instNm = item.path("instNm").asText(); // 기업명

                    // 검색어(companyName)가 기업명(instNm)에 포함되면 채택!
                    if (instNm.contains(companyName)) {
                        JobPostingDto dto = new JobPostingDto();

                        // ★ 필드명 매핑 (로그 기반)
                        dto.setTitle(item.path("recrutPbancTtl").asText());  // 공고 제목
                        dto.setDeadline(item.path("pbancEndYmd").asText());  // 마감일
                        dto.setLocation(item.path("workRgnNmLst").asText()); // 근무지

                        String url = item.path("srcUrl").asText();           // 상세 링크
                        dto.setUrl(url.isEmpty() ? "https://job.alio.go.kr" : url);

                        System.out.println("====== [SUCCESS] 공고 찾음: " + instNm);
                        return dto;
                    }
                }

                // 반복문 다 돌았는데 없으면? (첫 번째 거라도 리턴 or null)
                // 일단 첫 번째 공고를 리턴하도록 수정 (테스트용)
                JsonNode firstItem = resultArray.get(0);
                JobPostingDto fallbackDto = new JobPostingDto();
                fallbackDto.setTitle(firstItem.path("recrutPbancTtl").asText());
                fallbackDto.setDeadline(firstItem.path("pbancEndYmd").asText());
                fallbackDto.setLocation(firstItem.path("workRgnNmLst").asText());
                fallbackDto.setUrl(firstItem.path("srcUrl").asText());

                System.out.println("====== [INFO] 정확한 일치 없음, 첫 번째 결과 반환");
                return fallbackDto;

            } else {
                System.out.println("====== [DEBUG] 검색 결과(result)가 비어있음");
                return null;
            }

        } catch (Exception e) {
            System.out.println("====== [ERROR] 파싱 실패: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
/*
package com.example.job.recommendation.service;

import com.example.job.recommendation.dto.JobPostingDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱 도구

    // ★ 서울 열린데이터 광장 인증키 (일반 인증키 - Decoding Key 권장)
    private final String SEOUL_API_KEY = "6359554a4a6a796f36345144675264";

    public JobPostingDto getJobPosting(String companyName) {
        try {
            // 1. URL 만들기 (서울시 API는 슬래시 '/'로 구분합니다)
            // 형식: http://openapi.seoul.go.kr:8088/{KEY}/{TYPE}/{SERVICE}/{START}/{END}
            // 일단 최신 공고 100개를 가져와서 그 안에 기업이 있는지 찾습니다.
            String urlStr = "http://openapi.seoul.go.kr:8088/"
                    + SEOUL_API_KEY + "/json/GetJobInfo/1/100/";

            URI uri = URI.create(urlStr);

            System.out.println("====== [DEBUG] 요청 URL: " + uri.toString());

            // 2. API 호출
            String response = restTemplate.getForObject(uri, String.class);

            // 3. JSON 파싱 및 기업 찾기 (핵심 로직)
            JsonNode root = objectMapper.readTree(response);
            JsonNode rows = root.path("GetJobInfo").path("row"); // 공고 리스트

            for (JsonNode row : rows) {
                // API에서 준 기업명 (CMPNY_NM)
                String apiCompanyName = row.path("CMPNY_NM").asText();

                // 우리가 찾는 기업명이 포함되어 있는지 확인 (예: '네이버' 검색 시 '네이버클라우드'도 찾음)
                if (apiCompanyName.contains(companyName)) {

                    System.out.println("====== [DEBUG] 찾았다! 기업명: " + apiCompanyName);

                    // 4. 찾았으면 DTO에 담아서 리턴
                    JobPostingDto dto = new JobPostingDto();

                    // 명세서 보고 필드 매핑
                    dto.setTitle(row.path("JO_SJ").asText());           // JO_SJ: 구인제목
                    dto.setLocation(row.path("WORK_PARAR_BASS_ADRES_CN").asText()); // 근무예정지 주소
                    dto.setDeadline(row.path("RCEPT_CLOS_NM").asText()); // RCEPT_CLOS_NM: 마감일

                    // URL은 명세서에 따로 없어서 가이드라인(GUI_LN)이 있으면 넣음 (없을 수도 있음)
                    // (서울시 데이터는 상세 URL이 없는 경우가 많아 기본값 처리)
                    String detailUrl = "http://www.seoul.go.kr";
                    dto.setUrl(detailUrl);

                    return dto;
                }
            }

            // 5. 100개 뒤져봤는데 없으면? (없다고 표시)
            System.out.println("====== [DEBUG] 해당 기업의 공고가 없습니다: " + companyName);
            return null;

        } catch (Exception e) {
            System.out.println("====== [ERROR] API 호출 실패: " + e.getMessage());
            return null;
        }
    }
}*/
