package co.insou.refer.player;

import co.insou.refer.Refer;
import co.insou.refer.gui.page.ExternalIgnorance;
import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.pages.HistoryPage;
import co.insou.refer.gui.pages.MainMenuPage;
import co.insou.refer.gui.pages.admin.AdminHistoryPage;
import co.insou.refer.gui.pages.admin.AdminMenuPage;
import co.insou.refer.gui.pages.admin.playermanagement.select.PlayerManagementSelectOfflinePage;
import co.insou.refer.gui.pages.admin.playermanagement.select.PlayerManagementSelectOnlinePage;
import co.insou.refer.gui.pages.admin.playermanagement.select.PlayerManagementSelectionPage;
import co.insou.refer.gui.pages.refer.ReferMenuPage;
import co.insou.refer.gui.pages.refer.ReferOnlinePage;
import co.insou.refer.gui.pages.request.RequestPage;
import co.insou.refer.gui.pages.tutorial.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class ReferPlayer {

    private final Refer plugin;

    private final Player player;

    private List<GUIPage> breadcrumb = new ArrayList<>();

    private volatile boolean internalIgnore = false;
    private volatile List<ExternalIgnorance> externalIgnore = new ArrayList<>();
    private volatile List<ExternalIgnorance> externalCancel = new ArrayList<>();

    private volatile GUIPage absoluteInventory;

    public ReferPlayer(Refer plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void openPage(GUIPage page) {
        breadcrumb.add(page);
        absoluteInventory = page;
        internalIgnore = true;
        page.display();
        internalIgnore = false;
    }

    public synchronized void openUndocumentedPage(final GUIPage page) {
        internalIgnore = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                page.display();
                internalIgnore = false;
            }
        }, 1);
    }

    public List<GUIPage> getBreadcrumb() {
        return breadcrumb;
    }

    public void closePage() {
        breadcrumb.remove(getCurrentIndex());
    }

    public boolean inGUI() {
        return breadcrumb.size() > 0;
    }

    public GUIPage getCurrentPage() {
        if (!inGUI()) {
            return null;
        }
        return breadcrumb.get(getCurrentIndex());
    }

    private int getCurrentIndex() {
        return breadcrumb.size() - 1;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        if (inGUI()) {
            if (event.getClickedInventory() != null) {
                if (event.getClickedInventory().equals(player.getOpenInventory().getTopInventory())) {
                    getCurrentPage().onClick(event);
                }
            }
        }
    }

    public void onInventoryClose() {
//        System.out.println("onInventoryClose called");
        if (internalIgnore || getExternalIgnore()) {
            return;
        }
//        System.out.println("Ignore");
        if (getExternalCancel()) {
            openUndocumentedPage(absoluteInventory);
            return;
        }
//        System.out.println("Cancel");
        if (inGUI()) {
//            System.out.println("inGUI");
            getCurrentPage().onClose();
        }
    }

    public GUIPage getPage(GUIPageType type) {
        switch (type) {
            case MAIN_MENU:
                return new MainMenuPage(this);
            case REFER_MENU:
                return new ReferMenuPage(this);
            case REFER_ONLINE:
                return new ReferOnlinePage(this);
            case HISTORY:
                return new HistoryPage(this);
            case REQUEST_SELECT:
                return new RequestPage(this);
            case TUTORIAL:
                return new TutorialPage(this);
            case REFER_HELP:
                return new ReferHelpPage(this);
            case HISTORY_HELP:
                return new HistoryHelpPage(this);
            case POINTS_HELP:
                return new PointsHelpPage(this);
            case REQUEST_HELP:
                return new RequestHelpPage(this);
            case ADMIN_MENU:
                return new AdminMenuPage(this);
            case PLAYER_MANAGEMENT_SELECT:
                return new PlayerManagementSelectionPage(this);
            case PLAYER_MANAGEMENT_SELECT_OFFLINE:
                return new PlayerManagementSelectOfflinePage(this);
            case PLAYER_MANAGEMENT_SELECT_ONLINE:
                return new PlayerManagementSelectOnlinePage(this);
            case SERVER_HISTORY:
                return new AdminHistoryPage(this);
            case CLOSE_PAGE:
                getCurrentPage().onClose();
                return null;
            case CLOSE_GUI:
                breadcrumb.clear();
                absoluteInventory = null;
                player.closeInventory();
                return null;
            default:
                return null;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public List<ExternalIgnorance> getExternalIgnores() {
        return externalIgnore;
    }

    public boolean getExternalIgnore() {
        return externalIgnore.size() > 0;
    }

    public void addExternalIgnore(ExternalIgnorance ignorance) {
        this.externalIgnore.add(ignorance);
    }

    public void removeExternalIgnore(ExternalIgnorance ignorance) {
        this.externalIgnore.remove(ignorance);
    }

    public boolean getExternalCancel() {
        return externalCancel.size() > 0;
    }

    public void addExternalCancel(ExternalIgnorance ignorance) {
        this.externalCancel.add(ignorance);
    }

    public void removeExternalCancel(ExternalIgnorance ignorance) {
        this.externalCancel.remove(ignorance);
    }

    public void setAbsoluteInventory(GUIPage page) {
        this.absoluteInventory = page;
    }

    public void clearChat() {
        for (int i = 0; i < 150; i++) {
            player.sendMessage(" ");
        }
    }

    public int getReferences() {
        return plugin.getReferDatabase().getReferences(player);
    }

    public boolean canRefer() {
        return !plugin.getReferDatabase().hasPlayerReferred(player);
    }

    public void addMoney(Double money) {
        if (plugin.isEconomyEnabled()) {
            plugin.getEconomyHandler().addMoney(player, money);
        }
    }

    public Double getMoney() {
        if (!plugin.isEconomyEnabled()) {
            return -1.0;
        }
        return plugin.getEconomyHandler().getMoney(player);
    }

    public void playSound(String sound) {
        Sound s = sound != null ? Sound.valueOf(sound) : null;
        if (s != null) player.playSound(player.getLocation(), s, 1, 1);
    }

    public void playSound(Sound sound) {
        player.playSound(player.getLocation(), sound, 1, 1);
    }

    public void sendFormattedMessage(String message) {
        message = replaceMessage(message);
        if (message == null) return;
        player.sendMessage(message);
    }

    public String replaceMessage(String message) {
        if (message == null) {
            return null;
        }
        message = message.replace("%name%", player.getName())
                .replace("%money%", String.valueOf(getMoney()))
                .replace("%references%", String.valueOf(getReferences())
                        .replace("%displayname%", player.getDisplayName())
                        .replace("%uuid%", player.getUniqueId().toString())
                        .replace("%ip%", player.getAddress().getHostName())
                        .replace("%xp%", String.valueOf(player.getExp()))
                        .replace("%health%", String.valueOf(player.getHealth()))
                        .replace("%maxhealth%", String.valueOf(player.getMaxHealth())));
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

}
