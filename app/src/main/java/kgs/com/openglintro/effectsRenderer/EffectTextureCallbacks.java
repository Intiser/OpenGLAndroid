package kgs.com.openglintro.effectsRenderer;

import android.graphics.SurfaceTexture;

/**
 * Created by developerios on 8/14/18.
 */

public interface EffectTextureCallbacks {
    void onSuraceTextureCreated(SurfaceTexture surfaceTexture);
    void onSurfaceUpdate(SurfaceTexture surfaceTexture);
}
