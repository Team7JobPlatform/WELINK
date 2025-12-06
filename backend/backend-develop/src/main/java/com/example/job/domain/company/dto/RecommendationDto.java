package com.example.job.domain.company.dto;

public class RecommendationDto {

    private Long companyId;
    private String companyName;
    private String location;
    private int score;
    private int matchedWelfareCount;

    public RecommendationDto(Long companyId, String companyName, String location,
                             int score, int matchedWelfareCount) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.location = location;
        this.score = score;
        this.matchedWelfareCount = matchedWelfareCount;
    }

    public Long getCompanyId() { return companyId; }
    public String getCompanyName() { return companyName; }
    public String getLocation() { return location; }
    public int getScore() { return score; }
    public int getMatchedWelfareCount() { return matchedWelfareCount; }

    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setLocation(String location) { this.location = location; }
    public void setScore(int score) { this.score = score; }
    public void setMatchedWelfareCount(int matchedWelfareCount) { this.matchedWelfareCount = matchedWelfareCount; }
}
