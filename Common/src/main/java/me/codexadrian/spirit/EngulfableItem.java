package me.codexadrian.spirit;

public interface EngulfableItem {

    void resetEngulfing();
    void setMaxEngulfTime(int duration);
    boolean isEngulfed();
    boolean isFullyEngulfed();

}
