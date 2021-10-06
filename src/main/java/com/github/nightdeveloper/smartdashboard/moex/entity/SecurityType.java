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
public class SecurityType extends AbstractMoexJsonEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long trade_engine_id;
    private String trade_engine_name;
    private String trade_engine_title;
    private String security_type_name;
    private String security_type_title;
    private String security_group_name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityType that = (SecurityType) o;
        return Objects.equals(id, that.id) && Objects.equals(trade_engine_id, that.trade_engine_id) && Objects.equals(trade_engine_name, that.trade_engine_name) && Objects.equals(trade_engine_title, that.trade_engine_title) && Objects.equals(security_type_name, that.security_type_name) && Objects.equals(security_type_title, that.security_type_title) && Objects.equals(security_group_name, that.security_group_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trade_engine_id, trade_engine_name, trade_engine_title, security_type_name, security_type_title, security_group_name);
    }
}
