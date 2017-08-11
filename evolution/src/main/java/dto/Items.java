package dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Items {

	private String id;
	private String name;
	private String usageTyp;
	private String typ;
	private String range;
	private String damage;
	private String damageTyp;
	private String bonus;
	private String speed;
	private String description;
	private String fans;
	private String dex;
	private String isThrow;
	private String stamina;
	private String created_at;
	private String updated_at;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsageTyp() {
		return usageTyp;
	}

	public void setUsageTyp(String usageTyp) {
		this.usageTyp = usageTyp;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getDamage() {
		return damage;
	}

	public void setDamage(String damage) {
		this.damage = damage;
	}

	public String getDamageTyp() {
		return damageTyp;
	}

	public void setDamageTyp(String damageTyp) {
		this.damageTyp = damageTyp;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFans() {
		return fans;
	}

	public void setFans(String fans) {
		this.fans = fans;
	}

	public String getDex() {
		return dex;
	}

	public void setDex(String dex) {
		this.dex = dex;
	}

	public String getIsThrow() {
		return isThrow;
	}

	public void setIsThrow(String isThrow) {
		this.isThrow = isThrow;
	}

	public String getStamina() {
		return stamina;
	}

	public void setStamina(String stamina) {
		this.stamina = stamina;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public Items(String id, String name, String usageTyp, String typ, String range, String damage, String damageTyp,
			String bonus, String speed, String description, String fans, String dex, String isThrow, String stamina,
			String created_at, String updated_at) {
		super();
		this.id = id;
		this.name = name;
		this.usageTyp = usageTyp;
		this.typ = typ;
		this.range = range;
		this.damage = damage;
		this.damageTyp = damageTyp;
		this.bonus = bonus;
		this.speed = speed;
		this.description = description;
		this.fans = fans;
		this.dex = dex;
		this.isThrow = isThrow;
		this.stamina = stamina;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

}
