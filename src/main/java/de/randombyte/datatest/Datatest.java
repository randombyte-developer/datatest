package de.randombyte.datatest;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "datatest",
        name = "Datatest"
)
public class Datatest {

    @Inject
    @ConfigDir(sharedRoot = false)
    Path configDir;

    TypeToken<Config> typeToken = TypeToken.of(Config.class);

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws IOException {

        Path configFile = configDir.resolve("config.conf");
        Files.createDirectories(configDir);

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .setPath(configFile)
                .build();

        Sponge.getCommandManager().register(this, CommandSpec.builder()
                .executor((src, args) -> {
                    try {
                        ItemStack handItemStack = ((Player) src).getItemInHand(HandTypes.MAIN_HAND).get();

                        Config config = new Config(handItemStack.createSnapshot());
                        SimpleConfigurationNode root = SimpleConfigurationNode.root();
                        root.setValue(typeToken, config);

                        loader.save(root);
                    } catch (IOException | ObjectMappingException e) {
                        e.printStackTrace();
                    }

                    return CommandResult.success();
                })
                .build(), "saveitem");

        Sponge.getCommandManager().register(this, CommandSpec.builder()
                .executor((src, args) -> {
                    try {
                        CommentedConfigurationNode root = loader.load();
                        Config config = root.getValue(typeToken);
                        root.setValue(typeToken, config);
                        loader.save(root);
                    } catch (IOException | ObjectMappingException e) {
                        e.printStackTrace();
                    }

                    return CommandResult.success();
                })
                .build(), "loaditem");
    }
}
