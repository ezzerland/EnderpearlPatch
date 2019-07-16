package ezzerland.ravenloftmc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ezzerland.ravenloftmc.utilities.BlockCheck;

public class EnderpearlPatch extends JavaPlugin implements Listener
{
  public void onEnable() { getServer().getPluginManager().registerEvents(this,this); }
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onEvent(PlayerTeleportEvent event)
  {
    if(event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL))
    {
      event.getTo().setX(Math.floor(event.getTo().getX())+0.5f);
      event.getTo().setY(Math.floor(event.getTo().getY())+0.5f);
      event.getTo().setZ(Math.floor(event.getTo().getZ())+0.5f);
      if (!BlockCheck.isSolid(event.getTo().getBlock()) && !BlockCheck.isSolid(event.getTo().clone().add(0, 1, 0).getBlock()))
      {
        if (!BlockCheck.isSlab(event.getTo().getBlock())) { event.getTo().setY(Math.floor(event.getTo().getY())); }
        return;
      }
      double xMin = Math.min(event.getFrom().getX(), event.getTo().getX());
      double xMax = Math.max(event.getFrom().getX(), event.getTo().getX());
      double yMin = Math.min(event.getFrom().getY(), event.getTo().getY());
      double yMax = Math.max(event.getFrom().getY(), event.getTo().getY());
      double zMin = Math.min(event.getFrom().getZ(), event.getTo().getZ());
      double zMax = Math.max(event.getFrom().getZ(), event.getTo().getZ());
      List<Location> locations = new ArrayList<Location>();
      for (double x=xMin; x<xMax; x++)
      { for (double y=yMin; y<yMax; y++)
        { for (double z=zMin; z<zMax; z++)
          {
            locations.add(new Location(event.getTo().getWorld(), Math.floor(x)+0.5f, Math.floor(y)+0.5f, Math.floor(z)+0.5f));
          }
        }
      }
      locations.sort(Comparator.comparing(location -> event.getTo().distanceSquared(location)));
      boolean cancelTeleport = true;
      for (Location location : locations)
      {
        if (!BlockCheck.isSolid(location.getBlock()) && !BlockCheck.isSolid(location.clone().add(0, 1, 0).getBlock()))
        {
          location.setYaw(event.getTo().getYaw());
          location.setPitch(event.getTo().getPitch());
          if (!BlockCheck.isSlab(location.getBlock())) { location.setY(Math.floor(location.getY())); }
          event.setTo(location);
          cancelTeleport = false;
          break;
        }
      }
      if (cancelTeleport) { event.setCancelled(true); }
    }
  }
}