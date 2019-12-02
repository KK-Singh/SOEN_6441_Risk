package testModel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.Country;
import model.Player;

public class PlayerTest {

	@Test
	public void validateMoveAfterAttack() {
		Country c1 = new Country("c1");
		Country c2 = new Country("c1");
		c1.setArmyCount(10);
		c2.setArmyCount(0);
		
		Player p1 = new Player("p1");
		p1.fortify(c1, c2, 3);
		
		assertEquals(3, c2.getArmyCount());
	}
}
