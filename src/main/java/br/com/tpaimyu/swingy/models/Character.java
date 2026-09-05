package br.com.tpaimyu.swingy.models;

import java.util.Set;

import br.com.tpaimyu.swingy.config.ValidatorConfig;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public abstract class Character {
    @NotBlank(message = "Name cannot be blank")
    protected String name;
    @Min(value = 1, message = "Level must be at least 1")
    protected int level;
    @Min(value = 1, message = "Attack must be at least 1")
    protected int attack;
    @Min(value = 1, message = "Hit points must be at least 1")
    protected int hitPoints;
    @PositiveOrZero(message = "Defense must be zero or positive")
    protected int defense;

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getAttack() {
        return attack;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getDefense() {
        return defense;
    }

    public abstract static class Builder<T extends Builder<T>> {
        protected String name;
        protected int level;
        protected int attack;
        protected int hitPoints;
        protected int defense;
        protected final Validator VALIDATOR = ValidatorConfig.getValidator();
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

        public T setDefense(int defense) {
            this.defense = defense;
            return self();
        }

        protected void validate(Character character) {
            Set<ConstraintViolation<Character>> violations = VALIDATOR.validate(character);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }

        public abstract Character build();
    }
}
