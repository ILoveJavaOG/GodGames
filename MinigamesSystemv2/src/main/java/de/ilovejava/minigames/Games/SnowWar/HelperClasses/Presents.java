package de.ilovejava.minigames.Games.SnowWar.HelperClasses;

import lombok.Getter;
import org.bukkit.block.BlockFace;

import java.util.Arrays;
import java.util.Random;

/**
 * Enum to store possible presents
 */
public enum Presents {

	RED("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdlNmI3NDI5NzAyMDk3NWNkN2VjYWU1NDdlNWEwYmI5ZmVlYWM1ODk3NGE5NGNiYTY2ZDVmZmUifX19"),
	RED2("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjlhYTE0ZTg4NDc3M2VhYzEzNGE0ZWU4OTcyMDYzZjQ2NmRlNjc4MzYzY2Y3YjFhMjFhODViNyJ9fX0="),
	RED3("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWNhNjQzYTUzOWMyNzU3NTU3MzcwOGM1YTQyNmFkNjIwM2JjNTE5NzQ5ZmIxNDllZjdmMWIyYTY4MzM5OCJ9fX0="),
	BLUE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjU2MTJkYzdiODZkNzFhZmMxMTk3MzAxYzE1ZmQ5NzllOWYzOWU3YjFmNDFkOGYxZWJkZjgxMTU1NzZlMmUifX19"),
	GREEN("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTNjZmJmMmJkZmQ0ODUxNGJmYmFjZTk1MThjNzY2NDExMmRmMmMxNzNlOGM3YWQ5MmIzZTY1NjIxYTllZDZlMCJ9fX0="),
	GREEN2("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRhYmU4MWU2ZjQ5NjFlMGY2YmQ4MmYyZDQxMzViNmI1ZmM4NDU3MzllNzFjZmUzYjg5NDM1MzFkOTIxZSJ9fX0="),
	ORANGE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUzYThmZDA4NTI5Nzc0NDRkOWZkNzc5N2NhYzA3YjhkMzk0OGFkZGM0M2YwYmI1Y2UyNWFlNzJkOTVkYyJ9fX0="),
	YELLOW("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNlNThlYTdmMzExM2NhZWNkMmIzYTZmMjdhZjUzYjljYzljZmVkN2IwNDNiYTMzNGI1MTY4ZjEzOTFkOSJ9fX0="),
	PURPLE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFjYjNjMWUxYjM0Zjg3MzRhZWRmYWJkMWUxZjVlMGIyODBiZWY5MjRmYjhiYmYzZTY5MmQyNTM4MjY2ZjQifX19");

	//Array of valid block faces. Excludes UP, SELF and DOWN
	private static final BlockFace[] validFaces = Arrays.stream(BlockFace.values()).filter(face -> face != BlockFace.SELF && face != BlockFace.DOWN && face != BlockFace.UP).toArray(BlockFace[]::new);

	//Base64 String of the texture
	@Getter
	private final String data;

	/**
	 * Constructor for the enum value
	 *
	 * @param value(String): Base64 String
	 */
	Presents(String value) {
		this.data = value;
	}

	/**
	 * Function to retrieve a random present
	 *
	 * @return Random Base64 String from the available presents
	 */
	public static String randomPresent() {
		return values()[new Random().nextInt(values().length)].data;
	}

	/**
	 * Function to retrieve a random BlockFace
	 *
	 * @return Random BlockFace from the validFaces array
	 */
	public static BlockFace randomRotation() {
		return validFaces[new Random().nextInt(validFaces.length)];
	}
}
