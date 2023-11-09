package io.github.ultreon.gdxminecraft.impl;

import com.badlogic.gdx.ApplicationLogger;
import org.slf4j.LoggerFactory;

public class MinecraftGdxLogger implements ApplicationLogger {
    public MinecraftGdxLogger() {

    }

    @Override
    public void log(String tag, String message) {
        LoggerFactory.getLogger(tag).info(message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        LoggerFactory.getLogger(tag).info(message, exception);
    }

    @Override
    public void error(String tag, String message) {
        LoggerFactory.getLogger(tag).error(message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        LoggerFactory.getLogger(tag).error(message, exception);
    }

    @Override
    public void debug(String tag, String message) {
        LoggerFactory.getLogger(tag).debug(message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        LoggerFactory.getLogger(tag).debug(message, exception);
    }
}
