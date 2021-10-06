package com.github.nightdeveloper.smartdashboard.moex.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Board extends AbstractMoexJsonEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long board_group_id;
    private Long engine_id;
    private Long market_id;
    private String boardid;
    private String board_title;
    private Long is_traded;
    private Long has_candles;
    private Long is_primary;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(id, board.id) && Objects.equals(board_group_id, board.board_group_id) && Objects.equals(engine_id, board.engine_id) && Objects.equals(market_id, board.market_id) && Objects.equals(boardid, board.boardid) && Objects.equals(board_title, board.board_title) && Objects.equals(is_traded, board.is_traded) && Objects.equals(has_candles, board.has_candles) && Objects.equals(is_primary, board.is_primary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, board_group_id, engine_id, market_id, boardid, board_title, is_traded, has_candles, is_primary);
    }
}
