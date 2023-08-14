package earth.terrarium.spirit.api.abilities.armor;

import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class ArmorAbilityManager {
    private static final Map<String, ArmorAbility> ABILITY_MAP = new HashMap<>();

    public static final ArmorAbility BLANK = registerAbility("spirit:blank", new BlankArmorAbility());

    public static ArmorAbility registerAbility(String name, ArmorAbility ability) {
        ABILITY_MAP.put(name, ability);
        return ability;
    }

    public static ArmorAbility getAbility(String name) {
        return ABILITY_MAP.get(name);
    }

    public static String getName(ArmorAbility ability) {
        for (Map.Entry<String, ArmorAbility> entry : ABILITY_MAP.entrySet()) {
            if (entry.getValue() == ability) {
                return entry.getKey();
            }
        }
        throw new NotImplementedException("Ability " + ability + " is not registered!");
    }
}
