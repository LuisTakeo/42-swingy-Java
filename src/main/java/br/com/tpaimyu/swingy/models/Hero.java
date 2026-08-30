package br.com.tpaimyu.swingy.models;

import jakarta.validation.constraints.NotBlank;

public class Hero extends Character {
    @NotBlank(message = "Hero class cannot be blank")
    final private String heroClass;

    private Hero(HeroBuilder builder) {
        this.name = builder.name;
        this.level = builder.level;
        this.attack = builder.attack;
        this.hitPoints = builder.hitPoints;
        this.heroClass = builder.heroClass;
    }

    public String getHeroClass() {
        return heroClass;
    }

    public static class HeroBuilder extends Character.Builder<HeroBuilder> {
        private String heroClass;

        @Override
        protected HeroBuilder self() {
            return this;
        }

        public HeroBuilder setHeroClass(String heroClass) {
            this.heroClass = heroClass;
            return self();
        }

        @Override
        public Hero build() {
            return new Hero(this);
        }
    }

}
