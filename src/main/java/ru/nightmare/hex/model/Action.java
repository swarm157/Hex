package ru.nightmare.hex.model;

import java.util.Objects;

public class Action {
    Point executor;
    Point processable;
    ActionType type;
    Type build = null;

    public Action(Point executor, Point processable, ActionType type, Type build) {
        this.executor = executor;
        this.processable = processable;
        this.type = type;
        this.build = build;
    }

    public Point getExecutor() {
        return executor;
    }

    public void setExecutor(Point executor) {
        this.executor = executor;
    }

    public Point getProcessable() {
        return processable;
    }

    public void setProcessable(Point processable) {
        this.processable = processable;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Type getBuild() {
        return build;
    }

    public void setBuild(Type build) {
        this.build = build;
    }

    @Override
    public String toString() {
        return "Action{" +
                "executor=" + executor +
                ", processable=" + processable +
                ", type=" + type +
                ", build=" + build +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action action)) return false;
        return executor.equals(action.executor) && processable.equals(action.processable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executor, processable);
    }
}
