package br.com.tpaimyu.swingy.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import br.com.tpaimyu.swingy.models.Artifact;
import br.com.tpaimyu.swingy.models.ArtifactType;
import br.com.tpaimyu.swingy.models.Hero;

public class HeroRepository implements HeroStore {
    private final Path saveDirectory;

    public HeroRepository() {
        this(Paths.get(System.getProperty("user.home"), ".swingy", "heroes"));
    }

    HeroRepository(Path saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    public void save(Hero hero) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("name", hero.getName());
        properties.setProperty("heroClass", hero.getHeroClass());
        properties.setProperty("level", Integer.toString(hero.getLevel()));
        properties.setProperty("experience", Integer.toString(hero.getExperience()));
        properties.setProperty("attack", Integer.toString(hero.getBaseAttack()));
        properties.setProperty("defense", Integer.toString(hero.getBaseDefense()));
        properties.setProperty("hitPoints", Integer.toString(hero.getBaseHitPoints()));
        saveArtifact(properties, "weapon", hero.getWeapon());
        saveArtifact(properties, "armor", hero.getArmor());
        saveArtifact(properties, "helmet", hero.getHelmet());

        Files.createDirectories(saveDirectory);
        Path saveFile = saveDirectory.resolve(toFileName(hero.getName()));
        try (OutputStream output = Files.newOutputStream(saveFile)) {
            properties.store(output, "Swingy hero save");
        }
    }

    public Optional<Hero> load() throws IOException {
        List<Hero> heroes = loadAll();
        return heroes.isEmpty() ? Optional.empty() : Optional.of(heroes.get(0));
    }

    public List<Hero> loadAll() throws IOException {
        if (!Files.isDirectory(saveDirectory)) {
            return List.of();
        }

        List<Hero> heroes = new ArrayList<>();
        try (var files = Files.list(saveDirectory)) {
            files.filter(path -> path.getFileName().toString().endsWith(".properties"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .forEach(path -> {
                        try {
                            heroes.add(loadFile(path));
                        } catch (IOException | RuntimeException ignored) {
                            // Ignore corrupted saves and keep valid heroes available.
                        }
                    });
        }
        return heroes;
    }

    public Optional<Hero> load(int index) throws IOException {
        List<Hero> heroes = loadAll();
        if (index < 0 || index >= heroes.size()) {
            return Optional.empty();
        }
        return Optional.of(heroes.get(index));
    }

    private Hero loadFile(Path saveFile) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(saveFile)) {
            properties.load(input);
        }

        Hero hero = new Hero.HeroBuilder()
                .setName(properties.getProperty("name"))
                .setHeroClass(properties.getProperty("heroClass"))
                .setLevel(Integer.parseInt(properties.getProperty("level")))
                .setExperience(Integer.parseInt(properties.getProperty("experience")))
                .setAttack(Integer.parseInt(properties.getProperty("attack")))
                .setDefense(Integer.parseInt(properties.getProperty("defense")))
                .setHitPoints(Integer.parseInt(properties.getProperty("hitPoints")))
                .setWeapon(loadArtifact(properties, "weapon"))
                .setArmor(loadArtifact(properties, "armor"))
                .setHelmet(loadArtifact(properties, "helmet"))
                .build();
        return hero;
    }

    private String toFileName(String heroName) {
        String safeName = heroName.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return (safeName.isBlank() ? "hero" : safeName) + ".properties";
    }

    private void saveArtifact(Properties properties, String prefix, Artifact artifact) {
        if (artifact == null) {
            return;
        }
        properties.setProperty(prefix + ".type", artifact.getType().name());
        properties.setProperty(prefix + ".bonus", Integer.toString(artifact.getBonus()));
        properties.setProperty(prefix + ".name", artifact.getName());
    }

    private Artifact loadArtifact(Properties properties, String prefix) {
        String type = properties.getProperty(prefix + ".type");
        if (type == null) {
            return null;
        }
        return new Artifact.ArtifactBuilder()
                .setType(ArtifactType.valueOf(type))
                .setBonus(Integer.parseInt(properties.getProperty(prefix + ".bonus")))
                .setName(properties.getProperty(prefix + ".name"))
                .build();
    }
}