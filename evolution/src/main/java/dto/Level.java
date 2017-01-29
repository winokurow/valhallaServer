package dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Level {

	private int number;
	private int gladiators_quantity;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getGladiators_quantity() {
		return gladiators_quantity;
	}

	public void setGladiators_quantity(int gladiators_quantity) {
		this.gladiators_quantity = gladiators_quantity;
	}

	public Level(int number, int gladiators_quantity) {
		super();
		this.number = number;
		this.gladiators_quantity = gladiators_quantity;
	}

}
