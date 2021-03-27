package de.ilovejava.minigames.Games.SnowWar;

import de.ilovejava.minigames.MapTools.Options;

public enum GameOptions implements Options {

	TIME_BUILDING("Zeit bis das Bauen beginnt (in Sekunden)", 300),
	NUM_TEAMS("Anzahl der Teams für die Map", 2),
	DELAY_SNOWMAN("Zeit bis Scheemänner erneut gesetzt werden (in Sekunden)", 30),
	DELAY_PRESENT("Zeit bis Scheemänner erneut gesetzt werden (in Sekunden)", 30),
	MAX_NUM_SNOWMAN("Anzahl wie viele Schneemänner gleichzeitig auf dem Feld seien dürfen", 5),
	MAX_NUM_PRESENT("Anzahl wie viele Schneemänner gleichzeitig auf dem Feld seien dürfen", 5),
	MAX_GAMETIME("Maximale Spielzeit bis das Spiel beendet wird (in Sekunden)", -1),
	MAX_KILLS("Maximale Anzahl der Kills eines Teams bis das Spiel beendet wird", 50),
	HITPOINTS("Anzahl wie oft ein Spieler getroffen werdend darf", 5),
	NUM_DROP_SNOWBALL("Anzahl wie viele Schneebälle maximal gedroppt werden beim Tod", 10),
	NUM_DROP_SNOWBLOCK("Anzahl wie viele Schneeblöcke maximal gedroppt werden beim Tod", 5),
	TIME_SONAR("Zeit wie lange das Sonar hält (in Sekunden)", 15),
	NUM_DROP_SNOWBALL_SUPPLY_DROP("Maximale Anzahl wie viele Schneebälle pro Supply Drop Iteration gespawnt werden", 5),
	NUM_DROP_SNOWBLOCK_SUPPLY_DROP("Maximale Anzahl wie viele Schneeblöcke pro Supply Drop Iteration gespawnt werden", 3),
	NUM_ITERATIONS_SUPPLY_DROP("Anzahl der Supply Drop Iterationen", 5),
	MAX_TAKE_SNOWBALL("Anzahl wie viele Schneebälle ein Schneemann maximal abgeben kann", 20),
	SNOWMAN_COOLDOWN("Zeit ab wann ein Schneeman wieder berücksichtigt wird zum respawnen (in Sekunden)", 30),
	PRESENT_COOLDOWN("Zeit ab wann ein Geschenk wieder berücksichtigt wird zum respawnen (in Sekunden)", 30),
	RAPID_FIRE_DURATION("Zeit bis wann Rapid Fire hält", 10),
	BOMBER_DROPS("Anzahl der Bombadierungen für den Bomber", 5),
	DROP_HEIGHT("Höhe für Supply Drop und Bomber (Max 255)", 255.0);

	private final String description;
	private final Object defaultValue;

	GameOptions(String description, Object defaultValue) {
		this.description = description;
		this.defaultValue = defaultValue;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}
}
