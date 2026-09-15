package br.com.tpaimyu.swingy.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.tpaimyu.swingy.models.Artifact;
import br.com.tpaimyu.swingy.models.ArtifactType;
import br.com.tpaimyu.swingy.models.Hero;

public class JdbcHeroRepository implements HeroStore {
    private final String url;
    private final String user;
    private final String password;

    public JdbcHeroRepository(String url, String user, String password) throws IOException {
        this.url = url;
        this.user = user;
        this.password = password;
        initializeSchema();
    }

    @Override
    public void save(Hero hero) throws IOException {
        String sql = """
                INSERT INTO heroes (name, hero_class, level, experience, attack, defense, hit_points,
                    weapon_type, weapon_bonus, weapon_name, armor_type, armor_bonus, armor_name,
                    helmet_type, helmet_bonus, helmet_name)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT (name) DO UPDATE SET hero_class = EXCLUDED.hero_class,
                    level = EXCLUDED.level, experience = EXCLUDED.experience,
                    attack = EXCLUDED.attack, defense = EXCLUDED.defense,
                    hit_points = EXCLUDED.hit_points, weapon_type = EXCLUDED.weapon_type,
                    weapon_bonus = EXCLUDED.weapon_bonus, weapon_name = EXCLUDED.weapon_name,
                    armor_type = EXCLUDED.armor_type, armor_bonus = EXCLUDED.armor_bonus,
                    armor_name = EXCLUDED.armor_name, helmet_type = EXCLUDED.helmet_type,
                    helmet_bonus = EXCLUDED.helmet_bonus, helmet_name = EXCLUDED.helmet_name
                """;
        try (Connection connection = connection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            int index = 1;
            statement.setString(index++, hero.getName());
            statement.setString(index++, hero.getHeroClass());
            statement.setInt(index++, hero.getLevel());
            statement.setInt(index++, hero.getExperience());
            statement.setInt(index++, hero.getBaseAttack());
            statement.setInt(index++, hero.getBaseDefense());
            statement.setInt(index++, hero.getBaseHitPoints());
            index = setArtifact(statement, index, hero.getWeapon());
            index = setArtifact(statement, index, hero.getArmor());
            setArtifact(statement, index, hero.getHelmet());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IOException("Could not save hero to database", exception);
        }
    }

    @Override
    public List<Hero> loadAll() throws IOException {
        List<Hero> heroes = new ArrayList<>();
        String sql = "SELECT * FROM heroes ORDER BY name";
        try (Connection connection = connection(); Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql)) {
            while (result.next()) {
                heroes.add(readHero(result));
            }
            return heroes;
        } catch (SQLException exception) {
            throw new IOException("Could not load heroes from database", exception);
        }
    }

    @Override
    public Optional<Hero> load(int index) throws IOException {
        List<Hero> heroes = loadAll();
        return index >= 0 && index < heroes.size() ? Optional.of(heroes.get(index)) : Optional.empty();
    }

    private void initializeSchema() throws IOException {
        String sql = """
                CREATE TABLE IF NOT EXISTS heroes (
                    name VARCHAR(120) PRIMARY KEY, hero_class VARCHAR(80) NOT NULL,
                    level INTEGER NOT NULL, experience INTEGER NOT NULL,
                    attack INTEGER NOT NULL, defense INTEGER NOT NULL, hit_points INTEGER NOT NULL,
                    weapon_type VARCHAR(20), weapon_bonus INTEGER, weapon_name VARCHAR(120),
                    armor_type VARCHAR(20), armor_bonus INTEGER, armor_name VARCHAR(120),
                    helmet_type VARCHAR(20), helmet_bonus INTEGER, helmet_name VARCHAR(120)
                )
                """;
        try (Connection connection = connection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException exception) {
            throw new IOException("Could not initialize database schema", exception);
        }
    }

    private Connection connection() throws SQLException {
        return java.sql.DriverManager.getConnection(url, user, password);
    }

    private int setArtifact(PreparedStatement statement, int index, Artifact artifact) throws SQLException {
        if (artifact == null) {
            statement.setNull(index++, java.sql.Types.VARCHAR);
            statement.setNull(index++, java.sql.Types.INTEGER);
            statement.setNull(index++, java.sql.Types.VARCHAR);
        } else {
            statement.setString(index++, artifact.getType().name());
            statement.setInt(index++, artifact.getBonus());
            statement.setString(index++, artifact.getName());
        }
        return index;
    }

    private Hero readHero(ResultSet result) throws SQLException {
        return new Hero.HeroBuilder()
                .setName(result.getString("name"))
                .setHeroClass(result.getString("hero_class"))
                .setLevel(result.getInt("level"))
                .setExperience(result.getInt("experience"))
                .setAttack(result.getInt("attack"))
                .setDefense(result.getInt("defense"))
                .setHitPoints(result.getInt("hit_points"))
                .setWeapon(readArtifact(result, "weapon"))
                .setArmor(readArtifact(result, "armor"))
                .setHelmet(readArtifact(result, "helmet"))
                .build();
    }

    private Artifact readArtifact(ResultSet result, String prefix) throws SQLException {
        String type = result.getString(prefix + "_type");
        if (type == null) {
            return null;
        }
        return new Artifact.ArtifactBuilder()
                .setType(ArtifactType.valueOf(type))
                .setBonus(result.getInt(prefix + "_bonus"))
                .setName(result.getString(prefix + "_name"))
                .build();
    }
}