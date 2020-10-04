package net.threadly.commandapi.args;

import net.threadly.commandapi.args.cast.Caster;
import net.threadly.commandapi.exception.CastNotPossibleException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public final class GenericArguments {

    public static class Builder<T> {
        private String key;
        private Caster<T> caster;

        public Builder<T> caster(Caster<T> caster) {
            this.caster = caster;
            return this;
        }

        public Builder<T> key(String key){
            this.key = key;
            return this;
        }

        public CommandElement<T> build() {
            return new CommandElement<>(key, caster);
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static CommandElement<OfflinePlayer> offlinePlayer(String key) {
        return new CommandElement<>(key, (passedArgument) -> {
            return Bukkit.getOfflinePlayer(UUID.fromString(passedArgument));
        });
    }

    public static CommandElement<Player> onlinePlayer(String key) {
        return new CommandElement<>(key, (passedArgument) -> {
            Optional<Player> player = Optional.ofNullable(Bukkit.getPlayer(UUID.fromString(passedArgument)));
            player.orElseThrow(CastNotPossibleException::new);
            return player.get();
        });
    }

    public static CommandElement<String> string(String key) {
        return new CommandElement<>(key, (passedArgument) -> {return (String) passedArgument;});
    }

    public static CommandElement<Integer> integer(String key) {
        return new CommandElement<>(key, (passedArgument) -> {
            try {
                return Integer.valueOf(passedArgument);
            } catch (ClassCastException ex) {
                throw new CastNotPossibleException();
            }
        });
    }

    public static CommandElement<Boolean> bool(String key) {
        return new CommandElement<>(key, (passedArgument) -> {
            try{
                return Boolean.valueOf(passedArgument);
            }catch (ClassCastException ex) {
                throw new CastNotPossibleException();
            }
        });
    }
}
