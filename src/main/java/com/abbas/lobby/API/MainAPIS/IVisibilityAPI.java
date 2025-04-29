package com.abbas.lobby.API.MainAPIS;

import org.bukkit.entity.Player;

public interface IVisibilityAPI {

    boolean isPlayerHidingOthers(Player player);

    void togglePlayerVisibility(Player player);

    void hideAllFrom(Player player);

    void showAllTo(Player player);
}
