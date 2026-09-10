package br.com.tpaimyu.swingy.models;

import java.util.Random;

public class ArtifactFactory {
    
    private static final Random random = new Random();

    private static final String[] WEAPON_NAMES = {"Longsword", "Battle Axe", "Magic Wand", "Dagger of Swiftness"};
    private static final String[] ARMOR_NAMES = {"Iron Plate", "Leather Tunic", "Dragon Scale Armor", "Mithril Chainmail"};
    private static final String[] HELMET_NAMES = {"Steel Helm", "Wizard Hat", "Crown of Fortitude", "Viking Helmet"};


    public static Artifact generateLoot(int villainStrength) {
        
        ArtifactType[] types = ArtifactType.values();
        ArtifactType droppedType = types[random.nextInt(types.length)];
        int bonus = villainStrength + random.nextInt(3) + 1; 
        
        String name = generateName(droppedType);

        return new Artifact.ArtifactBuilder()
                .setType(droppedType)
                .setBonus(bonus)
                .setName(name)
                .build();
    }

    private static String generateName(ArtifactType type) {
        return switch (type) {
            case WEAPON -> WEAPON_NAMES[random.nextInt(WEAPON_NAMES.length)];
            case ARMOR -> ARMOR_NAMES[random.nextInt(ARMOR_NAMES.length)];
            case HELMET -> HELMET_NAMES[random.nextInt(HELMET_NAMES.length)];
            default -> "Unknown Artifact";
        };
    }
}