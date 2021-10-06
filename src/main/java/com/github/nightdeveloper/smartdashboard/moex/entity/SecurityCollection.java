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
public class SecurityCollection extends AbstractMoexJsonEntity {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
    private String name;
	private String title;
    private Long security_group_id;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SecurityCollection that = (SecurityCollection) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(title, that.title) && Objects.equals(security_group_id, that.security_group_id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, title, security_group_id);
	}
}
