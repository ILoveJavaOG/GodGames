package de.ilovejava.ItemStackBuilder;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Quadruple<L, S, T, E> {

	@Setter
	@Getter
	private L first;

	@Setter
	@Getter
	private S second;

	@Setter
	@Getter
	private T third;

	@Setter
	@Getter
	private E fourth;

	public Quadruple(L first, S second, T third, E fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	public String toString() {
		return "(" + this.first + ", " + this.second + ", " + this.third + ", " + this.fourth + ")";
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Quadruple)) {
			return false;
		} else {
			Quadruple<?, ?, ?, ?> other = (Quadruple<?, ?, ?, ?>) obj;
			return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second) && Objects.equals(this.third, other.third) && Objects.equals(this.fourth, other.fourth);
		}
	}

	public int hashCode() {
		return com.google.common.base.Objects.hashCode(this.first, this.second, this.third, this.fourth);
	}

	public static <L, S, T, E> Quadruple<L, S, T, E> of(L first, S second, T third, E fourth) {
		return new Quadruple<>(first, second, third, fourth);
	}
}
