package com.projectchronicles.core.cosmetic;

public record Cosmetic(String id, String name, String description, CosmeticType type) {
    public enum CosmeticType { TITLE, PARTICLE, PET }
}
