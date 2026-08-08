package com.projectchronicles.core.combat;

public enum ChroniclesClass {
    WARDEN("Страж", "Выдерживает больше урона и защищает союзников."),
    HUNTER("Охотник", "Быстрый дальний бой и усиленный урон по целям."),
    SEEKER("Искатель", "Мобильность, исследование и универсальные способности.");

    private final String displayName;
    private final String description;

    ChroniclesClass(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String displayName() { return displayName; }
    public String description() { return description; }
}
