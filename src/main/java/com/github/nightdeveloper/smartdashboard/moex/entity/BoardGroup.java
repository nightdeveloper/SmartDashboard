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
public class BoardGroup extends AbstractMoexJsonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long trade_engine_id;
    private String trade_engine_name;
    private String trade_engine_title;
    private Long market_id;
    private String market_name;
    private String name;
    private String title;
    private Long is_default;
    private Long board_group_id;
    private Long is_traded;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGroup that = (BoardGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(trade_engine_id, that.trade_engine_id) && Objects.equals(trade_engine_name, that.trade_engine_name) && Objects.equals(trade_engine_title, that.trade_engine_title) && Objects.equals(market_id, that.market_id) && Objects.equals(market_name, that.market_name) && Objects.equals(name, that.name) && Objects.equals(title, that.title) && Objects.equals(is_default, that.is_default) && Objects.equals(board_group_id, that.board_group_id) && Objects.equals(is_traded, that.is_traded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trade_engine_id, trade_engine_name, trade_engine_title, market_id, market_name, name, title, is_default, board_group_id, is_traded);
    }
}
