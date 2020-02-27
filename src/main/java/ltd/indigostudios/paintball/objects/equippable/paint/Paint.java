package ltd.indigostudios.paintball.objects.equippable.paint;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ltd.indigostudios.paintball.objects.games.GameSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import ltd.indigostudios.paintball.Main;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class Paint {

	private String name;
	private Material displayIcon;
	//private List<Material> paintMaterials = new ArrayList<Material>();
	
	//private static Paint defaultPaint;
	static List<Paint> customPaints = new ArrayList<>();
	static FileConfiguration paintConfig;
	// default paint config
	/*
	public Paint(FileConfiguration config, Main main) {
		List<String> defaultPaintStrings = config.getStringList("default-paint");
		for(String defaultPaintString : defaultPaintStrings) {
			if(Material.getMaterial(defaultPaintString) != null) {
				Material material = Material.getMaterial(defaultPaintString);
				paintMaterials.add(material);
			} else {
				main.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid paint material: " + ChatColor.GRAY + defaultPaintString);
			}
		}
	}*/
	
	public Paint(String name, Material displayIcon) {
		this.name = name;
		this.displayIcon = displayIcon;
		// Don't add default paint into the list; it'll show in the menu
		if (!name.equals("default")) {
			customPaints.add(this);
		}
	}
	
	public String getName() {
		return name;
	}

	public String getPermission() {
		return "paintball.paint.colour." + getName().toLowerCase();
	}

	public Material getDisplayIcon() {
		return displayIcon;
	}

	Set<Location> getLocsAround(Location centre) {
		int radius = GameSettings.getSettings().getPaintRadius();
		Set<Location> locations = new HashSet<>();
		for (int x = radius * -1; x < radius + 1; x++) {
			for (int y = radius * -1; y < radius + 1; y++) {
				for (int z = radius * -1; z < radius + 1; z++) {
					Location location = centre.clone().add(x, y, z);
					if (!GameSettings.getSettings().getUnpaintableMaterials().contains(location.getBlock().getType())) {
						locations.add(location);
					}
				}
			}
		}
		return locations;
	}
	
	public void showPaint(Location projectileLocation) {
		// Paint the locations
		Set<Location> paintLocs = getLocsAround(projectileLocation);
		for (Location location : paintLocs) {
			paintTile(location);
		}

		// Schedule removal of paint
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> paintLocs.forEach((this::removePaint)), 100);
	}

	public abstract void paintTile(Location location);

	public abstract void removePaint(Location location);
	
	public static Paint getPaint(String name) {
		for (Paint paint : customPaints) {
			if(paint.getName().equals(name)) {
				return paint;
			}
		}
		return null;
	}
	
	public static List<Paint> getAllCustomPaints() {
		return customPaints;
	}
	
	public static Paint getDefaultPaint() {
		return DefaultPaint.getInstance();
	}

	public static void loadPaints() {
		paintConfig = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), "paints.yml"));
		MonoColourPaint.loadMonoPaints();
	}
}
