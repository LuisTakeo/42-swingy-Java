package br.com.tpaimyu.swingy.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class Hero extends Character {
    @NotBlank(message = "Hero class cannot be blank")
    final private String heroClass;

    private Artifact weapon;
    private Artifact armor;
    private Artifact helmet;
    @PositiveOrZero(message = "Experience must be zero or positive")
    private int experience;
    @Positive(message = "Next level experience must be positive")
    private int nextLevelExperience;

    private Hero(HeroBuilder builder) {
        super(builder);
        this.heroClass = builder.heroClass;
        this.weapon = builder.weapon;
        this.armor = builder.armor;
        this.helmet = builder.helmet;
        this.experience = 0;
        this.nextLevelExperience = getRequiredXpForNextLevel();
    }

    public String getHeroClass() {
        return heroClass;
    }
    public Artifact getWeapon() {
        return weapon;
    }

    public Artifact getArmor() {
        return armor;
    }

    public Artifact getHelmet() {
        return helmet;
    }

    public void setWeapon(Artifact weapon) {
        this.weapon = weapon;
    }

    public void setArmor(Artifact armor) {
        this.armor = armor;
    }

    public void setHelmet(Artifact helmet) {
        this.helmet = helmet;
    }

    @Override
    public int getAttack() {
        int bonus = (weapon != null) ? weapon.getBonus() : 0;
        return super.getAttack() + bonus;
    }

    @Override
    public int getDefense() {
        int bonus = (armor != null) ? armor.getBonus() : 0;
        return super.getDefense() + bonus;
    }

    @Override
    public int getHitPoints() {
        int bonus = (helmet != null) ? helmet.getBonus() : 0;
        return super.getHitPoints() + bonus;
    }

    public void addExperience(int exp) {
        this.experience += exp;
        while (this.experience >= this.nextLevelExperience) {
            this.levelUp();
        }
    }

    @Positive(message = "Required experience for next level must be positive")
    private int getRequiredXpForNextLevel() {
        return (this.level * 1000) + (int)(Math.pow(this.level - 1, 2) * 450);
    }

    private void levelUp() {
        this.level++;
        this.attack += 5; 
        this.defense += 5; 
        this.hitPoints += 10; 
        
        this.nextLevelExperience = getRequiredXpForNextLevel();
    }


    public static class HeroBuilder extends Character.Builder<HeroBuilder> {
        private String heroClass;
        private Artifact weapon;
        private Artifact armor;
        private Artifact helmet;

        @Override
        protected HeroBuilder self() {
            return this;
        }

        public HeroBuilder setHeroClass(String heroClass) {
            this.heroClass = heroClass;
            return self();
        }

        public HeroBuilder setWeapon(Artifact weapon) {
            this.weapon = weapon;
            return self();
        }

        public HeroBuilder setArmor(Artifact armor) {
            this.armor = armor;
            return self();
        }

        public HeroBuilder setHelmet(Artifact helmet) {
            this.helmet = helmet;
            return self();
        }

        @Override
        public Hero build() {
            Hero hero = new Hero(this);
            this.validate(hero);
            return hero;
        }
    }

}
