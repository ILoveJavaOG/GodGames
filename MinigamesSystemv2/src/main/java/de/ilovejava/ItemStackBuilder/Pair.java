package de.ilovejava.ItemStackBuilder;

import lombok.Getter;

import java.util.Objects;

public class Pair<T, S> {

	@Getter
	private final T first;

	@Getter
	private final S second;

	public Pair(T first, S second) {
		this.first = first;
		this.second = second;
	}

	public String toString() {
		return "(" + this.first + ", " + this.second +  ")";
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Pair)) {
			return false;
		} else {
			Pair<?, ?> other = (Pair<?, ?>) obj;
			return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
		}
	}

	public int hashCode() {
		return com.google.common.base.Objects.hashCode(this.first, this.second);
	}

	public static <T, S> Pair<T, S> of(T first, S second) {
		return new Pair<>(first, second);
	}
}
