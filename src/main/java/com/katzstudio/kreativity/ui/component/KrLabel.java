package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.katzstudio.kreativity.ui.KrRenderer;
import com.katzstudio.kreativity.ui.KreativitySkin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A simple label component
 */
public class KrLabel extends KrWidget {

    @Getter @Setter private String text;

    @Getter @Setter private Style style;

    public KrLabel(String text) {
        setStyle(KreativitySkin.instance().getLabelStyle());
        this.text = text;
        setSize(getSelfPreferredSize());
    }

    @Override
    public Vector2 getSelfPreferredSize() {
        BitmapFont.TextBounds bounds = getStyle().font.getBounds(text);
        Vector2 textSize = new Vector2(bounds.width, bounds.height);

        return expandSizeWithPadding(textSize, getPadding());
    }

    @Override
    protected void drawSelf(KrRenderer renderer) {
        renderer.beginClip(getX(), getY(), getWidth(), getHeight());

        Style style = getStyle();
        renderer.renderDrawable(style.background, getX(), getY(), getWidth(), getHeight());

        style.font.setColor(style.foregroundColor);

        renderer.setFont(style.font);
        renderer.setForeground(style.foregroundColor);

        renderer.renderText(text, getX() + getPadding().left, getY() + getPadding().top);

        renderer.endClip();
    }

    @Override
    public String toString() {
        return toStringBuilder().type("KrLabel").toString();
    }

    @AllArgsConstructor
    public static final class Style {

        public Drawable background;

        public BitmapFont font;

        public Color foregroundColor;
    }
}