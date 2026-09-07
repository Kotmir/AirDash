package tech.kotmir.airdash.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.kotmir.airdash.config.AirDashConfig;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onServerTick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        // Если опция выключения урона включена в конфигурации и игрок находится в воздухе
        if (AirDashConfig.disableFallDamage) {
            if (!player.isOnGround() && !player.isTouchingWater() && !player.isClimbing()) {
                // Сбрасываем накопление урона от падения на стороне сервера
                player.fallDistance = 0.0f;
            }
        }
    }
}