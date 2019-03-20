package net.darkscorner.paintball;

public enum GunType {

    STANDARD(0), SHOTGUN(1);

    private final int gun;

    GunType(int gun) {
        this.gun = gun;
    }

    public int getGun() {
        return gun;
    }
}
