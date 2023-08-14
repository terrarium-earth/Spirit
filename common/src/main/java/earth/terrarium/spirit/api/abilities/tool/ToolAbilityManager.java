package earth.terrarium.spirit.api.abilities.tool;

import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class ToolAbilityManager {
    private static final Map<String, ToolAbility> ABILITY_MAP = new HashMap<>();

    public static final ToolAbility BLANK = registerAbility("spirit:blank", new BlankToolAbility());

    public static ToolAbility registerAbility(String name, ToolAbility ability) {
        ABILITY_MAP.put(name, ability);
        return ability;
    }

    public static ToolAbility getAbility(String name) {
        return ABILITY_MAP.get(name);
    }

    public static String getName(ToolAbility ability) {
        for (Map.Entry<String, ToolAbility> entry : ABILITY_MAP.entrySet()) {
            if (entry.getValue() == ability) {
                return entry.getKey();
            }
        }
        throw new NotImplementedException("Ability " + ability + " is not registered!");
    }
}
