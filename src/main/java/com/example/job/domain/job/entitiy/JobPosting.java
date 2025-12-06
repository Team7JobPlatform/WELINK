package com.example.job.domain.job.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "job_postings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String location;
    private String deadline;
    private String originalUrl; // URL 충돌 방지를 위해 이름 변경
}