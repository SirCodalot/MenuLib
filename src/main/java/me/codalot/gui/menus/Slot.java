package me.codalot.gui.menus;

import lombok.Getter;
import org.bukkit.inventory.Inventory;

@Getter
public class Slot {

    private int x;
    private int y;

    private int slot;

    public Slot(int x, int y) {
        this.x = x;
        this.y = y;
        completeSlot();
    }

    public Slot(int slot) {
        this.slot = slot;
        completePositions();
    }

    public Slot(String serialized) {
        String[] split = serialized.replace(" ", "").split(",");

        if (split.length < 2) {
            this.slot = Integer.parseInt(split[0]);
            completePositions();
        } else {
            this.x = Integer.parseInt(split[0]);
            this.y = Integer.parseInt(split[1]);
            completeSlot();
        }
    }

    public Slot(Object object) {
        if (object instanceof String) {
            String[] split = ((String) object).replace(" ", "").split(",");

            if (split.length < 2) {
                this.slot = Integer.parseInt(split[0]);
                completePositions();
            } else {
                this.x = Integer.parseInt(split[0]);
                this.y = Integer.parseInt(split[1]);
                completeSlot();
            }
        } else if (object instanceof Integer) {
            this.slot = (Integer) object;
            completePositions();
        } else {
            x = 0;
            y = 0;
            slot = 0;
        }
    }

    public Slot() {
        this(0, 0);
    }

    private void completePositions() {
        y = slot / 9;
        x = slot - y * 9;
    }

    private void completeSlot() {
        slot = y * 9 + x;
    }

    public boolean isOutside(Inventory inventory) {
        return slot >= inventory.getSize();
    }

    public Slot add(int x, int y) {
        this.x += x;
        this.y += y;
        completeSlot();

        return this;
    }

    public Slot subtract(int x, int y) {
        this.x -= x;
        this.y -= y;
        completeSlot();

        return this;
    }

    public Slot add(Slot other) {
        return add(other.x, other.y);
    }

    public Slot subtract(Slot other) {
        return subtract(other.x, other.y);
    }

    public boolean equals(Slot other) {
        return x == other.x && y == other.y;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Slot && equals((Slot) obj);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    @SuppressWarnings("all")
    public Slot clone() {
        return new Slot(x ,y);
    }
}
