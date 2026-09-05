package br.com.tpaimyu.swingy.models;

import br.com.tpaimyu.swingy.config.ValidatorConfig;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Artifact {
    @NotNull(message = "Artifact type cannot be null")
    private final ArtifactType type;
    @Min(value = 1, message = "Artifact bonus must be at least 1")
    private final int bonus;
    @NotBlank(message = "Artifact name cannot be blank")
    private final String name;

    private Artifact(ArtifactBuilder builder) {
        this.type = builder.type;
        this.bonus = builder.bonus;
        this.name = builder.name;
    }

    public ArtifactType getType() {
        return type;
    }

    public int getBonus() {
        return bonus;
    }

    public String getName() {
        return name;
    }

    public static class ArtifactBuilder {
        private ArtifactType type;
        private int bonus;
        private String name;
        private final Validator VALIDATOR = ValidatorConfig.getValidator();

        public ArtifactBuilder setType(ArtifactType type) {
            this.type = type;
            return this;
        }

        public ArtifactBuilder setBonus(int bonus) {
            this.bonus = bonus;
            return this;
        }

        public ArtifactBuilder setName(String name) {
            this.name = name;
            return this;
        }

        private void validate(Artifact artifact) {
            var violations = VALIDATOR.validate(artifact);
            if (!violations.isEmpty()) {
                throw new jakarta.validation.ConstraintViolationException(violations);
            }
        }

        public Artifact build() {
            Artifact artifact = new Artifact(this);
            this.validate(artifact);
            return artifact;
        }
    }

}
