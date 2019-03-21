package net.darkscorner.paintball;

public enum GunType {

    STANDARD(0), SHOTGUN(1), MACHINE_GUN(2), SNIPER(3);

    private final int gun;

    GunType(int gun) {
        this.gun = gun;
    }

    public int getGun() {
        return gun;
    }
}
