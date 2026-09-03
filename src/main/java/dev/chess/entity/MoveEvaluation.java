package dev.chess.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "move_evaluations")
public class MoveEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "analysis_id", nullable = false)
    private Analysis analysis;

    private Integer moveNumber;

    private String movePlayed;

    private String engineMove;

    private Double engineMoveEval;

    private Double evalDiff;

    public Long getId() {
        return id;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

    public Integer getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(Integer moveNumber) {
        this.moveNumber = moveNumber;
    }

    public String getMovePlayed() {
        return movePlayed;
    }

    public void setMovePlayed(String movePlayed) {
        this.movePlayed = movePlayed;
    }

    public String getEngineMove() {
        return engineMove;
    }

    public void setEngineMove(String engineMove) {
        this.engineMove = engineMove;
    }

    public Double getEngineMoveEval() {
        return engineMoveEval;
    }

    public void setEngineMoveEval(Double engineMoveEval) {
        this.engineMoveEval = engineMoveEval;
    }

    public Double getEvalDiff() {
        return evalDiff;
    }

    public void setEvalDiff(Double evalDiff) {
        this.evalDiff = evalDiff;
    }
}
