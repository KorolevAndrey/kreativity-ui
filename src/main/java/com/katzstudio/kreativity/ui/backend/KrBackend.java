package com.katzstudio.kreativity.ui.backend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrCursor;
import com.katzstudio.kreativity.ui.KrFontMetrics;
import com.katzstudio.kreativity.ui.render.KrRenderer;

/**
 * The {@link KrBackend} interface offers access to components that are bound to
 * the low level API of the platform.
 */
public interface KrBackend {

    KrRenderer getRenderer();

    KrInputSource getInputSource();

    KrFontMetrics getFontMetrics();

    void setCursor(KrCursor cursor);

    KrCursor getCursor();

    void writeToClipboard(String value);

    String readFromClipboard();

    Drawable createColorDrawable(Color color);

    int getScreenWidth();

    int getScreenHeight();
}
