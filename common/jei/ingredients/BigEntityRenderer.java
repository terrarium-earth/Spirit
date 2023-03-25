package earth.terrarium.spirit.compat.jei.ingredients;

public class BigEntityRenderer extends EntityRenderer {
    public static final BigEntityRenderer INSTANCE = new BigEntityRenderer();

    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 24;
    }
}
