package br.com.tpaimyu.swingy.models;

public class Villain extends Character {

    // Se o vilão tiver algo único (ex: um drop específico de XP), entra aqui.
    // O level, attack, name, etc, já estão no pai!

    private Villain(VillainBuilder builder) {
        super(builder);
    }

    public static class VillainBuilder extends Character.Builder<VillainBuilder> {

        @Override
        protected VillainBuilder self() {
            return this;
        }

        @Override
        public Villain build() {
            Villain villain = new Villain(this);
            validate(villain); 
            return villain;
        }
    }
}