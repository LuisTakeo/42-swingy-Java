package br.com.tpaimyu.swingy.models;

import jakarta.validation.constraints.NotBlank;

public class Hero extends Character {
    @NotBlank(message = "Hero class cannot be blank")
    final private String heroClass;

    private Artifact weapon;
    private Artifact armor;
    private Artifact helmet;

    private Hero(HeroBuilder builder) {
        this.name = builder.name;
        this.level = builder.level;
        this.attack = builder.attack;
        this.defense = builder.defense;
        this.hitPoints = builder.hitPoints;
        this.heroClass = builder.heroClass;
        this.weapon = builder.weapon;
        this.armor = builder.armor;
        this.helmet = builder.helmet;
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
