package de.ilovejava.ItemStackBuilder;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Triple<L, S, T> {

	@Setter
	@Getter
	private L first;

	@Setter
	@Getter
	private S second;

	@Setter
	@Getter
	private T third;

	public Triple(L first, S second, T third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public String toString() {
		return "(" + this.first + ", " + this.second + ", " + this.third +  ")";
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Triple)) {
			return false;
		} else {
			Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
			return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second) && Objects.equals(this.third, other.third);
		}
	}

	public int hashCode() {
		return com.google.common.base.Objects.hashCode(this.first, this.second, this.third);
	}

	public static <L, S, T> Triple<L, S, T> of(L first, S second, T third) {
		return new Triple<>(first, second, third);
	}
}
