package me.codexadrian.spirit.compat.jei.multiblock;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class RotatableList<T> extends ArrayList<T> {
    private int index;

    public RotatableList(@NotNull Collection<? extends T> c) {
        super(c);
    }

    public T getCurrent() {
        return this.get(index);
    }

    public void next() {
        this.index = this.size() == 0 ? this.index : (this.index + 1) % this.size();
    }

}
