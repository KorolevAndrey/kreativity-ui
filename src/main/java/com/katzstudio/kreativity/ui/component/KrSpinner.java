package com.katzstudio.kreativity.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.google.common.collect.Lists;
import com.katzstudio.kreativity.ui.KrModel;
import com.katzstudio.kreativity.ui.KrPadding;
import com.katzstudio.kreativity.ui.KrSkin;
import com.katzstudio.kreativity.ui.event.KrKeyEvent;
import com.katzstudio.kreativity.ui.event.KrMouseEvent;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.List;

import static com.katzstudio.kreativity.ui.event.KrMouseEvent.Button.LEFT;
import static com.katzstudio.kreativity.ui.event.KrMouseEvent.Button.RIGHT;

/**
 * Spinner component.
 */
public class KrSpinner extends KrTextField {

    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    private float value = 0;

    @Getter @Setter private float increment = 0.1f;

    private final List<EditListener> editListeners = Lists.newArrayList();

    @Getter @Setter private KrModel<Float> model;

    private boolean dragStarted;

    private boolean wasDragged = false;

    public KrSpinner() {
        setStyle(KrSkin.instance().getSpinnerStyle());

        KrPadding padding = getPadding();
        padding.right = 17;
        setPadding(padding);
    }

    @Override
    protected boolean keyPressedEvent(KrKeyEvent event) {
        if (event.getKeycode() == Input.Keys.ENTER || event.getKeycode() == Input.Keys.ESCAPE) {
            validateText();
            notifyEditStopped();
            clearFocus();
            return true;
        }

        return super.keyPressedEvent(event);
    }

    @Override
    protected boolean mousePressedEvent(KrMouseEvent event) {
        wasDragged = false;

        if (event.getButton() == RIGHT) {
            notifyEditStarted();
            setValue(0);
            notifyEditStopped();
        }

        if (event.getButton() == LEFT) {
            dragStarted = true;
            notifyEditStarted();
        }

        clearFocus();
        event.accept();
        return true;
    }

    @Override
    protected boolean mouseMoveEvent(KrMouseEvent event) {
        if (dragStarted) {
            wasDragged = true;
            incrementValue(Gdx.input.getDeltaX());
        }
        event.accept();
        return true;
    }

    @Override
    protected boolean mouseReleasedEvent(KrMouseEvent event) {
        dragStarted = false;
        if (!wasDragged && event.getButton() == LEFT) {
            requestFocus();
            textDocument.selectAll();
        } else {
            notifyEditStopped();
        }
        event.accept();
        return true;
    }

    private void validateText() {
        if (isFloatRepresentation(getText())) {
            setValue(Float.valueOf(getText()));
        } else {
            resetText();
        }
    }

    private void resetText() {
        setText(String.valueOf(value));
    }

    private void incrementValue(float times) {
        setValue(value + increment * times);
    }

    @SuppressWarnings("deprecation")
    public void setValue(float value) {
        if (this.value != value) {
            this.value = value;
            if (model != null) {
                model.setValue(value);
            }
            setText(FORMAT.format(value));
            notifyValueChanged(value);
        }
    }

    private boolean isFloatRepresentation(String value) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Float.valueOf(value);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    /**
     * @deprecated please use {@link KrSpinner#setValue} instead.
     */
    @Deprecated
    @Override
    public void setText(String str) {
        super.setText(str);
    }

//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        if (model != null && model.getValue() != value) {
//            setValue(model.getValue());
//        }
//
//        super.draw(batch, parentAlpha);
//    }

    public void addEditListener(EditListener editListener) {
        editListeners.add(editListener);
    }

    public void removeEditListener(EditListener editListener) {
        editListeners.remove(editListener);
    }

    private void notifyEditStarted() {
        editListeners.forEach(EditListener::editStarted);
    }

    private void notifyValueChanged(float newValue) {
        editListeners.forEach(listener -> listener.valueChanged(newValue));
    }

    private void notifyEditStopped() {
        editListeners.forEach(EditListener::editStopped);
    }

    public interface EditListener {
        void editStarted();

        void valueChanged(float newValue);

        void editStopped();
    }
}
