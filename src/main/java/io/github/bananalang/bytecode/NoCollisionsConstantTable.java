package io.github.bananalang.bytecode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.bananalang.bytecode.constants.BBCConstant;

public final class NoCollisionsConstantTable {
    private final List<BBCConstant> table = new ArrayList<>();
    private final Map<BBCConstant, Integer> indices = new HashMap<>();
    private int nextIndex = 0;

    public NoCollisionsConstantTable() {
    }

    public NoCollisionsConstantTable(Collection<? extends BBCConstant> col) {
        addAll(col);
    }

    public List<BBCConstant> getTable() {
        return Collections.unmodifiableList(table);
    }

    public int add(BBCConstant c) {
        Integer index = indices.putIfAbsent(c, nextIndex);
        if (index == null) {
            table.add(c);
            return nextIndex++;
        }
        return (int)index;
    }

    public int getIndex(BBCConstant c) {
        Integer index = indices.get(c);
        return index == null ? -1 : index.intValue();
    }

    public BBCConstant get(int index) {
        return table.get(index);
    }

    public int remove(BBCConstant c) {
        Integer index = indices.remove(c);
        return index == null ? -1 : index.intValue();
    }

    public int addAll(Collection<? extends BBCConstant> col) {
        int start = nextIndex;
        for (BBCConstant c : col) {
            add(c);
        }
        return start;
    }
}
