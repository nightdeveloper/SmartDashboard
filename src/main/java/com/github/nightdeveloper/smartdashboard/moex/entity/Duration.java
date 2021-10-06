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
public class Duration extends AbstractMoexJsonEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long interval;
    private Long duration;
    private Long days;
    private String title;
    private String hint;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duration duration1 = (Duration) o;
        return Objects.equals(interval, duration1.interval) && Objects.equals(duration, duration1.duration) && Objects.equals(days, duration1.days) && Objects.equals(title, duration1.title) && Objects.equals(hint, duration1.hint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interval, duration, days, title, hint);
    }
}
