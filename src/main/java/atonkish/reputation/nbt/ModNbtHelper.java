package atonkish.reputation.nbt;

import java.util.UUID;

import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;

public final class ModNbtHelper {

    public static IntArrayTag fromUuid(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        return new IntArrayTag(new int[]{
            (int) (msb >>> 32),
            (int) msb,
            (int) (lsb >>> 32),
            (int) lsb
        });
    }

    public static UUID toUuid(Tag element) {
        if (!(element instanceof IntArrayTag intArrayTag)) {
            throw new IllegalArgumentException(
                    "Expected UUID-Tag to be of type IntArrayTag, but found "
                            + element.getClass().getSimpleName() + ".");
        } else {
            int[] is = intArrayTag.getAsIntArray();
            if (is.length != 4) {
                throw new IllegalArgumentException(
                        "Expected UUID-Array to be of length 4, but found " + is.length + ".");
            } else {
                return new UUID(
                        ((long) is[0] << 32) | (is[1] & 0xFFFFFFFFL),
                        ((long) is[2] << 32) | (is[3] & 0xFFFFFFFFL));
            }
        }
    }
}
