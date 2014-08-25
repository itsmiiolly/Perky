package com.me.itsmiiolly.perky;

/**
 * Modules system for every command
 * Credit to molenzwiebel for the modules system code!
 */

import org.bukkit.event.Listener;

public abstract class PerkyModule implements Listener {
    public abstract void load(Core instance);
    public abstract void unload(Core instance);
}