package earth.terrarium.spirit.api.abilities;

public record ColorPalette(int primary, int slightyDarker, int slightlyLighter) {
    public ColorPalette(int primary) {
        this(primary, darken(primary, 0.1), brighten(primary, 0.1));
    }

    public static int darken(int color, double factor) {
        int red = (int) ((color >> 16) & 0xFF);
        int green = (int) ((color >> 8) & 0xFF);
        int blue = (int) (color & 0xFF);
        red = (int) Math.max(0, red - 255 * factor);
        green = (int) Math.max(0, green - 255 * factor);
        blue = (int) Math.max(0, blue - 255 * factor);
        return (red << 16) | (green << 8) | blue;
    }

    public static int brighten(int color, double factor) {
        int red = (int) ((color >> 16) & 0xFF);
        int green = (int) ((color >> 8) & 0xFF);
        int blue = (int) (color & 0xFF);
        red = (int) Math.min(255, red + 255 * factor);
        green = (int) Math.min(255, green + 255 * factor);
        blue = (int) Math.min(255, blue + 255 * factor);
        return (red << 16) | (green << 8) | blue;
    }

    public int[] asArray() {
        return new int[] {slightlyLighter, primary, slightyDarker};
    }
}
