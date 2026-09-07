package tech.kotmir.airdash.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.kotmir.airdash.config.AirDashConfig;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Unique private boolean airDash$canDoubleJump = false;
    @Unique private boolean airDash$wasSpacePressed = false;
    @Unique private boolean airDash$isDashing = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.options == null) return;

        boolean isSpacePressed = client.options.jumpKey.isPressed();

        // Сброс способности при приземлении
        if (player.isOnGround() || player.isTouchingWater() || player.isClimbing()) {
            airDash$canDoubleJump = true;
            airDash$isDashing = false;
        } else {
            // Активация рывка
            if (isSpacePressed && !airDash$wasSpacePressed && airDash$canDoubleJump) {
                airDash$canDoubleJump = false;
                airDash$isDashing = true;

                Vec3d look = player.getRotationVector();
                
                double forward = AirDashConfig.forwardPower;
                double jump = AirDashConfig.jumpPower;
                
                player.setVelocity(look.x * forward, jump, look.z * forward);
            }

            // Шлейф и защита от падения в полете
            if (airDash$isDashing && client.world != null) {
                if (AirDashConfig.disableFallDamage) {
                    player.fallDistance = 0.0f;
                }

                SimpleParticleType particle = getSelectedParticle(AirDashConfig.particleType);

                for (int i = 0; i < AirDashConfig.particleCount; i++) {
                    double offsetX = (player.getRandom().nextDouble() - 0.5) * 0.4;
                    double offsetY = player.getRandom().nextDouble() * 0.5;
                    double offsetZ = (player.getRandom().nextDouble() - 0.5) * 0.4;

                    client.world.addParticleClient(
                        particle,
                        player.getX() + offsetX,
                        player.getY() + offsetY,
                        player.getZ() + offsetZ,
                        -player.getVelocity().x * 0.2, 0.01, -player.getVelocity().z * 0.2
                    );
                }
            }
        }
        airDash$wasSpacePressed = isSpacePressed;
    }

    @Unique
    private SimpleParticleType getSelectedParticle(String type) {
        return switch (type) {
            case "SOUL_FIRE_FLAME" -> ParticleTypes.SOUL_FIRE_FLAME;
            case "END_ROD" -> ParticleTypes.END_ROD;
            case "HEART" -> ParticleTypes.HEART;
            case "SMOKE" -> ParticleTypes.SMOKE;
            default -> ParticleTypes.FLAME;
        };
    }
}