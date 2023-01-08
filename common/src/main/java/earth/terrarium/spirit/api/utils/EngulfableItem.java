package earth.terrarium.spirit.api.utils;

public interface EngulfableItem {

    void resetEngulfing();
    void setMaxEngulfTime(int duration);
    boolean isEngulfed();
    boolean isFullyEngulfed();
    boolean isRecipeOutput();
    void setRecipeOutput();

}
