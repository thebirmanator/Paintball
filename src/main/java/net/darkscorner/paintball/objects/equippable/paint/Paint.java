package net.darkscorner.paintball.objects.equippable.paint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.darkscorner.paintball.objects.games.GameSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import net.darkscorner.paintball.Main;

public abstract class Paint {

	private static Main main;
	
	private String name;
	private Material displayIcon;
	private List<Material> paintMaterials = new ArrayList<Material>();
	
	private static Paint defaultPaint;
	static Set<Paint> customPaints = new HashSet<>();
	
	public Paint(FileConfiguration config, Main main) {
		Paint.main = main;
		
		List<String> defaultPaintStrings = config.getStringList("default-paint");
		for(String defaultPaintString : defaultPaintStrings) {
			if(Material.getMaterial(defaultPaintString) != null) {
				Material material = Material.getMaterial(defaultPaintString);
				paintMaterials.add(material);
			} else {
				main.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid paint material: " + ChatColor.GRAY + defaultPaintString);
			}
		}
		//defaultPaint = new DefaultPaint("default", Material.STONE, paintMaterials);
		
	}
	
	public Paint(String name, Material displayIcon) {
		this.name = name;
		//this.paintMaterials = paintMaterials;
		this.displayIcon = displayIcon;
		
		if(!name.equals("default")) {
			customPaints.add(this);
		}
	}
	
	public String getName() {
		return name;
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
		// get the locations to paint
		//List<Location> locList = new ArrayList<Location>();
		/*
		int radius = Game.getPaintRadius();
		for(int x = radius * -1; x < radius + 1; x++) {
			for(int y = radius * -1; y < radius + 1; y++) {
				for(int z = radius * -1; z < radius + 1; z++) {
					Location loc = new Location(projectileLocation.getWorld(), projectileLocation.getBlockX() + x, projectileLocation.getBlockY() + y, projectileLocation.getBlockZ() + z);
					if(!getUnpaintableMaterials().contains(loc.getBlock().getType())) {
						locList.add(loc);
					}
				}
			}
		}*/

		// paint the locations
		Set<Location> paintLocs = getLocsAround(projectileLocation);
		for (Location location : paintLocs) {
			paintTile(location);
		}

		// schedule removal of paint
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> paintLocs.forEach((this::removePaint)), 100);

		/*
		// paint the locations for each player
		for(Location location : locList) {
			Random random = new Random();
			int index = random.nextInt(paintMaterials.size());
			Material paintBlock = paintMaterials.get(index);
			for(Player player : Bukkit.getOnlinePlayers()) {
				player.sendBlockChange(location, Bukkit.createBlockData(paintBlock));
			}
		}
		
		// schedule the removal of the paint
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				for(Location location : locList) {
					for(Player player : Bukkit.getOnlinePlayers()) {
						player.sendBlockChange(location, location.getBlock().getBlockData());
					}
				}
			}
		}, 100);*/
	}

	public abstract void paintTile(Location location);

	public abstract void removePaint(Location location);
	
	public static Paint getPaint(String name) {
		for(Paint paint : customPaints) {
			if(paint.getName().equals(name)) {
				return paint;
			}
		}
		return null;
	}
	
	public static Set<Paint> getAllCustomPaints() {
		return customPaints;
	}
	
	public static Paint getDefaultPaint() {
		return DefaultPaint.getInstance();
	}

	public static void loadPaints() {
		MonoColourPaint.loadMonoPaints();
	}
}
