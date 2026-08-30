package br.com.tpaimyu.swingy.models;

public abstract class Character {
    protected String name;
    protected int level;
    protected int attack;
    protected int hitPoints;

    public abstract static class Builder<T extends Builder<T>> {
        protected String name;
        protected int level;
        protected int attack;
        protected int hitPoints;

        protected abstract T self();

        public T setName(String name) {
            this.name = name;
            return self();
        }

        public T setLevel(int level) {
            this.level = level;
            return self();
        }

        public T setAttack(int attack) {
            this.attack = attack;
            return self();
        }

        public T setHitPoints(int hitPoints) {
            this.hitPoints = hitPoints;
            return self();
        }

        public abstract Character build();
    }
}
