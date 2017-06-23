package com.TominoCZ.PAYDAY.util;

import com.TominoCZ.PAYDAY.PAYDAY;

import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class DimensionUtil {

	public static int registerProvider(Class<? extends WorldProvider> provider) {
		int ID = 0;

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			try {
				DimensionManager.registerProviderType(i, provider, false);
				ID = i;
				break;
			} catch (Exception e) {

			}
		}

		return ID;
	}

	public static int registerDimension(int providerID) {
		int ID = 0;

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			if (!DimensionManager.isDimensionRegistered(i)) {
				DimensionManager.registerDimension(i, providerID);
				ID = i;
				break;
			}
		}

		return ID;
	}
}
