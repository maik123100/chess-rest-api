package dev.chess.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "move_evaluations")
public class MoveEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "analysis_id", nullable = false)
    @JsonIgnore
    private Analysis analysis;

    private Integer moveNumber;

    private String movePlayed;

    private String engineMove;

    private Double engineMoveEval;

    private Double evalDiff;
}