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
public class Market extends AbstractMoexJsonEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long trade_engine_id;
    private String trade_engine_name;
    private String trade_engine_title;
    private String market_name;
    private String market_title;
    private Long market_id;
    private String marketplace;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Market market = (Market) o;
        return Objects.equals(id, market.id) && Objects.equals(trade_engine_id, market.trade_engine_id) && Objects.equals(trade_engine_name, market.trade_engine_name) && Objects.equals(trade_engine_title, market.trade_engine_title) && Objects.equals(market_name, market.market_name) && Objects.equals(market_title, market.market_title) && Objects.equals(market_id, market.market_id) && Objects.equals(marketplace, market.marketplace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trade_engine_id, trade_engine_name, trade_engine_title, market_name, market_title, market_id, marketplace);
    }
}
