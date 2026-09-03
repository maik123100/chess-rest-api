package dev.chess.entity;

import dev.chess.converter.FenConverter;
import dev.chess.converter.MovesConverter;
import dev.chess.valueobject.Fen;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String externalId;

    @Column(columnDefinition = "TEXT")
    private String pgn;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = MovesConverter.class)
    private List<String> parsedMoves;

    @Enumerated(EnumType.STRING)
    private TimeClass timeClass;

    private Boolean rated;

    @Convert(converter = FenConverter.class)
    private Fen initialSetup;

    @Convert(converter = FenConverter.class)
    private Fen fen;

    private String rules;

    private String ecoCode;
    private String openingName;
    private String ecoUrl;

    @ManyToOne
    @JoinColumn(name = "white_id")
    private User white;

    private Integer whiteRating;
    private String whiteResult;

    @ManyToOne
    @JoinColumn(name = "black_id")
    private User black;

    private Integer blackRating;
    private String blackResult;

    private String result;

    private Boolean analyzed = false;

    private Boolean analysisRequested = false;

    @OneToOne(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Analysis analysis;

    public Long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getPgn() {
        return pgn;
    }

    public void setPgn(String pgn) {
        this.pgn = pgn;
    }

    public List<String> getParsedMoves() {
        return parsedMoves;
    }

    public void setParsedMoves(List<String> parsedMoves) {
        this.parsedMoves = parsedMoves;
    }

    public TimeClass getTimeClass() {
        return timeClass;
    }

    public void setTimeClass(TimeClass timeClass) {
        this.timeClass = timeClass;
    }

    public Boolean getRated() {
        return rated;
    }

    public void setRated(Boolean rated) {
        this.rated = rated;
    }

    public Fen getInitialSetup() {
        return initialSetup;
    }

    public void setInitialSetup(Fen initialSetup) {
        this.initialSetup = initialSetup;
    }

    public Fen getFen() {
        return fen;
    }

    public void setFen(Fen fen) {
        this.fen = fen;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getEcoCode() {
        return ecoCode;
    }

    public void setEcoCode(String ecoCode) {
        this.ecoCode = ecoCode;
    }

    public String getOpeningName() {
        return openingName;
    }

    public void setOpeningName(String openingName) {
        this.openingName = openingName;
    }

    public String getEcoUrl() {
        return ecoUrl;
    }

    public void setEcoUrl(String ecoUrl) {
        this.ecoUrl = ecoUrl;
    }

    public User getWhite() {
        return white;
    }

    public void setWhite(User white) {
        this.white = white;
    }

    public Integer getWhiteRating() {
        return whiteRating;
    }

    public void setWhiteRating(Integer whiteRating) {
        this.whiteRating = whiteRating;
    }

    public String getWhiteResult() {
        return whiteResult;
    }

    public void setWhiteResult(String whiteResult) {
        this.whiteResult = whiteResult;
    }

    public User getBlack() {
        return black;
    }

    public void setBlack(User black) {
        this.black = black;
    }

    public Integer getBlackRating() {
        return blackRating;
    }

    public void setBlackRating(Integer blackRating) {
        this.blackRating = blackRating;
    }

    public String getBlackResult() {
        return blackResult;
    }

    public void setBlackResult(String blackResult) {
        this.blackResult = blackResult;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Boolean getAnalyzed() {
        return analyzed;
    }

    public void setAnalyzed(Boolean analyzed) {
        this.analyzed = analyzed;
    }

    public Boolean getAnalysisRequested() {
        return analysisRequested;
    }

    public void setAnalysisRequested(Boolean analysisRequested) {
        this.analysisRequested = analysisRequested;
    }

    public Analysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }
}
