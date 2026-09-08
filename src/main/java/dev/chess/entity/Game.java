package dev.chess.entity;

import dev.chess.converter.MovesConverter;
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
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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

    @Column(columnDefinition = "TEXT")
    private String initialSetup;

    @Column(columnDefinition = "TEXT")
    private String fen;

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
}
