package io.github.ultreon.gdxminecraft.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.ultreon.gdxminecraft.test.GdxTestScreen;

public class ModMenuHandler implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return GdxTestScreen::new;
    }
}
