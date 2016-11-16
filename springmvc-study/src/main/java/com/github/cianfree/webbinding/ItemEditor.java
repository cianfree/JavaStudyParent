package com.github.cianfree.webbinding;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorSupport;

/**
 * @author Arvin
 * @time 2016/11/15 18:17
 */
public class ItemEditor extends PropertyEditorSupport {
    public ItemEditor() {
        super();
        System.out.println("构造函数！");
    }

    public ItemEditor(Object source) {
        super(source);
        System.out.println("构造函数！");
    }

    @Override
    public Object getSource() {
        System.out.println("getSource");
        return super.getSource();
    }

    @Override
    public void setSource(Object source) {
        System.out.println("setSource: " + source);
        super.setSource(source);
    }

    @Override
    public void setValue(Object value) {
        System.out.println("setValue: " + value);
        super.setValue(value);
    }

    @Override
    public Object getValue() {
        System.out.println("getValue");
        return super.getValue();
    }

    @Override
    public boolean isPaintable() {
        System.out.println("isPaintable");
        return super.isPaintable();
    }

    @Override
    public void paintValue(Graphics gfx, Rectangle box) {
        System.out.println("paintValue");
        super.paintValue(gfx, box);
    }

    @Override
    public String getJavaInitializationString() {
        System.out.println("getJavaInitializationString");
        return super.getJavaInitializationString();
    }

    @Override
    public String getAsText() {
        System.out.println("getAsText");
        return super.getAsText();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        System.out.println("setAsText: " + text);
        super.setAsText(text);
    }

    @Override
    public String[] getTags() {
        System.out.println("getTags:");
        return super.getTags();
    }

    @Override
    public Component getCustomEditor() {
        System.out.println("getCustomEditor");
        return super.getCustomEditor();
    }

    @Override
    public boolean supportsCustomEditor() {
        System.out.println("supportsCustomEditor");
        return super.supportsCustomEditor();
    }

    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        System.out.println("addPropertyChangeListener");
        super.addPropertyChangeListener(listener);
    }

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        System.out.println("removePropertyChangeListener");
        super.removePropertyChangeListener(listener);
    }

    @Override
    public void firePropertyChange() {
        System.out.println("firePropertyChange");
        super.firePropertyChange();
    }
}
