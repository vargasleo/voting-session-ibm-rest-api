package com.vargas.leo.gerenciadorassembleia.domain;

import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingResult;
import com.vargas.leo.gerenciadorassembleia.domain.enums.VotingSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "voting_session")
public class VotingSession {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id_voting_session")
    private Integer id;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_agenda", referencedColumnName = "id_agenda")
    private Agenda agenda;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    public final LocalDateTime DEFAULT_FINAL_DATE_TIME = LocalDate.now().atTime(LocalTime.from(createdAt)).plusMinutes(1);

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "result")
    private VotingResult result;

    @Transient
    private VotingResult looserOption;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "voting_session_status")
    private VotingSessionStatus status;

    @Column(name = "yes_votes")
    private int yesVotes = 0;

    @Column(name = "no_votes")
    private int noVotes = 0;

}
