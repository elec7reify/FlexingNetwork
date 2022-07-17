package com.flexingstudios.FlexingNetwork.api.conf;

import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.api.geom.Cuboid;
import com.flexingstudios.FlexingNetwork.api.geom.Vec3f;
import com.flexingstudios.FlexingNetwork.api.geom.Vec3i;
import org.apache.logging.log4j.core.config.json.JsonConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Configuration {
    private static final Function<String, Vec3i> VEC3I_PARSER;
    private static final Function<String, Vec3f> VEC3F_PARSER;
    private ConfigurationSection config;
    private YamlConfiguration yml;
    private File cfg;

    static {
        VEC3I_PARSER = (str -> {
            String[] s = str.split(",");
            return new Vec3i(Integer.parseInt(s[0].trim()), Integer.parseInt(s[1].trim()), Integer.parseInt(s[2].trim()));
        });
        VEC3F_PARSER = (str -> {
            String[] s = str.split(",");
            return new Vec3f(Float.parseFloat(s[0].trim()), Float.parseFloat(s[1].trim()), Float.parseFloat(s[2].trim()));
        });
    }

    public Configuration(Plugin plugin) {
        this(plugin, "config");
    }

    public Configuration(Plugin plugin, String file) {
        cfg = new File(plugin.getDataFolder(), file + ".yml");
        if (!cfg.exists())
            try {
                plugin.getDataFolder().mkdir();
                Files.copy(plugin.getResource(file + ".yml"), cfg.toPath(), new java.nio.file.CopyOption[0]);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, null, e);
            }

        yml = YamlConfiguration.loadConfiguration(cfg);
    }

    public Configuration(Plugin plugin, String name, String dir) {
        File d = new File(dir);
        if (!d.exists()) {
            if (!d.mkdirs()) {
                plugin.getLogger().log(Level.SEVERE, "Could not create " + d.getPath());
                return;
            }
        }

        cfg = new File(dir, name + ".yml");
        if (!cfg.exists()) {
            plugin.getLogger().log(Level.INFO, "Creating " + cfg.getPath());
            try {
                if (!cfg.createNewFile()) {
                    plugin.getLogger().log(Level.SEVERE, "Could not create " + cfg.getPath());
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        yml = YamlConfiguration.loadConfiguration(cfg);
        yml.options().copyDefaults(true);
    }

    public Configuration(File file) {
        yml = YamlConfiguration.loadConfiguration(file);
    }

    public Configuration(ConfigurationSection config) {
        this.config = config;
    }


    /**
     * Save config changes to file
     */
    public void save() {
        try {
            yml.save(cfg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get yml instance
     */
    public YamlConfiguration getYml() {
        return yml;
    }

    public void addDefault(String path, Object value) {
        yml.addDefault(path, value);
    }

    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }

    public List<Configuration> getConfigList(String path) {
        List<Configuration> list = new LinkedList<>();
        for (Map<?, ?> map : config.getMapList(path))
            list.add(new Configuration(config.createSection(path, map)));
        return list;
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public Object get(String path) {
        return get(path, null);
    }

    public Object get(String path, Object def) {
        return config.get(path, def);
    }

    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    public boolean getBoolean(String path, boolean def) {
        return yml.getBoolean(path, def);
    }

    public int getInt(String path) {
        return getInt(path, 0);
    }

    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    public long getLong(String path) {
        return getLong(path, 0L);
    }

    public long getLong(String path, long def) {
        return config.getLong(path, def);
    }

    public double getDouble(String path) {
        return getDouble(path, 0.0D);
    }

    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }

    public float getFloat(String path) {
        return getFloat(path, 0.0F);
    }

    public float getFloat(String path, float def) {
        String str = config.getString(path, null);
        if (str == null)
            return def;
        return Float.parseFloat(str);
    }

    public Configuration getSection(String path) {
        ConfigurationSection sec = config.getConfigurationSection(path);
        if (sec == null)
            return null;
        return new Configuration(sec);
    }

    public Configuration createSection(String path, Map<?, ?> map) {
        return new Configuration(config.createSection(path, map));
    }

    public Configuration createSection(String path) {
        return new Configuration(config.createSection(path));
    }

    public String getString(String path) {
        return yml.getString(path, null);
    }

    public String getString(String path, String def) {
        return yml.getString(path, def);
    }

    /**
     * Reload configuration.
     */
    public void reload() {
        yml = YamlConfiguration.loadConfiguration(cfg);
    }

    public World getWorld(String path) {
        return Bukkit.getWorld(getString(path));
    }

    public Location getLocation(World world, String path) {
        return Utilities.parseLocation(world, getString(path));
    }

    public Vec3i getVec3i(String path) {
        String str = getString(path, null);
        if (str == null)
            return null;
        return VEC3I_PARSER.apply(str);
    }

    public Vec3f getVec3f(String path) {
        String str = getString(path, null);
        if (str == null)
            return null;
        return VEC3F_PARSER.apply(str);
    }

    public Cuboid getCuboid(String path) {
        String str = getString(path, null);
        if (str == null)
            return null;
        String[] s = str.split(";", 2);
        return new Cuboid(VEC3I_PARSER.apply(s[0]), VEC3I_PARSER.apply(s[1]));
    }

    public List<Integer> getIntegerList(String path) {
        return yml.getIntegerList(path);
    }

    public List<String> getStringList(String path) {
        return yml.getStringList(path);
    }

    public List<Location> getLocationList(World world, String path) {
        return Utilities.parseLocations(world, getStringList(path));
    }

    public List<Vec3i> getVec3iList(String path) {
        return getStringList(path).stream()
                .<Vec3i>map(VEC3I_PARSER)
                .collect(Collectors.toList());
    }

    public List<Vec3f> getVec3fList(String path) {
        return getStringList(path).stream()
                .<Vec3f>map(VEC3F_PARSER)
                .collect(Collectors.toList());
    }

    public List<Cuboid> getCuboidList(String path) {
        return getStringList(path).stream()
                .map(str -> {
                    String[] s = str.split(";", 2);
                    return new Cuboid(VEC3I_PARSER.apply(s[0]), VEC3I_PARSER.apply(s[1]));
                }).collect(Collectors.toList());
    }

    public ConfigurationSection getHandle() {
        return config;
    }
}